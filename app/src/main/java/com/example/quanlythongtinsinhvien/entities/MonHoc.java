package com.example.quanlythongtinsinhvien.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "MonHoc", indices = {@Index(value = {"tenMon"}, unique = true)},
        foreignKeys = @ForeignKey(
                entity = HocKi.class,
                parentColumns = "MaHK",
                childColumns = "maHK",
                onDelete = ForeignKey.CASCADE))
public class MonHoc {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "MaMon")
    private String maMon;

    @NonNull
    @ColumnInfo(name = "tenMon")
    private String tenMon;

    @NonNull
    @ColumnInfo(name = "soTinChi")
    private int soTinchi;

    @NonNull
    @ColumnInfo(name = "maHK")
    private String maHK;


    public MonHoc() {
    }

    public MonHoc(@NonNull String maMon, @NonNull String tenMon, int soTinchi, @NonNull String maHK) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.soTinchi = soTinchi;
        this.maHK = maHK;
    }

    @NonNull
    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(@NonNull String maMon) {
        this.maMon = maMon;
    }

    @NonNull
    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(@NonNull String tenMon) {
        this.tenMon = tenMon;
    }

    public int getSoTinchi() {
        return soTinchi;
    }

    public void setSoTinchi(int soTinchi) {
        this.soTinchi = soTinchi;
    }

    @NonNull
    public String getMaHK() {
        return maHK;
    }

    public void setMaHK(@NonNull String maHK) {
        this.maHK = maHK;
    }
}
