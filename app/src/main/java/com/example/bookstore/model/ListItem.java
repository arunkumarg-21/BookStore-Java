package com.example.bookstore;

public class ListItem {
    private String mHead;
    private String mDesc;
    private byte[] mImage;
    private int Price;

    public ListItem(String mHead, String mDesc, byte[] mImage) {
        this.mHead = mHead;
        this.mDesc = mDesc;
        this.mImage = mImage;
    }

    public ListItem(String mHead,String mDesc, byte[] mImage,int Price){
        this(mHead,mDesc,mImage);
        this.Price = Price;
    }


    public byte[] getmImage() {
        return mImage;
    }

    public void setmImage(byte[] mImage) {
        this.mImage = mImage;
    }

    public String getHead() {
        return mHead;
    }

    public int getPrice() {
        return Price;
    }

    public void setHead(String head) {
        this.mHead = head;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        this.mDesc = desc;
    }
}
