package cl.duocuc.evaluacion2.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.outlined.NoPhotography
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cl.duocuc.evaluacion2.qr.CameraPermissionHelper
import cl.duocuc.evaluacion2.qr.QrScanner
import cl.duocuc.evaluacion2.ui.viewmodels.OportunidadViewModel
import cl.duocuc.evaluacion2.ui.viewmodels.QrViewModel

/**
 * Pantalla de escaneo de códigos QR para localizar oportunidades.
 * Solo los códigos válidos (prefijo «ATENAS:») pueden abrir una
 * oportunidad; un código inválido nunca modifica datos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EscanearQrScreen(
    qrViewModel: QrViewModel,
    oportunidadViewModel: OportunidadViewModel,
    onVolver: () -> Unit,
    onOportunidadEncontrada: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by qrViewModel.uiState.collectAsState()
    val oportunidades by oportunidadViewModel.oportunidades.collectAsState()

    var permisoConcedido by remember {
        mutableStateOf(CameraPermissionHelper.tienePermiso(context))
    }

    val solicitudPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        permisoConcedido = concedido
        if (concedido) {
            qrViewModel.reiniciarEscaneo()
        } else {
            qrViewModel.onPermisoDenegado()
        }
    }

    // Con un código válido, buscar la oportunidad asociada
    LaunchedEffect(uiState.contenido, uiState.esValido, oportunidades) {
        val contenido = uiState.contenido
        if (contenido != null && uiState.esValido && uiState.mensaje == null &&
            oportunidades.isNotEmpty()
        ) {
            val encontrada = oportunidades.firstOrNull { it.codigoQr == contenido }
            if (encontrada != null) {
                onOportunidadEncontrada(encontrada.id)
            } else {
                qrViewModel.onSinCoincidencia()
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Escanear código QR", fontWeight = FontWeight.SemiBold) },
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
            verticalArrangement = Arrangement.Center
        ) {
            if (!permisoConcedido) {
                Icon(
                    imageVector = Icons.Outlined.NoPhotography,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Permiso de cámara requerido",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Para escanear códigos QR de oportunidades necesitamos acceso a la cámara del dispositivo.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                uiState.mensaje?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.height(16.dp))
                Button(onClick = { solicitudPermiso.launch(Manifest.permission.CAMERA) }) {
                    Icon(Icons.Outlined.QrCodeScanner, contentDescription = null)
                    Spacer(Modifier.size(8.dp))
                    Text("Conceder permiso de cámara")
                }
            } else if (uiState.escaneando) {
                Text(
                    text = "Escanea un código QR",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    QrScanner(
                        onQrCodeScanned = { contenido ->
                            qrViewModel.onQrDetectado(contenido)
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Marco de ayuda para centrar el código QR
                    Surface(
                        modifier = Modifier
                            .size(250.dp)
                            .align(Alignment.Center),
                        color = Color.Transparent,
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    ) {}
                }

                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Enfoca el código QR dentro del marco central.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            } else if (uiState.contenido != null && uiState.esValido && uiState.mensaje == null) {
                // Código válido: buscando la oportunidad asociada
                CircularProgressIndicator()
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Código válido, buscando oportunidad…",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                // Resultado: código inválido, sin coincidencia o permiso denegado
                val esCodigoInvalido = uiState.contenido != null && !uiState.esValido
                val esSinCoincidencia = uiState.contenido != null && uiState.esValido

                Icon(
                    imageVector = when {
                        esCodigoInvalido -> Icons.Outlined.ErrorOutline
                        esSinCoincidencia -> Icons.Outlined.QrCodeScanner
                        else -> Icons.Outlined.NoPhotography
                    },
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = if (esCodigoInvalido) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = when {
                        esCodigoInvalido -> "Código QR no válido"
                        esSinCoincidencia -> "Oportunidad no encontrada"
                        else -> "No se pudo escanear"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = if (esCodigoInvalido) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )

                if (esCodigoInvalido) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                        )
                    ) {
                        Text(
                            text = uiState.contenido.orEmpty(),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                uiState.mensaje?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (esCodigoInvalido) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "No se creó ni modificó ninguna oportunidad.",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.height(20.dp))
                if (permisoConcedido) {
                    Button(
                        onClick = { qrViewModel.reiniciarEscaneo() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Outlined.QrCodeScanner, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text("Escanear otro código")
                    }
                } else {
                    Button(
                        onClick = { solicitudPermiso.launch(Manifest.permission.CAMERA) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Conceder permiso de cámara")
                    }
                }
            }
        }
    }
}
