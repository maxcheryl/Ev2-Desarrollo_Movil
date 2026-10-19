package cl.duocuc.evaluacion2.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val correoError: String? = null,
    val claveError: String? = null,
    val mensajeError: String? = null
)

/**
 * ViewModel del inicio de sesión con cuenta ficticia (demostración).
 * No se conecta con sistemas reales ni usa credenciales institucionales.
 */
class LoginViewModel : ViewModel() {

    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado

    fun onCorreoChange(valor: String) {
        _estado.update {
            it.copy(
                correo = valor,
                correoError = null,
                mensajeError = null
            )
        }
    }

    fun onClaveChange(valor: String) {
        _estado.update {
            it.copy(
                clave = valor,
                claveError = null,
                mensajeError = null
            )
        }
    }

    /** Valida el formulario y las credenciales de demostración. */
    fun ingresar(): Boolean {
        val actual = _estado.value

        val correoError = if (actual.correo.isBlank()) "El correo es obligatorio." else null
        val claveError = if (actual.clave.isBlank()) "La contraseña es obligatoria." else null

        if (correoError != null || claveError != null) {
            _estado.update {
                it.copy(correoError = correoError, claveError = claveError)
            }
            return false
        }

        val credencialesValidas =
            actual.correo.trim().equals(DEMO_CORREO, ignoreCase = true) &&
                actual.clave == DEMO_CLAVE

        return if (credencialesValidas) {
            _estado.update { it.copy(mensajeError = null) }
            true
        } else {
            _estado.update {
                it.copy(mensajeError = "Correo o contraseña incorrectos. Usa la cuenta de demostración.")
            }
            false
        }
    }

    fun cerrarSesion() {
        _estado.value = LoginUiState()
    }

    companion object {
        const val DEMO_CORREO = "dali.morales@max.com"
        const val DEMO_CLAVE = "dali123"
        const val DEMO_NOMBRE = "Dali"
    }
}
