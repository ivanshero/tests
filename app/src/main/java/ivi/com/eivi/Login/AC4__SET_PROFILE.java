package ivi.com.eivi.Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ivi.com.eivi.R;
import ivi.com.eivi.Home;

public class AC4__SET_PROFILE extends AppCompatActivity {


    private DatabaseReference databaseReference;
    private FirebaseAuth mAhut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac4___set__profile);


        mAhut = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Gold").child("Blocked_users");


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {


            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void X_OK(View view) {


        databaseReference.child("public").setValue("ok").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Intent intent = new Intent(AC4__SET_PROFILE.this, Home.class);
                    startActivity(intent);
                    finish();

                }

            }
        });


    }

    public void X_NO(View view) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        databaseReference.child("by_uid").child(mAhut.getUid()).setValue(mAhut.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    LoginManager.getInstance().logOut();
                                    Intent loginIntent  = new Intent(AC4__SET_PROFILE.this,Login.class);
                                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(loginIntent);


                                }

                            }
                        });
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button



                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.public_no).setPositiveButton(R.string.public_yes, dialogClickListener)
                .setNegativeButton(R.string.public_ok, dialogClickListener).show();


    }
    public void Con(View view) {

        ShareCompat.IntentBuilder.from(AC4__SET_PROFILE.this)
                .setType("message/rfc822")
                .addEmailTo("Eivi.de@Hotmail.com")
                .setSubject("اكتب رسالتك وارسلها للدعم الفني")
                .setText("الدعم الفني ليس له اي علاقة بالمنشورات التي تتم النشر او حتى بالحظر , نقوم فقط بالرد على  الرسائل التي تتعلق بالامان والمشاكل المخصصة للتطبيق برمجيا و من ناحية التصميم , اذا كانت لديك مشاكل مع احد المشرفين يرجة كتابته , سوف نقوم بحل هذه الامور ايضا")
                .setChooserTitle("فريق الدعم الفني")
                .startChooser();


    }

    public void Coo(View view) {


        finish();

    }
}
