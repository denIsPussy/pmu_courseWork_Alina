package com.example.flowershopapp.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flowershopapp.Database.RemoteKeys.DAO.RemoteKeysDAO
import com.example.flowershopapp.Database.RemoteKeys.Model.RemoteKeys
import com.example.flowershopapp.Entities.DAO.BouquetDAO
import com.example.flowershopapp.Entities.DAO.OrderDAO
import com.example.flowershopapp.Entities.DAO.OrdersWithBouquet
import com.example.flowershopapp.Entities.DAO.UserDAO
import com.example.flowershopapp.Entities.Model.Bouquet
import com.example.flowershopapp.Entities.Model.Order
import com.example.flowershopapp.Entities.Model.OrderBouquetCrossRef
import com.example.flowershopapp.Entities.Model.User

@Database(
    entities = [User::class, Order::class, Bouquet::class, OrderBouquetCrossRef::class, RemoteKeys::class],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun bouquetDao(): BouquetDAO
    abstract fun orderDao(): OrderDAO
    abstract fun orderWithBouquetsDao(): OrdersWithBouquet
    abstract fun remoteKeysDAO(): RemoteKeysDAO

    companion object {
        private const val DB_NAME: String = "flowershop-db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private suspend fun populateDatabase(appContext: Context) {
            INSTANCE?.let { database ->
            }
        }

        fun getInstance(appContext: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    appContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}