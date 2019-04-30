package com.example.searchfilter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    SearchView searchView;
    DatabaseReference databaseReference;
    Spinner spinner;
    boolean counter;
    Query query;

    private static final String[] paths = {"Name", "Follower"};

    ArrayList<User> list;
    ArrayAdapter<String> stringArrayAdapter;

    // Root Database Name for Firebase Database.
    public static final String Database_Path = "People_Details_Database";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.searhView);
        spinner = findViewById(R.id.spinner);

        stringArrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item,paths);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stringArrayAdapter);
        spinner.setOnItemSelectedListener(this);

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setKeyboardOpen();

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path).child("User");

        if (databaseReference!=null){
            orderItem(databaseReference, false);
        }
        if (searchView!=null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return false;
                }
            });
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        query = databaseReference.orderByChild("name");
        counter=false;
        switch (position) {
            case 0:
                Log.i("Tap","" + position);
                counter=false;
                query = databaseReference.orderByChild("name");
                orderItem(query,counter);
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                Log.i("Tap","" + position);
                counter=true;
                query = databaseReference.orderByChild("follower");
                orderItem(query,counter);
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                Log.i("Tap","" + position);
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void search(String s){
        ArrayList<User> mlist = new ArrayList<>();
        for (User user : list){
            if (user.getName().toLowerCase().contains(s.toLowerCase())){
                mlist.add(user);
            }
        }
        AdapterClass adapterClass = new AdapterClass(mlist, getApplicationContext());
        adapterClass.notifyDataSetChanged();
        recyclerView.setAdapter(adapterClass);
    }

    private void orderItem(Query query, final boolean counter){
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    list = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        list.add(dataSnapshot1.getValue(User.class));
                    }
                    if (counter==true){
                        Collections.reverse(list);
                    }
                    AdapterClass adapterClass = new AdapterClass(list, getApplicationContext());
                    adapterClass.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterClass);
                    Log.i("list", "" + list.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.addButton:
                Intent intent = new Intent(this, AddUser.class);
                startActivity(intent);
                Log.i("Bum", "Bumbum");
                break;
            case R.id.love:
                Intent intent1 = new Intent(this, Loved.class);
                startActivity(intent1);
                Log.i("Bum", "Lope");
                break;
        }
    }

    public void setKeyboardOpen(){

        final FloatingActionButton addButton = findViewById(R.id.addButton);

        KeyboardVisibilityEvent.setEventListener(this,
                new KeyboardVisibilityEventListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (isOpen){
                            addButton.setVisibility(View.INVISIBLE);
                        } else {
                            addButton.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
