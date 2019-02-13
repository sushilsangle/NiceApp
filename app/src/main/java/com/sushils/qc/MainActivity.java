package com.sushils.qc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sushils.qc.model.User;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnRegister;
    RelativeLayout rootLaout;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Quicksand-Regular.ttf")
                                                                     .setFontAttrId(R.attr.fontPath)
                                                                     .build());

        setContentView(R.layout.activity_main);
        //TODO init firebase..

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");




        //TODO init values..

        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        rootLaout = (RelativeLayout)findViewById(R.id.rootLayout);

        //TODO Events...

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });

    }
    public void showLoginDialog() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Please use mobile to sign in");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login, null);

        final MaterialEditText edtMobile = login_layout.findViewById(R.id.edtMobile);
        final MaterialEditText edtPassword = login_layout.findViewById(R.id.edtPassword);
//        final MaterialEditText edtName = login_layout.findViewById(R.id.edtName);
//        final MaterialEditText edtEmail = login_layout.findViewById(R.id.edtEmail);
//        final MaterialEditText edtAddress = login_layout.findViewById(R.id.edtAddress);


        dialog.setView(login_layout);

        //TODO Set buttons..

        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                dialogInterface.dismiss();


                //Set disbale button Sign in if is processing.

                //btnSignIn.isEnabled(false);

                //TODO check validation..
                if (TextUtils.isEmpty(edtMobile.getText().toString())) {
                    Snackbar.make(rootLaout, "Please enter mobile number", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtPassword.getText().toString())) {

                    Snackbar.make(rootLaout, "Password enter password", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (edtPassword.getText().toString().length() < 6) {
                    Snackbar.make(rootLaout, "Password too short !!!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                final AlertDialog waitingDialog  = new SpotsDialog(MainActivity.this);
                waitingDialog.show();


                //TODO Login..

                mAuth.signInWithEmailAndPassword(edtMobile.getText().toString(),edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                waitingDialog.dismiss();

                                  startActivity(new Intent(MainActivity.this,Welcome.class));
                                  finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        waitingDialog.dismiss();
                        Snackbar.make(rootLaout,"Failed "+e.getMessage(),Snackbar.LENGTH_SHORT).show();

                        //TODO Active signIn button

                        //btnSignIn.isEnabled(true);
                    }
                });
            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();


    }

    public void showRegisterDialog()
    {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
        dialog.setMessage("Please use mobile to register");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register ,null);

        final MaterialEditText edtMobile = register_layout.findViewById(R.id.edtMobile);
        final MaterialEditText edtPassword = register_layout.findViewById(R.id.edtPassword);
        final MaterialEditText edtName = register_layout.findViewById(R.id.edtName);
        final MaterialEditText edtEmail = register_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtAddress = register_layout.findViewById(R.id.edtAddress);



        dialog.setView(register_layout);

        //TODO Set buttons..

        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                dialogInterface.dismiss();

                //TODO check validation..
                if (TextUtils.isEmpty(edtMobile.getText().toString()))
                {
                    Snackbar.make(rootLaout,"Please enter mobile number",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(edtPassword.getText().toString().length() < 6 )
                {
                    Snackbar.make(rootLaout,"Password too short !!!",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtName.getText().toString()))
                {
                    Snackbar.make(rootLaout,"Please enter name",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtAddress.getText().toString()))
                {
                    Snackbar.make(rootLaout,"Please enter Address",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtEmail.getText().toString()))
                {
                    Snackbar.make(edtEmail,"Please enter email",Snackbar.LENGTH_SHORT);
                }
                //TODO Register new user..

                mAuth.createUserWithEmailAndPassword(edtMobile.getText().toString(),edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //TODO save user to DB..
                                User user = new User();
                                user.setMobile(edtMobile.getText().toString());
                                user.setEmail(edtEmail.getText().toString());
                                user.setName(edtName.getText().toString());
                                user.setPassword(edtPassword.getText().toString());
                                user.setAddress(edtAddress.getText().toString());

                                //TODO use mobile as a key..
                                users.child(user.getMobile())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(rootLaout,"Register success fully !!!",Snackbar.LENGTH_SHORT).show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(rootLaout,"Failed "+ e.getMessage(),Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(rootLaout,"Failed Testing"+ e.getMessage(),Snackbar.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }

}
