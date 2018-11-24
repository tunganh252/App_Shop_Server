package com.example.tunganh.owl_server.Details;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tunganh.owl_server.AdapterView.DetailsAdapterView;
import com.example.tunganh.owl_server.Display.ItemClickListener;
import com.example.tunganh.owl_server.General.General;
import com.example.tunganh.owl_server.Model.Details;
import com.example.tunganh.owl_server.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class Details_list extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;

    RelativeLayout rootLayout;

    ///// Firebase
    FirebaseDatabase db;
    DatabaseReference detailsList;
    FirebaseStorage storage;
    StorageReference storageReference;

    String categoryId = "";

    FirebaseRecyclerAdapter<Details, DetailsAdapterView> adapter;

    //// Add new product details
    MaterialEditText etName, etDescription, etPrice, etDiscount;
    Button bt_select, bt_upload;

    Details newDetails;

    Uri saveUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_list);
        //// Firebase
        db = FirebaseDatabase.getInstance();
        detailsList = db.getReference("Details");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //// Init
        recyclerView = findViewById(R.id.recycler_details);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rootLayout = findViewById(R.id.rootLayout);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //////***/////
                showAddDetailsDialog();


            }
        });

        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty())
            loadListDetails(categoryId);

    }

    /////////////  Đẩy danh sách sản phẩm chi tiết ////////////
    private void loadListDetails(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Details, DetailsAdapterView>(
                Details.class,
                R.layout.detail_item,
                DetailsAdapterView.class,
                detailsList.orderByChild("menuId").equalTo(categoryId)

        ) {
            @Override
            protected void populateViewHolder(DetailsAdapterView viewHolder, Details model, int position) {

                viewHolder.details_name.setText(model.getName());
                Picasso.with(getBaseContext())
                        .load(model.getImage())
                        .into(viewHolder.details_image);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        ///***///


                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    ///////////// Code chức năng show Dialog để ADD sản phẩm /////////////////
    private void showAddDetailsDialog() {

        ///// Code block này giống showdialog Activity Home ///////

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Details_list.this);
        alertDialog.setTitle("Add new Product Details");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu = inflater.inflate(R.layout.add_new_details, null);
        alertDialog.setIcon(R.drawable.ic_edit_black_24dp);



        etName = add_menu.findViewById(R.id.etName);
        etDescription = add_menu.findViewById(R.id.etDescription);
        etPrice = add_menu.findViewById(R.id.etPrice);
        etDiscount = add_menu.findViewById(R.id.etDiscount);
        bt_select = add_menu.findViewById(R.id.bt_select);
        bt_upload = add_menu.findViewById(R.id.bt_upload);

        //// Event Button
        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(); /// Copy code from Home Activity
            }
        });

        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(); /// Copy code from Home Activity
            }
        });

        alertDialog.setView(add_menu);

        /// Set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                if (newDetails != null) {
                    detailsList.push().setValue(newDetails);
                    Snackbar.make(rootLayout, "New category   *" + newDetails.getName().toUpperCase() + "*    was added", Snackbar.LENGTH_LONG)
                            .show();
                }
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

    ///////////// Upload Image ////////////
    private void uploadImage() {

        if (saveUri != null) {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading....");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("image/" + imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            mDialog.dismiss();
                            Toast.makeText(Details_list.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // set value for newCategory if Image upload  and we can get download link
                                    newDetails = new Details();
                                    newDetails.setName(etName.getText().toString());
                                    newDetails.setDescription(etDescription.getText().toString());
                                    newDetails.setPrice(etPrice.getText().toString());
                                    newDetails.setDiscount(etDiscount.getText().toString());
                                    newDetails.setMenuId(categoryId);
                                    newDetails.setImage(uri.toString());

                                }
                            });
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(Details_list.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded " + progress + "%");
                        }
                    });

        }

    }

    ///////////// Choose Image ////////////
    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), General.PICK_IMAGE_REQUEST);

    }

    //////////////////////////////////////


    ///////////// Move to gallery to select Image /////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == General.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            saveUri = data.getData();
            bt_select.setText("Image Selected !");
        }
    }

    //////////// Update and Remove details //////////////////


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(General.UPDATE)) {

            showUpdateDetailsDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));

        } else if (item.getTitle().equals(General.DELETE)) {
            deleteDetails(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);

    }

    ///////////// Code chức năng show Dialog để Update sản phẩm /////////////////
    private void showUpdateDetailsDialog(final String key, final Details item) {
        //// Just copy showAddDetailsDialog() ///////////

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Details_list.this);
        alertDialog.setTitle("Edit Product Details");
        alertDialog.setMessage("Please fill full information");
        alertDialog.setIcon(R.drawable.ic_edit_black_24dp);

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu = inflater.inflate(R.layout.add_new_details, null);

        etName = add_menu.findViewById(R.id.etName);
        etDescription = add_menu.findViewById(R.id.etDescription);
        etPrice = add_menu.findViewById(R.id.etPrice);
        etDiscount = add_menu.findViewById(R.id.etDiscount);

        //// Set default value for view
        etName.setText(item.getName());
        etDescription.setText(item.getDescription());
        etPrice.setText(item.getPrice());
        etDiscount.setText(item.getDiscount());


        bt_select = add_menu.findViewById(R.id.bt_select);
        bt_upload = add_menu.findViewById(R.id.bt_upload);

        //// Event Button
        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(); /// Copy code from Home Activity
            }
        });

        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeImage(item);
            }
        });

        alertDialog.setView(add_menu);


        /// Set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();


                ///// Update information
                item.setName(etName.getText().toString());
                item.setDescription(etDescription.getText().toString());
                item.setPrice(etPrice.getText().toString());
                item.setDiscount(etDiscount.getText().toString());


                detailsList.child(key).setValue(item);
                Snackbar.make(rootLayout, " Product details ***" + item.getName().toUpperCase() + "*** was edited", Snackbar.LENGTH_LONG)
                        .show();
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

    ///////////// Xóa sản phẩm chi tiết ////////////
    private void deleteDetails(String key) {
        detailsList.child(key).removeValue();
    }

    ///////////// Update Image /////////////////
    private void changeImage(final Details item) {

        if (saveUri != null) {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading....");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("image/" + imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            mDialog.dismiss();
                            Toast.makeText(Details_list.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // set value for newCategory if Image upload  and we can get download link
                                    item.setImage(uri.toString());


                                }
                            });
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(Details_list.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded " + progress + "%");
                        }
                    });

        }

    }


}