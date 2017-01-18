package com.blogspot.theandroidclassroom.tacseller.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.theandroidclassroom.tacseller.R;
import com.blogspot.theandroidclassroom.tacseller.pojo.OrderPojo;
import com.blogspot.theandroidclassroom.tacseller.storage.MySharedPrefrences;
import com.blogspot.theandroidclassroom.tacseller.util.Constants;
import com.blogspot.theandroidclassroom.tacseller.util.Util;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.home_recycle) RecyclerView mRecycleView;
    @BindView(R.id.home_text) TextView mText;
    private FirebaseAuth mAuth;
    private Query mItemRef;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Recent Orders");

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference data = FirebaseDatabase.getInstance().getReference().child(Constants.ORDERS);
        mItemRef = data.orderByChild(Constants.DEALERUID).equalTo(mAuth.getCurrentUser().getUid());


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        mRecycleView.setLayoutManager(manager);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,AddProductActivity.class));
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Util.isNetworkAvailable()){
            pd.show();

        FirebaseRecyclerAdapter<OrderPojo,MyViewHolder> adapter = new FirebaseRecyclerAdapter<OrderPojo, MyViewHolder>(
                OrderPojo.class,
                R.layout.home_list_layout,
                MyViewHolder.class,
                mItemRef
        ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, OrderPojo model, int position) {
                viewHolder.setupViews(getApplicationContext(),model.getImage(),model.getProductName(),model.getStatus(),model.getProductPrize());
                String key = getRef(position).getKey();

                viewHolder.ItemClick(HomeActivity.this,key);
                pd.dismiss();
                mText.setVisibility(View.GONE);
            }
        };
        mRecycleView.setAdapter(adapter);
    }
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder{

         View mView;
        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
         void setupViews(Context context,String imageUrl, String name, String staus, String prize){
            TextView mName = (TextView) mView.findViewById(R.id.name_recycle);
            TextView mStatus = (TextView) mView.findViewById(R.id.status);
            TextView mPrize = (TextView) mView.findViewById(R.id.prize_recycle);
            ImageView mImage = (ImageView) mView.findViewById(R.id.image_view_recycle);

            Picasso.with(context).load(imageUrl).resize(100,100).into(mImage);
            mName.setText(name);
            mStatus.setText(staus);
            mPrize.setText(prize);

        }
        void ItemClick (final Activity activity, final String key){
            RelativeLayout relativeLayout = (RelativeLayout) mView.findViewById(R.id.list_layout);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity,DetailOrderActivity.class);
                    intent.putExtra("key",key);
                    activity.startActivity(intent);
                }
            });
        }
    }
}
