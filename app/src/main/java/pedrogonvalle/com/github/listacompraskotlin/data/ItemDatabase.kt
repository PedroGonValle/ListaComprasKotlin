package pedrogonvalle.com.github.listacompraskotlin.data

import androidx.room.Database
import androidx.room.RoomDatabase
import pedrogonvalle.com.github.listacompraskotlin.model.ItemModel

@Database(entities = [ItemModel::class], version = 1)
abstract class ItemDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao
}