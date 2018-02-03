package com.youcare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private List<Person> personList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private Button btnGo;
    private EditText edtUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        initViews();
        recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new MyAdapter(personList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareDummy();
        mAdapter.notifyDataSetChanged();
    }

    private void initViews() {
        btnGo = findViewById(R.id.btn_go);
        edtUserName = findViewById(R.id.edt_user_name);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtUserName.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Empty", Toast.LENGTH_SHORT).show();
                } else {
                    makeApiCallAndUpdate(edtUserName.getText().toString());
                }
            }
        });

    }

    private void makeApiCallAndUpdate(String userName) {
        String url = "http://13.58.147.78:8000/?name=" + userName;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                updateList(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error occurred !", Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 5, 1f));
        requestQueue.add(jsonObjectRequest);
    }

    private void updateList(JSONObject response) {
        try {
            Log.i("deepak", response.toString());
            String name = response.getString("name");
            int positive = response.getInt("positive");
            int negative = response.getInt("negative");
            String score = response.getString("score");
            Person person = new Person(name, (float) positive, (float) negative, Float.parseFloat(score));
            personList.add(0, person);
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void prepareDummy() {
        personList.add(new Person("@deep23", 60f, 40f, 0.6f));
        personList.add(new Person("@ram", 20f, 80f, 0.1f));
        personList.add(new Person("@jatin767", 10f, 90f, 0.05f));
        personList.add(new Person("@Rahul54", 50f, 50f, 0.5f));
        personList.add(new Person("@abhi765", 30f, 70f, 0.3f));
    }
}
