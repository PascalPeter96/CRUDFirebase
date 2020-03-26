package com.rp.firebasefinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Step 1: Create object of Firebase Firestore
    private FirebaseFirestore objectFirebaseFirestore;

    private Dialog objectDialog;
    private EditText documentET,cityNameET,cityDetailsET;
    TextView valuesTv;
    DocumentReference objectDocumentReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Step 2: Initialize Firebase firestore object
        objectFirebaseFirestore=FirebaseFirestore.getInstance();
        objectDialog=new Dialog(this);

        objectDialog.setContentView(R.layout.please_wait);
        documentET=findViewById(R.id.documentIDET);
        valuesTv = findViewById(R.id.LTEXT);
        cityNameET=findViewById(R.id.cityNameTV);
        cityDetailsET=findViewById(R.id.citydetailsTV);
    }

    public void addValues(View v)
    {
        try
        {
            if(!documentET.getText().toString().isEmpty() && !cityNameET.getText().toString().isEmpty()
                    && !cityDetailsET.getText().toString().isEmpty()) {
                objectDialog.show();
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("city_name", cityNameET.getText().toString());

                objectMap.put("city_details", cityDetailsET.getText().toString());
                objectFirebaseFirestore.collection("NewCities")
                        .document(documentET.getText().toString()).set(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Fails to add data", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else
            {
                Toast.makeText(this, "Please enter valid details", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            objectDialog.dismiss();
            Toast.makeText(this, "addValues:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getValuesFromFb(View view)
    {

        try {
            if(!documentET.getText().toString().isEmpty()) {
                objectDocumentReference = objectFirebaseFirestore.collection("NewCities").document(

                        documentET.getText().toString()
                );

                objectDocumentReference.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String cityId= documentSnapshot.getId();
                                String cityDetails = documentSnapshot.getString("city_details");
                                String cityName = documentSnapshot.getString("city_name");

                                valuesTv.setText(
                                        "CityID : " +cityId + "\n"+
                                                "City Description : " +cityDetails + "\n"+
                                                "City Name : " +cityName



                                );

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Fails to get Values Back",Toast.LENGTH_SHORT).show();
                            }
                        });
            }



        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();


        }




    }
}
