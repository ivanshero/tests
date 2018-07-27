package ivi.com.eivi.Login;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import ivi.com.eivi.R;

public class AC3__SET_PROFILE extends AppCompatActivity {

    private ImageButton UserImagee;
    private static final int GALLERY_REQ = 5;
    private Uri mImageUri = null ;
    private DatabaseReference mDatabaseRefrenc;
    private StorageReference mStoreg;
    private FirebaseAuth mAuth;
    private Button bbprofielee , sett;
    private TextView oo , xx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac3___set__profile);



        UserImagee = (ImageButton)findViewById( R.id.addProfileBtnf );
        bbprofielee = (Button)findViewById( R.id.button11 );
        oo = (TextView) findViewById(R.id.textView17);
        xx = (TextView) findViewById(R.id.textView13);


        mDatabaseRefrenc = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mStoreg = FirebaseStorage.getInstance().getReference().child("profile_image");


    }


    public void setImagePBNoe (View view) {
        Intent gallaryIntent = new Intent();
        gallaryIntent.setAction( Intent.ACTION_GET_CONTENT );
        gallaryIntent.setType( "image/*" );
        startActivityForResult( gallaryIntent, GALLERY_REQ );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == GALLERY_REQ && resultCode == RESULT_OK){

            Uri imageUri = data.getData();
            xx.setVisibility(View.INVISIBLE);
            CropImage.activity( imageUri )
                    .setGuidelines( CropImageView.Guidelines.ON )
                    .setAspectRatio( 1,1 )
                    .start( this );

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if ( resultCode == RESULT_OK) {

                mImageUri = result.getUri();
                UserImagee.setImageURI(mImageUri);
            }

            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){

                Exception   error = result.getError();
            }
        }
    }




    ///////////////////////////////////////
    public void sdsdsdsdsaaaa (View view){


        final  String user_id = mAuth.getCurrentUser().getUid();

        if (mImageUri !=null){

            Toast.makeText(AC3__SET_PROFILE.this,"عذرا , لم تقم باضافة اي صورة ؟", Toast.LENGTH_LONG).setGravity( Gravity.TOP|Gravity.CENTER,0,0 );

            StorageReference filepath = mStoreg.child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    //noinspection deprecation
                    String downloadurl = taskSnapshot.getDownloadUrl().toString();
                    mDatabaseRefrenc.child( user_id ).child("userprofile").setValue(downloadurl).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Intent mm = new Intent( AC3__SET_PROFILE.this, AC4__SET_PROFILE.class );
                            startActivity( mm );

                        }
                    });
                }
            } );
        }
        else if (mImageUri == null){

            String l = "انقر على الصورة لتحديد صورة" ;
            oo.setText(l);

        }

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {


            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    public  void no_ok (View view){

        Intent mm = new Intent( AC3__SET_PROFILE.this, AC4__SET_PROFILE.class );
        startActivity( mm );


    }

    public void Con(View view) {

        ShareCompat.IntentBuilder.from(AC3__SET_PROFILE.this)
                .setType("message/rfc822")
                .addEmailTo("Eivi.de@Hotmail.com")
                .setSubject("اكتب رسالتك وارسلها للدعم الفني")
                .setText("الدعم الفني ليس له اي علاقة بالمنشورات التي تتم النشر او حتى بالحظر , نقوم فقط بالرد على  الرسائل التي تتعلق بالامان والمشاكل المخصصة للتطبيق برمجيا و من ناحية التصميم , اذا كانت لديك مشاكل مع احد المشرفين يرجة كتابته , سوف نقوم بحل هذه الامور ايضا")
                .setChooserTitle("فريق الدعم الفني")
                .startChooser();


    }

}

