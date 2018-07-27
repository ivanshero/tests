package ivi.com.eivi.Admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import ivi.com.eivi.R;

public class Admin_Add_Posts extends AppCompatActivity {


    private DatabaseReference firebaseDatabase;
    private FirebaseAuth mAthu ;
    private EditText editText;

    private String post_key1 = null;

    private static final int GALLERY_REQ = 1;
    private Uri mImageUri = null ;

    private ImageView imageView;
    private FirebaseDatabase database;
    private DatabaseReference mDatabaseRefrenc , Block;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_posts);

        post_key1 = getIntent().getExtras().getString("post").toString();

        mAthu =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");

        editText = (EditText)findViewById(R.id.editText2);
        imageView = (ImageView)findViewById(R.id.imageView2);

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseRefrenc = database.getInstance().getReference().child("Posts");

    }


    public void ADD_IMAGE_ON_CLIKE (View view){
        Intent gallaryIntent = new Intent();
        gallaryIntent.setAction( Intent.ACTION_GET_CONTENT );
        gallaryIntent.setType("image/*");
        startActivityForResult( gallaryIntent, GALLERY_REQ );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == GALLERY_REQ && resultCode == RESULT_OK){


            Uri imageUri = data.getData();
            CropImage.activity( imageUri )
                    .setGuidelines( CropImageView.Guidelines.ON )
                    .setActivityTitle("Ivan")
                    .setAutoZoomEnabled(true)
                    .setAspectRatio(5,5)
                    .start( this );

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if ( resultCode == RESULT_OK) {

                mImageUri = result.getUri();
                imageView.setImageURI(mImageUri);

            }

            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){

                Exception   error = result.getError();
            }
        }
    }








    public void add_Post (View view){

        final String Buttoon = editText.getText().toString().trim();


        if (mImageUri != null){


            StorageReference filepath = storageReference.child("post_image").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Uri downloadurl = taskSnapshot.getDownloadUrl();

                    Toast.makeText(Admin_Add_Posts.this, R.string.download_is_success, Toast.LENGTH_LONG).show();


                    firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {


                        final DatabaseReference newPost = firebaseDatabase.child(post_key1).child("posts").push();

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            newPost.child("image").setValue(downloadurl.toString());
                            newPost.child("username").setValue(Buttoon);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });


        }else if (mImageUri == null){

            firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {


                final DatabaseReference newPost = firebaseDatabase.child(post_key1).child("posts").push();

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    newPost.child("username").setValue(Buttoon);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


                }



                public void cancel1 (View view){


                            Admin_Add_Posts.this.finish();



                }




    }




















