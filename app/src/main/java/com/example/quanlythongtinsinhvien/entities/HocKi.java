package com.example.quanlythongtinsinhvien.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "HocKi", indices = {@Index(value = {"tenHK"}, unique = true)})
public class HocKi {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "MaHK")
    private String MaHK;

    @ColumnInfo(name = "tenHK")
    @NonNull
    private String TenHK;

    @ColumnInfo(name = "namHoc")
    @NonNull
    private String NamHoc;

    public HocKi() {
    }

    public HocKi(@NonNull String maHK, @NonNull String tenHK, @NonNull String namHoc) {
        MaHK = maHK;
        TenHK = tenHK;
        NamHoc = namHoc;
    }

    @NonNull
    public String getMaHK() {
        return MaHK;
    }

    public void setMaHK(@NonNull String maHK) {
        MaHK = maHK;
    }

    @NonNull
    public String getTenHK() {
        return TenHK;
    }

    public void setTenHK(@NonNull String tenHK) {
        TenHK = tenHK;
    }

    @NonNull
    public String getNamHoc() {
        return NamHoc;
    }

    public void setNamHoc(@NonNull String namHoc) {
        NamHoc = namHoc;
    }
}
