package com.example.crud.entity;

import org.json.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Table(name = "tblAddress")
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id")
    private long addressId;

    @NotEmpty(message = "*Please provider your city")
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "detail_address")
    @NotEmpty(message = "*Please provider your address")
    private String detailAddress;

    @NotEmpty(message = "*Please provider your phone")
    @Column(name = "phone")
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Address(long addressId, @NotEmpty(message = "*Please provider your city") String fullName, @NotEmpty(message = "*Please provider your address") String detailAddress, @NotEmpty(message = "*Please provider your phone") String phone, User user) {
        this.addressId = addressId;
        this.fullName = fullName;
        this.detailAddress = detailAddress;
        this.phone = phone;
        this.user = user;
    }

    public Address() {

    }



    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
