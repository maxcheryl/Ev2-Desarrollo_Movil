package cl.duocuc.evaluacion2.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duocuc.evaluacion2.data.local.OpportunityEntity
import cl.duocuc.evaluacion2.data.model.EstadoGestion
import cl.duocuc.evaluacion2.data.model.Prioridad
import cl.duocuc.evaluacion2.data.model.TipoOportunidad

/**
 * Tarjeta reutilizable de una oportunidad, usada en la lista principal.
 * Muestra nombre, tipo, prioridad y estado de gestión.
 */
@Composable
fun TarjetaOportunidad(
    oportunidad: OpportunityEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tipo = TipoOportunidad.desdeNombre(oportunidad.tipo)
    val prioridad = Prioridad.desdeNombre(oportunidad.prioridad)
    val estado = EstadoGestion.desdeNombre(oportunidad.estado)

    ElevatedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = tipo.etiqueta,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = oportunidad.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ChipEtiqueta(
                    texto = "Prioridad ${prioridad.etiqueta.lowercase()}",
                    colorFondo = colorDePrioridad(prioridad)
                )
                ChipEtiqueta(
                    texto = estado.etiqueta,
                    colorFondo = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
    }
}

@Composable
private fun ChipEtiqueta(texto: String, colorFondo: Color) {
    Surface(
        shape = RoundedCornerShape(50),
        color = colorFondo
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

private fun colorDePrioridad(prioridad: Prioridad): Color = when (prioridad) {
    Prioridad.ALTA -> Color(0xFFF8DEDE)
    Prioridad.MEDIA -> Color(0xFFF5E6C8)
    Prioridad.BAJA -> Color(0xFFCFE8E5)
}
