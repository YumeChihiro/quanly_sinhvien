package com.example.quanlythongtinsinhvien.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Diem",
        foreignKeys = {
                @ForeignKey(
                        entity = SinhVien.class,
                        parentColumns = "MsSV",
                        childColumns = "maSV",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = MonHoc.class,
                        parentColumns = "MaMon",
                        childColumns = "maMon",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"maSV", "maMon"}, unique = true)
        })
public class Diem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "DiemId")
    @NonNull
    private int diemId;

    @ColumnInfo(name = "maSV")
    @NonNull
    private String MaSV;

    @ColumnInfo(name = "maMon")
    @NonNull
    private String maMon;

    @ColumnInfo(name = "Diem15p")
    private float diem15p;

    @ColumnInfo(name = "DiemGiuaKi")
    private float diemGiuaKi;

    @ColumnInfo(name = "DiemCuoiKi")
    private float diemCuoiKi;

    @ColumnInfo(name = "DiemTrungBinh")
    private float diemTrungBinh;

    public Diem() {
    }

    public Diem(int diemId, @NonNull String maSV, @NonNull String maMon, float diem15p, float diemGiuaKi, float diemCuoiKi, float diemTrungBinh) {
        this.diemId = diemId;
        MaSV = maSV;
        this.maMon = maMon;
        this.diem15p = diem15p;
        this.diemGiuaKi = diemGiuaKi;
        this.diemCuoiKi = diemCuoiKi;
        this.diemTrungBinh = diemTrungBinh;
    }

    public Diem(@NonNull String maSV, @NonNull String maMon, float diem15p, float diemGiuaKi, float diemCuoiKi, float diemTrungBinh) {
        MaSV = maSV;
        this.maMon = maMon;
        this.diem15p = diem15p;
        this.diemGiuaKi = diemGiuaKi;
        this.diemCuoiKi = diemCuoiKi;
        this.diemTrungBinh = diemTrungBinh;
    }

    public int getDiemId() {
        return diemId;
    }

    public void setDiemId(int diemId) {
        this.diemId = diemId;
    }

    @NonNull
    public String getMaSV() {
        return MaSV;
    }

    public void setMaSV(@NonNull String maSV) {
        MaSV = maSV;
    }

    @NonNull
    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(@NonNull String maMon) {
        this.maMon = maMon;
    }

    public float getDiem15p() {
        return diem15p;
    }

    public void setDiem15p(float diem15p) {
        this.diem15p = diem15p;
    }

    public float getDiemGiuaKi() {
        return diemGiuaKi;
    }

    public void setDiemGiuaKi(float diemGiuaKi) {
        this.diemGiuaKi = diemGiuaKi;
    }

    public float getDiemCuoiKi() {
        return diemCuoiKi;
    }

    public void setDiemCuoiKi(float diemCuoiKi) {
        this.diemCuoiKi = diemCuoiKi;
    }

    public float getDiemTrungBinh() {
        return diemTrungBinh;
    }

    public void setDiemTrungBinh(float diemTrungBinh) {
        this.diemTrungBinh = diemTrungBinh;
    }
}

