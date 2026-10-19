package cl.duocuc.evaluacion2.ui.viewmodels

import androidx.lifecycle.ViewModel
import cl.duocuc.evaluacion2.qr.QrMaxValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Estado de la pantalla de escaneo QR. */
data class EscanearQrUiState(
    val escaneando: Boolean = true,
    val contenido: String? = null,
    val esValido: Boolean = false,
    val mensaje: String? = null
)

/**
 * ViewModel del escáner QR: valida el contenido detectado según las
 * reglas de Agencia Max y controla el ciclo escanear → resultado.
 */
class QrViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EscanearQrUiState())
    val uiState: StateFlow<EscanearQrUiState> = _uiState.asStateFlow()

    /**
     * Se dispara una sola vez por escaneo: la pantalla deja de pedir códigos
     * al detectar el primero y aquí se decide si abre detalle o muestra error.
     */
    fun onQrDetectado(contenido: String) {
        val valido = QrMaxValidator.esValido(contenido)
        _uiState.value = EscanearQrUiState(
            escaneando = false,
            contenido = contenido,
            esValido = valido,
            mensaje = if (valido) {
                null
            } else {
                "El código QR escaneado no pertenece a Agencia Max."
            }
        )
    }

    fun onPermisoDenegado() {
        _uiState.value = EscanearQrUiState(
            escaneando = false,
            mensaje = "Permiso de cámara denegado. No es posible escanear códigos QR."
        )
    }

    fun onSinCoincidencia() {
        _uiState.update {
            it.copy(
                mensaje = "El código es válido, pero no corresponde a ninguna oportunidad registrada."
            )
        }
    }

    /** Vuelve al estado inicial de escaneo. */
    fun reiniciarEscaneo() {
        _uiState.value = EscanearQrUiState()
    }
}
