package com.example.android.hello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.android.hello.databinding.ActivityAddSlotBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddSlotActivity extends AppCompatActivity {

    ActivityAddSlotBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String s_location,noOfSlots,pricePerSlot,userId;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSlotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Addslot");

        binding.btnSubmitSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_location = binding.etLocation.getText().toString();
                noOfSlots = binding.etNoOfSlots.getText().toString();
                pricePerSlot = binding.etPricePerSlot.getText().toString();

                if (TextUtils.isEmpty(s_location) && TextUtils.isEmpty(noOfSlots) && TextUtils.isEmpty(pricePerSlot)) {
                    // if the text fields are empty
                    // then show the below message.
                    Toast.makeText(AddSlotActivity.this, "Please add some data", Toast.LENGTH_SHORT).show();
                } else {
                    // else call the method to add
                    // data to our database.
                    addDatatoFirebase(s_location,noOfSlots,pricePerSlot,userId);
                    //Toast.makeText(AddSlotActivity.this, userId, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addDatatoFirebase(String s_location,String noOfSlots,String pricePerSlot,String userId) {
        AddSlot addSlot = new AddSlot(s_location,noOfSlots,pricePerSlot,userId);

        databaseReference.child(s_location).setValue(addSlot).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                binding.etLocation.setText("");
                binding.etNoOfSlots.setText("");
                binding.etPricePerSlot.setText("");
                Toast.makeText(AddSlotActivity.this, "Slot Added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddSlotActivity.this, "AddSlotActivity Error!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}