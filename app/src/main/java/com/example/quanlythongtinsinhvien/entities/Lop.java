package com.example.quanlythongtinsinhvien.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Lop",  indices = {@Index(value = {"MaLop", "tenLop"}, unique = true)},
        foreignKeys = @ForeignKey(
                entity = Khoa.class,
                parentColumns = "maKhoa",
                childColumns = "maKhoa",
                onDelete = ForeignKey.CASCADE))
public class Lop {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "MaLop")
    private String maLop;

    @ColumnInfo(name = "tenLop")
    @NonNull
    private String tenLop;

    @ColumnInfo(name = "maKhoa")
    @NonNull
    private String khoaId;

    public Lop() {
    }

    public Lop(@NonNull String maLop, @NonNull String tenLop, @NonNull String khoaId) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.khoaId = khoaId;
    }

    @NonNull
    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(@NonNull String maLop) {
        this.maLop = maLop;
    }

    @NonNull
    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(@NonNull String tenLop) {
        this.tenLop = tenLop;
    }

    @NonNull
    public String getKhoaId() {
        return khoaId;
    }

    public void setKhoaId(@NonNull String khoaId) {
        this.khoaId = khoaId;
    }
}
