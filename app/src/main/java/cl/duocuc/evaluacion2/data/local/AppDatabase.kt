package cl.duocuc.evaluacion2.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Base de datos Room de la aplicación.
 * Se usa una única instancia para todo el proceso porque abrir
 * varias conexiones es caro y no aporta nada en una app tan pequeña.
 */
@Database(entities = [OpportunityEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun opportunityDao(): OpportunityDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        // Double-checked locking: @Volatile garantiza que todos vean
        // la instancia recién creada y synchronized evita crear dos a la vez
        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "max.db"
                ).build().also { instance = it }
            }
    }
}
