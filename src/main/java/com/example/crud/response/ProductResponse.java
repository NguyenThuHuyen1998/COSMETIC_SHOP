package com.example.crud.response;

import com.example.crud.entity.Category;
import com.example.crud.entity.Product;

public class ProductResponse {
    private long id;
    private Category category;
    private String name ;
    private double price;
    private String description;
    private String preview;
    private String dateAdd;
    private String image;

    public ProductResponse(){

    }

    public ProductResponse(Product product){
        this.id= product.getId();
        this.dateAdd= product.getDateAdd();
        this.category= product.getCategory();
        this.name= product.getName();
        this.price= product.getPrice();
        this.description= product.getDescription();
        this.preview= product.getPreview();
        this.image= product.getImage();
    }

    public ProductResponse(Category category, String name, double price, String description, String preview, String multipartFile){
        this.category= category;
        this.name= name;
        this.price= price;
        this.preview= preview;
        this.description= description;
        this.image= multipartFile;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(String dateAdd) {
        this.dateAdd = dateAdd;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
