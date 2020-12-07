package com.example.sdl.OrderSummary;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.sdl.R;

import java.util.ArrayList;
class SummaryListAdapter implements ListAdapter {
    ArrayList<Order> orderList;
    ArrayList<Order> orderListForDatabase = new ArrayList<>();
    Context context;
    public SummaryListAdapter(Context context, ArrayList<Order> orderList) {
        this.orderList=orderList;
        this.context=context;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public int getCount() {
        return orderList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Order order=orderList.get(position);
        if(convertView==null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.list_order_summary, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            //getting the view elements of the list from the view
            TextView sNumber = convertView.findViewById(R.id.sNo);
            TextView item = convertView.findViewById(R.id.item);
            final TextView quantity = convertView.findViewById(R.id.quantity);
            Button buttonSubtract = convertView.findViewById(R.id.subtract);
            Button buttonAdd =convertView .findViewById(R.id.add);

            //adding values to the list item
            sNumber.setText(Integer.toString(order.getsNo()));
            item.setText(order.getItem());
            quantity.setText(Integer.toString(order.getQuantity()));

            //adding a click listener to the button to decrease quanity
            buttonSubtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //we will call this method to decrease the selected value from the list
                    //we are passing the position which is to be removed in the method
                    int i = order.getQuantity();
                    if (i > 1) {
                        order.setQuantity((order.getQuantity()-1));
                        order.setTotalPrice();
                        quantity.setText(Integer.toString(order.getQuantity()));
                    } else {

                    }
                }
            });

            //adding a click listener to the button to increase quanity
            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //we will call this method to increase the selected value from the list
                    //we are passing the position which is to be removed in the method
                    order.setQuantity(order.getQuantity() + 1);
                    quantity.setText(Integer.toString(order.getQuantity()));
                    order.setTotalPrice();
                }
            });}
        orderListForDatabase.add(order);
        return convertView;

    }
    public ArrayList<Order> passOrderListForDatabase(){
        return orderListForDatabase;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return 1;
        //return orderList.size();
    }
    @Override
    public boolean isEmpty() {
        return false;
    }
}
