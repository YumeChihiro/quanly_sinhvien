package com.example.quanlythongtinsinhvien.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "SinhVien_HocKi",
        primaryKeys = {"MaSinhVien", "MaHocKi"},
        foreignKeys = {
                @ForeignKey(
                        entity = SinhVien.class,
                        parentColumns = "MsSV",
                        childColumns = "MaSinhVien",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = HocKi.class,
                        parentColumns = "MaHK",
                        childColumns = "MaHocKi",
                        onDelete = ForeignKey.CASCADE
                )

        })
public class SinhVien_HocKi {
    @ColumnInfo(name = "MaSinhVien")
    @NonNull
    private String maSinhVien;

    @ColumnInfo(name = "MaHocKi")
    @NonNull
    private String maHocKi;

    @ColumnInfo(name = "xepLoai")
    private String xepLoai;

    public SinhVien_HocKi() {
    }

    public SinhVien_HocKi(@NonNull String maSinhVien, @NonNull String maHocKi, String xepLoai) {
        this.maSinhVien = maSinhVien;
        this.maHocKi = maHocKi;
        this.xepLoai = xepLoai;
    }

    @NonNull
    public String getMaSinhVien() {
        return maSinhVien;
    }

    public void setMaSinhVien(@NonNull String maSinhVien) {
        this.maSinhVien = maSinhVien;
    }

    @NonNull
    public String getMaHocKi() {
        return maHocKi;
    }

    public void setMaHocKi(@NonNull String maHocKi) {
        this.maHocKi = maHocKi;
    }

    public String getXepLoai() {
        return xepLoai;
    }

    public void setXepLoai(String xepLoai) {
        this.xepLoai = xepLoai;
    }
}
