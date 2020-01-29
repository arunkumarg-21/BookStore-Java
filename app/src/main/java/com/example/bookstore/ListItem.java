package com.example.bookstore;

public class ListItem {
    private String mHead;
    private String mDesc;
    private int mImage;

    public ListItem(String mHead, String mDesc,int mImage) {
        this.mHead = mHead;
        this.mDesc = mDesc;
        this.mImage=mImage;
    }

    public int getmImage() {
        return mImage;
    }

    public void setmImage(int mImage) {
        this.mImage = mImage;
    }

    public String getHead() {
        return mHead;
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
