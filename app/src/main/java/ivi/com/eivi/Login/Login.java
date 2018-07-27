package ivi.com.eivi.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import ivi.com.eivi.R;
import ivi.com.eivi.Home;

public class Login extends AppCompatActivity {

    private EditText emLogin;
    private EditText emPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRefrenc;
    private TextView rigNoe;

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private AccessToken token;
    private Button login;
    private TextView textView7 , textView19;
    private TextView textView8;
    private TextView textView9;
    private static final String TAG = "Login";
    //private AnimatedSvgView svgView;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private FirebaseAuth.AuthStateListener mAuthListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.new_login);
        Log.d(TAG, "onCreate: login.");


        mAuth = FirebaseAuth.getInstance();

        avLoadingIndicatorView =(AVLoadingIndicatorView)findViewById(R.id.LOG_Prog);
        avLoadingIndicatorView.setVisibility(View.INVISIBLE);


        emLogin = (EditText)findViewById(R.id.ll_mail_login);
        emPassword = (EditText)findViewById(R.id.pp_password_login);
        textView7 = (TextView)findViewById(R.id.textView7);
        textView8 = (TextView)findViewById(R.id.textView87);
        textView9 = (TextView)findViewById(R.id.textView1565);
        textView19 = (TextView)findViewById(R.id.textView19);


        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null) {

                    Intent loginIntent = new Intent(Login.this, Home.class);
                    startActivity(loginIntent);

                }
            }
        };

//

        // FaceBook ***********************************************
        callbackManager = CallbackManager.Factory.create();
        token = AccessToken.getCurrentAccessToken();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
               // nextActivity(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions( "email" , "public_profile");
        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                avLoadingIndicatorView.setVisibility(View.VISIBLE);
                handleFacebookAccessToken(loginResult.getAccessToken());

                //Profile profile = Profile.getCurrentProfile();
                //nextActivity(profile);

            }
            @Override
            public void onCancel() {

                avLoadingIndicatorView.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onError(FacebookException e) {
            }
        };
        loginButton.registerCallback(callbackManager, callback);

        // Email *********************************************
        rigNoe = (TextView) findViewById(R.id.goRigNow);
        rigNoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ff = new Intent(Login.this , Rig.class);
                startActivity(ff);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
     // Profile profile = Profile.getCurrentProfile();
      //nextActivity(profile);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }



    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        //Facebook login
        callbackManager.onActivityResult(requestCode, responseCode, intent);
        // twitter
    }

  //  private void nextActivity (Profile profile){
  //      if(profile != null){
//
  //          Intent main = new Intent(Login.this, Home.class);
  //         // main.putExtra("name", profile.getFirstName());
  //         // main.putExtra("surname", profile.getLastName());
  //         // main.putExtra("imageUrl", profile.getProfilePictureUri(200,200).toString());
  //          startActivity(main);
  //      }
  //  }

  //  public boolean onKeyDown(int keyCode, KeyEvent event) {
    //    if (keyCode == KeyEvent.KEYCODE_BACK) {
//
  //          finish();

    //        return false;
      //  }
        //return super.onKeyDown(keyCode, event);
    //}

   @Override
   public void onStart() {

       mAuth.addAuthStateListener(mAuthListner);
       super.onStart();


 }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(Login.this , Home.class);
                            startActivity(intent);
                            //checkUserExists();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                        }
                });
    }

    /// Start With Email


 public void email_sign_in_button (View view){


        String email     =   emLogin.getText().toString().trim();
        String  password =   emPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            avLoadingIndicatorView.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword( email,password ).addOnCompleteListener( new OnCompleteListener <AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    //svgView.start();
                    if (task.isSuccessful()){

                        checkUserExists();

                    }else{
                        textView7.setText(R.string.email_fauler);
                        textView8.setVisibility(View.INVISIBLE);
                        textView9.setVisibility(View.INVISIBLE);
                        avLoadingIndicatorView.setVisibility(View.INVISIBLE);
                    }
                }
            } );

        }
        if (TextUtils.isEmpty(email)){

            textView8.setText(R.string.email);


        }
        if (TextUtils.isEmpty(password)){
            textView9.setText(R.string.password);

        }

    }

    // Searche Users Info ^^ you can to searche With Finger
    public void checkUserExists() {

        final String  user_id = mAuth.getCurrentUser().getUid();

        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        mDatabaseRefrenc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(user_id)){
                    Intent loginInetntt = new Intent(Login.this,Home.class);
                    startActivity(loginInetntt);
                }else {

                    textView8.setText(R.string.email_exwst);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                textView8.setText(R.string.error_canedl);

            }
        });
    }


    public void Con (View view){

        ShareCompat.IntentBuilder.from(Login.this)
                .setType("message/rfc822")
                .addEmailTo("Eivi.de@Hotmail.com")
                .setSubject("اكتب رسالتك وارسلها للدعم الفني")
                .setText("الدعم الفني ليس له اي علاقة بالمنشورات التي تتم النشر او حتى بالحظر , نقوم فقط بالرد على  الرسائل التي تتعلق بالامان والمشاكل المخصصة للتطبيق برمجيا و من ناحية التصميم , اذا كانت لديك مشاكل مع احد المشرفين يرجة كتابته , سوف نقوم بحل هذه الامور ايضا")
                .setChooserTitle("فريق الدعم الفني")
                .startChooser();

    }




}


