package cl.duocuc.evaluacion2

import cl.duocuc.evaluacion2.qr.QrMaxValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QrMaxValidatorTest {

    @Test
    fun contenidoConPrefijoYCodigoEsValido() {
        assertTrue(QrMaxValidator.esValido("MAX:OP-001"))
    }

    @Test
    fun contenidoSinPrefijoNoEsValido() {
        assertFalse(QrMaxValidator.esValido("https://ejemplo.com/op-001"))
    }

    @Test
    fun contenidoSoloConPrefijoNoEsValido() {
        assertFalse(QrMaxValidator.esValido("MAX:"))
    }

    @Test
    fun contenidoVacioONuloNoEsValido() {
        assertFalse(QrMaxValidator.esValido(""))
        assertFalse(QrMaxValidator.esValido(null))
    }

    @Test
    fun prefijoEnMinusculasNoEsValido() {
        assertFalse(QrMaxValidator.esValido("max:OP-001"))
    }

    @Test
    fun extraerCodigoDevuelveSoloElIdentificador() {
        assertEquals("OP-001", QrMaxValidator.extraerCodigo("MAX:OP-001"))
        assertEquals("FAIRA-2026", QrMaxValidator.extraerCodigo("MAX:FAIRA-2026"))
    }
}
