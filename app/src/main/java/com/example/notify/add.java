package com.example.notify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add extends AppCompatActivity {

    EditText txt;
    Button btn;
    DatabaseReference refer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        txt = findViewById(R.id.text_ed);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_data();
            }
        });
    }
    public void add_data(){
        String s = txt.getText().toString();
        String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
        refer = FirebaseDatabase.getInstance().getReference("users");
        String key = refer.child(str).push().getKey();
        refer.child(str).child(key).setValue(s).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(add.this,"Successfully added",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(add.this,"failed to add!!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}