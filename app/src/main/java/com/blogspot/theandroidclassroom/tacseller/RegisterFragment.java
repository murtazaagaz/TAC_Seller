package com.blogspot.theandroidclassroom.tacseller;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.theandroidclassroom.tacseller.activity.HomeActivity;
import com.blogspot.theandroidclassroom.tacseller.storage.MySharedPrefrences;
import com.blogspot.theandroidclassroom.tacseller.util.Constants;
import com.blogspot.theandroidclassroom.tacseller.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    @BindView(R.id.name_reg) EditText mNameEdit;
    @BindView(R.id.email_reg) EditText mEmailEdit;
    @BindView(R.id.storename_reg) EditText mstoreNameEdit;
    @BindView(R.id.pass_reg) EditText mPassEdit;
    @BindView(R.id.reg_btn) Button mRegBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private DatabaseReference mDealersRef;
    private ProgressDialog pd;
    private MySharedPrefrences sp;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this,v);
        sp = MySharedPrefrences.getInstance(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Intent i =  new Intent(getActivity(),HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        };
        mDealersRef = FirebaseDatabase.getInstance().getReference().child(Constants.DEALER_REF);
        pd =  new ProgressDialog(getActivity());
        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isNetworkAvailable()){
                    checkDetailAndRegister();
                }
                else {
                    Toast.makeText(getContext(),"No Internet!",Toast.LENGTH_LONG).show();
                }

            }
        });
        return v;
    }

    private void checkDetailAndRegister(){
        String email = mEmailEdit.getText().toString().trim();
        String name = mNameEdit.getText().toString().trim();
        String storeName = mstoreNameEdit.getText().toString().trim();
        String password = mPassEdit.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(name) || TextUtils.isEmpty(storeName) || TextUtils.isEmpty(password)){
            Toast.makeText(getContext(),"Please fill All the Fields..",Toast.LENGTH_LONG).show();
            return;

        }
       if (password.length() <= 7){
           Toast.makeText(getContext(),"Password must be more than 8 char",Toast.LENGTH_LONG).show();
           return;
       }
        else {
           doRegister(email,name,storeName,password);
       }

    }

    private void doRegister(final String email, final String name, final String storeName, String password) {
        pd.setMessage("Signing Up...");
        pd.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    mDealersRef.child(mAuth.getCurrentUser().getUid()).child(Constants.NAME).setValue(name);
                    mDealersRef.child(mAuth.getCurrentUser().getUid()).child(Constants.STORE_NAME).setValue(storeName);
                    mDealersRef.child(mAuth.getCurrentUser().getUid()).child(Constants.EMAIL).setValue(email);
                    mDealersRef.child(mAuth.getCurrentUser().getUid()).child(Constants.UID).setValue(mAuth.getCurrentUser().getUid());
                    pd.dismiss();
                    sp.setDealerEmail(email);
                    sp.setDealerName(name);
                    sp.setDealerStoreName(storeName);
                    sp.setDealerUid(mAuth.getCurrentUser().getUid());
                    Intent i =  new Intent(getActivity(),HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                else{
                    pd.dismiss();
                    Log.w("REGISTER","MUR:",task.getException());
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String exep = e.getMessage();
                pd.dismiss();
                Toast.makeText(getContext(),exep,Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListner);
    }
}
