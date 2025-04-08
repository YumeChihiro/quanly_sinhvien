package com.example.quanlythongtinsinhvien.sinhvien_activitis;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.quanlythongtinsinhvien.R;
import com.example.quanlythongtinsinhvien.dao.DaoKhoa;
import com.example.quanlythongtinsinhvien.dao.DaoLop;
import com.example.quanlythongtinsinhvien.dao.DaoSinhVien;
import com.example.quanlythongtinsinhvien.entities.QuanLySinhVien;
import com.example.quanlythongtinsinhvien.entities.SinhVien;
import com.example.quanlythongtinsinhvien.xem_adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

public class Xem_SinhVien extends AppCompatActivity {
    private Spinner spinner;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ImageButton btn_back;

    List<String> khoaList = new ArrayList<>(Arrays.asList("Rỗng"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_sinh_vien);

        init();
        getKhoaList();
    }

    private void init() {
        spinner = findViewById(R.id.spinner);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // Thiết lập adapter cho Spinner
        ArrayAdapter<String> adapterKhoa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, khoaList);
        adapterKhoa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterKhoa);

        // Xử lý khi chọn item trong Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedKhoa = khoaList.get(position);
                if (!selectedKhoa.equals("Rỗng")) {
                    getLopList(selectedKhoa); // Gọi getLopList khi có lựa chọn khoa
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không cần xử lý
            }
        });

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getKhoaList() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(Xem_SinhVien.this);
                DaoKhoa daoKhoa = quanLySinhVien.DaoKhoa();

                List<String> listKhoaName = daoKhoa.getAllTenKhoa();

                runOnUiThread(() -> {
                    if (listKhoaName != null && !listKhoaName.isEmpty()) {
                        khoaList.clear();
                        khoaList.addAll(listKhoaName);

                        // Cập nhật adapter cho Spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Xem_SinhVien.this, android.R.layout.simple_spinner_item, khoaList){
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
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }
                });

            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void getLopList(String tenKhoa) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(Xem_SinhVien.this);
                DaoLop daoLop = quanLySinhVien.DaoLop();
                DaoKhoa daoKhoa = quanLySinhVien.DaoKhoa();
                DaoSinhVien daoSinhVien = quanLySinhVien.DaoSinhVien();

                String maKhoa = daoKhoa.getMaKhoaByTenKhoa(tenKhoa);
                List<String> allMaLop = daoLop.getLopByKhoaId(maKhoa);

                // Lấy danh sách sinh viên cho mỗi lớp
                List<List<SinhVien>> allSinhVienList = new ArrayList<>();
                for (String maLop : allMaLop) {
                    List<SinhVien> sinhVienList = daoSinhVien.getSVByMaLop(maLop);
                    allSinhVienList.add(sinhVienList);
                }

                runOnUiThread(() -> {
                    if (allMaLop != null && !allMaLop.isEmpty()) {
                        // Truyền danh sách sinh viên vào Adapter
                        ViewPagerAdapter adapter = new ViewPagerAdapter(this, allMaLop, allSinhVienList);
                        viewPager.setAdapter(adapter);

                        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                            tab.setText(allMaLop.get(position));
                        }).attach();
                    } else {
                        Toast.makeText(this, "Không có lớp nào thuộc khoa này.", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show());
            }
        });
    }
}
