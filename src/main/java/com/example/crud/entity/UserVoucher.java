package com.example.crud.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tbl_user_voucher")
public class UserVoucher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_voucher_id")
    private long userVoucherId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id", nullable = false)
    private Voucher voucher;


    public UserVoucher(long userVoucherId, User user, Voucher voucher) {
        this.userVoucherId = userVoucherId;
        this.user = user;
        this.voucher = voucher;
    }

    public UserVoucher(){

    }

    public UserVoucher(User user, Voucher voucher){
        this.user= user;
        this.voucher= voucher;
    }

    public long getUserVoucherId() {
        return userVoucherId;
    }

    public void setUserVoucherId(long userVoucherId) {
        this.userVoucherId = userVoucherId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }
}
