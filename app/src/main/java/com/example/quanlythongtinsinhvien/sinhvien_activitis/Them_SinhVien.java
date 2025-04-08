package com.example.quanlythongtinsinhvien.sinhvien_activitis;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlythongtinsinhvien.Home;
import com.example.quanlythongtinsinhvien.R;
import com.example.quanlythongtinsinhvien.dao.DaoDiem;
import com.example.quanlythongtinsinhvien.dao.DaoLop;
import com.example.quanlythongtinsinhvien.dao.DaoSinhVien;
import com.example.quanlythongtinsinhvien.entities.Diem;
import com.example.quanlythongtinsinhvien.entities.QuanLySinhVien;
import com.example.quanlythongtinsinhvien.entities.SinhVien;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class Them_SinhVien extends AppCompatActivity {
    Spinner spinner;
    EditText ngaysinh, mssv,ten,diachi,email,phone;
    RadioGroup gioitinh;
    Button add, out;

    List<String> lopList = new ArrayList<>(Arrays.asList("Rỗng"));

    private String[] MON_HOC_IDS = {
            "1A", "2B", "3C", "4D", "5E", "6F", "7G", "8H"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sinhvien);
        init();
        getLopList();
        Action();
    }

    public void init() {
        ngaysinh = (EditText) findViewById(R.id.ngaysinh);
        mssv = (EditText) findViewById(R.id.mssv);
        ten = (EditText) findViewById(R.id.ten);
        diachi = (EditText) findViewById(R.id.diachi);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        gioitinh = (RadioGroup) findViewById(R.id.gioitinh);
        add = (Button) findViewById(R.id.add);
        out = (Button) findViewById(R.id.out);
        spinner =(Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lopList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void Action() {
        ngaysinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(Them_SinhVien.this, Home.class);
                startActivity(home);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String MaSV = mssv.getText().toString();
                String Hoten = ten.getText().toString();
                String Diachi = diachi.getText().toString();
                String Ngaysinh = ngaysinh.getText().toString();
                String Email = email.getText().toString();
                String Phone = phone.getText().toString();
                String MaLop = spinner.getSelectedItem().toString();

                boolean Gioitinh = true;
                int idGioitinh = gioitinh.getCheckedRadioButtonId();
                if(idGioitinh != -1){
                    RadioButton selectedRadioButton = findViewById(idGioitinh);
                    String check = selectedRadioButton.getText().toString();
                    if(check.equals("Nam")){
                        Gioitinh = true;
                    }else{
                        Gioitinh = false;
                    }
                }
                boolean finalGioitinh = Gioitinh;

                if(MaSV.isEmpty() || MaSV.contains(" ")){
                    mssv.setError("Không đúng định dạng");
                    mssv.requestFocus();
                    return;
                }
                if(Hoten.isEmpty()){
                    ten.setError("Vui lòng nhập");
                    ten.requestFocus();
                    return;
                }
                if(!isValidEmail(Email)){
                    email.setError("Không đúng định dạng");
                    email.requestFocus();
                    return;
                }
                if(!isValidVietnamPhoneNumber(Phone)){
                    phone.setError("Không đúng định dạng");
                    phone.requestFocus();
                    return;
                }

                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(Them_SinhVien.this);
                        DaoSinhVien daoSinhVien = quanLySinhVien.DaoSinhVien();
                        DaoDiem daoDiem = quanLySinhVien.DaoDiem();
                        SinhVien sv = new SinhVien(MaSV, Hoten, Diachi, Ngaysinh, finalGioitinh, Email, Phone, MaLop);
                        daoSinhVien.insert(sv);

                        List<Diem> diem = new ArrayList<>();
                        for (String monHocId : MON_HOC_IDS) {
                            Diem diemItem = new Diem(MaSV, monHocId, 0, 0, 0, 0);
                            diem.add(diemItem);
                        }
                        daoDiem.insertListDiem(diem);

                        runOnUiThread(() -> {
                            if(sv!=null){
                                successful();
                                clearFields();
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(Them_SinhVien.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void getLopList() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                QuanLySinhVien quanLySinhVien = QuanLySinhVien.getDatabase(Them_SinhVien.this);
                DaoLop daoLop = quanLySinhVien.DaoLop();
                List<String> lop = daoLop.getAllMaLop();

                runOnUiThread(() -> {
                    if (lop != null && !lop.isEmpty()) {
                        lopList.clear();
                        lopList.addAll(lop);

                        // Cập nhật adapter
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Them_SinhVien.this, android.R.layout.simple_spinner_item, lopList){
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

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = (view, selectedYear, selectedMonth, selectedDay) -> {
            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            ngaysinh.setText(selectedDate);
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Them_SinhVien.this, dateSetListener, year, month, day);
        datePickerDialog.show();
    }

    private void successful() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomSuccessDialogStyle);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_success, null);

        TextView titleText = dialogView.findViewById(R.id.dialog_title);
        TextView messageText = dialogView.findViewById(R.id.dialog_message);
        Button okButton = dialogView.findViewById(R.id.dialog_ok_button);

        titleText.setText("Thành Công");
        messageText.setText("Thêm sinh viên thành công!");

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
        ngaysinh.setText("");
        mssv.setText("");
        ten.setText("");
        diachi.setText("");
        email.setText("");
        phone.setText("");

        gioitinh.clearCheck();
        spinner.setSelection(0);
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
