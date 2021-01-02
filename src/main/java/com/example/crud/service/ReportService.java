package com.example.crud.service;

import com.example.crud.entity.Order;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ReportService {
    Map<String, Object> getReport() throws ParseException;
}
