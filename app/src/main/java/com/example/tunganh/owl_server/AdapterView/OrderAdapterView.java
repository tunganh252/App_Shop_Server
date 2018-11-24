package com.example.tunganh.owl_server.AdapterView;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.example.tunganh.owl_server.Display.ItemClickListener;
import com.example.tunganh.owl_server.R;

public class OrderAdapterView extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
    public TextView tv_order_id, tv_order_status, tv_order_name, tv_order_phone, tv_order_address, tv_order_email;

    private ItemClickListener itemClickListener;

    public OrderAdapterView(View itemView) {
        super(itemView);

        tv_order_id = itemView.findViewById(R.id.order_id);
        tv_order_status = itemView.findViewById(R.id.order_status);
        tv_order_name = itemView.findViewById(R.id.order_name);
        tv_order_phone = itemView.findViewById(R.id.order_phone);
        tv_order_email = itemView.findViewById(R.id.order_email);
        tv_order_address = itemView.findViewById(R.id.order_address);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select Action");

        contextMenu.add(0,0,getAdapterPosition(),"Update");
        contextMenu.add(0,1,getAdapterPosition(),"Delete");
    }
}