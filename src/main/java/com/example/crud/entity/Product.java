package com.example.crud.entity;

import com.example.crud.response.ProductResponse;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "tblPRODUCTS")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cate_id", nullable = false)
//    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private Category category;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "description")
    private String description;

    @Column(name = "preview")
    private String preview;

    @Column(name = "dateAdd")
    private long dateAdd;

    @Column(name = "image")
    private String image;

    @Column(name = "active")
    private boolean active;

    public Product() {
    }

    public Product(long id, Category category, String name, double price, String description, String preview, long dateAdd, String image) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.price = price;
        this.description = description;
        this.preview= preview;
        this.dateAdd= dateAdd;
        this.image= image;
    }

    public Product(ProductResponse productResponse){
//        this.category= productForm.getCategoryId();

    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public long categoryId(){
        return category.getId();
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
        Timestamp ts =new Timestamp(dateAdd);
        return ts.toString();
    }

    public long getDate(){
        return dateAdd;
    }

    public void setDateAdd(long dateAdd) {
        this.dateAdd = dateAdd;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
