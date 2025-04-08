package com.example.quanlythongtinsinhvien.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "User", indices = {@Index(value = {"email"}, unique = true)})
public class UserEntity {

    @PrimaryKey
    @NonNull
    private String userId;

    @ColumnInfo(name = "email")
    private String emailUser;

    @ColumnInfo(name = "password")
    private String password;


    public UserEntity(String userId, String emailUser, String password) {
        this.userId = userId;
        this.emailUser = emailUser;
        this.password = password;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
