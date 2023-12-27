    package com.example.flowershopapp.Entities.DAO

    import androidx.paging.PagingSource
    import androidx.room.Dao
    import androidx.room.Delete
    import androidx.room.Insert
    import androidx.room.Query
    import androidx.room.Update
    import com.example.flowershopapp.Entities.Model.Bouquet

    @Dao
    interface BouquetDAO {
        @Query("select * from bouquets")
        fun getAll(): PagingSource<Int, Bouquet>

        @Query("select * from bouquets where bouquetId = :id")
        fun getBouquet(id: Int): Bouquet

        @Insert
        suspend fun insert(vararg bouquet: Bouquet)

        @Update
        suspend fun update(bouquet: Bouquet)

        @Delete
        suspend fun delete(bouquet: Bouquet)

        @Query("DELETE FROM bouquets")
        suspend fun deleteAll()
    }