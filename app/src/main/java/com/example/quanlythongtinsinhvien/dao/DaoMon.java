package com.example.quanlythongtinsinhvien.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.quanlythongtinsinhvien.entities.MonHoc;

import java.util.List;

@Dao
public interface DaoMon {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListMonHoc(List<MonHoc> monHoc);

    @Query("SELECT tenMon FROM MonHoc WHERE maHK = :maHK")
    List<String> getALlTenMonByMaHocKi(String maHK);

    @Query("SELECT MaMon FROM MonHoc WHERE maHK = :maHK")
    List<String> getALlMaMonByMaHocKi(String maHK);

    @Query("SELECT MaMon FROM MonHoc WHERE tenMon = :tenMon")
    String getMaMonByTenMon(String tenMon);

    @Query("SELECT SUM(soTinChi) FROM MonHoc WHERE MaMon IN (:maMonList)")
    int getTotalTinChi(List<String> maMonList);

    @Query("SELECT maMon FROM Diem WHERE maSV = :maSV AND DiemTrungBinh < 5 AND maMon IN (:maMonList)")
    List<String> getMaMonUnderFive(String maSV, List<String> maMonList);


}
