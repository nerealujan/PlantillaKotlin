package db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = arrayOf(
        //aqui se añadiran las diferentes clases que conformaran la tabla de la bbdd
    ),
    version = 14,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

}
