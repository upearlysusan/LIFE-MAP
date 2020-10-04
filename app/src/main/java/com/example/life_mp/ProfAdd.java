package com.example.life_mp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class ProfAdd extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db;
    private EditText email;
    private EditText pass;
    private String check="";
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_prof_add );
        db = FirebaseFirestore.getInstance();
        email=findViewById( R.id.login_email );
        pass=findViewById( R.id.login_pass );
        submit=findViewById( R.id.login_button );
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        db.collection(email.getText().toString()).document("personal_data").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if(document.getString("sec").equals( pass.getText().toString() )){
                            check="YES" ;
                        }
                        else{   Toast.makeText(ProfAdd.this, "ERROR", Toast.LENGTH_SHORT).show();}

                    } else {
                        Toast.makeText(ProfAdd.this, "No such document", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfAdd.this, "ERROR LOADING", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (check.equals( "YES" )){
            Intent intent = new Intent(ProfAdd.this,ProfDataMain.class);
            String message = email.getText().toString();
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
                //Toast.makeText(ProfAdd.this, check, Toast.LENGTH_SHORT).show();
            }

        });


    }
}