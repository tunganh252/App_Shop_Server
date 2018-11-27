package com.example.tunganh.owl_server;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.tunganh.owl_server.AdapterView.OrderDetailAdapterView;
import com.example.tunganh.owl_server.General.General;

public class Order_detail extends AppCompatActivity {

    TextView order_id,order_name,order_phone,order_total,order_address,order_comment;
    String order_id_value="";
    RecyclerView list_order_detail;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);

        order_id=findViewById(R.id.order_id);
        order_name=findViewById(R.id.order_name);
        order_phone=findViewById(R.id.order_phone);
        order_total=findViewById(R.id.order_total);
        order_address=findViewById(R.id.order_address);
        order_comment=findViewById(R.id.order_comment);

        list_order_detail=findViewById(R.id.list_order_detail);
        list_order_detail.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        list_order_detail.setLayoutManager(layoutManager);

        if (getIntent() != null)
            order_id_value=getIntent().getStringExtra("OrderId");

        /// Set value
        order_id.setText(order_id_value);
        order_name.setText(General.currentOrder_Details.getName());
        order_phone.setText(General.currentOrder_Details.getPhone());
        order_total.setText(General.currentOrder_Details.getTotal());
        order_address.setText(General.currentOrder_Details.getAddress());
        order_comment.setText(General.currentOrder_Details.getComment());


        OrderDetailAdapterView adapter= new OrderDetailAdapterView(General.currentOrder_Details.getDetails());
        adapter.notifyDataSetChanged();
        list_order_detail.setAdapter(adapter);


    }

}
