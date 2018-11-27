package com.example.tunganh.owl_server.AdapterView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tunganh.owl_server.Model.Order;
import com.example.tunganh.owl_server.R;

import java.util.List;

class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView name, quantity, price, discount;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.product_name);
        quantity = itemView.findViewById(R.id.product_quantity);
        price = itemView.findViewById(R.id.product_price);
        discount = itemView.findViewById(R.id.product_discount);
    }
}

public class OrderDetailAdapterView extends RecyclerView.Adapter<MyViewHolder> {

    List<Order> myOders;

    public OrderDetailAdapterView(List<Order> myOders) {
        this.myOders = myOders;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_layout, parent, false);

        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = myOders.get(position);
        holder.name.setText(String.format("Name : %s",order.getProductName()));
        holder.quantity.setText(String.format("Quantity : %s",order.getQuantity()));
        holder.price.setText(String.format("Price : %s",order.getPrice()));
        holder.discount.setText(String.format("Discount : %s",order.getDiscount()));

    }

    @Override
    public int getItemCount() {
        return myOders.size();
    }
}
