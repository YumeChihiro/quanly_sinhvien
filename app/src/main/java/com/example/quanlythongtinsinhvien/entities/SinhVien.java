package com.example.quanlythongtinsinhvien.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "SinhVien",
        indices = {@Index(value = {"email"}, unique = true)},
        foreignKeys = @ForeignKey(
                entity = Lop.class,
                parentColumns = "MaLop",
                childColumns = "maLop",
                onDelete = ForeignKey.CASCADE
        )
)
public class SinhVien implements Parcelable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "MsSV")
    private String MsSV;

    @ColumnInfo(name = "hoTen")
    @NonNull
    private String HoTen;

    @ColumnInfo(name = "diaChi")
    private String DiaChi;

    @ColumnInfo(name = "ngaySinh")
    private String NgaySinh;

    @ColumnInfo(name = "gioiTinh")
    private boolean GioiTinh;

    @ColumnInfo(name = "email")
    @NonNull
    private String Email;

    @ColumnInfo(name = "phone")
    private String SoDienThoai;

    @ColumnInfo(name = "maLop")
    @NonNull
    private String MaLop;

    // Constructor mặc định
    public SinhVien() {
    }

    // Constructor đầy đủ
    public SinhVien(@NonNull String msSV, @NonNull String hoTen, String diaChi, String ngaySinh, boolean gioiTinh, @NonNull String email, String soDienThoai, @NonNull String maLop) {
        MsSV = msSV;
        HoTen = hoTen;
        DiaChi = diaChi;
        NgaySinh = ngaySinh;
        GioiTinh = gioiTinh;
        Email = email;
        SoDienThoai = soDienThoai;
        MaLop = maLop;
    }

    // Getter và Setter
    @NonNull
    public String getMsSV() {
        return MsSV;
    }

    public void setMsSV(@NonNull String msSV) {
        MsSV = msSV;
    }

    @NonNull
    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(@NonNull String hoTen) {
        HoTen = hoTen;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        NgaySinh = ngaySinh;
    }

    public boolean isGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        GioiTinh = gioiTinh;
    }

    @NonNull
    public String getEmail() {
        return Email;
    }

    public void setEmail(@NonNull String email) {
        Email = email;
    }

    public String getSoDienThoai() {
        return SoDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        SoDienThoai = soDienThoai;
    }

    @NonNull
    public String getMaLop() {
        return MaLop;
    }

    public void setMaLop(@NonNull String maLop) {
        MaLop = maLop;
    }

    // Parcelable implementation
    protected SinhVien(Parcel in) {
        MsSV = in.readString();
        HoTen = in.readString();
        DiaChi = in.readString();
        NgaySinh = in.readString();
        GioiTinh = in.readByte() != 0; // true nếu 1, false nếu 0
        Email = in.readString();
        SoDienThoai = in.readString();
        MaLop = in.readString();
    }

    public static final Creator<SinhVien> CREATOR = new Creator<SinhVien>() {
        @Override
        public SinhVien createFromParcel(Parcel in) {
            return new SinhVien(in);
        }

        @Override
        public SinhVien[] newArray(int size) {
            return new SinhVien[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(MsSV);
        dest.writeString(HoTen);
        dest.writeString(DiaChi);
        dest.writeString(NgaySinh);
        dest.writeByte((byte) (GioiTinh ? 1 : 0)); // 1 nếu true, 0 nếu false
        dest.writeString(Email);
        dest.writeString(SoDienThoai);
        dest.writeString(MaLop);
    }
}
