package com.example.sdl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ParcelFragment extends Fragment {GridView gridView;
    TextView textView;
    String[] tableNumbers = {
            "P1", "P2", "P3", "P4", "P5", "P6", "P7"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.table_list, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridView);
        textView = (TextView)rootView.findViewById(R.id.textView);

        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),R.layout.table,R.id.textView,tableNumbers);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub

                /* appending I Love with car brand names */

            }
        });
        return rootView;}
}