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

public class DetailsAdapterView extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener
{

    public TextView details_name;
    public ImageView details_image;

    private ItemClickListener itemClickListener;

    public DetailsAdapterView(@NonNull View itemView) {
        super(itemView);

        details_name=itemView.findViewById(R.id.details_name);
        details_image=itemView.findViewById(R.id.details_image);

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
