package cl.duocuc.evaluacion2.qr

/**
 * Reglas de validación de los códigos QR de la aplicación.
 * Un código QR es válido para Agencia Atenas únicamente si comienza
 * con el prefijo «ATENAS:» seguido de un código identificador.
 * Ejemplo válido: ATENAS:OP-001
 */
object QrAtenasValidator {

    const val PREFIJO = "ATENAS:"

    fun esValido(contenido: String?): Boolean {
        return contenido != null &&
            contenido.startsWith(PREFIJO) &&
            contenido.length > PREFIJO.length
    }

    /** Devuelve el código identificador sin el prefijo (p. ej. «OP-001»). */
    fun extraerCodigo(contenido: String): String {
        return contenido.removePrefix(PREFIJO)
    }
}
