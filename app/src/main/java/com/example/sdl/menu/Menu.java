package com.example.sdl.menu;

import android.os.Parcel;
import android.os.Parcelable;

public class Menu implements Parcelable {
    String itemName;
    int ItemPrice;

    public Menu(String itemName, int itemPrice) {
        this.itemName = itemName;
        ItemPrice = itemPrice;
    }

    protected Menu(Parcel in) {
        itemName = in.readString();
        ItemPrice = in.readInt();
    }

    public static final Creator<Menu> CREATOR = new Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel in) {
            return new Menu(in);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(int itemPrice) {
        ItemPrice = itemPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemName);
        dest.writeInt(ItemPrice);
    }
}
