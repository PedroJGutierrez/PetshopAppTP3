package com.proyecto.petshopapp.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItemRaw(item: CartItemEntity): Long

    @Update
    suspend fun updateItem(item: CartItemEntity)

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    suspend fun findItemByProductId(productId: Int): CartItemEntity?

    suspend fun insertItem(item: CartItemEntity) {
        val existing = findItemByProductId(item.productId)
        if (existing != null) {
            val updated = existing.copy(quantity = existing.quantity + item.quantity)
            updateItem(updated)
        } else {
            insertItemRaw(item)
        }
    }

    @Delete
    suspend fun deleteItem(item: CartItemEntity)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
