package com.example.quanlythongtinsinhvien.diem;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythongtinsinhvien.R;
import com.example.quanlythongtinsinhvien.dao.DaoDiem;
import com.example.quanlythongtinsinhvien.dao.DaoHocKi;
import com.example.quanlythongtinsinhvien.dao.DaoKhoa;
import com.example.quanlythongtinsinhvien.dao.DaoLop;
import com.example.quanlythongtinsinhvien.dao.DaoMon;
import com.example.quanlythongtinsinhvien.dao.DaoSinhVien;
import com.example.quanlythongtinsinhvien.entities.Diem;
import com.example.quanlythongtinsinhvien.entities.QuanLySinhVien;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class Them_Diem extends AppCompatActivity {

    private TextView chonKhoa, chonLop, chonSinhVien, chonHocKi, chonMon;
    private ImageButton btn_back, btn_backLop, btn_backSinhVien, btn_backMon, btn_backHocKi;

    private Spinner spinnerKhoa, spinnerLop, spinnerSinhVien, spinnerHocKi, spinnerMon;
    private List<String> listTenKhoa = new ArrayList<>();
    private List<String> listMaLop = new ArrayList<>();
    private List<String> listMaSV = new ArrayList<>();
    private List<String> listTenHocKi = new ArrayList<>();
    private List<String> listTenMon = new ArrayList<>();

    LinearLayout linearKhoa, linearLop, linearSinhVien,linearHocKi, linearMon, nhapDiem;

    private EditText diem15p, giuaki, cuoiki, trungbinh;
    private TextView done;
    private Diem diemBefore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_diem);
        init();
        ActionBack();
        getKhoa();
        spinnearOfKhoa();
        spinnearOfLop();
        spinnearOfSinhVien();
        spinnearOfHocKi();
        spinnearOfMon();

        buttonDone();

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void spinnearOfHocKi(){
        spinnerHocKi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getMon();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void spinnearOfMon(){
        spinnerMon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getDiemOfMon();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void getMon() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(Them_Diem.this);
                DaoHocKi daoHocKi = quanLySinhVien.DaoHocKi();
                DaoMon daoMon = quanLySinhVien.DaoMonHoc();

                String tenHocki = "";
                if (spinnerHocKi != null && spinnerHocKi.getSelectedItem().toString() != null) {
                    tenHocki = spinnerHocKi.getSelectedItem().toString();
                }

                if (!tenHocki.isEmpty()) {
                    String maHocKi = daoHocKi.getMaHkByTenHK(tenHocki);

                    if(!maHocKi.isEmpty()){
                        List<String> tenMon = daoMon.getALlTenMonByMaHocKi(maHocKi);

                        if(!tenMon.isEmpty()){
                            runOnUiThread(() -> {
                                listTenMon.clear();
                                listTenMon.addAll(tenMon);

                                ArrayAdapter<String> adapterMon = new ArrayAdapter<>(Them_Diem.this, android.R.layout.simple_spinner_item, listTenMon){
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

                            });
                        }
                    }
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show());
            }
        });


    }

    public void getDiemOfMon(){
        chonMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenMon = "";
                if (spinnerMon != null && spinnerMon.getSelectedItem().toString() != null) {
                    tenMon = spinnerMon.getSelectedItem().toString();

                    if(!tenMon.isEmpty()){
                        String finalTenMon = tenMon;
                        Executors.newSingleThreadExecutor().execute(() -> {
                            try {
                                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(Them_Diem.this);
                                DaoMon daoMon = quanLySinhVien.DaoMonHoc();
                                DaoDiem daoDiem = quanLySinhVien.DaoDiem();

                                String maMon = daoMon.getMaMonByTenMon(finalTenMon);

                                if(!maMon.isEmpty()){
                                    String maSv = spinnerSinhVien.getSelectedItem().toString();

                                    if(maSv != null && !maSv.isEmpty()){
                                        Diem diem = daoDiem.getDiemByMaSvAndMaMon(maSv, maMon);
                                        diemBefore = diem;

                                        if(diem != null){
                                            runOnUiThread(() -> {
                                                String Diem15p = String.valueOf(diem.getDiem15p());
                                                String DiemGiuaKi = String.valueOf(diem.getDiemGiuaKi());
                                                String DiemCuoiki = String.valueOf(diem.getDiemCuoiKi());
                                                String DiemTrungBinh = String.valueOf(diem.getDiemTrungBinh());

                                                nhapDiem.setVisibility(View.VISIBLE);
                                                chonMon.setText("Sửa điểm");
                                                diem15p.setText(Diem15p);
                                                giuaki.setText(DiemGiuaKi);
                                                cuoiki.setText(DiemCuoiki);
                                                trungbinh.setText(DiemTrungBinh);
                                            });
                                        }
                                    }

                                }

                            } catch (Exception e) {
                                runOnUiThread(() -> Toast.makeText(Them_Diem.this, "Lỗi: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show());
                            }
                        });
                    }
                }

            }
        });
    }

    private void successful() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomSuccessDialogStyle);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_success, null);

        TextView titleText = dialogView.findViewById(R.id.dialog_title);
        TextView messageText = dialogView.findViewById(R.id.dialog_message);
        Button okButton = dialogView.findViewById(R.id.dialog_ok_button);

        titleText.setText("Thành Công");
        messageText.setText("Điểm đã được cập nhật!");

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Tùy chỉnh dialog
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        okButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public boolean isDataChange(){
        if(diemBefore == null){
            return true;
        }
        String Diem15p = String.valueOf(diemBefore.getDiem15p());
        String DiemGiuaKi = String.valueOf(diemBefore.getDiemGiuaKi());
        String DiemCuoiki = String.valueOf(diemBefore.getDiemCuoiKi());
        String DiemTrungBinh = String.valueOf(diemBefore.getDiemTrungBinh());
        return Diem15p.equals(diem15p) ||
                DiemGiuaKi.equals(giuaki) ||
                DiemCuoiki.equals(cuoiki) ||
                DiemTrungBinh.equals(trungbinh);
    }

    private void update(Diem diem) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(Them_Diem.this);
                DaoDiem daoDiem = quanLySinhVien.DaoDiem();
                daoDiem.processNewDiem(diem);

                runOnUiThread(() -> {
                    successful();
                    linearMon.setVisibility(View.GONE);
                    nhapDiem.setVisibility(View.GONE);
                    chonMon.setText("Chọn môn");
                    diem15p.setText("");
                    giuaki.setText("");
                    cuoiki.setText("");
                    trungbinh.setText("");

                    linearKhoa.setVisibility(View.VISIBLE);
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(Them_Diem.this, "Lỗi khi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    public void updateDiem() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(Them_Diem.this);
                DaoMon daoMon = quanLySinhVien.DaoMonHoc();

                Diem after = new Diem();

                // Lấy mã sinh viên trực tiếp từ spinner (vì spinner sinh viên đã hiển thị mã SV)
                String maSV = spinnerSinhVien.getSelectedItem().toString();
                after.setMaSV(maSV);

                // Chuyển từ tên môn sang mã môn
                String tenMon = spinnerMon.getSelectedItem().toString();
                String maMon = daoMon.getMaMonByTenMon(tenMon);
                after.setMaMon(maMon);

                after.setDiemId(100);

                // Lấy điểm từ các trường input
                float Diem15p = Float.parseFloat(diem15p.getText().toString());
                float DiemGiuaKi = Float.parseFloat(giuaki.getText().toString());
                float DiemCuoiki = Float.parseFloat(cuoiki.getText().toString());
                float DiemTrungBinh = Float.parseFloat(trungbinh.getText().toString());

                // Kiểm tra điểm hợp lệ (trong khoảng 0-10)
                if (Diem15p < 0 || Diem15p > 10 || DiemGiuaKi < 0 || DiemGiuaKi > 10 || DiemCuoiki < 0 || DiemCuoiki > 10 || DiemTrungBinh < 0 || DiemTrungBinh > 10) {
                    runOnUiThread(() -> {
                        Toast.makeText(Them_Diem.this, "Điểm phải nằm trong khoảng từ 0 đến 10.", Toast.LENGTH_SHORT).show();
                    });
                    return; // Dừng hàm nếu có điểm không hợp lệ
                }

                after.setDiem15p(Diem15p);
                after.setDiemGiuaKi(DiemGiuaKi);
                after.setDiemCuoiKi(DiemCuoiki);
                after.setDiemTrungBinh(DiemTrungBinh);

                update(after);

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(Them_Diem.this, "Lỗi khi cập nhật: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
            }
        });
    }


    public void buttonDone(){
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDataChange()){
                    Toast.makeText(Them_Diem.this, "Không có thay đổi nào", Toast.LENGTH_SHORT).show();
                    return;
                }

                updateDiem();
            }
        });
    }

    public void spinnearOfKhoa(){
        spinnerKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getLop();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    public void spinnearOfLop(){
        spinnerLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSinhVien();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    public void spinnearOfSinhVien(){
        spinnerSinhVien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getHocKi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    public void getKhoa(){

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(Them_Diem.this);
                DaoKhoa daoKhoa = quanLySinhVien.DaoKhoa();

                List<String> listKhoaName = daoKhoa.getAllTenKhoa();

                runOnUiThread(() -> {
                    if (listKhoaName != null && !listKhoaName.isEmpty()) {
                        listTenKhoa.clear();
                        listTenKhoa.addAll(listKhoaName);

                        // Cập nhật adapter cho Spinner
                        ArrayAdapter<String> adapterKhoa = new ArrayAdapter<>(Them_Diem.this, android.R.layout.simple_spinner_item, listTenKhoa){
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
                        adapterKhoa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerKhoa.setAdapter(adapterKhoa);
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show());
            }
        });

        chonKhoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
                Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left);

                linearKhoa.startAnimation(slideIn);
                linearKhoa.setVisibility(View.GONE);

                linearLop.startAnimation(slideOut);
                linearLop.setVisibility(View.VISIBLE);
            }
        });
    }

    public void getLop(){
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(Them_Diem.this);
                DaoKhoa daoKhoa = quanLySinhVien.DaoKhoa();
                DaoLop daoLop = quanLySinhVien.DaoLop();

                // Kiểm tra null và lấy giá trị an toàn
                String tenKhoa = "";
                if (spinnerKhoa != null && spinnerKhoa.getSelectedItem() != null) {
                    tenKhoa = spinnerKhoa.getSelectedItem().toString();
                }

                // Chỉ tiếp tục nếu có tenKhoa
                if (!tenKhoa.isEmpty()) {
                    String maKhoa = daoKhoa.getMaKhoaByTenKhoa(tenKhoa);
                    List<String> ListmaLop = daoLop.getLopByKhoaId(maKhoa);

                    runOnUiThread(() -> {
                        if (ListmaLop != null && !ListmaLop.isEmpty()) {
                            listMaLop.clear();
                            listMaLop.addAll(ListmaLop);

                            ArrayAdapter<String> adapterLop = new ArrayAdapter<>(Them_Diem.this,
                                    android.R.layout.simple_spinner_item, listMaLop){
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
                            adapterLop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerLop.setAdapter(adapterLop);
                        }
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show());
            }
        });

        chonLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
                Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left);

                linearLop.startAnimation(slideIn);
                linearLop.setVisibility(View.GONE);

                linearSinhVien.startAnimation(slideOut);
                linearSinhVien.setVisibility(View.VISIBLE);
            }
        });
    }


    public void getSinhVien(){
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(Them_Diem.this);
                DaoSinhVien daoSinhVien = quanLySinhVien.DaoSinhVien();

                String maLop = "";
                if (spinnerLop != null && spinnerLop.getSelectedItem() != null) {
                    maLop = spinnerLop.getSelectedItem().toString();
                }

                if (!maLop.isEmpty()) {
                    List<String> allMaSinhVien = daoSinhVien.getMaSVByMaLop(maLop);

                    runOnUiThread(() -> {
                        if (allMaSinhVien != null && !allMaSinhVien.isEmpty()) {
                            listMaSV.clear();
                            listMaSV.addAll(allMaSinhVien);

                            ArrayAdapter<String> adapterSinhVien = new ArrayAdapter<>(Them_Diem.this, android.R.layout.simple_spinner_item, listMaSV){
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
                            adapterSinhVien.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerSinhVien.setAdapter(adapterSinhVien);
                        }
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show());
            }
        });

        chonSinhVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
                Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left);

                linearSinhVien.startAnimation(slideIn);
                linearSinhVien.setVisibility(View.GONE);

                linearHocKi.startAnimation(slideOut);
                linearHocKi.setVisibility(View.VISIBLE);
            }
        });
    }

    public void getHocKi(){
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(Them_Diem.this);
                DaoHocKi daoHocKi = quanLySinhVien.DaoHocKi();
                List<String> listHocKi = daoHocKi.getAllTenHocKi();

                runOnUiThread(() -> {
                    if (listHocKi != null && !listHocKi.isEmpty()) {
                        listTenHocKi.clear();
                        listTenHocKi.addAll(listHocKi);

                        ArrayAdapter<String> adapterHocKi = new ArrayAdapter<>(Them_Diem.this, android.R.layout.simple_spinner_item, listTenHocKi){
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
                    }
                });

            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show());
            }
        });

        chonHocKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
                Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left);

                linearHocKi.startAnimation(slideOut);
                linearHocKi.setVisibility(View.GONE);

                linearMon.startAnimation(slideIn);
                linearMon.setVisibility(View.VISIBLE);
            }
        });
    }


    public void ActionBack(){
        btn_backLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_right);
                Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_left);
                linearLop.startAnimation(slideIn);
                linearLop.setVisibility(View.GONE);

                linearKhoa.startAnimation(slideOut);
                linearKhoa.setVisibility(View.VISIBLE);
            }
        });

        btn_backSinhVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_right);
                Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_left);
                linearSinhVien.startAnimation(slideIn);
                linearSinhVien.setVisibility(View.GONE);

                linearLop.startAnimation(slideOut);
                linearLop.setVisibility(View.VISIBLE);
            }
        });

        btn_backHocKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_right);
                Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_left);
                linearHocKi.startAnimation(slideIn);
                linearHocKi.setVisibility(View.GONE);

                linearSinhVien.startAnimation(slideOut);
                linearSinhVien.setVisibility(View.VISIBLE);
            }
        });

        btn_backMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slideOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_right);
                Animation slideIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_left);
                linearMon.startAnimation(slideIn);
                linearMon.setVisibility(View.GONE);

                linearHocKi.startAnimation(slideOut);
                linearHocKi.setVisibility(View.VISIBLE);

                nhapDiem.setVisibility(View.GONE);
                chonMon.setText("Chọn môn");
                diem15p.setText("");
                giuaki.setText("");
                cuoiki.setText("");
                trungbinh.setText("");
            }
        });
    }



    private void init() {
        diem15p = findViewById(R.id.diem15p);
        giuaki = findViewById(R.id.giuaki);
        cuoiki = findViewById(R.id.cuoiki);
        trungbinh = findViewById(R.id.trungbinh);
        done = findViewById(R.id.done);

        chonKhoa = findViewById(R.id.chonKhoa);
        chonLop = findViewById(R.id.chonLop);
        chonSinhVien = findViewById(R.id.chonSinhVien);
        chonHocKi = findViewById(R.id.chonHocKi);
        chonMon = findViewById(R.id.chonMon);

        linearKhoa = findViewById(R.id.linearKhoa);
        linearLop = findViewById(R.id.linearLop);
        linearSinhVien = findViewById(R.id.linearSinhVien);
        linearMon = findViewById(R.id.linearMon);
        linearHocKi = findViewById(R.id.linearHocKi);
        nhapDiem = findViewById(R.id.nhapDiem);

        spinnerKhoa = findViewById(R.id.spinnerKhoa);
        ArrayAdapter<String> adapterKhoa = new ArrayAdapter<>(Them_Diem.this, android.R.layout.simple_spinner_item, listTenKhoa);
        adapterKhoa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKhoa.setAdapter(adapterKhoa);

        spinnerLop = findViewById(R.id.spinnerLop);
        ArrayAdapter<String> adapterLop = new ArrayAdapter<>(Them_Diem.this, android.R.layout.simple_spinner_item, listMaLop);
        adapterLop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLop.setAdapter(adapterLop);

        spinnerSinhVien = findViewById(R.id.spinnerSinhVien);
        ArrayAdapter<String> adapterSinhVien = new ArrayAdapter<>(Them_Diem.this, android.R.layout.simple_spinner_item, listMaSV);
        adapterSinhVien.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSinhVien.setAdapter(adapterSinhVien);

        spinnerHocKi = findViewById(R.id.spinnerHocKi);
        ArrayAdapter<String> adapterHocKi = new ArrayAdapter<>(Them_Diem.this, android.R.layout.simple_spinner_item, listTenHocKi);
        adapterHocKi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHocKi.setAdapter(adapterHocKi);

        spinnerMon = findViewById(R.id.spinnerMon);
        ArrayAdapter<String> adapterMon = new ArrayAdapter<>(Them_Diem.this, android.R.layout.simple_spinner_item, listTenMon);
        adapterMon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMon.setAdapter(adapterMon);

        btn_backLop = findViewById(R.id.btn_backLop);
        btn_backSinhVien = findViewById(R.id.btn_backSinhVien);
        btn_backMon = findViewById(R.id.btn_backMon);
        btn_backHocKi = findViewById(R.id.btn_backHocKi);
    }

}
