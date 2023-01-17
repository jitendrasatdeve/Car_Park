package com.example.android.hello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.hello.databinding.ActivityBookingDoneBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BookingDoneActivity extends AppCompatActivity {
    ActivityBookingDoneBinding binding;
    DatabaseReference locationRef;
    String lastSlotStr,newSlotStr;
    int lastSlotInt,newSlotInt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingDoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        locationRef = FirebaseDatabase.getInstance().getReference("Addslot");

        Intent intent = getIntent();
        String currLocation = intent.getStringExtra("currLocation");

        long duration = TimeUnit.MINUTES.toMillis(1);

        new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {
                String sDutarion = String.format(Locale.ENGLISH,"%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(l),
                        TimeUnit.MILLISECONDS.toSeconds(l)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
                binding.tvTimer.setText(sDutarion);
            }

            @Override
            public void onFinish() {
                binding.tvTimer.setText("00 : 00");
                Toast.makeText(BookingDoneActivity.this, "Booking Cancelled", Toast.LENGTH_SHORT).show();

                locationRef.child(currLocation).child("noOfSlots").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Log.i("msg1",snapshot.toString());
                        lastSlotStr  = snapshot.getValue(String.class);
                        lastSlotInt = Integer.parseInt(lastSlotStr);
                        newSlotInt = lastSlotInt+1;
                        newSlotStr = Integer.toString(newSlotInt);
                        Log.i("msg2_lastSlotStr",lastSlotStr);
                        Log.i("msg3_newSlot",newSlotStr);
                        locationRef.child(currLocation).child("noOfSlots").setValue(newSlotStr);

                        binding.animation1.setVisibility(View.INVISIBLE);
                        binding.animation2.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }.start();

    }
}