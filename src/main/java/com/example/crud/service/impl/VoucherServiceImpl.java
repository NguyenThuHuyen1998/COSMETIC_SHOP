package com.example.crud.service.impl;

import com.example.crud.entity.Order;
import com.example.crud.entity.UserVoucher;
import com.example.crud.entity.Voucher;
import com.example.crud.repository.UserVoucherRepository;
import com.example.crud.repository.VoucherRepository;
import com.example.crud.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class VoucherServiceImpl implements VoucherService {

    private VoucherRepository voucherRepository;
    private UserVoucherRepository userVoucherRepository;

    @Autowired
    public VoucherServiceImpl(VoucherRepository voucherRepository,
                              UserVoucherRepository userVoucherRepository){
        this.voucherRepository = voucherRepository;
        this.userVoucherRepository= userVoucherRepository;
    }

    @Override
    public List<Voucher> getListVoucher() {
        return (List<Voucher>) voucherRepository.findAll();
    }

    @Override
    public Voucher getVoucherByCode(String code) {
        List<Voucher> listVoucher= getListVoucher();
        for(Voucher voucher: listVoucher){
            if (voucher.getCode().equals(code)){
                return voucher;
            }
        }
        return null;
    }

    @Override
    public Voucher getVoucherById(long voucherId) {
        return voucherRepository.findById(voucherId).get();
    }

    @Override
    public void addVoucher(Voucher voucher) {
        voucherRepository.save(voucher);
    }

    @Override
    public void deleteVoucher(Voucher voucher) {
        voucherRepository.delete(voucher);
    }

    @Override
    public void addUserVoucher(UserVoucher userVoucher) {
        userVoucherRepository.save(userVoucher);
    }

    @Override
    public void deleteUserVoucher(UserVoucher userVoucher) {
        userVoucherRepository.delete(userVoucher);
    }

    @Override
    public boolean validateVoucher(Order order, Voucher voucher) {
        double valueApplyVoucher= voucher.getPriceApply();
        if(order.getTotal()>= valueApplyVoucher){
            return true;
        }
        return false;
    }


}
