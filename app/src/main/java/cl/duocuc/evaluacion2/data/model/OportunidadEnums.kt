package cl.duocuc.evaluacion2.data.model

/**
 * Catálogos de la aplicación (una sola fuente de verdad).
 * En la base de datos se almacena [name] y en la UI se muestra [etiqueta].
 */

enum class TipoOportunidad(val etiqueta: String) {
    EVENTO_CORPORATIVO("Evento corporativo"),
    FERIA_EMPRESARIAL("Feria empresarial"),
    ACTIVIDAD_MUNICIPAL("Actividad municipal"),
    JORNADA_DE_BENEFICIOS("Jornada de beneficios"),
    FERIA_INSTITUCIONAL("Feria institucional"),
    ACTIVIDAD_DE_TRABAJADORES("Actividad de trabajadores"),
    ACTIVACION_COMERCIAL("Activación comercial"),
    OTRA_INSTANCIA("Otra instancia");

    companion object {
        fun desdeNombre(nombre: String): TipoOportunidad =
            entries.firstOrNull { it.name == nombre } ?: EVENTO_CORPORATIVO
    }
}

enum class Prioridad(val etiqueta: String) {
    ALTA("Alta"),
    MEDIA("Media"),
    BAJA("Baja");

    companion object {
        fun desdeNombre(nombre: String): Prioridad =
            entries.firstOrNull { it.name == nombre } ?: MEDIA
    }
}

enum class PotencialComercial(val etiqueta: String) {
    ALTO("Alto"),
    MEDIO("Medio"),
    BAJO("Bajo");

    companion object {
        fun desdeNombre(nombre: String): PotencialComercial =
            entries.firstOrNull { it.name == nombre } ?: MEDIO
    }
}

enum class EstadoGestion(val etiqueta: String) {
    EVENTO_IDENTIFICADO("Evento identificado"),
    CONTACTO_PENDIENTE("Contacto pendiente"),
    CONTACTO_REALIZADO("Contacto realizado"),
    PROPUESTA_ENVIADA("Propuesta enviada"),
    EN_ESPERA_DE_RESPUESTA("En espera de respuesta"),
    PARTICIPACION_CONFIRMADA("Participación confirmada"),
    ACTIVACION_REALIZADA("Activación realizada"),
    OPORTUNIDAD_DESCARTADA("Oportunidad descartada");

    companion object {
        fun desdeNombre(nombre: String): EstadoGestion =
            entries.firstOrNull { it.name == nombre } ?: EVENTO_IDENTIFICADO
    }
}
