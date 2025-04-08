package com.example.quanlythongtinsinhvien.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.quanlythongtinsinhvien.entities.Khoa;

import java.util.List;

@Dao
public interface DaoKhoa {

    @Query("SELECT tenKhoa FROM Khoa WHERE maKhoa = :maKhoa")
    String getTenKhoaByMaKhoa(String maKhoa);

    @Query("SELECT tenKhoa FROM Khoa")
    List<String> getAllTenKhoa();

    @Query("SELECT maKhoa FROM Khoa WHERE tenKhoa = :tenKhoa")
    String getMaKhoaByTenKhoa(String tenKhoa);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Khoa khoa);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<Khoa> khoa);

}
