package com.example.tunganh.owl_server.General;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.tunganh.owl_server.Model.Order_Details;
import com.example.tunganh.owl_server.Model.User;

public class General {
    public static User currentUser;
    public static Order_Details currentOrder_Details;



    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

    public static final int PICK_IMAGE_REQUEST = 71;

    public static  String convertCodetoStatus (String status) {
        if (status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "Shipping";
        else
            return "Finish Deliver";
    }

}
