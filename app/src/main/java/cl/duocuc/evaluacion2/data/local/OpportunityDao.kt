package cl.duocuc.evaluacion2.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OpportunityDao {

    @Query("SELECT * FROM oportunidades ORDER BY fechaActualizacion DESC")
    fun observeAll(): Flow<List<OpportunityEntity>>

    @Query("SELECT * FROM oportunidades WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): OpportunityEntity?

    @Query("SELECT COUNT(*) FROM oportunidades")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(oportunidad: OpportunityEntity): Long

    @Update
    suspend fun update(oportunidad: OpportunityEntity)
}
