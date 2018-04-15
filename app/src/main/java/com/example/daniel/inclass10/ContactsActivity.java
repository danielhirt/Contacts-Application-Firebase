package com.example.daniel.inclass10;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    FirebaseUser user;
    private FirebaseAuth mAuth;

    ArrayList<Contact> contactList;
    ArrayList<String> contactKeys;
    ContactAdapter adapter;
    boolean contactAdded;
    Contact contactToAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        contactList = new ArrayList<>();
        contactKeys = new ArrayList<>();
        contactToAdd = null;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();




        try {
            contactAdded = getIntent().getExtras().getBoolean(MainActivity.CONTACT_ADDED_KEY);
        }
        catch (NullPointerException e){
            contactAdded = false;
        }
        if(contactAdded) {
            contactToAdd = (Contact) getIntent().getExtras().getSerializable(MainActivity.NEW_CONTACT_KEY);
            mDatabase.child(user.getUid()).child("contacts").push().setValue(contactToAdd);
        }



/*
        Contact contact = new Contact("Bob Smith","b@b.com","111111","SIS","5");
        Contact contact2 = new Contact("Bobby Smithy","b@c.com","1111yy11","SS","4");
        mDatabase.child(user.getUid()).child("contacts").push().setValue(contact);
        mDatabase.child(user.getUid()).child("contacts").push().setValue(contact2);
*/
        ListView listView = (ListView)findViewById(R.id.contactListview);
        adapter = new ContactAdapter(this, R.layout.contact_layout, contactList);
        listView.setAdapter(adapter);

        mDatabase.child(user.getUid()).child("contacts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactList.clear();
                contactKeys.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    //Log.d("tag", "onDataChange: " + postSnapshot.getValue(Contact.class));
                    contactList.add(postSnapshot.getValue(Contact.class));
                    contactKeys.add(postSnapshot.getKey());

                }
                //Log.d("test",contactList.get(1) + "");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("test", "Delete: " + contactList.get(i));
                mDatabase.child(user.getUid()).child("contacts").child(contactKeys.get(i)).removeValue();
                return true;
            }
        });



        //mDatabase.child(user.getUid()).child("contactList").setValue(contactList);




        /*Contact contact = new Contact("Bob Smith","b@b.com","111111","SIS","5");
        Contact contact2 = new Contact("Bobby Smithy","b@c.com","1111yy11","SS","4");
        mDatabase.child(user.getUid()).child("contacts").push().setValue(contact);

        mDatabase.child(user.getUid()).child("contacts").child("-KysGH0vlBuM9FrqUNsR").removeValue();

        mDatabase.child(user.getUid()).child("contacts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d("tag", "onDataChange: " + postSnapshot.getValue(Contact.class));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/



        findViewById(R.id.createContactButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createIntent = new Intent(ContactsActivity.this, NewContactActivity.class);
                createIntent.putExtra(MainActivity.AVATAR_KEY,-1);
                startActivity(createIntent);
                finish();
            }
        });

        findViewById(R.id.logoutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(ContactsActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public class ContactAdapter extends ArrayAdapter<Contact> {
        public ContactAdapter(Context context, int resource, List<Contact> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Contact contact = getItem(position);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_layout, parent, false);

            TextView textViewname = (TextView) convertView.findViewById(R.id.itemNameText);
            TextView textViewEmail = (TextView) convertView.findViewById(R.id.itemEmailText);
            TextView textViewPhone = (TextView) convertView.findViewById(R.id.itemPhoneText);
            ImageView avatarImageButton = (ImageView) convertView.findViewById(R.id.itemAvatar);

            //set the data from the email object

            textViewname.setText(contact.name);
            textViewEmail.setText(contact.email);
            textViewPhone.setText(contact.phone);


            switch (Integer.parseInt(contact.image)){
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

            return convertView;
        }
    }


}
