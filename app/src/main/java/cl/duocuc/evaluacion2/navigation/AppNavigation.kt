package cl.duocuc.evaluacion2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duocuc.evaluacion2.ui.screens.FormularioOportunidadScreen
import cl.duocuc.evaluacion2.ui.screens.LoginScreen
import cl.duocuc.evaluacion2.ui.screens.MenuScreen
import cl.duocuc.evaluacion2.ui.screens.OportunidadDetailScreen
import cl.duocuc.evaluacion2.ui.screens.OportunidadListScreen
import cl.duocuc.evaluacion2.ui.screens.PantallaInicialScreen
import cl.duocuc.evaluacion2.ui.viewmodels.LoginViewModel
import cl.duocuc.evaluacion2.ui.viewmodels.OportunidadViewModel

/** Rutas de la aplicación (fuente única de verdad). */
sealed class Rutas(val route: String) {
    object PantallaInicial : Rutas("pantalla_inicial")
    object Login : Rutas("login")
    object MenuPrincipal : Rutas("menu_principal")
    object Oportunidades : Rutas("oportunidades")
    object DetalleOportunidad : Rutas("detalle_oportunidad/{id}") {
        fun conId(id: Long) = "detalle_oportunidad/$id"
    }
    object FormularioOportunidad : Rutas("formulario_oportunidad?id={id}") {
        fun conId(id: Long) = "formulario_oportunidad?id=$id"
    }
}

/**
 * Host de navegación: organiza el flujo de pantallas de la aplicación.
 */
@Composable
fun AppNavigation(
    loginViewModel: LoginViewModel,
    oportunidadViewModel: OportunidadViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.PantallaInicial.route
    ) {
        composable(Rutas.PantallaInicial.route) {
            PantallaInicialScreen(
                onIngresar = { navController.navigate(Rutas.Login.route) }
            )
        }

        composable(Rutas.Login.route) {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginExitoso = {
                    navController.navigate(Rutas.MenuPrincipal.route) {
                        popUpTo(Rutas.PantallaInicial.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.MenuPrincipal.route) {
            MenuScreen(
                onOportunidades = { navController.navigate(Rutas.Oportunidades.route) },
                onCerrarSesion = {
                    loginViewModel.cerrarSesion()
                    navController.navigate(Rutas.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.Oportunidades.route) {
            OportunidadListScreen(
                viewModel = oportunidadViewModel,
                onVolver = { navController.popBackStack() },
                onNueva = {
                    navController.navigate(Rutas.FormularioOportunidad.conId(0L))
                },
                onSeleccionar = { id ->
                    navController.navigate(Rutas.DetalleOportunidad.conId(id))
                }
            )
        }

        composable(
            route = Rutas.DetalleOportunidad.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { entry ->
            val id = entry.arguments?.getLong("id") ?: 0L
            OportunidadDetailScreen(
                id = id,
                viewModel = oportunidadViewModel,
                onVolver = { navController.popBackStack() },
                onEditar = {
                    navController.navigate(Rutas.FormularioOportunidad.conId(id))
                }
            )
        }

        composable(
            route = Rutas.FormularioOportunidad.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { entry ->
            val id = entry.arguments?.getLong("id") ?: 0L
            FormularioOportunidadScreen(
                id = id,
                viewModel = oportunidadViewModel,
                onVolver = { navController.popBackStack() },
                onGuardado = { idGuardado ->
                    navController.navigate(Rutas.DetalleOportunidad.conId(idGuardado)) {
                        popUpTo(Rutas.Oportunidades.route)
                    }
                }
            )
        }
    }
}
