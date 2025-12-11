package com.example.jewelryshopapp.data

class ItemRepository(private val itemDao: ItemDao) {

    // Получение всех изделий из базы
    suspend fun getAllItems(): List<Item> {
        return itemDao.getAllItems()
    }

    // Добавление изделия
    suspend fun addItem(item: Item) {
        itemDao.insertItem(item)
    }
}
