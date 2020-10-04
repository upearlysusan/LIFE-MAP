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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainMemberActivity extends AppCompatActivity {
    private Button submit;
    private Button data;
    private Button edit;
    private EditText name;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db;
    private String email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main_member );
        edit= findViewById(R.id.edit);
        submit=findViewById( R.id.submit );
        data=findViewById( R.id.data );
        name=findViewById( R.id.name );
        name.setEnabled( false );
        db = FirebaseFirestore.getInstance();
        email = user.getEmail();
        DocumentReference docRef = db.collection(email).document("personal_data");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        name.setText( document.getString("name") );
                    } else {
                        Toast.makeText(MainMemberActivity.this, "NO DOCUMENT ERROR", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainMemberActivity.this, "NOT SUCCESSFUL", Toast.LENGTH_SHORT).show();
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                data.setVisibility(View.INVISIBLE);
                data.setClickable( false );
                edit.setVisibility( View.INVISIBLE );
                edit.setClickable( false );
                submit.setVisibility( View.VISIBLE);
                submit.setClickable( true );
                name.setEnabled( true );

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                data.setVisibility(View.VISIBLE);
                data.setClickable( true);
                edit.setVisibility( View.VISIBLE );
                edit.setClickable( true );
                submit.setVisibility( View.INVISIBLE);
                submit.setClickable( false );
                name.setEnabled( false );
                if (user != null) {
                    Map<String, Object> new_data = new HashMap<>();
                    new_data.put("name", name.getText().toString());
                    db.collection(email).document("personal_data")
                            .set(new_data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainMemberActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                                }
                            });


                } else {
                    Toast.makeText(MainMemberActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }


            }
        });
        data = findViewById(R.id.data);
        data.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMemberActivity.this,MemberDataMain.class);
                startActivity(intent);
            }
        });

    }
}