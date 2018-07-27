package ivi.com.eivi.Login;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import ivi.com.eivi.Home;
import ivi.com.eivi.R;

public class AC1___SET_PROFILE extends AppCompatActivity {
    private EditText namm1;
    private EditText namm2;
    private EditText date3;
    private TextView date300;

    private TextView t1;
    private TextView t2;
    private TextView t3;

    private DatabaseReference mDatabaseRefrenc , mCoins;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUsers;
    private FirebaseDatabase database;
    private String nnn;
    private TextView xzy;
    private Button login;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac1____set__profile);




        namm1 = (EditText) findViewById(R.id.nam1);
        namm2 = (EditText) findViewById(R.id.nam2);
        date3 = (EditText) findViewById(R.id.date3);
        date300 =(TextView) findViewById(R.id.dat00);


        t1 = (TextView) findViewById(R.id.textView22);
        t2 = (TextView) findViewById(R.id.textView23);



        t3 = (TextView) findViewById(R.id.textView25);

        login = (Button)findViewById(R.id.login);

        t1.setVisibility(View.INVISIBLE);
        t2.setVisibility(View.INVISIBLE);
        t3.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        mDatabaseRefrenc = FirebaseDatabase.getInstance().getReference().child("Users");
        mCoins = FirebaseDatabase.getInstance().getReference().child("Gold");

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {


            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void getNext11 (View view){

        final String name  = namm1.getText().toString().trim();
        final String name2  = namm2.getText().toString().trim();
        final String dat3  = date3.getText().toString().trim();

            if (dat3.equals("01") |
                dat3.equals("02") |
                dat3.equals("03") |
                dat3.equals("04") |
                dat3.equals("05") |
                dat3.equals("06") |
                dat3.equals("07") |
                dat3.equals("08") |
                dat3.equals("09") |
                dat3.equals("10") |
                dat3.equals("00") |
                dat3.equals("0")  |
                dat3.equals("1")  |
                dat3.equals("2")  |
                dat3.equals("3")  |
                dat3.equals("4")  |
                dat3.equals("6")  |
                dat3.equals("7")  |
                dat3.equals("8")  |
                dat3.equals("9")  |
                dat3.equals("10")) {

                date300.setText(R.string.kind);
            }
            else {
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(name2) && !TextUtils.isEmpty(dat3)) {
                    mDatabaseRefrenc.addValueEventListener(new ValueEventListener() {
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mDatabaseRefrenc.child(mAuth.getUid());

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // newPost.child("title").setValue(titleaddtext);
                            current_user_db.child("FerstName").setValue(name);
                            current_user_db.child("LastName").setValue(name2);
                            current_user_db.child("username").setValue(name + " " + name2);
                            current_user_db.child("Date").setValue(dat3);
                            mCoins.child(mAuth.getUid()).setValue(40);

                            Intent m = new Intent(AC1___SET_PROFILE.this, AC3__SET_PROFILE.class);
                            startActivity(m);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }
            }

        //****   ******   ***  **  ***//
        if (TextUtils.isEmpty(name)){
            String oo = String.valueOf(R.string.name_free);
            t1.setText(oo);
            t1.setVisibility(View.VISIBLE);
        }else {
            t1.setVisibility(View.INVISIBLE);
        }

        //****   ******   ***  **  ***//
        if (TextUtils.isEmpty(name2)){
            String oo = String.valueOf(R.string.namme2_free);
            t2.setText(oo);
            t2.setVisibility(View.VISIBLE);
        }else {
            t2.setVisibility(View.INVISIBLE);
        }
        if (TextUtils.isEmpty(dat3)){
            String oo = String.valueOf(R.string.date_free);
            t3.setText(oo);
            t3.setVisibility(View.VISIBLE);
        }else {
            t3.setVisibility(View.INVISIBLE);
        }
    }

    public void Con(View view) {

        ShareCompat.IntentBuilder.from(AC1___SET_PROFILE.this)
                .setType("message/rfc822")
                .addEmailTo("Eivi.de@Hotmail.com")
                .setSubject("اكتب رسالتك وارسلها للدعم الفني")
                .setText("الدعم الفني ليس له اي علاقة بالمنشورات التي تتم النشر او حتى بالحظر , نقوم فقط بالرد على  الرسائل التي تتعلق بالامان والمشاكل المخصصة للتطبيق برمجيا و من ناحية التصميم , اذا كانت لديك مشاكل مع احد المشرفين يرجة كتابته , سوف نقوم بحل هذه الامور ايضا")
                .setChooserTitle("فريق الدعم الفني")
                .startChooser();


    }
}
