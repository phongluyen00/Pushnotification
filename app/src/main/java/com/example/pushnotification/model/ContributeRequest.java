package com.example.pushnotification.model;

public class ContributeRequest {
    private String MaSanPham;
    private String TenSanPham;
    private String LoaiSanPhamId;
    private String TenDoanhNghiep;
    private String DiaChi;
    private String SoDienThoai;
    private String Email;
    private String MaSoThue;
    private String C1;
    private String C2;
    private String GiaSanPham;
    private String TrongLuong;
    private String MoTa;
    private String AnhDaiDien;
    private String CreatedBy;
    private String CreatedDate;

    public ContributeRequest() {
    }

    public ContributeRequest(String maSanPham, String tenSanPham, String loaiSanPhamId, String tenDoanhNghiep, String diaChi, String soDienThoai, String email, String maSoThue, String c1, String c2, String giaSanPham, String trongLuong, String moTa, String createdBy, String createdDate) {
        MaSanPham = maSanPham;
        TenSanPham = tenSanPham;
        LoaiSanPhamId = loaiSanPhamId;
        TenDoanhNghiep = tenDoanhNghiep;
        DiaChi = diaChi;
        SoDienThoai = soDienThoai;
        Email = email;
        MaSoThue = maSoThue;
        C1 = c1;
        C2 = c2;
        GiaSanPham = giaSanPham;
        TrongLuong = trongLuong;
        MoTa = moTa;
        CreatedBy = createdBy;
        CreatedDate = createdDate;
    }

    public String getMaSanPham() {
        return MaSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        MaSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return TenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        TenSanPham = tenSanPham;
    }

    public String getLoaiSanPhamId() {
        return LoaiSanPhamId;
    }

    public void setLoaiSanPhamId(String loaiSanPhamId) {
        LoaiSanPhamId = loaiSanPhamId;
    }

    public String getTenDoanhNghiep() {
        return TenDoanhNghiep;
    }

    public void setTenDoanhNghiep(String tenDoanhNghiep) {
        TenDoanhNghiep = tenDoanhNghiep;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getSoDienThoai() {
        return SoDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        SoDienThoai = soDienThoai;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMaSoThue() {
        return MaSoThue;
    }

    public void setMaSoThue(String maSoThue) {
        MaSoThue = maSoThue;
    }

    public String getC1() {
        return C1;
    }

    public void setC1(String c1) {
        C1 = c1;
    }

    public String getC2() {
        return C2;
    }

    public void setC2(String c2) {
        C2 = c2;
    }

    public String getGiaSanPham() {
        return GiaSanPham;
    }

    public void setGiaSanPham(String giaSanPham) {
        GiaSanPham = giaSanPham;
    }

    public String getTrongLuong() {
        return TrongLuong;
    }

    public void setTrongLuong(String trongLuong) {
        TrongLuong = trongLuong;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }

    public String getAnhDaiDien() {
        return AnhDaiDien;
    }

    public void setAnhDaiDien(String anhDaiDien) {
        AnhDaiDien = anhDaiDien;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }
}
