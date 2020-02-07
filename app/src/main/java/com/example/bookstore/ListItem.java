package com.example.bookstore;

public class ListItem {
    private String mHead;
    private String mDesc;
    private byte[] mImage;

    public ListItem(String mHead, String mDesc,byte[] mImage) {
        this.mHead = mHead;
        this.mDesc = mDesc;
        this.mImage = mImage;
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
