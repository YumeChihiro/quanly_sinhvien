package com.example.quanlythongtinsinhvien.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.quanlythongtinsinhvien.entities.HocKi;

import java.util.List;

@Dao
public interface DaoHocKi {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListHocKi(List<HocKi> hocKi);

    @Query("SELECT tenHK FROM HocKi")
    List<String> getAllTenHocKi();

    @Query("SELECT MaHK FROM HocKi WHERE tenHK =:tenHk")
    String getMaHkByTenHK(String tenHk);

}
