package com.example.crud.entity;

import org.json.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "tblVouchers")
public class Voucher implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "voucher_id")
    private long couponId;

    @NotEmpty
    @Column(name = "code", unique = true)
    private String code;

    @NotEmpty
    @Column(name = "date_start")
    private String dateStart;

    @NotEmpty
    @Column(name = "date_end")
    private String dateEnd;

    @NotNull
    @Column(name = "value_discount")
    private double valueDiscount;

    @Column(name = "type_discount")
    private String typeDiscount;

    @Column(name = "price_apply", nullable = false)
    private double priceApply;

    public Voucher(long couponId, @NotEmpty String code, @NotEmpty String dateStart, @NotEmpty String dateEnd, @NotEmpty double valueDiscount, @NotEmpty String typeDiscount, double priceApply) {
        this.couponId = couponId;
        this.code = code;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.valueDiscount = valueDiscount;
        this.typeDiscount= typeDiscount;
        this.priceApply = priceApply;
    }

    public Voucher() {

    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public double getValueDiscount() {
        return valueDiscount;
    }

    public void setValueDiscount(double valueDiscount) {
        this.valueDiscount = valueDiscount;
    }

    public String getTypeDiscount() {
        return typeDiscount;
    }

    public void setTypeDiscount(String typeDiscount) {
        this.typeDiscount = typeDiscount;
    }

    public double getPriceApply() {
        return priceApply;
    }

    public void setPriceApply(double priceApply) {
        this.priceApply = priceApply;
    }
}


