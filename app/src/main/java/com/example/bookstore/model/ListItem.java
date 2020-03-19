package com.example.bookstore.model;

public class ListItem {
    private int id;
    private String mName;
    private String mDesc;
    private byte[] mImage;
    private double Price;

    public ListItem(String mName, String mDesc, byte[] mImage, double Price){
        this.mName = mName;
        this.mDesc = mDesc;
        this.mImage = mImage;
        this.Price = Price;
    }

    public ListItem(int id, String mName, String mDesc, byte[] mImage, double Price){
        this(mName,mDesc,mImage,Price);
        this.id=id;
    }



    public int getId() {
        return id;
    }

    public byte[] getmImage() {
        return mImage;
    }

    public void setmImage(byte[] mImage) {
        this.mImage = mImage;
    }

    public String getHead() {
        return mName;
    }

    public double getPrice() {
        return Price;
    }

    public void setHead(String head) {
        this.mName = head;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        this.mDesc = desc;
    }
}
