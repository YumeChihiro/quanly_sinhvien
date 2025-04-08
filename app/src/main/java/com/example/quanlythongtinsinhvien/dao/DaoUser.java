package com.example.quanlythongtinsinhvien.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.quanlythongtinsinhvien.entities.UserEntity;

@Dao
public interface DaoUser {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserEntity user);

    @Query("SELECT * FROM User WHERE email = :email and password = :password")
    UserEntity getUserByEmailAndPassword(String email, String password);

}
