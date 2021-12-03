package com.example.pushnotification.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentRequest {
    @SerializedName("NguoiDanhGia")
    @Expose
    private String nguoiDanhGia;
    @SerializedName("SoDiem")
    @Expose
    private String soDiem;
    @SerializedName("NoiDungDonGia")
    @Expose
    private String noiDungDonGia;
    @SerializedName("MaSanPham")
    @Expose
    private String maSanPham;
    @SerializedName("StatusId")
    @Expose
    private String statusId;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;
    @SerializedName("C1")
    @Expose
    private String c1;

    public CommentRequest() {
    }

    public CommentRequest(String nguoiDanhGia, String soDiem, String noiDungDonGia, String maSanPham, String statusId, String createdBy, String c1) {
        this.nguoiDanhGia = nguoiDanhGia;
        this.soDiem = soDiem;
        this.noiDungDonGia = noiDungDonGia;
        this.maSanPham = maSanPham;
        this.statusId = statusId;
        this.createdBy = createdBy;
        this.c1 = c1;
    }

    public String getNguoiDanhGia() {
        return nguoiDanhGia;
    }

    public void setNguoiDanhGia(String nguoiDanhGia) {
        this.nguoiDanhGia = nguoiDanhGia;
    }

    public String getSoDiem() {
        return soDiem;
    }

    public void setSoDiem(String soDiem) {
        this.soDiem = soDiem;
    }

    public String getNoiDungDonGia() {
        return noiDungDonGia;
    }

    public void setNoiDungDonGia(String noiDungDonGia) {
        this.noiDungDonGia = noiDungDonGia;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }
}
