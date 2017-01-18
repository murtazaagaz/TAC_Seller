package com.blogspot.theandroidclassroom.tacseller.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.blogspot.theandroidclassroom.tacseller.R;
import com.blogspot.theandroidclassroom.tacseller.util.Constants;
import com.blogspot.theandroidclassroom.tacseller.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailOrderActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.image_detail) ImageView mImageView;
    @BindView(R.id.title_deail) TextView mTitle;
    @BindView(R.id.buyer_name) TextView mBuyerName;
    @BindView(R.id.buyer_email) TextView mBuyerEmail;
    @BindView(R.id.buyer_address) TextView mBuyerAddress;
    @BindView(R.id.spinner) Spinner mSpinner;
    @BindView(R.id.order_staus) TextView mStatus;
    @BindView(R.id.update_btn) Button mUpdateBtn;
    private DatabaseReference mDatabaseReference;
    private String mKey;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Detail");
        pd = new ProgressDialog(this);
        if (getIntent().hasExtra("key")){
             mKey = getIntent().getStringExtra("key");
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.ORDERS).child(mKey);
            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String imageUrl = (String) dataSnapshot.child(Constants.IMAGE).getValue();
                    String title = (String) dataSnapshot.child(Constants.PRODUCT_NAME).getValue();
                    String name = (String) dataSnapshot.child(Constants.NAME).getValue();
                    String email = (String) dataSnapshot.child(Constants.EMAIL).getValue();
                    String address = (String) dataSnapshot.child(Constants.ADDRESS).getValue();
                    String status = (String) dataSnapshot.child(Constants.ORDER_STATUS).getValue();

                    Picasso.with(getApplicationContext()).load(imageUrl).resize(250,250).into(mImageView);
                    mTitle.setText(title);
                    mBuyerName.setText("Buyer Name:  "+name);
                    mBuyerEmail.setText("Buyer Email:  "+email);
                    mBuyerAddress.setText("Buyer Addres:  "+address);
                    mStatus.setText("Order Status:  "+status);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mUpdateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Util.isNetworkAvailable()) {
                        pd.setMessage("Uploading");
                        pd.show();
                        updateStatus();
                    }
                }
            });
        }
        else {
            finish();
        }
    }

    private void updateStatus() {
        String status = (String) mSpinner.getSelectedItem();
       mDatabaseReference.child(Constants.ORDER_STATUS).setValue(status);
        pd.dismiss();


    }
}
