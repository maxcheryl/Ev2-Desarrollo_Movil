package cl.duocuc.evaluacion2

import cl.duocuc.evaluacion2.data.model.EstadoGestion
import cl.duocuc.evaluacion2.data.model.PotencialComercial
import cl.duocuc.evaluacion2.data.model.Prioridad
import cl.duocuc.evaluacion2.data.model.TipoOportunidad
import cl.duocuc.evaluacion2.ui.viewmodels.FuentesOportunidad
import cl.duocuc.evaluacion2.ui.viewmodels.OportunidadFormState
import cl.duocuc.evaluacion2.validation.OportunidadValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OportunidadValidatorTest {

    private fun formularioValido() = OportunidadFormState(
        nombre = "Feria de bienestar 2026",
        tipo = TipoOportunidad.FERIA_EMPRESARIAL.name,
        descripcion = "Feria corporativa con beneficios para colaboradores.",
        fuente = FuentesOportunidad.opciones.first(),
        prioridad = Prioridad.ALTA.name,
        potencialComercial = PotencialComercial.ALTO.name,
        estado = EstadoGestion.EVENTO_IDENTIFICADO.name,
        observaciones = ""
    )

    @Test
    fun formularioValidoNoTieneErrores() {
        val errores = OportunidadValidator.validar(formularioValido())
        assertFalse(errores.hayErrores)
    }

    @Test
    fun camposObligatoriosVaciosReportanErrores() {
        val errores = OportunidadValidator.validar(OportunidadFormState())

        assertNotNull(errores.nombre)
        assertNotNull(errores.tipo)
        assertNotNull(errores.descripcion)
        assertNotNull(errores.fuente)
        assertNotNull(errores.prioridad)
        assertNotNull(errores.potencialComercial)
        // El estado inicia en «Evento identificado», por lo que ya es válido
        assertNull(errores.estado)
        assertTrue(errores.hayErrores)
    }

    @Test
    fun nombreMuyCortoEsRechazado() {
        val errores = OportunidadValidator.validar(formularioValido().copy(nombre = "ab"))
        assertNotNull(errores.nombre)
    }

    @Test
    fun opcionesDeSeleccionInvalidasSonRechazadas() {
        val errores = OportunidadValidator.validar(
            formularioValido().copy(
                tipo = "No es un tipo",
                fuente = "Tienda no registrada",
                prioridad = "Urgente",
                potencialComercial = "Gigantesco",
                estado = "Inventado"
            )
        )

        assertNotNull(errores.tipo)
        assertNotNull(errores.fuente)
        assertNotNull(errores.prioridad)
        assertNotNull(errores.potencialComercial)
        assertNotNull(errores.estado)
    }

    @Test
    fun observacionesEsOpcional() {
        val sinObservaciones = OportunidadValidator.validar(formularioValido())
        val conObservaciones = OportunidadValidator.validar(
            formularioValido().copy(observaciones = "Confirmar asistencia del área de marketing.")
        )

        assertFalse(sinObservaciones.hayErrores)
        assertFalse(conObservaciones.hayErrores)
    }

    @Test
    fun nombreConEspaciosEsRecortadoAlValidar() {
        val errores = OportunidadValidator.validar(formularioValido().copy(nombre = "   "))
        assertNotNull(errores.nombre)
    }
}
