package cl.duocuc.evaluacion2.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cl.duocuc.evaluacion2.data.model.EstadoGestion
import cl.duocuc.evaluacion2.data.model.PotencialComercial
import cl.duocuc.evaluacion2.data.model.Prioridad
import cl.duocuc.evaluacion2.data.model.TipoOportunidad
import cl.duocuc.evaluacion2.ui.viewmodels.FuentesOportunidad
import cl.duocuc.evaluacion2.ui.viewmodels.OportunidadViewModel

/**
 * Formulario de creación y edición de oportunidades.
 * Los campos obligatorios están marcados con «*» y muestran errores
 * junto al campo correspondiente.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioOportunidadScreen(
    id: Long,
    viewModel: OportunidadViewModel,
    onVolver: () -> Unit,
    onGuardado: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val editor by viewModel.editor.collectAsState()

    LaunchedEffect(id) {
        if (id == 0L) viewModel.beginNueva() else viewModel.beginEditar(id)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (id == 0L) "Nueva oportunidad" else "Editar oportunidad",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Cancelar")
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
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Datos de la oportunidad",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            FormTextField(
                label = "Nombre de la oportunidad *",
                value = editor.form.nombre,
                error = editor.errores.nombre,
                onValueChange = viewModel::onNombreChange
            )

            FormDropdown(
                label = "Tipo de oportunidad *",
                selected = editor.form.tipo,
                options = TipoOportunidad.entries.map { OpcionDropdown(it.name, it.etiqueta) },
                placeholder = "Seleccione un tipo",
                error = editor.errores.tipo,
                onSelected = viewModel::onTipoChange
            )

            FormTextField(
                label = "Descripción *",
                value = editor.form.descripcion,
                error = editor.errores.descripcion,
                singleLine = false,
                onValueChange = viewModel::onDescripcionChange
            )

            FormDropdown(
                label = "Fuente u origen *",
                selected = editor.form.fuente,
                options = FuentesOportunidad.opciones.map { OpcionDropdown(it, it) },
                placeholder = "Seleccione una fuente",
                error = editor.errores.fuente,
                onSelected = viewModel::onFuenteChange
            )

            Text(
                text = "Gestión",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            FormDropdown(
                label = "Prioridad *",
                selected = editor.form.prioridad,
                options = Prioridad.entries.map { OpcionDropdown(it.name, it.etiqueta) },
                placeholder = "Seleccione una prioridad",
                error = editor.errores.prioridad,
                onSelected = viewModel::onPrioridadChange
            )

            FormDropdown(
                label = "Potencial comercial *",
                selected = editor.form.potencialComercial,
                options = PotencialComercial.entries.map { OpcionDropdown(it.name, it.etiqueta) },
                placeholder = "Seleccione el potencial",
                error = editor.errores.potencialComercial,
                onSelected = viewModel::onPotencialComercialChange
            )

            FormDropdown(
                label = "Estado de gestión *",
                selected = editor.form.estado,
                options = EstadoGestion.entries.map { OpcionDropdown(it.name, it.etiqueta) },
                placeholder = "Seleccione un estado",
                error = editor.errores.estado,
                onSelected = viewModel::onEstadoChange
            )

            Text(
                text = "Observaciones",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            FormTextField(
                label = "Observaciones adicionales (opcional)",
                value = editor.form.observaciones,
                error = null,
                singleLine = false,
                onValueChange = viewModel::onObservacionesChange
            )

            // Mensaje de error global (p. ej. fallo al guardar) con animación
            AnimatedVisibility(
                visible = editor.mensajeError != null,
                enter = fadeIn(animationSpec = tween(250)) + expandVertically(),
                exit = fadeOut(animationSpec = tween(200)) + shrinkVertically()
            ) {
                Text(
                    text = editor.mensajeError.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = { viewModel.guardarOportunidad(onGuardado) },
                    enabled = !editor.guardando,
                    modifier = Modifier.weight(1f)
                ) {
                    if (editor.guardando) {
                        CircularProgressIndicator(
                            modifier = Modifier.height(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Guardar")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

private data class OpcionDropdown(val valor: String, val etiqueta: String)

@Composable
private fun FormTextField(
    label: String,
    value: String,
    error: String?,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        isError = error != null,
        supportingText = error?.let {
            {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine,
        maxLines = if (singleLine) 1 else 4
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormDropdown(
    label: String,
    selected: String,
    options: List<OpcionDropdown>,
    placeholder: String,
    error: String?,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = options.firstOrNull { it.valor == selected }?.etiqueta ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            isError = error != null,
            supportingText = error?.let {
                {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion.etiqueta) },
                    onClick = {
                        onSelected(opcion.valor)
                        expanded = false
                    }
                )
            }
        }
    }
}
