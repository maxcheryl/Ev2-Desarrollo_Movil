package cl.duocuc.evaluacion2.validation

import cl.duocuc.evaluacion2.data.model.EstadoGestion
import cl.duocuc.evaluacion2.data.model.PotencialComercial
import cl.duocuc.evaluacion2.data.model.Prioridad
import cl.duocuc.evaluacion2.data.model.TipoOportunidad
import cl.duocuc.evaluacion2.ui.viewmodels.FuentesOportunidad
import cl.duocuc.evaluacion2.ui.viewmodels.OportunidadErrores
import cl.duocuc.evaluacion2.ui.viewmodels.OportunidadFormState

/**
 * Lógica de validación del formulario, separada de la presentación visual.
 * La UI solo consume [OportunidadErrores]; aquí no hay referencias a Compose.
 */
object OportunidadValidator {

    fun validar(form: OportunidadFormState): OportunidadErrores {
        return OportunidadErrores(
            nombre = when {
                form.nombre.isBlank() -> "El nombre es obligatorio."
                form.nombre.trim().length < 3 -> "Ingresa al menos 3 caracteres."
                else -> null
            },
            tipo = if (esTipoValido(form.tipo)) {
                null
            } else {
                "Selecciona un tipo de oportunidad."
            },
            descripcion = if (form.descripcion.isBlank()) {
                "La descripción es obligatoria."
            } else {
                null
            },
            fuente = if (FuentesOportunidad.esValida(form.fuente)) {
                null
            } else {
                "Selecciona la fuente u origen."
            },
            prioridad = if (esPrioridadValida(form.prioridad)) {
                null
            } else {
                "Selecciona una prioridad."
            },
            potencialComercial = if (esPotencialValido(form.potencialComercial)) {
                null
            } else {
                "Selecciona el potencial comercial."
            },
            estado = if (esEstadoValido(form.estado)) {
                null
            } else {
                "Selecciona un estado de gestión."
            }
        )
    }

    // valueOf lanza excepción si el string no pertenece al enum;
    // runCatching la convierte en un simple false para validar sin try/catch
    fun esTipoValido(valor: String): Boolean =
        runCatching { TipoOportunidad.valueOf(valor) }.isSuccess

    fun esPrioridadValida(valor: String): Boolean =
        runCatching { Prioridad.valueOf(valor) }.isSuccess

    fun esPotencialValido(valor: String): Boolean =
        runCatching { PotencialComercial.valueOf(valor) }.isSuccess

    fun esEstadoValido(valor: String): Boolean =
        runCatching { EstadoGestion.valueOf(valor) }.isSuccess
}
