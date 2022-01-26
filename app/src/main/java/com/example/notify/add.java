package com.example.notify;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class add extends AppCompatActivity {

    EditText txt;
    Button btn;
    DatabaseReference refer;
    loader load;
    private RequestQueue mQueue;
    private RequestQueue m2Queue;
    private String uid;
    public JSONObject laJso;
    public String lid=null;
    public JSONObject filJso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        txt = findViewById(R.id.text_ed);
        btn = findViewById(R.id.btn);
        load = new loader(add.this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setEnabled(false);
                add_data();
            }
        });
        mQueue = Volley.newRequestQueue(this);
        m2Queue = Volley.newRequestQueue(this);
        uid = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if(lid==null)
            createLabel();
    }
    public void add_data(){
        String s = txt.getText().toString();
        if(isValidEmail(s)) {
            load.startLoadingDialog();
            String str = FirebaseAuth.getInstance().getCurrentUser().getUid();
            refer = FirebaseDatabase.getInstance().getReference("users");
            String key = refer.child(str).push().getKey();
            refer.child(str).child(key).setValue(s).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    try {
                        createFilter(s);
                        Toast.makeText(add.this, "Successfully added", Toast.LENGTH_SHORT).show();
                        btn.setEnabled(true);
                        load.dismissDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(add.this, "failed to add!!", Toast.LENGTH_SHORT).show();
                    btn.setEnabled(true);
                    load.dismissDialog();
                }
            });
        }
        else {
            Toast.makeText(add.this,"Enter a valid e-mail",Toast.LENGTH_SHORT).show();
            btn.setEnabled(true);
        }
        txt.setText(null);
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public void createLabel(){
        String url="https://gmail.googleapis.com/gmail/v1/users/"+uid+"/labels?access_token="+Profile1.access;
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("labelListVisibility","labelShow");
            jsonBody.put("messageListVisibility","show");
            jsonBody.put("name","note");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,jsonBody,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("success in label");
                try {
                    laJso = response;
                     lid= laJso.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            public Map<String,String> getHeaders() throws AuthFailureError{
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                return headerMap;
            }

        };
        mQueue.add(jsonObjectRequest);
    }
    public void createFilter(String str) throws JSONException {
        System.out.println(lid+" in filter func");
        JSONArray ja = new JSONArray();
        ja.put(lid);
        JSONObject jbj = new JSONObject();
        jbj.put("addLabelIds",ja);
        JSONObject cro = new JSONObject();
        cro.put("from",str);
        JSONObject jsonBody =new JSONObject();
        jsonBody.put("action",jbj);
        jsonBody.put("criteria",cro);
        System.out.println(jsonBody.toString());
        String url="https://gmail.googleapis.com/gmail/v1/users/"+uid+"/settings/filters?access_token="+Profile1.access;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("success in filter");
                filJso = response;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            public Map<String,String> getHeaders() throws AuthFailureError{
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                return headerMap;
            }

        };
        m2Queue.add(jsonObjectRequest);
    }
}