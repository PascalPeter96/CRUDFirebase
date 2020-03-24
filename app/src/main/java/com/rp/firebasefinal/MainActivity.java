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
    private static final String KEY_TITLE = "NewCities";
    private static final String KEY_DESCRIPTION = "LahoreCity";
    private TextView textViewData;

    //Step 1: Create object of Firebase Firestore
    private FirebaseFirestore objectFirebaseFirestore;

    private Dialog objectDialog;
    private EditText documentET,cityNameET,cityDetailsET;
    private DocumentReference noteref = objectFirebaseFirestore.collection("NewCities").document("LahoreCity");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Step 2: Initialize Firebase firestore object
        objectFirebaseFirestore=FirebaseFirestore.getInstance();
        objectDialog=new Dialog(this);

        objectDialog.setContentView(R.layout.please_wait);
        documentET=findViewById(R.id.documentIDET);
        textViewData = findViewById(R.id.infoTV);
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

    public  void loadNote(View v){
    noteref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {

            if (documentSnapshot.exists()) {
                String title = documentSnapshot.getString(KEY_TITLE);
                String description = documentSnapshot.getString(KEY_DESCRIPTION);



                textViewData.setText("NewCities " + title + "\n" + "LahoreCity: " + description);
            } else {
                Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
            }
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();

        }
    });



    }
}
