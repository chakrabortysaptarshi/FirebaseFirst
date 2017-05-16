package com.example.ssapt.firebasefirst;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    EditText editTextuserName, editTextMessage;
    Button buttonSetuserName, buttonSendMessage;


    Button foggyButton;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mRootRef.child("condition");
    //DatabaseReference vegRef = conditionRef.child("Potato");

    static final String[] fruits = {"apple", "oranges", "banana", "watermelon", "lemon", "Grapes"};
    ArrayList<String> fruitList;

    ArrayAdapter adapter;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextuserName = (EditText) findViewById(R.id.editTextuserName);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        buttonSetuserName = (Button) findViewById(R.id.buttonSetUserName);
        buttonSendMessage = (Button) findViewById(R.id.buttonSend);



        listView = (ListView) findViewById(R.id.fruitList);

        fruitList = new ArrayList<String>();
        for(String s:fruits)
            fruitList.add(s);


        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, fruitList);
        listView.setAdapter(adapter);

       // conditionView = (TextView) findViewById(R.id.textViewCondition);
       // sunnyButton = (Button) findViewById(R.id.buttonSunny);
       // foggyButton = (Button) findViewById(R.id.buttonFoggy);
    }

    @Override
    protected void onStart() {
        super.onStart();

        editTextuserName.setSelection(0);

        if(editTextuserName.getText().toString().trim().length() == 0)
            Toast.makeText(getApplicationContext(), "Set Username", Toast.LENGTH_LONG).show();

        buttonSetuserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextuserName.getText().toString().trim().length() == 0)
                    Toast.makeText(getApplicationContext(), "Empty Username not allowed", Toast.LENGTH_LONG).show();
                else {
                    editTextuserName.setKeyListener(null);
                    buttonSetuserName.setEnabled(false);
                }
            }
        });

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRootRef.child("condition").child(editTextuserName.getText().toString()).setValue(editTextMessage.getText().toString());
            }
        });

        conditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()) {
                    DataSnapshot dataSnapshot1 = iterator.next();
                    Log.i("Childern is", dataSnapshot1.getKey() + "\t" + dataSnapshot1.getValue());
                    if(!fruitList.contains(dataSnapshot1.getValue().toString()))
                        fruitList.add(dataSnapshot1.getValue().toString());
                }



                // notifies the UI thread that background thread has updated the underlying data structure
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*sunnyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conditionRef.setValue("Sunny!");
            }
        });

        foggyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conditionRef.setValue("Foggy");
            }
        });
*/
    }


}
