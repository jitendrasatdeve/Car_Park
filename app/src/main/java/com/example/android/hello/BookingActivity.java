package com.example.android.hello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.android.hello.databinding.ActivityBookingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookingActivity extends AppCompatActivity {

    ActivityBookingBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,locationRef;
    String p_name,location,carNo,userId;
    FirebaseUser firebaseUser;
    ArrayList<String> locations = new ArrayList<>();
    ArrayAdapter<String> locationsItem;
    AddSlot addSlot;
    String newSlotStr,lastSlotStr;
    int newSlotInt,lastSlotInt;
    String bookingStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Booking");
        locationRef = firebaseDatabase.getReference("Addslot");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();

        locationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    addSlot = dataSnapshot.getValue(AddSlot.class);
                    locations.add(addSlot.getS_location());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
            //<> me string (down)
        locationsItem = new ArrayAdapter<>(this,R.layout.location_list_item,locations);

        binding.autoCompleteText.setAdapter(locationsItem);
        binding.autoCompleteText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                location = adapterView.getItemAtPosition(i).toString();
            }
        });


        binding.btnSubmitBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                p_name = binding.etName.getText().toString();
                carNo = binding.etCarNo.getText().toString();

                if (TextUtils.isEmpty(p_name) || TextUtils.isEmpty(location) || TextUtils.isEmpty(carNo)) {
                    Toast.makeText(BookingActivity.this, "Please add some data", Toast.LENGTH_SHORT).show();
                } else {

                    Booking booking = new Booking(p_name,location,carNo);

                    locationRef.child(location).child("noOfSlots").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.i("msg1",snapshot.toString());
                            lastSlotStr  = snapshot.getValue(String.class);
                            lastSlotInt = Integer.parseInt(lastSlotStr);
                            if(lastSlotInt<=0){
                                Toast.makeText(BookingActivity.this, "Sorry! No slots available at "+ location, Toast.LENGTH_SHORT).show();

                            }else{
                                newSlotInt = lastSlotInt-1;
                                newSlotStr = Integer.toString(newSlotInt);
                                Log.i("msg2_lastSlotStr",lastSlotStr);
                                Log.i("msg3_newSlot",newSlotStr);
                                locationRef.child(location).child("noOfSlots").setValue(newSlotStr);

                                databaseReference.child(userId).setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        binding.etName.setText("");
                                        binding.autoCompleteText.setText("");
                                        binding.etCarNo.setText("");
                                        Toast.makeText(BookingActivity.this, "Booking Initiated ", Toast.LENGTH_SHORT).show();
                                        bookingStatus = "Initiated";

                                        Intent intent = new Intent(BookingActivity.this,BookingDoneActivity.class);
                                        intent.putExtra("currLocation",location);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }
        });
    }
}