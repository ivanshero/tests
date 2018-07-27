package ivi.com.eivi.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ivi.com.eivi.R;

public class Rig extends AppCompatActivity {

    private EditText User_Prevate_name;
    private EditText User_Prevate_email;
    private EditText User_Prevate_password;
    private EditText User_Prevate_password2;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRefrenc;
    private DatabaseReference mDatabaseRefrenc11;
    private TextView textView23;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.rig);




    //    User_Prevate_name = (EditText) findViewById(R.id.User_Name_Rig);
        User_Prevate_email = (EditText) findViewById(R.id.User_Email);
        User_Prevate_password = (EditText) findViewById(R.id.User_Password);
        User_Prevate_password2 = (EditText) findViewById(R.id.User_Password2);
        textView23 = (TextView)findViewById(R.id.textView23);





        mAuth = FirebaseAuth.getInstance();
        mDatabaseRefrenc = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseRefrenc11 = FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(Rig.this ,Login.class);
            startActivity(intent);

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void   email_sign_in_button12  (View view){

        final String email  = User_Prevate_email.getText().toString().trim();
        String password     = User_Prevate_password.getText().toString().trim();
        String password2     = User_Prevate_password2.getText().toString().trim();


        if (password.equals(password2)){

            if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){



                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener <AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            String user_id = mAuth.getCurrentUser().getUid();

                            DatabaseReference current_user_db = mDatabaseRefrenc.child(user_id);
                            current_user_db.child("uid").setValue(mAuth.getUid());
                            current_user_db.child("uEmail").setValue(email);
                            current_user_db.child("userprofile").setValue("userprofile").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){

                                        Intent rigIntent = new Intent(Rig.this,AC1___SET_PROFILE.class);
                                        rigIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(rigIntent);


                                    }


                                }
                            });



                        }

                    }
                });

            }

        }else if (!password.equals(password2)) {

            textView23.setText(R.string.password_equals);
            User_Prevate_password.setText(null);
            User_Prevate_password2.setText(null);

        }
    }

    public void Con(View view) {

        ShareCompat.IntentBuilder.from(Rig.this)
                .setType("message/rfc822")
                .addEmailTo("Eivi.de@Hotmail.com")
                .setSubject("اكتب رسالتك وارسلها للدعم الفني")
                .setText("الدعم الفني ليس له اي علاقة بالمنشورات التي تتم النشر او حتى بالحظر , نقوم فقط بالرد على  الرسائل التي تتعلق بالامان والمشاكل المخصصة للتطبيق برمجيا و من ناحية التصميم , اذا كانت لديك مشاكل مع احد المشرفين يرجة كتابته , سوف نقوم بحل هذه الامور ايضا")
                .setChooserTitle("فريق الدعم الفني")
                .startChooser();


    }

    public void Coo (View view){

        Intent intent = new Intent(Rig.this , Login.class);
        startActivity(intent);

    }


}


















