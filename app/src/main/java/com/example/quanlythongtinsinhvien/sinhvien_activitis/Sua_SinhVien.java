package com.example.quanlythongtinsinhvien.sinhvien_activitis;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythongtinsinhvien.Home;
import com.example.quanlythongtinsinhvien.R;
import com.example.quanlythongtinsinhvien.dao.DaoLop;
import com.example.quanlythongtinsinhvien.dao.DaoSinhVien;
import com.example.quanlythongtinsinhvien.entities.QuanLySinhVien;
import com.example.quanlythongtinsinhvien.entities.SinhVien;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class Sua_SinhVien extends AppCompatActivity {
    EditText inputmssv,ngaysinh, mssv,ten,diachi,email,phone;
    TextView check;
    RadioGroup gioitinh;
    Button  sua, out;
    Spinner spinner;
    LinearLayout linearLayout;

    List<String> lopList = new ArrayList<>();
    private SinhVien svBefore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_sinh_vien);
        init();
        getLopList();
        Action();
    }

    public void init(){
        inputmssv = (EditText) findViewById(R.id.inputmssv);
        ngaysinh = (EditText) findViewById(R.id.ngaysinh);
        mssv = (EditText) findViewById(R.id.mssv);
        ten = (EditText) findViewById(R.id.ten);
        diachi = (EditText) findViewById(R.id.diachi);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        check = (TextView) findViewById(R.id.check);
        gioitinh = (RadioGroup) findViewById(R.id.gioitinh);
        sua = (Button) findViewById(R.id.sua);
        out = (Button) findViewById(R.id.out);

        spinner =(Spinner) findViewById(R.id.spinner);
        setupSpinner();

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.setVisibility(View.GONE);
    }

    private void setupSpinner() {
        lopList.add("Rỗng");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lopList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void Action(){
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(Sua_SinhVien.this, Home.class);
                startActivity(home);
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Mssv = inputmssv.getText().toString();
                String MaLop = spinner.getSelectedItem().toString();

                if(Mssv.isEmpty() || Mssv.contains(" ")){
                    inputmssv.setError("Không đúng định dạng");
                    inputmssv.requestFocus();
                    return;
                }
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(Sua_SinhVien.this);
                        DaoSinhVien daoSinhVien = quanLySinhVien.DaoSinhVien();
                        SinhVien sv = daoSinhVien.getByIdAndLop(Mssv,MaLop);

                        runOnUiThread(() -> {
                            if(sv==null){
                                inputmssv.setError("Không tìm thấy");
                                inputmssv.requestFocus();
                                return;
                            }

                            linearLayout.setVisibility(View.VISIBLE);
                            svBefore = sv;

                            mssv.setText(Mssv);
                            ten.setText(sv.getHoTen());
                            diachi.setText(sv.getDiaChi());
                            ngaysinh.setText(sv.getNgaySinh());

                            if(sv.isGioiTinh()){
                                gioitinh.check(R.id.Nam);
                            }else{
                                gioitinh.check(R.id.Nu);
                            }
                            email.setText(sv.getEmail());
                            phone.setText(sv.getSoDienThoai());

                        });
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            Toast.makeText(Sua_SinhVien.this, "Lỗi : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }
        });

        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isDataChanged()) {
                    Toast.makeText(Sua_SinhVien.this, "Không có thay đổi nào", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mssv.getText().toString().isEmpty() || mssv.getText().toString().contains(" ")){
                    mssv.setError("Không đúng định dạng");
                    mssv.requestFocus();
                    return;
                }
                if(ten.getText().toString().isEmpty()){
                    ten.setError("Vui lòng nhập");
                    ten.requestFocus();
                    return;
                }
                if(!isValidEmail(email.getText().toString())){
                    email.setError("Không đúng định dạng");
                    email.requestFocus();
                    return;
                }
                if(!isValidVietnamPhoneNumber(phone.getText().toString())){
                    phone.setError("Không đúng định dạng");
                    phone.requestFocus();
                    return;
                }

                updateSinhVien();
            }
        });
    }

    private boolean isDataChanged() {
        if (svBefore == null) return false;

        boolean gioitinhValue = gioitinh.getCheckedRadioButtonId() == R.id.Nam;
        String maLop = spinner.getSelectedItem().toString();

        return !svBefore.getMsSV().equals(mssv.getText().toString()) ||
                !svBefore.getHoTen().equals(ten.getText().toString()) ||
                !svBefore.getDiaChi().equals(diachi.getText().toString()) ||
                !svBefore.getNgaySinh().equals(ngaysinh.getText().toString()) ||
                !svBefore.getEmail().equals(email.getText().toString()) ||
                !svBefore.getSoDienThoai().equals(phone.getText().toString()) ||
                svBefore.isGioiTinh() != gioitinhValue ||
                !svBefore.getMaLop().equals(maLop);
    }

    private void updateSinhVien() {
        SinhVien svAfter = new SinhVien();
        svAfter.setMsSV(mssv.getText().toString());
        svAfter.setHoTen(ten.getText().toString());
        svAfter.setDiaChi(diachi.getText().toString());
        svAfter.setNgaySinh(ngaysinh.getText().toString());
        svAfter.setGioiTinh(gioitinh.getCheckedRadioButtonId() == R.id.Nam);
        svAfter.setEmail(email.getText().toString());
        svAfter.setSoDienThoai(phone.getText().toString());

        String maLop = spinner.getSelectedItem().toString();

        if (!svBefore.getMaLop().equals(maLop)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Cảnh báo!")
                    .setMessage("Bạn sẽ chuyển sinh viên từ lớp " + svBefore.getMaLop() + " sang " + maLop + "!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            svAfter.setMaLop(maLop);
                            update(svAfter);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String MaLop = svBefore.getMaLop();
                            int position = getPosition(lopList, MaLop);
                            if (position != -1) {
                                spinner.setSelection(position);
                            }
                            svAfter.setMaLop(MaLop);
                            update(svAfter);
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false); // Không cho phép tắt bằng cách nhấn ngoài dialog

            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            String MaLop = svBefore.getMaLop();
            svAfter.setMaLop(MaLop);
            update(svAfter);
        }
    }

    private void update(SinhVien sv) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(Sua_SinhVien.this);
                DaoSinhVien daoSinhVien = quanLySinhVien.DaoSinhVien();
                daoSinhVien.update(sv);

                runOnUiThread(() -> {
                    successful();
                    clearFields();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(Sua_SinhVien.this, "Lỗi khi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }


    private void getLopList() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(Sua_SinhVien.this);
                DaoLop daoLop = quanLySinhVien.DaoLop();
                List<String> lop = daoLop.getAllMaLop();

                runOnUiThread(() -> {
                    if (lop != null && !lop.isEmpty()) {
                        lopList.clear();
                        lopList.addAll(lop);

                        // Cập nhật adapter
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Sua_SinhVien.this, android.R.layout.simple_spinner_item, lopList){
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

    private void successful() {
       AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomSuccessDialogStyle);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_success, null);

        TextView titleText = dialogView.findViewById(R.id.dialog_title);
        TextView messageText = dialogView.findViewById(R.id.dialog_message);
        Button okButton = dialogView.findViewById(R.id.dialog_ok_button);

        titleText.setText("Thành Công");
        messageText.setText("Sửa sinh viên thành công!");

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Tùy chỉnh dialog
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        okButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void clearFields() {
        inputmssv.setText("");
        ngaysinh.setText("");
        mssv.setText("");
        ten.setText("");
        diachi.setText("");
        email.setText("");
        phone.setText("");
        gioitinh.clearCheck();
    }

    private int getPosition(List<String> items, String value) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(value)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email != null && email.matches(emailPattern);
    }
    public boolean isValidVietnamPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return true;
        }
        String phonePattern = "^(0\\d{9}|\\+84\\d{9}|84\\d{9})$"; // Kiểm tra số điện thoại di động hợp lệ
        return phoneNumber.matches(phonePattern);
    }

}