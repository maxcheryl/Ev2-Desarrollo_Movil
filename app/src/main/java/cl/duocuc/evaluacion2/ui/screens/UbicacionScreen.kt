package cl.duocuc.evaluacion2.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cl.duocuc.evaluacion2.location.ResultadoUbicacion
import cl.duocuc.evaluacion2.location.UbicacionHelper
import cl.duocuc.evaluacion2.ui.viewmodels.UbicacionViewModel
import java.util.Locale

/**
 * Pantalla de ubicación del dispositivo: una única consulta por acción
 * del usuario, resultado visible solo en pantalla (no se persiste).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UbicacionScreen(
    viewModel: UbicacionViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val solicitudPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            viewModel.obtenerUbicacion()
        } else {
            viewModel.onPermisoDenegado()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Ubicación del dispositivo", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Ubicación de referencia",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Obtén tu ubicación aproximada para contextualizar la gestión de activaciones. " +
                    "La coordenada solo se muestra en pantalla: no se guarda ni se realiza seguimiento continuo.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = {
                    if (UbicacionHelper.tienePermiso(context)) {
                        viewModel.obtenerUbicacion()
                    } else {
                        solicitudPermiso.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                enabled = !uiState.obteniendo
            ) {
                Icon(Icons.Outlined.LocationOn, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text("Obtener mi ubicación")
            }

            when {
                uiState.obteniendo -> {
                    CircularProgressIndicator()
                    Text(
                        text = "Obteniendo ubicación…",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                uiState.resultado is ResultadoUbicacion.Disponible -> {
                    val ubicacion = uiState.resultado as ResultadoUbicacion.Disponible
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Latitud: " + String.format(
                                    Locale.US,
                                    "%.6f",
                                    ubicacion.latitud
                                ),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Longitud: " + String.format(
                                    Locale.US,
                                    "%.6f",
                                    ubicacion.longitud
                                ),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "¡Ubicación lista para tu gestión!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                uiState.resultado is ResultadoUbicacion.PermisoDenegado -> {
                    Icon(
                        imageVector = Icons.Outlined.ErrorOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "Permiso de ubicación denegado. Puedes seguir usando la aplicación con normalidad.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                uiState.resultado is ResultadoUbicacion.NoDisponible -> {
                    Icon(
                        imageVector = Icons.Outlined.ErrorOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "No fue posible obtener la ubicación. Revisa que el GPS o los datos estén activados e inténtalo nuevamente.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
