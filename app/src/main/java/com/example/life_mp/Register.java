package com.example.life_mp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private EditText Email;
    private EditText Password;
    private Button register;
    private String Type;
    private FirebaseAuth mAuth;
    private RadioButton member_new;
    private RadioButton prof_new;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );

        Email = findViewById(R.id.register_email);
        member_new=findViewById( R.id.member_new );
        prof_new=findViewById( R.id.prof_new );
        Password = findViewById(R.id.register_pass);

        mAuth = FirebaseAuth.getInstance();
        register = (Button) findViewById(R.id.register_button);

        Type = "None";
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = Email.getText().toString();
                String txt_pass = Password.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pass)) {
                    Toast.makeText(Register.this, "Empty credentials!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register.this, MainActivity.class));
                } else if (txt_pass.length() < 6 ) {
                    Toast.makeText(Register.this, "Password too short", Toast.LENGTH_SHORT).show();
                } else if(Type == "None") {
                    Toast.makeText(Register.this, "Select Member/Professional", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txt_email,txt_pass,Type);
                }
            }
        });


    }
    private void registerUser(String email, final String password, String Type1) {
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Register.this, "User registration Successful", Toast.LENGTH_SHORT).show();
                            UpdateDataBase(user,password);
                            //Add the respective activity if a caretaker or if an admin person
                        } else {
                            Toast.makeText(Register.this, "User registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private void UpdateDataBase(FirebaseUser user, String password) {
        String email = user.getEmail();
        String uid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> new_user = new HashMap<>();
        new_user.put("email",email);
        new_user.put("password",password);
        new_user.put("type",Type);

        db.collection("users").document(uid).set(new_user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Register.this,"User Added to Database",Toast.LENGTH_SHORT).show();

                            if(Type.equals("Member") ) {
                                Intent intent = new Intent(Register.this,MainMemberActivity.class);
                                startActivity(intent);
                            } else if (Type.equals("Professional")) {
                                Intent intent = new Intent(Register.this,MainProfActivity.class);
                                startActivity(intent);
                            }

                        }
                    }
                });

    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.member_new:
                if (checked)
                    Type = "Member";
                Toast.makeText(Register.this,"Member",Toast.LENGTH_SHORT).show();
                break;
            case R.id.prof_new:
                if (checked)
                    Type = "Professional";
                Toast.makeText(Register.this,"Professional",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}