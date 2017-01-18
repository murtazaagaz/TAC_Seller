package com.blogspot.theandroidclassroom.tacseller.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.theandroidclassroom.tacseller.R;
import com.blogspot.theandroidclassroom.tacseller.util.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddProductActivity extends AppCompatActivity {

    @BindView(R.id.product_name) EditText mProductNameEdit;
    @BindView(R.id.product_description) EditText mProductDescripEdit;
    @BindView(R.id.product_prize) EditText mProductPrizeEdit;
    @BindView(R.id.add_product_button) Button mAddBtn;
    @BindView(R.id.image_btn) ImageButton mImageBtn;
    @BindView(R.id.product_quantity) EditText mProductQuantity;
    @BindView(R.id.temp_image) ImageView mTempImage;
    @BindView(R.id.image_text) TextView mTempText;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    private ProgressDialog pd;
    private FirebaseAuth mAuth;
    private Uri mImageUri;
    private int REQ_CODE = 1;
    private StorageReference mImageRefs;
    private DatabaseReference mStoreRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);
        pd = new ProgressDialog(this);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Item For Sale");
        mImageRefs = FirebaseStorage.getInstance().getReference().child(Constants.IMAGE);
        mStoreRef = FirebaseDatabase.getInstance().getReference().child(Constants.STORE_REF);
        mAuth =  FirebaseAuth.getInstance();
        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent = new Intent();
                imageIntent.setAction(Intent.ACTION_PICK);
                imageIntent.setType("image/*");
                startActivityForResult(imageIntent,REQ_CODE);

            }
        });

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkDetails();
            }
        });
    }



    private void checkDetails() {
        final String productName = mProductNameEdit.getText().toString().trim();
        final String productDescrip = mProductDescripEdit.getText().toString().trim();
        final String productPrize = mProductPrizeEdit.getText().toString().trim();
        final String productQuantity = mProductQuantity.getText().toString().trim();
        if (TextUtils.isEmpty(productName) || TextUtils.isEmpty(productDescrip)
                || TextUtils.isEmpty(productPrize) ||TextUtils.isEmpty(productQuantity)){
            Toast.makeText(getApplicationContext(),"Please Fill All Fields",Toast.LENGTH_LONG).show();
            return;
        }
        if (mImageUri == null){
            Toast.makeText(getApplicationContext(),"Please upload an image",Toast.LENGTH_LONG).show();
        }
        else {
            pd.setMessage("Uploading Item...");
            pd.show();
            mImageRefs.child(productName+random()).putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                    long ts = System.currentTimeMillis();
                    long tsInNeg = -ts ;
                    DatabaseReference mData = mStoreRef.push();
                    mData.child(Constants.PRODUCT_NAME).setValue(productName);
                    mData.child(Constants.PRODUCT_DESCRIPTION).setValue(productDescrip);
                    mData.child(Constants.PRODUCT_Prize).setValue(productPrize);
                    mData.child(Constants.PRODUCT_QUANTITY).setValue(productQuantity);
                    mData.child(Constants.IMAGE).setValue(downloadUrl);
                    mData.child(Constants.UID).setValue(mAuth.getCurrentUser().getUid());
                    mData.child(Constants.TIMESTAMP).setValue(tsInNeg);
                    pd.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK){
            Uri tempUri = data.getData();
            CropImage.activity(tempUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
            Log.d("TAG","MUR: 1");

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
            mImageUri = result.getUri();
            mImageBtn.setImageURI(mImageUri);
            mTempImage.setVisibility(View.GONE);
            mTempText.setVisibility(View.GONE);
            Log.d("TAG","MUR: 2");
            }
            else{
                Exception e = result.getError();
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("TAG","MUR:"+e.getMessage());

            }

        }
    }
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(100);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

}
