package cl.duocuc.evaluacion2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import cl.duocuc.evaluacion2.navigation.AppNavigation
import cl.duocuc.evaluacion2.ui.theme.AtenasTheme
import cl.duocuc.evaluacion2.ui.viewmodels.LoginViewModel
import cl.duocuc.evaluacion2.ui.viewmodels.OportunidadViewModel
import cl.duocuc.evaluacion2.ui.viewmodels.QrViewModel
import cl.duocuc.evaluacion2.ui.viewmodels.UbicacionViewModel

class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    private val oportunidadViewModel: OportunidadViewModel by viewModels {
        OportunidadViewModel.factory(application)
    }

    private val qrViewModel: QrViewModel by viewModels()

    private val ubicacionViewModel: UbicacionViewModel by viewModels {
        UbicacionViewModel.factory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AtenasTheme {
                AppNavigation(
                    loginViewModel = loginViewModel,
                    oportunidadViewModel = oportunidadViewModel,
                    qrViewModel = qrViewModel,
                    ubicacionViewModel = ubicacionViewModel
                )
            }
        }
    }
}
