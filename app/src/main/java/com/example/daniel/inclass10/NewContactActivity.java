package com.example.daniel.inclass10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewContactActivity extends AppCompatActivity {

    EditText nameField;
    EditText emailField;
    EditText phoneField;
    RadioGroup deptRadioGroup;
    ImageView avatarImageButton;
    int imageIndex;
    private DatabaseReference mDatabase;
    FirebaseUser user;
    private FirebaseAuth mAuth;
    ArrayList<Contact> cList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        cList = new ArrayList<>();
        nameField = findViewById(R.id.newContactNameField);
        emailField = findViewById(R.id.newContactEmailField);
        phoneField = findViewById(R.id.newContactPhoneField);
        avatarImageButton = findViewById(R.id.avatarImageButton);



        findViewById(R.id.newContactSubmitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Contact newContact = new Contact(nameField.getText().toString(),
                        emailField.getText().toString(),
                        phoneField.getText().toString(),
                        Integer.toString(imageIndex));

                Intent contactListIntent = new Intent(NewContactActivity.this, ContactsActivity.class);
                contactListIntent.putExtra(MainActivity.NEW_CONTACT_KEY, newContact);
                contactListIntent.putExtra(MainActivity.CONTACT_ADDED_KEY,true);
                //add to database
                //mDatabase.child(user.getUid()).child("contacts").push().setValue(newContact);
                //contactListIntent.putExtra(MainActivity.CONTACT_LIST_KEY, cList);
                startActivity(contactListIntent);
                finish();
            }
        });


/*
        mDatabase.child(user.getUid()).child("contacts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    //Log.d("tag", "onDataChange: " + postSnapshot.getValue(Contact.class));
                    Log.d("test", "Added " + postSnapshot.getValue(Contact.class));
                    cList.add(postSnapshot.getValue(Contact.class));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/



    }

    @Override
    protected void onResume() {
        super.onResume();
        imageIndex = getIntent().getExtras().getInt(MainActivity.AVATAR_KEY);
        switch (imageIndex){
            case -1:
                avatarImageButton.setImageResource(R.drawable.select_avatar);
                break;
            case 0:
                avatarImageButton.setImageResource(R.drawable.avatar_f_1);
                break;
            case 1:
                avatarImageButton.setImageResource(R.drawable.avatar_f_2);
                break;
            case 2:
                avatarImageButton.setImageResource(R.drawable.avatar_f_3);
                break;
            case 3:
                avatarImageButton.setImageResource(R.drawable.avatar_m_1);
                break;
            case 4:
                avatarImageButton.setImageResource(R.drawable.avatar_m_2);
                break;
            case 5:
                avatarImageButton.setImageResource(R.drawable.avatar_m_3);
                break;
        }
    }
}
