package cl.duocuc.evaluacion2.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat

/** Resultado de una solicitud única de ubicación (sin seguimiento continuo). */
sealed class ResultadoUbicacion {
    data class Disponible(val latitud: Double, val longitud: Double) : ResultadoUbicacion()
    data object PermisoDenegado : ResultadoUbicacion()
    data object NoDisponible : ResultadoUbicacion()
}

/**
 * Obtención de la ubicación del dispositivo en una sola consulta
 * usando LocationManager (sin servicios externos de Google).
 */
object UbicacionHelper {

    private const val TIMEOUT_MILLIS = 10_000L

    fun tienePermiso(context: Context): Boolean {
        val conFina = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val conAproximada = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return conFina || conAproximada
    }

    /** Pide la ubicación actual una vez y responde por callback en el hilo principal. */
    @SuppressLint("MissingPermission", "DEPRECATION")
    fun obtenerUbicacionUnaVez(
        context: Context,
        onResultado: (ResultadoUbicacion) -> Unit
    ) {
        if (!tienePermiso(context)) {
            onResultado(ResultadoUbicacion.PermisoDenegado)
            return
        }

        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Preferir GPS si está activo; si no, red (menos precisa pero más rápida)
        val proveedor = when {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ->
                LocationManager.GPS_PROVIDER
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ->
                LocationManager.NETWORK_PROVIDER
            else -> null
        }

        if (proveedor == null) {
            onResultado(ResultadoUbicacion.NoDisponible)
            return
        }

        // El timeout y el listener pueden llegar casi al mismo tiempo;
        // esta bandera garantiza que solo se responda una vez
        var respondido = false
        val handler = Handler(Looper.getMainLooper())

        // Si el proveedor tarda más de 10 segundos, se rinde para no
        // dejar la pantalla cargando indefinidamente
        val timeout = Runnable {
            if (!respondido) {
                respondido = true
                onResultado(ResultadoUbicacion.NoDisponible)
            }
        }

        val listener = android.location.LocationListener { location ->
            if (!respondido) {
                respondido = true
                handler.removeCallbacks(timeout)
                onResultado(
                    ResultadoUbicacion.Disponible(
                        latitud = location.latitude,
                        longitud = location.longitude
                    )
                )
            }
        }

        try {
            locationManager.requestSingleUpdate(
                proveedor,
                listener,
                Looper.getMainLooper()
            )
            handler.postDelayed(timeout, TIMEOUT_MILLIS)
        } catch (e: Exception) {
            handler.removeCallbacks(timeout)
            if (!respondido) {
                respondido = true
                onResultado(ResultadoUbicacion.NoDisponible)
            }
        }
    }
}
