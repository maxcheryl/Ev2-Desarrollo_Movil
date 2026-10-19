package cl.duocuc.evaluacion2.ui.viewmodels

import cl.duocuc.evaluacion2.data.model.EstadoGestion

/**
 * Estado del formulario de creación/edición de oportunidades.
 * Los campos de selección almacenan el nombre del enum correspondiente.
 */
data class OportunidadFormState(
    val id: Long = 0,
    val nombre: String = "",
    val tipo: String = "",
    val descripcion: String = "",
    val fuente: String = "",
    val prioridad: String = "",
    val potencialComercial: String = "",
    val estado: String = EstadoGestion.EVENTO_IDENTIFICADO.name,
    val observaciones: String = "",
    val codigoQr: String = "",
    val fechaCreacion: Long? = null
)

/** Errores de validación por campo (solo la UI decide cómo mostrarlos). */
data class OportunidadErrores(
    val nombre: String? = null,
    val tipo: String? = null,
    val descripcion: String? = null,
    val fuente: String? = null,
    val prioridad: String? = null,
    val potencialComercial: String? = null,
    val estado: String? = null
) {
    val hayErrores: Boolean
        get() = listOf(
            nombre, tipo, descripcion, fuente, prioridad, potencialComercial, estado
        ).any { it != null }
}

/** Opciones válidas de la fuente u origen de la oportunidad. */
object FuentesOportunidad {
    val opciones = listOf(
        "Redes sociales",
        "Sitio web",
        "Correo electrónico",
        "Contacto directo",
        "Referencia",
        "Comunicación comercial"
    )

    fun esValida(fuente: String): Boolean = fuente in opciones
}
