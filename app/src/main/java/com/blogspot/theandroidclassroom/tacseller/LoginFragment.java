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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    @BindView(R.id.email_login) EditText mEmailEdit;
    @BindView(R.id.pass_login) EditText mPassEdit;
    @BindView(R.id.login_btn) Button mLoginBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDealerRef;
    private MySharedPrefrences sp;
    private ProgressDialog pd;



    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this,v);
        pd = new ProgressDialog(getContext());
        sp = MySharedPrefrences.getInstance(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mDealerRef = FirebaseDatabase.getInstance().getReference().child(Constants.DEALER_REF);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isNetworkAvailable()){
                    checkDetailAndLogin();
                }
            }
        });
        return v;
    }

    private void checkDetailAndLogin() {
        String email = mEmailEdit.getText().toString().trim();
        String password = mPassEdit.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(getContext(),"Please fill all fields...",Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length() <=7){
            Toast.makeText(getContext(),"password must be 8 or more chars..",Toast.LENGTH_LONG).show();
        }
        else {
            doLogin(email,password);
        }
    }

    private void doLogin(final String email, String password) {
        pd.setMessage("Signing In...");
        pd.show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    pd.dismiss();
                    if(mAuth.getCurrentUser() != null){
                        String uid = mAuth.getCurrentUser().getUid();
                        DatabaseReference mRef = mDealerRef.child(uid);
                        mRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String email = (String) dataSnapshot.child(Constants.EMAIL).getValue();
                                String storename = (String) dataSnapshot.child(Constants.STORE_NAME).getValue();
                                String name = (String) dataSnapshot.child(Constants.NAME).getValue();
                                String uid = (String) dataSnapshot.child(Constants.UID).getValue();


                                sp.setDealerUid(uid);
                                sp.setDealerEmail(email);
                                sp.setDealerName(name);
                                sp.setDealerStoreName(storename);
                                Log.d("TAG","MUR:" + email);

                                Intent i =  new Intent(getActivity(),HomeActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }


                }
                else {
                    pd.dismiss();
                    //Toast.makeText(getContext(),"Sorry Something went Wrong try again later",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                String exep = e.getMessage();
                Toast.makeText(getContext(),exep,Toast.LENGTH_LONG).show();
            }
        });



    }

}
