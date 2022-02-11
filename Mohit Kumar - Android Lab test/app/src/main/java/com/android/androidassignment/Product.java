package com.android.androidassignment;

public class Product {
    int id;
    String productname,productdesc,productprice,latitude,longitude;

    public Product(int id, String productname,String productdesc,String productprice,
                   String latitude,String longitude)
    {
        this.id = id;
        this.productname = productname;
        this.productdesc = productdesc;
        this.productprice = productprice;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId()
    {
        return id;
    }

    public String getProductname() {
        return productname;
    }

    public String getProductdesc() {
        return productdesc;
    }

    public String getProductprice() {
        return productprice;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
