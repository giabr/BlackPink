package com.example.searchfilter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.searchfilter.MainActivity.Database_Path;

public class Profile extends AppCompatActivity {

    String receiveUser;
    TextView nameProfile,statusProfile;
    ImageView imageProfile;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameProfile = findViewById(R.id.nameProfile);
        statusProfile = findViewById(R.id.statusProfile);
        imageProfile = findViewById(R.id.imgProfile);

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path).child("User");

        receiveUser = getIntent().getExtras().get("profile").toString();
        getProfile(receiveUser);

        Toast.makeText(this, receiveUser, Toast.LENGTH_SHORT).show();
    }

    public void getProfile(final String user){
        ValueEventListener profile = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.child(user).getValue(User.class);
                String name = user1.getName();
                String status = user1.getStatus();
                String url = user1.getImg();
                String follower = user1.getFollower().toString();

                nameProfile.setText(name);
                statusProfile.setText(status);
                Glide.with(getApplicationContext()).load(url).into(imageProfile);

                Log.i("User", name + status + url + follower);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(profile);
    }
}
