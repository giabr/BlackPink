package com.example.searchfilter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.searchfilter.MainActivity.Database_Path;

public class AddUser extends AppCompatActivity {

    EditText nameInput, statusInput, urlInput;
    String name,status,url;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        nameInput = findViewById(R.id.namaInput);
        statusInput = findViewById(R.id.statusInput);
        urlInput = findViewById(R.id.urlInput);

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path).child("User");

    }

    public void getData(View view){
        name = nameInput.getText().toString();
        status = statusInput.getText().toString();
        url = urlInput.getText().toString();

        User user = new User();
        user.setName(name);
        user.setStatus(status);
        user.setImg(url);
        databaseReference.child(name).setValue(user);
        Toast.makeText(this, name + " succesfully added", Toast.LENGTH_SHORT).show();
        Log.i("Tes",name+status+url);
    }
}
