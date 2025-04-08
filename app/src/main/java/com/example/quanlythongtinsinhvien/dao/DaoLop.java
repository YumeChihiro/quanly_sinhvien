package com.example.quanlythongtinsinhvien.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.quanlythongtinsinhvien.entities.Lop;

import java.util.List;

@Dao
public interface DaoLop {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListLop(List<Lop> lop);

    @Query("SELECT MaLop FROM Lop")
    List<String> getAllMaLop();


    @Query("SELECT tenLop FROM Lop WHERE MaLop = :maLop")
    String getTenLopById(String maLop);


    @Query("SELECT maLop FROM Lop WHERE maKhoa =:KhoaId")
    List<String> getLopByKhoaId(String KhoaId);

    @Query(("SELECT maKhoa FROM Lop WHERE maLop = :maLop"))
    String getKhoaByMaLop(String maLop);

}
