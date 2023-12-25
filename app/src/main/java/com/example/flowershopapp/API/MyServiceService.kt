package com.example.flowershopapp.API

import com.example.flowershopapp.API.Model.BouquetRemote
import com.example.flowershopapp.API.Model.OrderBouquetCrossRefRemote
import com.example.flowershopapp.API.Model.OrderByDateRemote
import com.example.flowershopapp.API.Model.OrderRemote
import com.example.flowershopapp.API.Model.UserRemote
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MyServerService {

    @POST("users")
    suspend fun createUser(
        @Body user: UserRemote,
    ): UserRemote

    @GET("users")
    suspend fun getUsers(): List<UserRemote>

    @GET("users")
    suspend fun getUser(
        @Query("userName") userName: String
    ): List<UserRemote>

    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body user: UserRemote,
    ): UserRemote

    @DELETE("users/{id}")
    suspend fun deleteUser(
        @Path("id") id: Int,
    ): UserRemote

    @GET("/ordersByUser")
    suspend fun getOrders(
        @Query("_userId") userId: Int,
        @Query("_page") page: Int,
        @Query("_limit") limit: Int,
    ): List<OrderRemote>

    @GET("orders/{id}")
    suspend fun getOrder(
        @Path("id") id: Int,
    ): OrderRemote

    @POST("orders")
    suspend fun createOrder(
        @Body order: OrderRemote,
    ): OrderRemote

    @DELETE("orders/{id}")
    suspend fun deleteOrder(
        @Path("id") id: Int,
    ): OrderRemote

    @GET("bouquets")
    suspend fun getBouquets(
        @Query("_page") page: Int,
        @Query("_limit") limit: Int,
    ): List<BouquetRemote>

    @GET("/ordersByDate")
    suspend fun getOrdersByDate(
        @Query("userId") userId: Int,
        @Query("start") startDate: String,
        @Query("end") endDate: String
    ): OrderByDateRemote

    @GET("/popularBouquets")
    suspend fun getPopularBouquets(): List<BouquetRemote>

    @GET("bouquets")
    suspend fun getAllBouquets(): List<BouquetRemote>

    @GET("bouquets")
    suspend fun getBouquetsSortedByPrice(
        @Query("_sort") sortBy: String,
        @Query("_order") order: String
    ): List<BouquetRemote>

    @GET("bouquets/{id}")
    suspend fun getBouquet(
        @Path("id") id: Int,
    ): BouquetRemote

    @POST("bouquets")
    suspend fun createBouquet(
        @Body bouquet: BouquetRemote,
    ): BouquetRemote

    @PUT("bouquets/{id}")
    suspend fun updateBouquet(
        @Path("id") id: Int,
        @Body bouquet: BouquetRemote,
    ): BouquetRemote

    @DELETE("bouquets/{id}")
    suspend fun deleteBouquet(
        @Path("id") id: Int,
    ): BouquetRemote

    @POST("orderBouquetCrossRefs")
    suspend fun createOrderBouquet(
        @Body orderBouquet: OrderBouquetCrossRefRemote,
    )

    @GET("orderBouquetCrossRefs")
    suspend fun getOrdersBouquets(): List<OrderBouquetCrossRefRemote>

    @DELETE("orderBouquetCrossRefs/{id}")
    suspend fun deleteOrderBouquet(
        @Path("id") id: Int,
    )

    @DELETE("userOrderCrossRefs/{id}")
    suspend fun deleteUserOrder(
        @Path("id") id: Int,
    )

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8079/"

        @Volatile
        private var INSTANCE: MyServerService? = null

        fun getInstance(): MyServerService {
            return INSTANCE ?: synchronized(this) {
                val logger = HttpLoggingInterceptor()
                logger.level = HttpLoggingInterceptor.Level.BASIC
                val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                    .build()
                    .create(MyServerService::class.java)
                    .also { INSTANCE = it }
            }
        }
    }
}