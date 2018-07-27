package ivi.com.eivi.Login;


import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import ivi.com.eivi.R;

public class AC5__SET_PROFILE extends AppCompatActivity {


    private DatabaseReference databaseReference;
    private FirebaseAuth mAhut;
    private String device_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac5___set__profile);

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {


            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


        public void Con(View view) {

            ShareCompat.IntentBuilder.from(AC5__SET_PROFILE.this)
                    .setType("message/rfc822")
                    .addEmailTo("Eivi.de@Hotmail.com")
                    .setSubject("اكتب رسالتك وارسلها للدعم الفني")
                    .setText("الدعم الفني ليس له اي علاقة بالمنشورات التي تتم النشر او حتى بالحظر , نقوم فقط بالرد على  الرسائل التي تتعلق بالامان والمشاكل المخصصة للتطبيق برمجيا و من ناحية التصميم , اذا كانت لديك مشاكل مع احد المشرفين يرجة كتابته , سوف نقوم بحل هذه الامور ايضا")
                    .setChooserTitle("فريق الدعم الفني")
                    .startChooser();


        }

    public void Coo (View view){

        finish();

    }

}
