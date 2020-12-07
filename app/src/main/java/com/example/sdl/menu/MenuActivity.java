package com.example.sdl.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sdl.ActivityForTable;
import com.example.sdl.OrderSummary.Order;
import com.example.sdl.OrderSummary.OrderActivity;
import com.example.sdl.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.sdl.Flags.cFlag;



public class MenuActivity extends AppCompatActivity {
    private RecyclerView recycler_view;
    private Button confirm;
    private Button reset;
    int tablePos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        final String tableNo =  intent.getStringExtra("tableNo");
        if(tableNo!=null) {
             tablePos = Integer.parseInt(String.valueOf(tableNo.charAt(1)));
        }

        //Define buttons
        confirm= (Button) findViewById(R.id.confirm_button);
        reset= (Button) findViewById(R.id.reset_button);
        //Define recycleview
        recycler_view = (RecyclerView) findViewById(R.id.recycler_Expand);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        //Initialize your Firebase app
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Reference to your entire Firebase database
        DatabaseReference parentReference;
        parentReference = database.getReference("menu");

        parentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<ParentList> Parent = new ArrayList<>();
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()){


                    final String ParentKey = snapshot.getKey().toString();

                    snapshot.child("titre").getValue();

                    DatabaseReference childReference =
                            FirebaseDatabase.getInstance().getReference().child("menu/"+ParentKey);
                    childReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final List<ChildList> Child = new ArrayList<>();


                            for (DataSnapshot snapshot1:dataSnapshot.getChildren())
                            {
                                final String ChildName =  snapshot1.getKey().toString();
                                final int ChildValue =( (Long) snapshot1.getValue()).intValue();


                                snapshot1.child("titre").getValue();

                                Child.add(new ChildList(ChildName,ChildValue));

                            }

                            Parent.add(new ParentList(ParentKey, Child));

                            DocExpandableRecyclerAdapter adapter = new DocExpandableRecyclerAdapter(Parent);

                            recycler_view.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            System.out.println("Failed to read value." + error.toException());
                        }

                    });}}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!cFlag[tablePos-1]) {
                    if (menuList.size() != 0) {

                        Intent orderIntent = new Intent(MenuActivity.this, OrderActivity.class);
                        Bundle args = new Bundle();
                        args.putParcelableArrayList("ARRAYLIST",menuList);
                        orderIntent.putExtra("BUNDLE",args);
                        orderIntent.putExtra("tableNoFromMenu",tableNo);
                        cFlag[tablePos-1] = true;
                        startActivity(orderIntent);
                        finish();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "No item selected", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else{

                    cFlag[tablePos-1]=false;
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",menuList);
                    setResult(2,returnIntent);

                    finish();
                }
            }

        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuList.clear();
            }
        });

    }


    ArrayList<Menu> menuList = new ArrayList<>( );
    public class DocExpandableRecyclerAdapter extends ExpandableRecyclerViewAdapter<MyParentViewHolder,MyChildViewHolder> {


        public DocExpandableRecyclerAdapter(List<ParentList> groups) {
            super(groups);
        }
        @Override
        public MyParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_item, parent, false);
            return new MyParentViewHolder(view);
        }

        @Override
        public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item, parent, false);
            return new MyChildViewHolder(view);
        }

        @Override
        public void onBindChildViewHolder(final MyChildViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
            final ChildList childItem = ((ParentList) group).getItems().get(childIndex);

            holder.onBind(childItem.getTitle());
            final String TitleChild=childItem.getTitle();
            final int TitlePrice=childItem.getPrice();
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.checkBox.isChecked())
                    {
                        menuList.add(new Menu(TitleChild,TitlePrice));
                        Toast toast= null;
                        if (toast!= null) {
                            toast.cancel();
                        }
                        toast = Toast.makeText(getApplicationContext(), menuList.size()+" item selected", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else
                    {
                        for(int i = 0 ; i < menuList.size() ; i++){
                            if(TitleChild.equalsIgnoreCase(menuList.get(i).itemName)){
                                menuList.remove(i);
                            }
                        }
                        Toast toast;
                        toast = Toast.makeText(getApplicationContext(), TitleChild+" removed", Toast.LENGTH_SHORT);
                        toast.show();
                    }




                }

            });

        }


        @Override
        public void onBindGroupViewHolder(MyParentViewHolder holder, int flatPosition, final ExpandableGroup group) {
            holder.setParentTitle(group);

            if(group.getItems()==null)
            {
                holder.listGroup.setOnClickListener(  new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast toast = Toast.makeText(getApplicationContext(), group.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

            }
        }


    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(MenuActivity.this, ActivityForTable.class));
        finish();

    }



}