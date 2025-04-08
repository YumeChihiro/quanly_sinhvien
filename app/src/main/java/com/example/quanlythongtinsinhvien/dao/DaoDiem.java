package com.example.quanlythongtinsinhvien.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.quanlythongtinsinhvien.entities.Diem;

import java.util.List;

@Dao
public abstract class DaoDiem {
    private static final float XUAT_SAC_THRESHOLD = 9.0f;
    private static final float GIOI_THRESHOLD = 8.0f;
    private static final float KHA_THRESHOLD = 7.0f;
    private static final float TRUNG_BINH_THRESHOLD = 5.0f;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertDiem(Diem diem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertListDiem(List<Diem> diemList);

    @Update
    protected abstract void updateDiemInternal(Diem diem);

    @Query("SELECT MaMon FROM MonHoc WHERE maHK = :maHocKi")
    protected abstract List<String> getMonHocTrongHocKi(String maHocKi);

    @Query("SELECT * FROM Diem WHERE maSV = :maSv AND maMon =:maMon")
    public abstract Diem getDiemByMaSvAndMaMon(String maSv, String maMon);

    @Query("SELECT DiemTrungBinh FROM Diem WHERE maSV = :maSV AND maMon = :maMon")
    protected abstract float getDiemMonHoc(String maSV, String maMon);

    @Query("SELECT maHK FROM MonHoc WHERE MaMon = :maMon")
    protected abstract String getHocKiCuaMon(String maMon);


    @Query("UPDATE SinhVien_HocKi SET xepLoai = :xepLoai " +
            "WHERE MaSinhVien = :maSV AND MaHocKi = :maHocKi")
    protected abstract void updateXepLoai(String maSV, String maHocKi, String xepLoai);

    @Transaction
    public void processDiemList(List<Diem> diemList) {
        if (diemList == null || diemList.isEmpty()) {
            return;
        }

        insertListDiem(diemList);
        updateXepLoaiForDiemList(diemList);
    }

    @Transaction
    public void updateDiem(Diem diem) {
        if (diem == null) {
            return;
        }

        updateDiemInternal(diem);
        updateXepLoaiForDiem(diem);
    }

    @Transaction
    public void processNewDiem(Diem diem) {
        if (diem == null || !isValidDiem(diem)) {
            throw new IllegalArgumentException("Điểm không hợp lệ");
        }
        insertDiem(diem);

        updateXepLoaiForDiem(diem);
    }

    private boolean isValidDiem(Diem diem) {
        return diem.getDiemTrungBinh() >= 0 &&
                diem.getDiemTrungBinh() <= 10 &&
                diem.getMaSV() != null &&
                !diem.getMaSV().isEmpty() &&
                diem.getMaMon() != null &&
                !diem.getMaMon().isEmpty();
    }

    private void updateXepLoaiForDiemList(List<Diem> diemList) {
        for (Diem diem : diemList) {
            updateXepLoaiForDiem(diem);
        }
    }

    private void updateXepLoaiForDiem(Diem diem) {
        String maHocKi = getHocKiCuaMon(diem.getMaMon());
        float diemTBHocKi = tinhDiemTrungBinhHocKi(diem.getMaSV(), maHocKi);
        String xepLoai = calculateXepLoai(diemTBHocKi);
        updateXepLoai(diem.getMaSV(), maHocKi, xepLoai);
    }

    protected float tinhDiemTrungBinhHocKi(String maSV, String maHocKi) {
        List<String> danhSachMon = getMonHocTrongHocKi(maHocKi);
        if (danhSachMon == null || danhSachMon.isEmpty()) {
            return 0;
        }

        float tongDiem = 0;
        for (String maMon : danhSachMon) {
            tongDiem += getDiemMonHoc(maSV, maMon);
        }

        return tongDiem / danhSachMon.size();
    }

    protected String calculateXepLoai(float diemTrungBinh) {
        if (diemTrungBinh >= XUAT_SAC_THRESHOLD) {
            return "Xuất sắc";
        } else if (diemTrungBinh >= GIOI_THRESHOLD) {
            return "Giỏi";
        } else if (diemTrungBinh >= KHA_THRESHOLD) {
            return "Khá";
        } else if (diemTrungBinh >= TRUNG_BINH_THRESHOLD) {
            return "Trung bình";
        } else {
            return "Yếu";
        }
    }
}