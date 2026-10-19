package cl.duocuc.evaluacion2

import cl.duocuc.evaluacion2.qr.QrAtenasValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QrAtenasValidatorTest {

    @Test
    fun contenidoConPrefijoYCodigoEsValido() {
        assertTrue(QrAtenasValidator.esValido("ATENAS:OP-001"))
    }

    @Test
    fun contenidoSinPrefijoNoEsValido() {
        assertFalse(QrAtenasValidator.esValido("https://ejemplo.com/op-001"))
    }

    @Test
    fun contenidoSoloConPrefijoNoEsValido() {
        assertFalse(QrAtenasValidator.esValido("ATENAS:"))
    }

    @Test
    fun contenidoVacioONuloNoEsValido() {
        assertFalse(QrAtenasValidator.esValido(""))
        assertFalse(QrAtenasValidator.esValido(null))
    }

    @Test
    fun prefijoEnMinusculasNoEsValido() {
        assertFalse(QrAtenasValidator.esValido("atenas:OP-001"))
    }

    @Test
    fun extraerCodigoDevuelveSoloElIdentificador() {
        assertEquals("OP-001", QrAtenasValidator.extraerCodigo("ATENAS:OP-001"))
        assertEquals("FAIRA-2026", QrAtenasValidator.extraerCodigo("ATENAS:FAIRA-2026"))
    }
}
