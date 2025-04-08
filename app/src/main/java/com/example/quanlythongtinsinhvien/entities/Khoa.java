package com.example.quanlythongtinsinhvien.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Khoa")
public class Khoa {

    @PrimaryKey
    @NonNull
    private String maKhoa;

    @NonNull
    private String tenKhoa;

    public Khoa(@NonNull String maKhoa, @NonNull String tenKhoa) {
        this.maKhoa = maKhoa;
        this.tenKhoa = tenKhoa;
    }

    @NonNull
    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(@NonNull String maKhoa) {
        this.maKhoa = maKhoa;
    }

    @NonNull
    public String getTenKhoa() {
        return tenKhoa;
    }

    public void setTenKhoa(@NonNull String tenKhoa) {
        this.tenKhoa = tenKhoa;
    }
}
