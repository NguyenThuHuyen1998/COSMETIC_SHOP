package com.example.crud.controller;

import com.example.crud.entity.Voucher;
import com.example.crud.helper.TimeHelper;
import com.example.crud.service.JwtService;
import com.example.crud.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
public class VoucherController {

    private VoucherService voucherService;
    private JwtService jwtService;

    @Autowired
    public VoucherController(VoucherService voucherService,
                             JwtService jwtService){
        this.voucherService= voucherService;
        this.jwtService= jwtService;
    }

    @GetMapping(value = "/adminPage/vouchers")
    public ResponseEntity<Voucher> getListVoucher(HttpServletRequest request){
        if(jwtService.isAdmin(request)){
            List<Voucher> voucherList= voucherService.getListVoucher();
            if (voucherList== null || voucherList.size()==0){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity(voucherList, HttpStatus.OK);
        }
        return new ResponseEntity("Bạn không phải là admin", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @PostMapping(value = "/adminPage/vouchers")
    public ResponseEntity<Voucher> createVoucher(@RequestBody Voucher voucher,
                                                 HttpServletRequest request) throws ParseException {
        if(jwtService.isAdmin(request)){
            String dateStart= voucher.getDateStart();
            String dateEnd= voucher.getDateEnd();
            long start= TimeHelper.getInstance().convertTimestamp(dateStart + " 00:00:00");
            long end= TimeHelper.getInstance().convertTimestamp(dateEnd+" 23:59:59");
            if (end< new Date().getTime()){
                return new ResponseEntity("Ngày kết thúc không hợp lệ!", HttpStatus.BAD_REQUEST);
            }
            if (start>= end){
                return new ResponseEntity("Ngày kết thúc không thể nhỏ hơn ngày bắt đầu!", HttpStatus.BAD_REQUEST);
            }
            voucherService.addVoucher(voucher);
            return new ResponseEntity<>(voucher, HttpStatus.OK);
        }
        return new ResponseEntity("Bạn không phải là admin", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @DeleteMapping(value = "/adminPage/voucher/{voucher-id}")
    public ResponseEntity<Voucher> deleteVoucher(@PathVariable(name = "voucher-id") long voucherId,
                                                 HttpServletRequest request){
        if(jwtService.isAdmin(request)){
            Voucher voucher= voucherService.getVoucherById(voucherId);
            if (voucher!= null){
                voucherService.deleteVoucher(voucher);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity("Voucher không tồn tại!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Bạn không phải là admin", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @PutMapping(value = "/adminPage/voucher/{voucher-id}")
    public ResponseEntity<Voucher> updateVoucher(@PathVariable(name = "voucher-id") long voucherId,
                                                 @RequestBody Voucher voucher,
                                                 HttpServletRequest request) throws ParseException {
        if(jwtService.isAdmin(request)){
            if (voucher.getCouponId()== voucherId){
                String dateStart= voucher.getDateStart();
                String dateEnd= voucher.getDateEnd();
                long start= TimeHelper.getInstance().convertTimestamp(dateStart + " 00:00:00");
                long end= TimeHelper.getInstance().convertTimestamp(dateEnd+" 23:59:59");
                if (end< new Date().getTime()){
                    return new ResponseEntity("Ngày kết thúc không hợp lệ!", HttpStatus.BAD_REQUEST);
                }
                if (start>= end){
                    return new ResponseEntity("Ngày kết thúc không thể nhỏ hơn ngày bắt đầu!", HttpStatus.BAD_REQUEST);
                }
                voucherService.addVoucher(voucher);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity("Voucher không tồn tại!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Bạn không phải là admin", HttpStatus.METHOD_NOT_ALLOWED);
    }

}
