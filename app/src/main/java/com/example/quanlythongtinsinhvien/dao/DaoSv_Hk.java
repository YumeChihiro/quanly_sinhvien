package com.example.quanlythongtinsinhvien.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.quanlythongtinsinhvien.entities.SinhVien_HocKi;

import java.util.List;

@Dao
public interface DaoSv_Hk {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListSinhVien_HocKi(List<SinhVien_HocKi> sinhVienHocKi);

    @Query("SELECT  * " +
            "FROM SinhVien_HocKi " +
            "WHERE MaSinhVien IN (:listMaSinhVien) AND MaHocKi =:maHK")
    List<SinhVien_HocKi> getXepLoaiTheoHocKiVaSinhVien(List<String> listMaSinhVien, String maHK);

    @Query("SELECT * FROM SinhVien_HocKi WHERE xepLoai = 'Xuất sắc' AND MaHocKi = :maHocKi")
    List<SinhVien_HocKi> getAllSinhVienXuatSacByHocKi(String maHocKi);

    @Query("SELECT  xepLoai " +
            "FROM SinhVien_HocKi " +
            "WHERE MaSinhVien IN (:listMaSinhVien) AND MaHocKi =:maHK")
    List<String> getXepLoai(List<String> listMaSinhVien, String maHK);
}

