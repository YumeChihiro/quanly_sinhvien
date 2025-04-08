package com.example.quanlythongtinsinhvien.entities;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.quanlythongtinsinhvien.dao.DaoDiem;
import com.example.quanlythongtinsinhvien.dao.DaoHocKi;
import com.example.quanlythongtinsinhvien.dao.DaoKhoa;
import com.example.quanlythongtinsinhvien.dao.DaoLop;
import com.example.quanlythongtinsinhvien.dao.DaoMon;
import com.example.quanlythongtinsinhvien.dao.DaoSinhVien;
import com.example.quanlythongtinsinhvien.dao.DaoSv_Hk;
import com.example.quanlythongtinsinhvien.dao.DaoUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {
        SinhVien.class,
        Khoa.class,
        Lop.class,
        HocKi.class,
        Diem.class,
        MonHoc.class,
        SinhVien_HocKi.class,
        UserEntity.class
}, version = 1)
public abstract class QuanLySinhVien extends RoomDatabase {

    public abstract DaoSinhVien DaoSinhVien();
    public abstract DaoKhoa DaoKhoa();
    public abstract DaoLop DaoLop();
    public abstract DaoHocKi DaoHocKi();
    public abstract DaoDiem DaoDiem();
    public abstract DaoMon DaoMonHoc();
    public abstract DaoSv_Hk DaoSinhVienHocKi();
    public abstract DaoUser daoUser();

    private static volatile QuanLySinhVien INSTANCE;

    private static final String[] SINH_VIEN_IDS = {
            "1SV", "2SV", "3SV", "4SV", "5SV", "6SV", "7SV",
            "8SV", "9SV", "10SV", "11SV", "22CTHA0132"
    };
    private static final String[] MON_HOC_IDS = {
            "1A", "2B", "3C", "4D", "5E", "6F", "7G", "8H"
    };
    private static final String[] HOC_KI_IDS = {"1", "2", "3", "4"};

    public static QuanLySinhVien getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (QuanLySinhVien.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    QuanLySinhVien.class,
                                    "QuanLySinhVien")
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        QuanLySinhVien database = getDatabase(context);
                                        initializeSampleData(database);
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void initializeSampleData(QuanLySinhVien database) {
        initializeUsers(database);

        initializeKhoa(database);

        initializeLop(database);

        initializeHocKi(database);

        initializeMonHoc(database);

        initializeSinhVien(database);

        initializeSinhVienHocKi(database);

        initializeDiem(database);

    }

    private static void initializeUsers(QuanLySinhVien database) {
        DaoUser userDao = database.daoUser();
        userDao.insert(new UserEntity("1", "bao@gmail.com", "123456"));
        userDao.insert(new UserEntity("2", "elaina@gmail.com", "123456"));
    }

    private static void initializeKhoa(QuanLySinhVien database) {
        DaoKhoa daoKhoa = database.DaoKhoa();
        List<Khoa> khoa = Arrays.asList(
                new Khoa("1", "Công Nghệ"),
                new Khoa("2", "Thông Tin")
        );
        daoKhoa.insertList(khoa);
    }

    private static void initializeLop(QuanLySinhVien database) {
        DaoLop daoLop = database.DaoLop();
        List<Lop> lop = Arrays.asList(
                new Lop("CCNTT22A", "Lớp A", "1"),
                new Lop("CCNTT22B", "Lớp B", "1"),
                new Lop("CCNTT22C", "Lớp C", "2"),
                new Lop("CCNTT22D", "Lớp D", "2")
        );
        daoLop.insertListLop(lop);
    }

    private static void initializeHocKi(QuanLySinhVien database) {
        DaoHocKi daoHocKi = database.DaoHocKi();
        List<HocKi> hocKi = Arrays.asList(
                new HocKi("1", "Học Kì I", "2022"),
                new HocKi("2", "Học Kì II", "2023"),
                new HocKi("3", "Học Kì III", "2024"),
                new HocKi("4", "Học Kì IV", "2025")
        );
        daoHocKi.insertListHocKi(hocKi);
    }

    private static void initializeMonHoc(QuanLySinhVien database) {
        DaoMon daoMon = database.DaoMonHoc();
        List<MonHoc> monHoc = Arrays.asList(
                new MonHoc("1A", "Lập trình Android", 3, "1"),
                new MonHoc("2B", "Lập trình Căn bản", 3, "1"),
                new MonHoc("3C", "Lập trình Truyền Thông", 2, "2"),
                new MonHoc("4D", "Cấu trúc dữ liệu ", 10, "2"),
                new MonHoc("5E", "Giải thuật", 9, "3"),
                new MonHoc("6F", "Rèn nghề Android", 1, "3"),
                new MonHoc("7G", "Rèn nghề Web", 5, "4"),
                new MonHoc("8H", "Cơ sở dữ liệu", 6, "4")
        );
        daoMon.insertListMonHoc(monHoc);
    }

    private static void initializeSinhVien(QuanLySinhVien database) {
        DaoSinhVien daoSinhVien = database.DaoSinhVien();
        List<SinhVien> sinhVien = Arrays.asList(
                new SinhVien("1SV", "Văn A", "Cần Thơ", "01-01-2004", true, "vana@gmail.com", "0123456721", "CCNTT22A"),
                new SinhVien("2SV", "Văn B", "Vĩnh Long", "01-01-2004", true, "vanb@gmail.com", "0123456722", "CCNTT22A"),
                new SinhVien("3SV", "Văn C", "Hà Tiên", "02-02-2004", true, "vanc@gmail.com", "0123456723", "CCNTT22A"),
                new SinhVien("4SV", "Văn D", "Hà Nội", "03-03-2004", true, "vand@gmail.com", "0123456724", "CCNTT22B"),
                new SinhVien("5SV", "Thị A", "Bạc Liêu", "04-04-2004", false, "thia@gmail.com", "0123456725", "CCNTT22B"),
                new SinhVien("6SV", "Thị B", "Phú Quốc", "05-05-2004", false, "thib@gmail.com", "0123456712", "CCNTT22B"),
                new SinhVien("7SV", "Thị C", "Đà Lạt", "06-06-2004", false, "thic@gmail.com", "0123456713", "CCNTT22C"),
                new SinhVien("8SV", "Thị D", "Lai Châu", "07-07-2004", false, "thida@gmail.com", "0123456714", "CCNTT22C"),
                new SinhVien("9SV", "Nguyễn A", "Cà Mau", "08-08-2004", true, "nguyena@gmail.com", "0123456715", "CCNTT22D"),
                new SinhVien("10SV", "Nguyễn B", "Côn Đảo", "09-09-2004", false, "nguyenb@gmail.com", "0123456716", "CCNTT22D"),
                new SinhVien("11SV", "Nguyễn C", "Japan", "10-10-2004", true, "nguyenc@gmail.com", "0123456717", "CCNTT22D"),
                new SinhVien("22CTHA0132", "Nguyễn Trường Bảo", "Trà Vinh", "25-02-2004", true, "truongbao022004@gmail.com", "0123456715", "CCNTT22C")
        );
        daoSinhVien.insertList(sinhVien);
    }

    private static void initializeDiem(QuanLySinhVien database) {
        DaoDiem daoDiem = database.DaoDiem();
        List<Diem> diem = new ArrayList<>();

        int diemId = 0;
        for (String sinhVienId : SINH_VIEN_IDS) {
            for (String monHocId : MON_HOC_IDS) {
                Diem diemItem = new Diem(diemId++, sinhVienId, monHocId, 10, 10, 10, 10);
                diem.add(diemItem);
            }
        }
        daoDiem.processDiemList(diem);
    }

    private static void initializeSinhVienHocKi(QuanLySinhVien database) {
        DaoSv_Hk daoSv_hk = database.DaoSinhVienHocKi();
        List<SinhVien_HocKi> sinhVien_hocKi = new ArrayList<>();

        for (String sinhvienId : SINH_VIEN_IDS) {
            for (String hockiId : HOC_KI_IDS) {
                SinhVien_HocKi sv_hk = new SinhVien_HocKi(sinhvienId, hockiId, "Yếu");
                sinhVien_hocKi.add(sv_hk);
            }
        }
        daoSv_hk.insertListSinhVien_HocKi(sinhVien_hocKi);
    }
}