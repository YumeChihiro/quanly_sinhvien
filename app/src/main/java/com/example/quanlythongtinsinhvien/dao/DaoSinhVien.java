package com.example.quanlythongtinsinhvien.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlythongtinsinhvien.entities.SinhVien;

import java.util.List;

@Dao
public interface DaoSinhVien {

    @Query("SELECT * FROM SinhVien WHERE MsSV IN (:msSVList)")
    List<SinhVien> getByIds(List<String> msSVList);

    @Query("SELECT * FROM SinhVien WHERE MsSV =:mssv AND maLop = :maLop ")
    SinhVien getByIdAndLop(String mssv, String maLop);

    @Query("SELECT * FROM SinhVien WHERE MsSV =:mssv")
    SinhVien getById(String mssv);

    @Query("SELECT * FROM SinhVien WHERE maLop =:maLop")
    List<SinhVien> getSVByMaLop(String maLop);

    @Query("SELECT MsSV FROM SinhVien WHERE maLop =:maLop")
    List<String> getMaSVByMaLop(String maLop);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SinhVien sinhVien);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<SinhVien> sinhVien);

    @Update
    void update(SinhVien sinhVien);

    @Delete
    int delete(SinhVien sinhVien);

}
