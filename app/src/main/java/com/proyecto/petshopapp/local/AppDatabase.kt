package com.proyecto.petshopapp.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.proyecto.petshopapp.local.CartDao
import com.proyecto.petshopapp.local.CartItemEntity

@Database(entities = [CartItemEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}
