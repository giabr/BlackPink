package com.example.searchfilter;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    ImageView loveProfile;
    boolean isLoved;

    boolean counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameProfile = findViewById(R.id.nameProfile);
        statusProfile = findViewById(R.id.statusProfile);
        imageProfile = findViewById(R.id.imgProfile);
        loveProfile = findViewById(R.id.loveProfile);

        loveProfile.setColorFilter(getApplicationContext().getResources().getColor(R.color.grey));

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path).child("User");

        receiveUser = getIntent().getExtras().get("profile").toString();
        getProfile(receiveUser);
        loveTap();

        Toast.makeText(this, receiveUser, Toast.LENGTH_SHORT).show();
    }

    public void loveTap(){

        counter = true;

        loveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter==true){
                    updateLove(receiveUser, true);
                    loveProfile.setColorFilter(getApplicationContext().getResources().getColor(R.color.colorAccent));
                    Log.i("love", "pink");
                    counter=false;
                } else {
                    updateLove(receiveUser, false);
                    loveProfile.setColorFilter(getApplicationContext().getResources().getColor(R.color.grey));
                    Log.i("love", "grey");
                    counter=true;
                }
            }
        });
    }

    public void updateLove(final String user1, final boolean counter){
        databaseReference.child(user1).child("love").setValue(counter);
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
                isLoved = user1.isLove();

                if (isLoved == true){
                    loveProfile.setColorFilter(getApplicationContext().getResources().getColor(R.color.colorAccent));
                }

                nameProfile.setText(name);
                statusProfile.setText(status);
                Glide.with(getApplicationContext()).load(url).into(imageProfile);

                Log.i("User", name + status + url + follower + isLoved);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(profile);
    }
}
