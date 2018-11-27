package com.example.tunganh.owl_server.Order_status;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.tunganh.owl_server.AdapterView.OrderAdapterView;
import com.example.tunganh.owl_server.Display.ItemClickListener;
import com.example.tunganh.owl_server.General.General;
import com.example.tunganh.owl_server.Model.Order_Details;
import com.example.tunganh.owl_server.Order_detail;
import com.example.tunganh.owl_server.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class Order_status extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Order_Details, OrderAdapterView> adapter;

    FirebaseDatabase db;
    DatabaseReference order_details;

    MaterialSpinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);



        //// Firebase
        db = FirebaseDatabase.getInstance();
        order_details = db.getReference("Order_Details");

        //// Init
        recyclerView = findViewById(R.id.list_order);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrder();/// Load all Order

    }

    private void loadOrder() {

        adapter = new FirebaseRecyclerAdapter<Order_Details, OrderAdapterView>(
                Order_Details.class,
                R.layout.order_item,
                OrderAdapterView.class,
                order_details

        ) {
            @Override
            protected void populateViewHolder(OrderAdapterView viewHolder, final Order_Details model, int position) {

                viewHolder.tv_order_id.setText(adapter.getRef(position).getKey());
                viewHolder.tv_order_status.setText(General.convertCodetoStatus(model.getStatus()));
                viewHolder.tv_order_name.setText(model.getName());
                viewHolder.tv_order_phone.setText(model.getPhone());
                viewHolder.tv_order_email.setText(model.getEmail());
                viewHolder.tv_order_address.setText(model.getAddress());

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

/////////////////////////////// Google Map Direction Shipping /////////////////////////////////////////////
//                        Intent delivery_location= new Intent(Order_status.this,Delivery_Location.class);
//                        General.currentOrder_Details=model;
//                        startActivity(delivery_location);

                        Intent orderDetail = new Intent(Order_status.this,Order_detail.class);
                        General.currentOrder_Details=model;
                        orderDetail.putExtra("OrderId",adapter.getRef(position).getKey());
                        startActivity(orderDetail);




                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(General.UPDATE))
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        else if (item.getTitle().equals(General.DELETE))
            deleteOrder(adapter.getRef(item.getOrder()).getKey());

        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(String key, final Order_Details item) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Order_status.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Delivery Status");
        alertDialog.setIcon(R.drawable.ic_edit_black_24dp);

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.order_update, null);

        spinner = (MaterialSpinner) view.findViewById(R.id.statusSpinner);
        spinner.setItems("Placed", "Shipping", "Finish Deliver");


        //////

        alertDialog.setView(view);

        final String localKey = key;
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));

                order_details.child(localKey).setValue(item);

            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDialog.show();

    }


    private void deleteOrder(String key) {
        order_details.child(key).removeValue();
    }


}




