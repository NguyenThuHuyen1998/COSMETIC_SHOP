package com.example.crud.service;

import com.example.crud.entity.Order;
import com.example.crud.entity.UserVoucher;
import com.example.crud.entity.Voucher;

import java.util.List;

public interface VoucherService {

    List<Voucher> getListVoucher();
    Voucher getVoucherByCode(String code);
    Voucher getVoucherById(long voucherId);
    void addVoucher(Voucher voucher);
    void deleteVoucher(Voucher voucher);
    void addUserVoucher(UserVoucher userVoucher);
    void deleteUserVoucher(UserVoucher userVoucher);
    boolean validateVoucher(Order order, Voucher voucher);
//    Voucher getVoucherByCode(String code);
}
