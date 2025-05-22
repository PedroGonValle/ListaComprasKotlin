package pedrogonvalle.com.github.listacompraskotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pedrogonvalle.com.github.listacompraskotlin.data.ItemDao
import pedrogonvalle.com.github.listacompraskotlin.data.ItemDatabase
import pedrogonvalle.com.github.listacompraskotlin.model.ItemModel

/**
 * Uma classe ViewModel que estende AndroidViewModel. Esta classe é responsável por gerenciar e preparar os dados para a UI.
 * Ela mantém uma referência para o DAO e um LiveData que contém a lista de itens.
 * A classe também fornece métodos para adicionar e remover itens.
 *
 * @author Ewerton Carreira
 * @version 1.0
 * @since 2023-03-01
 */
class ItemsViewModel(application: Application) : AndroidViewModel(application) {

    private val itemDao: ItemDao

    val itemsLiveData: LiveData<List<ItemModel>>

    init {
        val database = Room.databaseBuilder(
            getApplication(),
            ItemDatabase::class.java,
            "items_database"
        ).build()

        itemDao = database.itemDao()

        itemsLiveData = itemDao.getAll()
    }

    fun addItem(item: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newItem = ItemModel(name = item)
            itemDao.insert(newItem)
        }
    }

    fun removeItem(item: ItemModel) {
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.delete(item)
        }
    }
}