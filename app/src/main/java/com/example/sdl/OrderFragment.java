package com.example.sdl;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.sdl.OrderSummary.OrderActivity;
import com.example.sdl.menu.MenuActivity;

import static com.example.sdl.Flags.fromOrderActivity;


public class OrderFragment extends Fragment {
    GridView gridView;
    TextView textView, GridViewItems;
    String[] tableNumbers = {
            "T1", "T2", "T3", "T4", "T5", "T6", "T7"};

    int tableNo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       String table =getActivity().getIntent().getStringExtra("tableNoFromOrderSummary");//t1


        if(table!=null){
            tableNo=Integer.parseInt(String.valueOf(table.charAt(1)));
            //System.out.println(tableNo);
            fromOrderActivity[tableNo-1]=true;
            //System.out.println(fromOrderActivity[tableNo-1]);
        }


        View rootView = inflater.inflate(R.layout.table_list, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridView);
        textView = (TextView)rootView.findViewById(R.id.textView);

        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),R.layout.table,R.id.textView,tableNumbers);
        final GridView gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setAdapter(adapter);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                //Toast.makeText(getActivity(),"Text! "+position,Toast.LENGTH_SHORT).show();
                //System.out.println(fromOrderActivity[position]);
               // System.out.println(tablePosition[position]);

                if(fromOrderActivity[position]) {
                    Intent orderIntent = new Intent(getContext(), OrderActivity.class);
                    orderIntent.putExtra("tableNoFromOrder", tableNumbers[position]);
                    startActivity(orderIntent);

                }
               else{
                    Intent menuIntent = new Intent(getContext(), MenuActivity.class);
                    menuIntent.putExtra("tableNo", tableNumbers[position]);
                    startActivity(menuIntent);


                }


            }
        });
    return rootView;
    }
}
