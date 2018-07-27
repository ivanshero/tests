package ivi.com.eivi.Admin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ivi.com.eivi.R;

public class Admin extends AppCompatActivity {


    private DatabaseReference firebaseDatabase;
    private FirebaseAuth mAthu ;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin2);


        mAthu =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");

        editText = (EditText)findViewById(R.id.editText2);




    }

    public void add_Post (View view){

        final String Buttoon = editText.getText().toString().trim();

        if (!Buttoon.equals(null)){

            firebaseDatabase.child(Buttoon).child("username").setValue(Buttoon);
            Admin.this.finish();

        }else if (Buttoon.equals(null)){

            Toast.makeText(this, "  هذا الزر غير مقبول , قم بكتابة اسم الازرار اولا ثم انقر على موافق   ", Toast.LENGTH_SHORT).show();

        }


    }

    public void cancel1 (View view){

        Admin.this.finish();

    }




}
