package com.example.tunganh.owl_server;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tunganh.owl_server.General.General;
import com.example.tunganh.owl_server.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
    MaterialEditText et_user, et_pass;
    Button bt_dangnhap;

    FirebaseDatabase db;
    DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        et_user = findViewById(R.id.et_user);
        et_pass = findViewById(R.id.et_pass);
        bt_dangnhap = findViewById(R.id.bt_dangnhap);

        /// Truyền database
        db = FirebaseDatabase.getInstance();
        users = db.getReference("User");

        bt_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser(et_user.getText().toString(), et_pass.getText().toString());

            }
        });
    }

    private void signInUser(String user, String pass) {

        final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
        mDialog.setMessage("Please waiting...");
        mDialog.show();


        final String localUser = user;
        final String localPassword = pass;
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                if (dataSnapshot.child(et_user.getText().toString()).exists()) {
                    // Lay thong tin User

                    mDialog.dismiss();
                    User user = dataSnapshot.child(et_user.getText().toString()).getValue(User.class);

                    if (Boolean.parseBoolean(user.getAdmin())) {
                    } else
                        Toast.makeText(SignIn.this, "Please login with Admin account !!!", Toast.LENGTH_LONG      ).show();

                    if (user.getPass().equals(et_pass.getText().toString())) {
                    } else
                        Toast.makeText(SignIn.this, "Wrong Password", Toast.LENGTH_SHORT).show();

                    if (Boolean.parseBoolean(user.getAdmin()) && user.getPass().equals(et_pass.getText().toString())) {
                        Toast.makeText(SignIn.this, "Login Successfull !!!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignIn.this, Home.class);
                        General.currentUser = user;
                        startActivity(i);
                        finish();
                    }
                } else
                    Toast.makeText(SignIn.this, "Please login with Admin account !!!", Toast.LENGTH_LONG).show();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
