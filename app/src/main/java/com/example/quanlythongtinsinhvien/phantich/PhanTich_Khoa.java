package com.example.quanlythongtinsinhvien.phantich;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythongtinsinhvien.R;
import com.example.quanlythongtinsinhvien.dao.DaoLop;
import com.example.quanlythongtinsinhvien.dao.DaoSinhVien;
import com.example.quanlythongtinsinhvien.dao.DaoSv_Hk;
import com.example.quanlythongtinsinhvien.entities.QuanLySinhVien;
import com.example.quanlythongtinsinhvien.entities.SinhVien_HocKi;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class PhanTich_Khoa extends AppCompatActivity {
    Button congnghe, thongtin;
    ImageButton btn_back;
    private PieChart pieChart;
    TableLayout tableThongKe;
    Button hocki1, hocki2, hocki3;
    private List<String> allMaKhoa = Arrays.asList("1", "2");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phan_tich_khoa);

        init();
        action();

    }

    public void init(){
        congnghe = (Button) findViewById(R.id.congnghe);
        thongtin = (Button) findViewById(R.id.thongtin);
        pieChart = (PieChart) findViewById(R.id.pieChart);
        tableThongKe = (TableLayout) findViewById(R.id.tableThongKe);
        hocki1 =(Button) findViewById(R.id.hocki1);
        hocki2 =(Button) findViewById(R.id.hocki2);
        hocki3 =(Button) findViewById(R.id.hocki3);
        btn_back =(ImageButton) findViewById(R.id.btn_back);

        pieChart.setNoDataText("Vui lòng chọn khoa để xem");
        pieChart.setNoDataTextColor(Color.RED); // Màu chữ
        pieChart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD); // Kiểu chữ
    }

    private void resetButtonColors() {
        // Reset màu học kỳ
        hocki1.setBackgroundColor(Color.WHITE);
        hocki2.setBackgroundColor(Color.WHITE);
        hocki3.setBackgroundColor(Color.WHITE);
        hocki1.setTextColor(Color.BLACK);
        hocki2.setTextColor(Color.BLACK);
        hocki3.setTextColor(Color.BLACK);

        // Reset màu khoa
        congnghe.setBackgroundColor(Color.WHITE);
        thongtin.setBackgroundColor(Color.WHITE);
        congnghe.setTextColor(Color.BLACK);
        thongtin.setTextColor(Color.BLACK);
    }

    public void selectLopOfKhoa(String maKhoa, String maHocKi) {
        String[] cacLoai = {"Xuất sắc", "Giỏi", "Khá", "Trung bình", "Yếu"};

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(PhanTich_Khoa.this);
                DaoLop daoLop = quanLySinhVien.DaoLop();
                DaoSinhVien daoSinhVien = quanLySinhVien.DaoSinhVien();
                DaoSv_Hk daoSv_hk = quanLySinhVien.DaoSinhVienHocKi();

                // Lấy danh sách lớp của khoa
                List<String> allMaLopOfKhoa = daoLop.getLopByKhoaId(maKhoa);
                Map<String, List<String>> lopSinhVienMap = new HashMap<>();

                // Lấy danh sách sinh viên của từng lớp
                for (String maLop : allMaLopOfKhoa) {
                    List<String> listMaSvOfLop = daoSinhVien.getMaSVByMaLop(maLop);
                    lopSinhVienMap.put(maLop, listMaSvOfLop);
                }

                // Thống kê số lượng xếp loại theo lớp
                Map<String, Map<String, Integer>> thongKeTheoLop = new HashMap<>();

                for (Map.Entry<String, List<String>> entry : lopSinhVienMap.entrySet()) {

                    String maLop = entry.getKey();
                    List<String> danhSachSinhVien = entry.getValue();

                    // Truy vấn xếp loại từ bảng SinhVien_HocKi
                    List<SinhVien_HocKi> xepLoaiList = daoSv_hk.getXepLoaiTheoHocKiVaSinhVien(danhSachSinhVien, maHocKi);

                    // Thống kê số lượng theo xếp loại
                    Map<String, Integer> thongKeLoaiMap = new HashMap<>();
                    for (SinhVien_HocKi sv : xepLoaiList) {
                        String xepLoai = sv.getXepLoai();
                        thongKeLoaiMap.put(xepLoai, thongKeLoaiMap.getOrDefault(xepLoai, 0) + 1);
                    }

                    thongKeTheoLop.put(maLop, thongKeLoaiMap);
                }

                // Tính tổng điểm theo lớp
                Map<String, Integer> tongDiemTheoLop = new HashMap<>();
                for (Map.Entry<String, Map<String, Integer>> entryLop : thongKeTheoLop.entrySet()) {
                    String maLopThongKe = entryLop.getKey();
                    Map<String, Integer> thongKeLoai = entryLop.getValue();

                    int tongDiem = 0;

                    // Duyệt qua từng loại và tính điểm
                    for (Map.Entry<String, Integer> entryLoai : thongKeLoai.entrySet()) {
                        String loai = entryLoai.getKey();
                        int soLuong = entryLoai.getValue();

                        int diem = 0;
                        switch (loai) {
                            case "Xuất sắc":
                                diem = 10;
                                break;
                            case "Giỏi":
                                diem = 8;
                                break;
                            case "Khá":
                                diem = 6;
                                break;
                            case "Trung bình":
                                diem = 4;
                                break;
                            case "Yếu":
                                diem = -10;
                                break;
                        }
                        tongDiem += soLuong * diem;
                    }
                    tongDiemTheoLop.put(maLopThongKe, tongDiem);
                }

                // Tính tổng điểm của tất cả lớp
                int tongDiemToanBoLop = tongDiemTheoLop.values().stream()
                        .mapToInt(Integer::intValue)
                        .sum();

                // Tính tỷ lệ phần trăm cho mỗi lớp
                Map<String, Double> tiLePhanTramTheoLop = new HashMap<>();
                for (Map.Entry<String, Integer> entryPhanTram : tongDiemTheoLop.entrySet()) {
                    String maLopPhanTram = entryPhanTram.getKey();
                    int tongDiemLop = entryPhanTram.getValue();

                    // Tính tỷ lệ phần trăm
                    double tiLePhanTram = (double) tongDiemLop / tongDiemToanBoLop * 100;
                    tiLePhanTramTheoLop.put(maLopPhanTram, tiLePhanTram);
                }

                // Cập nhật UI trên thread chính
                runOnUiThread(() -> {
                    ArrayList<PieEntry> entries = new ArrayList<>();
                    for (Map.Entry<String, Double> entryFinal : tiLePhanTramTheoLop.entrySet()) {
                        String loai = entryFinal.getKey();
                        double tiLe = entryFinal.getValue();
                        entries.add(new PieEntry((float) tiLe, loai));
                    }

// Tạo dataset cho PieChart
                    PieDataSet dataSet = new PieDataSet(entries, "");
                    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    dataSet.setValueTextSize(12f);
                    dataSet.setValueTextColor(Color.WHITE);

// Định dạng hiển thị phần trăm
                    dataSet.setValueFormatter(new PercentFormatter(pieChart)); // Thêm dòng này
// Tạo PieData
                    PieData data = new PieData(dataSet);


// Cấu hình biểu đồ cơ bản
                    pieChart.setUsePercentValues(true); // Thêm dòng này
                    pieChart.setData(data);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setDrawHoleEnabled(true);
                    pieChart.setHoleRadius(35f);
                    pieChart.setTransparentCircleRadius(40f);

// Cấu hình hiệu ứng xoay
                    pieChart.setRotationEnabled(true);
                    pieChart.setHighlightPerTapEnabled(true);
                    pieChart.setDragDecelerationFrictionCoef(0.95f);

// Thêm hiệu ứng xoay ban đầu
                    pieChart.spin(2000, 0, 360f, Easing.EaseInOutQuad);

// Thêm legend (chú thích) và đặt ở dưới
                    Legend legend = pieChart.getLegend();
                    legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM); // Thay đổi từ TOP thành BOTTOM
                    legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER); // Thay đổi từ RIGHT thành CENTER
                    legend.setOrientation(Legend.LegendOrientation.HORIZONTAL); // Thay đổi từ VERTICAL thành HORIZONTAL
                    legend.setDrawInside(false); // Thêm dòng này để đảm bảo legend nằm ngoài biểu đồ
                    legend.setXEntrySpace(7f); // Khoảng cách giữa các mục trong legend
                    legend.setYEntrySpace(0f); // Khoảng cách dọc giữa các mục
                    legend.setYOffset(5f); // Khoảng cách từ legend đến biểu đồ

                    pieChart.setCenterText("Tỉ lệ học lực");
                    pieChart.setCenterTextSize(16f); // Kích thước chữ
                    pieChart.setCenterTextColor(Color.BLACK); // Màu chữ
// Cập nhật biểu đồ
                    pieChart.invalidate();

                    pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                        @Override
                        public void onValueSelected(Entry e, Highlight h) {
                            if (e != null) {
                                PieEntry pieEntry = (PieEntry) e; // Ép kiểu Entry thành PieEntry
                                String maLop = pieEntry.getLabel(); // Lấy mã lớp từ nhãn

                                // Lấy danh sách xếp loại và số lượng từ `thongKeTheoLop`
                                Map<String, Integer> thongKeLoai = thongKeTheoLop.get(maLop);
                                if (thongKeLoai != null) {
                                    // Xóa các hàng cũ trong bảng
                                    tableThongKe.removeViews(1, tableThongKe.getChildCount() - 1);

                                    // Hiển thị bảng và thêm các hàng
                                    for (Map.Entry<String, Integer> entry : thongKeLoai.entrySet()) {
                                        String xepLoai = entry.getKey();
                                        int soLuong = entry.getValue();

                                        // Tạo một hàng mới
                                        TableRow row = new TableRow(PhanTich_Khoa.this);
                                        row.setLayoutParams(new TableRow.LayoutParams(
                                                TableRow.LayoutParams.MATCH_PARENT,
                                                TableRow.LayoutParams.WRAP_CONTENT));

// Cột "Loại"
                                        TextView loaiText = new TextView(PhanTich_Khoa.this);
                                        loaiText.setText(xepLoai); // Gán dữ liệu loại
                                        loaiText.setTextColor(Color.BLACK);
                                        loaiText.setPadding(8, 8, 8, 8);
                                        loaiText.setGravity(Gravity.CENTER); // Căn giữa nội dung

// Cột "Số lượng"
                                        TextView soLuongText = new TextView(PhanTich_Khoa.this);
                                        soLuongText.setText(String.valueOf(soLuong)); // Gán dữ liệu số lượng
                                        soLuongText.setPadding(8, 8, 8, 8);
                                        soLuongText.setTextColor(Color.BLACK);
                                        soLuongText.setGravity(Gravity.CENTER); // Căn giữa nội dung

// Thêm các cột vào hàng
                                        row.addView(loaiText);
                                        row.addView(soLuongText);

// Thêm hàng vào bảng
                                        tableThongKe.addView(row);
                                    }

                                    // Hiển thị bảng
                                    tableThongKe.setVisibility(View.VISIBLE);
                                } else {
                                    Toast.makeText(PhanTich_Khoa.this, "Không có dữ liệu cho lớp " + maLop, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected() {

                        }
                    });
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(PhanTich_Khoa.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }


    public void action() {
        hocki1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButtonColors();
                hocki1.setBackgroundColor(Color.BLUE);
                hocki1.setTextColor(Color.WHITE);
                selectLopOfKhoa("1", "1"); // Học kỳ 1
            }
        });

        hocki2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButtonColors();
                hocki2.setBackgroundColor(Color.BLUE);
                hocki2.setTextColor(Color.WHITE);
                selectLopOfKhoa("1", "2"); // Học kỳ 2
            }
        });

        hocki3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButtonColors();
                hocki3.setBackgroundColor(Color.BLUE);
                hocki3.setTextColor(Color.WHITE);
                selectLopOfKhoa("1", "3"); // Học kỳ 3
            }
        });

        congnghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButtonColors();
                congnghe.setBackgroundColor(Color.GREEN);
                congnghe.setTextColor(Color.WHITE);
                selectLopOfKhoa("1", "1");
            }
        });

        thongtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButtonColors();
                thongtin.setBackgroundColor(Color.GREEN);
                thongtin.setTextColor(Color.WHITE);
                selectLopOfKhoa("2", "1");
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}