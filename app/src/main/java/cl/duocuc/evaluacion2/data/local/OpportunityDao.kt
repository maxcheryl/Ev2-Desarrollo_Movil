package cl.duocuc.evaluacion2.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * DAO de la tabla de oportunidades.
 * Los métodos que devuelven Flow mantienen la UI actualizada
 * en vivo sin tener que recargar a mano.
 */
@Dao
interface OpportunityDao {

    /** La lista se ordena por la última actualización para ver primero lo más reciente. */
    @Query("SELECT * FROM oportunidades ORDER BY fechaActualizacion DESC")
    fun observeAll(): Flow<List<OpportunityEntity>>

    @Query("SELECT COUNT(*) FROM oportunidades")
    suspend fun count(): Int

    // REPLACE permite usar el mismo método para crear y actualizar
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(oportunidad: OpportunityEntity): Long

    @Update
    suspend fun update(oportunidad: OpportunityEntity)

    @Query("DELETE FROM oportunidades WHERE id = :id")
    suspend fun deleteById(id: Long): Int
}
