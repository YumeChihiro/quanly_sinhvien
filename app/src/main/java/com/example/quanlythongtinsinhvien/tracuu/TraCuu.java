package com.example.quanlythongtinsinhvien.tracuu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythongtinsinhvien.Home;
import com.example.quanlythongtinsinhvien.R;
import com.example.quanlythongtinsinhvien.dao.DaoDiem;
import com.example.quanlythongtinsinhvien.dao.DaoLop;
import com.example.quanlythongtinsinhvien.dao.DaoMon;
import com.example.quanlythongtinsinhvien.dao.DaoSinhVien;
import com.example.quanlythongtinsinhvien.entities.Diem;
import com.example.quanlythongtinsinhvien.entities.QuanLySinhVien;
import com.example.quanlythongtinsinhvien.entities.SinhVien;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

public class TraCuu extends AppCompatActivity {

    ImageButton btn_back, btn_search;
    EditText edit_text_search_masv;
    TextView text_view_lop, text_view_masv, text_view_ten, text_view_khoa, tinchi, tinhtrang, diem15p, giuaki, cuoiki, diemtrungbinh;
    Spinner spinnerHocKi, spinnerMon;

    LinearLayout linear_traucuumon;

    List<String> maHKList = Arrays.asList("1", "2", "3");
    List<String> tenMonList = new ArrayList<>();
    List<String> maMonList = new ArrayList<>(); // Thêm list để lưu mã môn tương ứng
    String selectedHocKi = "1"; // Mặc định học kì 1
    String selectedMon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tra_cuu);
        init();
        Action();
    }

    public void init() {
        btn_back = findViewById(R.id.btn_back);
        btn_search = findViewById(R.id.btn_search);
        edit_text_search_masv = findViewById(R.id.edit_text_search_masv);
        text_view_lop = findViewById(R.id.text_view_lop);
        text_view_masv = findViewById(R.id.text_view_masv);
        text_view_ten = findViewById(R.id.text_view_ten);
        text_view_khoa = findViewById(R.id.text_view_khoa);
        diem15p = findViewById(R.id.diem15p);
        giuaki = findViewById(R.id.giuaki);
        cuoiki = findViewById(R.id.cuoiki);
        diemtrungbinh = findViewById(R.id.diemtrungbinh);
        tinhtrang = findViewById(R.id.tinhtrang);
        tinchi = findViewById(R.id.tinchi);

        linear_traucuumon = (LinearLayout) findViewById(R.id.linear_traucuumon);

        spinnerHocKi = findViewById(R.id.spinnerHocKi);
        spinnerMon = findViewById(R.id.spinnerMon);

        // Setup spinner học kỳ
        ArrayAdapter<String> adapterHocKi = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, maHKList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.parseColor("#FF5722")); // Màu chữ cho mục được chọn
                return textView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setTextColor(Color.parseColor("#4CAF50")); // Màu chữ cho danh sách xổ xuống
                return textView;
            }
        };
        adapterHocKi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHocKi.setAdapter(adapterHocKi);

        // Setup spinner môn học
        ArrayAdapter<String> adapterMon = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenMonList);
        adapterMon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMon.setAdapter(adapterMon);
    }

    public void Action() {
        btn_back.setOnClickListener(view -> {
            Intent home = new Intent(TraCuu.this, Home.class);
            startActivity(home);
        });

        btn_search.setOnClickListener(view -> {
            String maSv = edit_text_search_masv.getText().toString().trim();
            if (maSv.isEmpty()) {
                edit_text_search_masv.setError("Vui lòng nhập mã sinh viên");
                edit_text_search_masv.requestFocus();
                return;
            }
            getSinhVien(maSv);
        });

        // Xử lý sự kiện chọn học kỳ

        spinnerHocKi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean checkFirst = false;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!checkFirst){
                    checkFirst = true;
                    return;
                }

                linear_traucuumon.setVisibility(View.VISIBLE);
                selectedHocKi = maHKList.get(position);
                getAllMon(selectedHocKi);
                updateTinChiInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Xử lý sự kiện chọn môn học
        spinnerMon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!tenMonList.isEmpty() && position < maMonList.size()) {
                    selectedMon = maMonList.get(position); // Lấy mã môn thay vì tên môn
                    String maSv = text_view_masv.getText().toString();
                    if (!maSv.isEmpty()) {
                        getDiem(selectedMon, maSv);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void getSinhVien(String maSv) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(TraCuu.this);
                DaoSinhVien daoSinhVien = quanLySinhVien.DaoSinhVien();
                SinhVien sv = daoSinhVien.getById(maSv);
                if (sv != null) {
                    runOnUiThread(() -> {
                        text_view_lop.setText(sv.getMaLop());
                        text_view_masv.setText(sv.getMsSV());
                        text_view_ten.setText(sv.getHoTen());
                        getKhoa(sv.getMaLop());
                        // Reload môn học cho học kỳ hiện tại
                        getAllMon(selectedHocKi);
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(TraCuu.this, "Không tìm thấy sinh viên", Toast.LENGTH_SHORT).show();
                        // Clear all fields when no student is found
                        clearAllFields();
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(TraCuu.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    clearAllFields();
                });
            }
        });
    }

    private void clearAllFields() {
        text_view_lop.setText("");
        text_view_masv.setText("");
        text_view_ten.setText("");
        text_view_khoa.setText("");
        diem15p.setText("");
        giuaki.setText("");
        cuoiki.setText("");
        diemtrungbinh.setText("");
        tinchi.setText("");
        tinhtrang.setText("");
        tenMonList.clear();
        maMonList.clear();
        spinnerMon.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenMonList));
    }

    private void updateTinChiInfo() {
        String maSv = text_view_masv.getText().toString();
        if (!maSv.isEmpty()) {
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(TraCuu.this);
                    DaoMon daoMon = quanLySinhVien.DaoMonHoc();
                    List<String> allMaMon = daoMon.getALlMaMonByMaHocKi(selectedHocKi);
                    int tongTinChi = daoMon.getTotalTinChi(allMaMon);

                    List<String> allMaMonKhongDat = daoMon.getMaMonUnderFive(maSv, allMaMon);

                    if (allMaMonKhongDat.isEmpty()) {
                        runOnUiThread(() -> {
                            tinchi.setText(tongTinChi + "/" + tongTinChi);
                            tinhtrang.setText("Đạt");
                        });
                    } else {
                        List<String> maMonKhongDatOfHK = new ArrayList<>(allMaMon);
                        maMonKhongDatOfHK.retainAll(allMaMonKhongDat);
                        final int tongTinChiDat = tongTinChi - daoMon.getTotalTinChi(maMonKhongDatOfHK);
                        runOnUiThread(() -> {
                            tinchi.setText(tongTinChiDat + "/" + tongTinChi);
                            tinhtrang.setText(maMonKhongDatOfHK.isEmpty() ? "Đạt" : "Nợ");
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(TraCuu.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }

    public void getKhoa(String maLop) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(TraCuu.this);
                DaoLop daoLop = quanLySinhVien.DaoLop();
                String maKhoa = daoLop.getKhoaByMaLop(maLop);
                runOnUiThread(() -> {
                    text_view_khoa.setText(maKhoa);
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(TraCuu.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    public void getAllMon(String maHK) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(TraCuu.this);
                DaoMon daoMon = quanLySinhVien.DaoMonHoc();
                List<String> allTenMon = daoMon.getALlTenMonByMaHocKi(maHK);
                List<String> allMaMon = daoMon.getALlMaMonByMaHocKi(maHK);

                runOnUiThread(() -> {
                    tenMonList.clear();
                    maMonList.clear();
                    tenMonList.addAll(allTenMon);
                    maMonList.addAll(allMaMon);

                    ArrayAdapter<String> adapterMon = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_item, tenMonList){
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            TextView textView = (TextView) super.getView(position, convertView, parent);
                            textView.setTextColor(Color.parseColor("#FF5722")); // Màu chữ cho mục được chọn
                            return textView;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                            textView.setTextColor(Color.parseColor("#4CAF50")); // Màu chữ cho danh sách xổ xuống
                            return textView;
                        }
                    };
                    adapterMon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMon.setAdapter(adapterMon);

                    // If there are subjects and a student is selected, get scores for the first subject
                    if (!maMonList.isEmpty() && !text_view_masv.getText().toString().isEmpty()) {
                        selectedMon = maMonList.get(0);
                        getDiem(selectedMon, text_view_masv.getText().toString());
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(TraCuu.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    public void getDiem(String maMon, String maSv) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(TraCuu.this);
                DaoDiem daoDiem = quanLySinhVien.DaoDiem();
                Diem diem = daoDiem.getDiemByMaSvAndMaMon(maSv, maMon);

                runOnUiThread(() -> {
                    if (diem != null) {
                        diem15p.setText(String.valueOf(diem.getDiem15p()));
                        giuaki.setText(String.valueOf(diem.getDiemGiuaKi()));
                        cuoiki.setText(String.valueOf(diem.getDiemCuoiKi()));
                        diemtrungbinh.setText(String.valueOf(diem.getDiemTrungBinh()));
                    } else {
                        // Clear điểm khi không có dữ liệu
                        diem15p.setText("");
                        giuaki.setText("");
                        cuoiki.setText("");
                        diemtrungbinh.setText("");
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(TraCuu.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}