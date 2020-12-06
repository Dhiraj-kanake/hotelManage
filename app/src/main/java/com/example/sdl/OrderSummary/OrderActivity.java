package com.example.sdl.OrderSummary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sdl.ActivityForTable;
import com.example.sdl.R;
import com.example.sdl.menu.Menu;
import com.example.sdl.menu.MenuActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.sdl.Flags.cFlag;
import static com.example.sdl.Flags.fromOrderActivity;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    ArrayList<Menu> menuList = new ArrayList<>();
    public ArrayList<Order>  orderList = new ArrayList<Order>();
    public ArrayList<Order> orderListForDatabase = new ArrayList<>();
    ListView list;
    String tableNoFromMenu;
    String tableNoFromOrder;
     boolean dataExist= false;
    SummaryListAdapter summaryListAdapterAdapter;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Button addItemButton = (Button) findViewById(R.id.add_item);
        Button passOrderButton = (Button) findViewById(R.id.passToChef);
        Button endOrder = (Button) findViewById(R.id.endOrder);
        TextView table = (TextView) findViewById(R.id.tableNo);


        tableNoFromMenu = getIntent().getStringExtra("tableNoFromMenu");
        tableNoFromOrder = getIntent().getStringExtra("tableNoFromOrder");

        list = findViewById(R.id.listViewOrder);


        //to check from where orderActivity is opened
        if (tableNoFromOrder == null) {

            Bundle args = getIntent().getBundleExtra("BUNDLE");
            menuList =args.getParcelableArrayList("ARRAYLIST");

            addItemToOrderList();
            table.setText(tableNoFromMenu);
            displayList();
        } else {

            loadFromDatabase();
            table.setText(tableNoFromOrder);
        }


        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderActivity.this, MenuActivity.class);
                startActivityForResult(i, 1);
            }

        });

        passOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadToDatabase("Table/"+tableNoFromMenu);
                Intent intent = new Intent(OrderActivity.this, ActivityForTable.class);
                intent.putExtra("tableNoFromOrderSummary", tableNoFromMenu);
                //intent.putExtra();
                startActivity(intent);
                finish();
            }
        });
        endOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == 2) {
                Bundle args = getIntent().getBundleExtra("BUNDLE");
               // ArrayList<Menu> addItemList = (ArrayList<Menu>) args.getParcelableArrayList("ARRAYLIST");
                ArrayList<Menu> addItemList = this.getIntent().getExtras().getParcelableArrayList("ARRAYLIST");



                // System.out.println(addItemList.get(0) + "arrayllist has come");

                for (int i = 0; i < addItemList.size(); i++) {
                    menuList.add(addItemList.get(i));

                }

                // System.out.println(menuList.size() + "added menu size");
                addItemToOrderList();
                displayList();


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    protected void addItemToOrderList() {
        int i=0;
        for(Menu menu : menuList)
        {
            orderList.add(new Order(i++ + 1, menu.getItemName(),1,menu.getItemPrice()));

        }
    }

    protected void displayList() {

        summaryListAdapterAdapter = new SummaryListAdapter(this, orderList);
        list.setAdapter(summaryListAdapterAdapter);
    }


    protected void loadToDatabase(String path) {

        //Getting Instance of Firebase realtime database
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance();


        //Getting Reference to a User node, (it will be created if not already there)
        DatabaseReference itemNode = databaseInstance.getReference(path + "/");
        orderListForDatabase =  summaryListAdapterAdapter.passOrderListForDatabase();
        itemNode.setValue(orderListForDatabase);

    }

    protected void loadFromDatabase() {
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference tableNode = databaseInstance.getReference("Table/" + tableNoFromOrder + "/");
        tableNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // ArrayList<Order> orderListFromDatabase = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Order order = ds.getValue(Order.class);
                    orderList.add(order);

                }

                displayList();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.d( "debug",orderList.size()+" uuuuuuuuuuuuuuuuuuuuuuu");

    }

    protected void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
        builder.setTitle("Confirmation").setMessage("Do you want this to CheckOut")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadToDatabase("OrderForCheckout/"+tableNoFromOrder);
                        removeDataFromCaptain(tableNoFromOrder);
                        int tableNo=Integer.parseInt(String.valueOf(tableNoFromOrder.charAt(1)));
                        fromOrderActivity[tableNo-1]=false;
                        cFlag[tableNo-1] = false;
                        Intent checkoutIntent = new Intent(OrderActivity.this, ActivityForTable.class);
                        startActivity(checkoutIntent);
                        finish();

                    }
                }).setNegativeButton("cancel",null);
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void removeDataFromCaptain(String tableNo){

        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference tableNode = databaseInstance.getReference("Table/" + tableNo );
        tableNode.removeValue();
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(OrderActivity.this, ActivityForTable.class));
        finish();

    }



}