package com.example.tunganh.owl_server.AdapterView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tunganh.owl_server.Display.ItemClickListener;
import com.example.tunganh.owl_server.General.General;
import com.example.tunganh.owl_server.R;

public class MenuAdapterView extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener
{

    public TextView tvMenuName;
    public ImageView imageView1;

    private ItemClickListener itemClickListener;

    public MenuAdapterView(@NonNull View itemView) {
        super(itemView);

        tvMenuName=itemView.findViewById(R.id.menu_name);
        imageView1=itemView.findViewById(R.id.menu_image);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select Action");

        contextMenu.add(0,0,getAdapterPosition(),General.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(),General.DELETE);
    }
}