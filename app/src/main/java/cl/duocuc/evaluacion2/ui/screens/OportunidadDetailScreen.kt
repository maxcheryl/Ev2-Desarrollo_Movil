package cl.duocuc.evaluacion2.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duocuc.evaluacion2.data.local.OpportunityEntity
import cl.duocuc.evaluacion2.data.model.EstadoGestion
import cl.duocuc.evaluacion2.data.model.PotencialComercial
import cl.duocuc.evaluacion2.data.model.Prioridad
import cl.duocuc.evaluacion2.data.model.TipoOportunidad
import cl.duocuc.evaluacion2.ui.viewmodels.OportunidadViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Detalle de una oportunidad seleccionada, con opción de editarla.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OportunidadDetailScreen(
    id: Long,
    viewModel: OportunidadViewModel,
    onVolver: () -> Unit,
    onEditar: () -> Unit,
    onEliminada: () -> Unit,
    modifier: Modifier = Modifier
) {
    val oportunidades by viewModel.oportunidades.collectAsState()
    val oportunidad = oportunidades.firstOrNull { it.id == id }
    var confirmarEliminacion by remember(id) { mutableStateOf(false) }
    var eliminando by remember(id) { mutableStateOf(false) }
    var errorEliminacion by remember(id) { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Detalle de oportunidad", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (oportunidad != null) {
                        IconButton(onClick = onEditar) {
                            Icon(Icons.Outlined.Edit, contentDescription = "Editar oportunidad")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (oportunidad == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "La oportunidad ya no está disponible.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            DetalleContenido(
                oportunidad = oportunidad,
                onEliminar = { confirmarEliminacion = true },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }

    if (confirmarEliminacion && oportunidad != null) {
        AlertDialog(
            onDismissRequest = {
                if (!eliminando) {
                    confirmarEliminacion = false
                    errorEliminacion = null
                }
            },
            title = { Text("Eliminar oportunidad") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("¿Seguro que deseas eliminar «${oportunidad.nombre}»? Esta acción no se puede deshacer.")
                    errorEliminacion?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = !eliminando,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = {
                        eliminando = true
                        errorEliminacion = null
                        viewModel.eliminarOportunidad(
                            id = id,
                            onListo = onEliminada,
                            onError = { mensaje ->
                                eliminando = false
                                errorEliminacion = mensaje
                            }
                        )
                    }
                ) {
                    Text(if (eliminando) "Eliminando..." else "Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !eliminando,
                    onClick = {
                        confirmarEliminacion = false
                        errorEliminacion = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun DetalleContenido(
    oportunidad: OpportunityEntity,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tipo = TipoOportunidad.desdeNombre(oportunidad.tipo)
    val prioridad = Prioridad.desdeNombre(oportunidad.prioridad)
    val potencial = PotencialComercial.desdeNombre(oportunidad.potencialComercial)
    val estado = EstadoGestion.desdeNombre(oportunidad.estado)
    val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = tipo.etiqueta,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = oportunidad.nombre,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Text(
                text = estado.etiqueta,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CampoDetalle("Descripción", oportunidad.descripcion)
                CampoDetalle("Fuente u origen", oportunidad.fuente)
                CampoDetalle("Prioridad", prioridad.etiqueta)
                CampoDetalle("Potencial comercial", potencial.etiqueta)
                CampoDetalle("Estado de gestión", estado.etiqueta)
                if (oportunidad.observaciones.isNotBlank()) {
                    CampoDetalle("Observaciones", oportunidad.observaciones)
                }
                if (oportunidad.codigoQr.isNotBlank()) {
                    CampoDetalle("Referencia QR", oportunidad.codigoQr)
                }
            }
        }

        Text(
            text = "Creada: ${formatoFecha.format(Date(oportunidad.fechaCreacion))}" +
                "\nÚltima actualización: ${formatoFecha.format(Date(oportunidad.fechaActualizacion))}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedButton(
            onClick = onEliminar,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Outlined.Delete, contentDescription = null)
            Text("Eliminar oportunidad", modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun CampoDetalle(etiqueta: String, valor: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
