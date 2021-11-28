package com.example.pushnotification.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "Product")
public class Product implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "isSuccess")
    private boolean isSuccess;
    @ColumnInfo(name = "productId")
    private String productId;
    @ColumnInfo(name = "productName")
    private String productName;
    @ColumnInfo(name = "money")
    private String money;
    @ColumnInfo(name = "origin")
    private String origin;
    @ColumnInfo(name = "image")
    private String image;
    @ColumnInfo(name = "cateType")
    private String cateType;
    @ColumnInfo(name = "company")
    private String company;
    @ColumnInfo(name = "MoTa")
    private String MoTa;
    @ColumnInfo(name = "TrongLuong")
    private String TrongLuong;
    @ColumnInfo(name = "NgaySanXuat")
    private String NgaySanXuat;
    @ColumnInfo(name = "HanSuDung")
    private String HanSuDung;
    @ColumnInfo(name = "timeSave")
    private String timeSave;
    private String message;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Product(String productId) {
        this.productId = productId;
    }

    public Product() {
    }

    public Product(boolean isSuccess, String productId, String productName, String money, String origin, String image, String cateType, String company, String moTa, String trongLuong, String ngaySanXuat, String hanSuDung, String timeSave) {
        this.isSuccess = isSuccess;
        this.productId = productId;
        this.productName = productName;
        this.money = money;
        this.origin = origin;
        this.image = image;
        this.cateType = cateType;
        this.company = company;
        MoTa = moTa;
        TrongLuong = trongLuong;
        NgaySanXuat = ngaySanXuat;
        HanSuDung = hanSuDung;
        this.timeSave = timeSave;
    }

    protected Product(Parcel in) {
        isSuccess = in.readByte() != 0;
        productId = in.readString();
        productName = in.readString();
        money = in.readString();
        origin = in.readString();
        image = in.readString();
        cateType = in.readString();
        company = in.readString();
        MoTa = in.readString();
        TrongLuong = in.readString();
        NgaySanXuat = in.readString();
        HanSuDung = in.readString();
        timeSave = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getProductId() {
        if (productId == null) {
            productId = "";
        }
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOrigin() {
        if (origin == null) {
            origin = "";
        }
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCateType() {
        return cateType;
    }

    public void setCateType(String cateType) {
        this.cateType = cateType;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }

    public String getTrongLuong() {
        return TrongLuong;
    }

    public void setTrongLuong(String trongLuong) {
        TrongLuong = trongLuong;
    }

    public String getNgaySanXuat() {
        return NgaySanXuat;
    }

    public void setNgaySanXuat(String ngaySanXuat) {
        NgaySanXuat = ngaySanXuat;
    }

    public String getHanSuDung() {
        return HanSuDung;
    }

    public void setHanSuDung(String hanSuDung) {
        HanSuDung = hanSuDung;
    }

    public String getTimeSave() {
        return timeSave;
    }

    public void setTimeSave(String timeSave) {
        this.timeSave = timeSave;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isSuccess ? 1 : 0));
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(money);
        dest.writeString(origin);
        dest.writeString(image);
        dest.writeString(cateType);
        dest.writeString(company);
        dest.writeString(MoTa);
        dest.writeString(TrongLuong);
        dest.writeString(NgaySanXuat);
        dest.writeString(HanSuDung);
        dest.writeString(timeSave);
    }
}
