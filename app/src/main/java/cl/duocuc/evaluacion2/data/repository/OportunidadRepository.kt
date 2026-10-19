package cl.duocuc.evaluacion2.data.repository

import cl.duocuc.evaluacion2.data.local.OpportunityDao
import cl.duocuc.evaluacion2.data.local.OpportunityEntity
import cl.duocuc.evaluacion2.data.model.EstadoGestion
import cl.duocuc.evaluacion2.data.model.PotencialComercial
import cl.duocuc.evaluacion2.data.model.Prioridad
import cl.duocuc.evaluacion2.data.model.TipoOportunidad
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio de oportunidades: única puerta de acceso a la persistencia
 * local (Room). La UI y los ViewModels no conocen la base de datos.
 */
class OportunidadRepository(private val dao: OpportunityDao) {

    fun observeAll(): Flow<List<OpportunityEntity>> = dao.observeAll()

    suspend fun contar(): Int = dao.count()

    /** Inserta una oportunidad nueva o actualiza una existente. Devuelve el id. */
    suspend fun guardar(oportunidad: OpportunityEntity): Long {
        return if (oportunidad.id == 0L) {
            dao.insert(oportunidad)
        } else {
            // Al editar se conserva la fecha de creación original y solo se mueve la de actualización
            dao.update(oportunidad.copy(fechaActualizacion = System.currentTimeMillis()))
            oportunidad.id
        }
    }

    /** Carga datos ficticios de ejemplo solo si la tabla está vacía. */
    suspend fun semillarSiVacia() {
        if (contar() > 0) return
        datosDeEjemplo().forEach { dao.insert(it) }
    }

    private fun datosDeEjemplo(): List<OpportunityEntity> {
        val ahora = System.currentTimeMillis()
        val unDia = 24 * 60 * 60 * 1000L

        return listOf(
            OpportunityEntity(
                nombre = "Feria de bienestar laboral",
                tipo = TipoOportunidad.JORNADA_DE_BENEFICIOS.name,
                descripcion = "Jornada de beneficios dirigida a colaboradores de Corporación Horizonte, con stands de salud, servicios y actividades para las familias.",
                fuente = "Referencia",
                prioridad = Prioridad.ALTA.name,
                potencialComercial = PotencialComercial.ALTO.name,
                estado = EstadoGestion.PROPUESTA_ENVIADA.name,
                observaciones = "Datos de demostración ficticios. Buscar contacto del área de eventos.",
                codigoQr = "MAX:OP-001",
                fechaCreacion = ahora - 5 * unDia,
                fechaActualizacion = ahora - 1 * unDia
            ),
            OpportunityEntity(
                nombre = "Encuentro de proveedores locales",
                tipo = TipoOportunidad.ACTIVIDAD_MUNICIPAL.name,
                descripcion = "Actividad municipal que reúne a proveedores de la zona; oportunidad de activación con público familiar y comercio local.",
                fuente = "Redes sociales",
                prioridad = Prioridad.MEDIA.name,
                potencialComercial = PotencialComercial.MEDIO.name,
                estado = EstadoGestion.CONTACTO_PENDIENTE.name,
                observaciones = "Datos de demostración ficticios.",
                codigoQr = "MAX:OP-002",
                fechaCreacion = ahora - 3 * unDia,
                fechaActualizacion = ahora - 2 * unDia
            ),
            OpportunityEntity(
                nombre = "Expo empresas y beneficios",
                tipo = TipoOportunidad.FERIA_EMPRESARIAL.name,
                descripcion = "Feria empresarial en el Centro Empresarial Andino donde las empresas presentan beneficios corporativos a trabajadores de la zona.",
                fuente = "Sitio web",
                prioridad = Prioridad.ALTA.name,
                potencialComercial = PotencialComercial.ALTO.name,
                estado = EstadoGestion.EVENTO_IDENTIFICADO.name,
                observaciones = "Datos de demostración ficticios. Evaluar costo de stand.",
                codigoQr = "MAX:OP-003",
                fechaCreacion = ahora - 1 * unDia,
                fechaActualizacion = ahora
            )
        )
    }
}
