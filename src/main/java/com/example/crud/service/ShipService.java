package com.example.crud.service;

import com.example.crud.entity.Address;

import java.util.List;

public interface ShipService {
    void save(Address address);
    void delete(Address address);
    Address getAddress(long addressId);
    List<Address> getListAddressOfUser(long userId);
}
