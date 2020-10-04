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

public class MainProfActivity extends AppCompatActivity {
private EditText name;
private Button edit;
private Button add;
private Button submit;
FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main_prof );
        name=findViewById( R.id.editTextTextPersonName );
        edit=findViewById( R.id.button2 );
        add=findViewById( R.id.button3 );
        submit=findViewById( R.id.button );
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
                        Toast.makeText(MainProfActivity.this, "NO DOCUMENT ERROR", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainProfActivity.this, "NOT SUCCESSFUL", Toast.LENGTH_SHORT).show();
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                add.setVisibility(View.INVISIBLE);
                add.setClickable( false );
                edit.setVisibility( View.INVISIBLE );
                edit.setClickable( false );
                submit.setVisibility( View.VISIBLE);
                submit.setClickable(true);
                name.setEnabled( true );

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                add.setVisibility(View.VISIBLE);
                add.setClickable( true);
                edit.setVisibility( View.VISIBLE );
                edit.setClickable( true );
                submit.setVisibility( View.INVISIBLE);
                submit.setClickable( false );
                name.setEnabled( false );
                if (user != null) {
                    Map<String, Object> new_data = new HashMap<>();
                    new_data.put("name", name.getText().toString());
                    email = user.getEmail();
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
                                    Toast.makeText(MainProfActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Toast.makeText(MainProfActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainProfActivity.this,ProfAdd.class);
                startActivity(intent);
            }
        });

    }

}