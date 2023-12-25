package com.example.flowershopapp.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.flowershopapp.Database.RemoteKeys.DAO.RemoteKeysDAO
import com.example.flowershopapp.Database.RemoteKeys.Model.RemoteKeys
import com.example.flowershopapp.Entities.Converters
import com.example.flowershopapp.Entities.DAO.BouquetDAO
import com.example.flowershopapp.Entities.DAO.OrderDAO
import com.example.flowershopapp.Entities.DAO.OrdersWithBouquet
import com.example.flowershopapp.Entities.DAO.UserDAO
import com.example.flowershopapp.Entities.Model.Bouquet
import com.example.flowershopapp.Entities.Model.Order
import com.example.flowershopapp.Entities.Model.OrderBouquetCrossRef
import com.example.flowershopapp.Entities.Model.User

@Database(entities = [User::class, Order::class, Bouquet::class, OrderBouquetCrossRef::class, RemoteKeys::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun bouquetDao(): BouquetDAO
    abstract fun orderDao(): OrderDAO
    abstract fun orderWithBouquetsDao(): OrdersWithBouquet
    abstract fun remoteKeysDAO(): RemoteKeysDAO

    companion object {
        private const val DB_NAME: String = "flowershop-db"
        val converters = Converters()
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private suspend fun populateDatabase(appContext: Context) {
            INSTANCE?.let { database ->
//                val order1 = Order("12.06.2023", 15000)
//                val order2 = Order("07.08.2023", 7800)
//                val order3 = Order("07.08.2022", 252)
//                val order4 = Order("23.04.2021", 5554)
//                val order5 = Order("14.12.2022", 23552)
//
//                val orderDao: OrderDAO = database.orderDao()
//                orderDao.insert(order1)
//                orderDao.insert(order2)
//                orderDao.insert(order3)
//                orderDao.insert(order4)
//                orderDao.insert(order5)
//
//                val user1 = User(1, "Alina", "23.09.2005", "89020990981", "123")
//                val user2 = User(2, "3", "3", "3", "3")
//
//                val userDao: UserDAO = database.userDao()
//                userDao.insert(user1)
//                userDao.insert(user2)
//
//                var image1 = converters.imageResourceToByteArray(appContext, R.drawable.image_bouquet1)
//                var image2 = converters.imageResourceToByteArray(appContext, R.drawable.image_bouquet2)
//                val resizedImage1 = converters.resizeByteArrayImage(image1, 300, 300)
//                val resizedImage2 = converters.resizeByteArrayImage(image2, 300, 300)
//                val bouquets = listOf(
//                    Bouquet(
//                        1,
//                        "Bouquet1",
//                        5,
//                        3432,
//                        resizedImage1,
//                    ),
//                    Bouquet(
//                        2,
//                        "Bouquet2",
//                        9,
//                        2342,
//                        resizedImage2,
//                    )
//                )
//
//                val bouquetDao: BouquetDAO = database.bouquetDao()
//                bouquets.forEach { movie ->
//                    bouquetDao.insert(movie)
//                }
//
//                val usersDao: UsersWithOrders = database.userWithOrders()
//                val usersWithOrders = listOf(
//                    UserOrderCrossRef(userId = 1, orderId = 1), // Movie A with Action
//                    UserOrderCrossRef(userId = 2, orderId = 2), // Movie A with Comedy
//                    UserOrderCrossRef(userId = 1, orderId = 3), // Movie A with Comedy
//                    UserOrderCrossRef(userId = 2, orderId = 4), // Movie A with Comedy
//                    UserOrderCrossRef(userId = 1, orderId = 5), // Movie A with Comedy
//                )
//
//                usersWithOrders.forEach { usersDao.insert(it) }
//
//                val ordersDao: OrdersWithBouquet = database.orderWithBouquetsDao()
//                val orderWithBouquets = listOf(
//                    OrderBouquetCrossRef(bouquetId = 1, orderId = 1), // Movie A with Action
//                    OrderBouquetCrossRef(bouquetId = 2, orderId = 2), // Movie A with Comedy
//                    OrderBouquetCrossRef(bouquetId = 3, orderId = 1), // Movie A with Comedy
//                    OrderBouquetCrossRef(bouquetId = 4, orderId = 2), // Movie A with Comedy
//                    OrderBouquetCrossRef(bouquetId = 5, orderId = 2), // Movie A with Comedy
//                )
//
//                orderWithBouquets.forEach { ordersDao.insert(it) }
            }
        }

        fun getInstance(appContext: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    appContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
//                    .addCallback(object : RoomDatabase.Callback() {
//                        override fun onCreate(db: SupportSQLiteDatabase) {
//                            super.onCreate(db)
//                            CoroutineScope(Dispatchers.IO).launch {
//                                populateDatabase(appContext)
//                            }
//                        }
//                    })
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}