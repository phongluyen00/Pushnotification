package com.example.pushnotification.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.pushnotification.model.Product;

import java.util.List;

import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ProductDAO {
    @Insert(onConflict = REPLACE)
    void insert(Product... items);

    @Query("Select * from Product")
    Single<List<Product>> findProduct();

    @Query("Delete from Product")
    void deleteAll();

}
