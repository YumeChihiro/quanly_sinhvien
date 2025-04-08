package com.example.quanlythongtinsinhvien.trichxuat;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythongtinsinhvien.R;
import com.example.quanlythongtinsinhvien.dao.DaoLop;
import com.example.quanlythongtinsinhvien.dao.DaoSinhVien;
import com.example.quanlythongtinsinhvien.dao.DaoSv_Hk;
import com.example.quanlythongtinsinhvien.entities.QuanLySinhVien;
import com.example.quanlythongtinsinhvien.entities.SinhVien;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class TrichXuat extends AppCompatActivity {

    Spinner dssinhvien, loaisv;
    ImageButton xuatDSSV, XepLoaiDs, btn_back;

    List<String> lopList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trich_xuat);
        init();
        // Xuất dữ liệu ra file Excel
        printDSSv();
        printXeLoai();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void init(){
        dssinhvien = (Spinner) findViewById(R.id.dssinhvien);
        loaisv = (Spinner) findViewById(R.id.loaisv);
        xuatDSSV = (ImageButton) findViewById(R.id.xuatDSSV);
        XepLoaiDs = (ImageButton) findViewById(R.id.XepLoaiDs);
        btn_back = (ImageButton) findViewById(R.id.btn_back);

        ArrayAdapter<String> adapterDS = new ArrayAdapter<>(TrichXuat.this, android.R.layout.simple_spinner_item, lopList);
        adapterDS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dssinhvien.setAdapter(adapterDS);
        loaisv.setAdapter(adapterDS);

        getLopList();
    }

    private void getLopList() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(TrichXuat.this);
                DaoLop daoLop  = quanLySinhVien.DaoLop();
                List<String> allMaLop = daoLop.getAllMaLop();

                runOnUiThread(() -> {
                    if (allMaLop != null && !allMaLop.isEmpty()) {
                        lopList.clear();
                        lopList.addAll(allMaLop);

                        // Cập nhật adapter cho Spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(TrichXuat.this, android.R.layout.simple_spinner_item, allMaLop){
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
                        dssinhvien.setAdapter(adapter);
                        loaisv.setAdapter(adapter);
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void printDSSv(){
        xuatDSSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maLop = dssinhvien.getSelectedItem().toString();
                if(maLop != null && !maLop.isEmpty()){
                    Executors.newSingleThreadExecutor().execute(() -> {
                        try {
                            QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(TrichXuat.this);
                            DaoSinhVien daoSinhVien = quanLySinhVien.DaoSinhVien();
                            List<SinhVien> listSinhVien = daoSinhVien.getSVByMaLop(maLop);

                            runOnUiThread(() -> {
                                if (listSinhVien != null && !listSinhVien.isEmpty()) {
                                    Workbook workbook = new XSSFWorkbook();
                                    Sheet sheet = workbook.createSheet("Sinh Viên");

                                    Row headerRow = sheet.createRow(0);
                                    headerRow.createCell(0).setCellValue("STT");
                                    headerRow.createCell(1).setCellValue("MSSV");
                                    headerRow.createCell(2).setCellValue("Họ tên");
                                    headerRow.createCell(3).setCellValue("Địa chỉ");
                                    headerRow.createCell(4).setCellValue("Ngày sinh");
                                    headerRow.createCell(5).setCellValue("Giới tính");
                                    headerRow.createCell(6).setCellValue("Email");
                                    headerRow.createCell(7).setCellValue("Số điện thoại");

                                    int rowNum = 1;
                                    for (SinhVien sv : listSinhVien) {
                                        Row row = sheet.createRow(rowNum++);
                                        row.createCell(0).setCellValue(rowNum - 1); // ID là số thứ tự
                                        row.createCell(1).setCellValue(sv.getMsSV());
                                        row.createCell(2).setCellValue(sv.getHoTen());
                                        row.createCell(3).setCellValue(sv.getDiaChi());
                                        row.createCell(4).setCellValue(sv.getNgaySinh());
                                        row.createCell(5).setCellValue(sv.isGioiTinh());
                                        row.createCell(6).setCellValue(sv.getEmail());
                                        row.createCell(7).setCellValue(sv.getSoDienThoai());
                                    }

                                    if(saveFileToDocuments(workbook, "DSSV_" + maLop + ".xlsx")){
                                        xuatDSSV.setEnabled(false);
                                    }else{
                                        xuatDSSV.setBackground(null);
                                    }
                                }
                            });
                        } catch (Exception e) {
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show());
                        }
                    });
                }
            }
        });
    }

    public void printXeLoai(){
        XepLoaiDs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maLop = loaisv.getSelectedItem().toString();
                if(maLop != null && !maLop.isEmpty()){
                    Executors.newSingleThreadExecutor().execute(() -> {
                        try {
                            QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(TrichXuat.this);
                            DaoSinhVien daoSinhVien = quanLySinhVien.DaoSinhVien();
                            DaoSv_Hk daoHocKi = quanLySinhVien.DaoSinhVienHocKi();

                            List<SinhVien> listSinhVien = daoSinhVien.getSVByMaLop(maLop);
                            List<String> listMaSV = daoSinhVien.getMaSVByMaLop(maLop);
                            List<String> xepLoaiHK1 = daoHocKi.getXepLoai(listMaSV,"1");
                            List<String> xepLoaiHK2 = daoHocKi.getXepLoai(listMaSV,"2");
                            List<String> xepLoaiHK3 = daoHocKi.getXepLoai(listMaSV,"3");


                            runOnUiThread(() -> {
                                if (listSinhVien != null && !listSinhVien.isEmpty()) {
                                    Workbook workbook = new XSSFWorkbook();
                                    Sheet sheet = workbook.createSheet("Sinh Viên");

                                    Row headerRow = sheet.createRow(0);
                                    headerRow.createCell(0).setCellValue("STT");
                                    headerRow.createCell(1).setCellValue("MSSV");
                                    headerRow.createCell(2).setCellValue("Họ tên");
                                    headerRow.createCell(3).setCellValue("Email");
                                    headerRow.createCell(4).setCellValue("Học kì 1");
                                    headerRow.createCell(5).setCellValue("Học kì 2");
                                    headerRow.createCell(6).setCellValue("Học kì 3");

                                    int rowNum = 1;
                                    int countLoai = 0;
                                    for (SinhVien sv : listSinhVien) {
                                        Row row = sheet.createRow(rowNum++);
                                        row.createCell(0).setCellValue(rowNum - 1); // ID là số thứ tự
                                        row.createCell(1).setCellValue(sv.getMsSV());
                                        row.createCell(2).setCellValue(sv.getHoTen());
                                        row.createCell(3).setCellValue(sv.getEmail());

                                        row.createCell(4).setCellValue(xepLoaiHK1.get(countLoai));
                                        row.createCell(5).setCellValue(xepLoaiHK2.get(countLoai));
                                        row.createCell(6).setCellValue(xepLoaiHK3.get(countLoai));
                                        countLoai ++;
                                    }

                                    if(saveFileToDocuments(workbook, "XepLoai_" + maLop + ".xlsx")){
                                        xuatDSSV.setEnabled(false);
                                    }else{
                                        xuatDSSV.setBackground(null);
                                    }
                                }
                            });
                        } catch (Exception e) {
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show());
                        }
                    });
                }
            }
        });
    }

    private boolean saveFileToDocuments(Workbook workbook, String fileName) {
        ContentResolver resolver = getContentResolver();

        // Cấu hình metadata cho file
        ContentValues values = new ContentValues();
        values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName); // Sử dụng tên file được truyền vào
        values.put(MediaStore.Files.FileColumns.MIME_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        values.put(MediaStore.Files.FileColumns.RELATIVE_PATH, "Documents/");

        // Tạo URI cho file
        Uri uri = resolver.insert(MediaStore.Files.getContentUri("external"), values);

        // Ghi dữ liệu vào file
        try (OutputStream outputStream = resolver.openOutputStream(uri)) {
            if (outputStream != null) {
                workbook.write(outputStream);
                Toast.makeText(this, "File Excel đã được lưu vào Documents!", Toast.LENGTH_LONG).show();
                return true;
            } else {
                Toast.makeText(this, "Không thể mở file để ghi.", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu file Excel.", Toast.LENGTH_LONG).show();
            return false;
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }


}
