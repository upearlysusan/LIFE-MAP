package com.example.life_mp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class MemberDataMain extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private List<Object> bill_list = new ArrayList<>();
    private List<String> list_ids = new ArrayList<>();
    private FirebaseFirestore db;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_member_data_main );


        db = FirebaseFirestore.getInstance();
        email = user.getEmail();
        listView = (ListView) findViewById(R.id.listView_bills);

        db.collection(email).whereEqualTo("doctor","true").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                bill_list.clear();
                list_ids.clear();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots ) {
                    Map<String,Object> rec = snapshot.getData();
                    String id = snapshot.getId();

                    //String current =  date + " " + month + " " + year;
                   // String total = rec.get("amount").toString();
                    list_ids.add(id);
                    bill_list.add(id);
                }
                ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(getApplicationContext(),R.layout.view_scroll,bill_list);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String entry = bill_list.get(i).toString();
                        String entry_id = list_ids.get(i).toString();
                        //Intent intent = new Intent(AllBillsMonthlyActivity.this,IndBillActivity.class);
                        //intent.putExtra("id_collection",entry_id);
                        Toast.makeText(MemberDataMain.this,entry_id,Toast.LENGTH_SHORT).show();
                        //startActivity(intent);
                    }
                });
            }
        });

    }
}