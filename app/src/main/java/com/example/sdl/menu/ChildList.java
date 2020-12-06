package com.example.sdl.menu;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sdl.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class ChildList implements Parcelable {

    private String title;
    private int price;


   /* public ChildList(String title) {
        this.title = title;
    }*/

    public ChildList(String title, int price) {
        this.title = title;
        this.price = price;
    }

    protected ChildList(Parcel in) {
        title = in.readString();
        price = in.readInt();

    }

    public static final Creator<ChildList> CREATOR = new Creator<ChildList>() {
        @Override
        public ChildList createFromParcel(Parcel in) {
            return new ChildList(in);
        }

        @Override
        public ChildList[] newArray(int size) {
            return new ChildList[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String Title) {
        this.title = Title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int Price) {
        this.price = Price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeInt(price);
    }
}

class MyChildViewHolder extends ChildViewHolder {

    public TextView listChild;

    public MyChildViewHolder(View itemView) {
        super(itemView);
        listChild = (TextView) itemView.findViewById(R.id.listChild);


    }

    public void onBind(String Sousdoc) {
        listChild.setText(Sousdoc);

    }


}


