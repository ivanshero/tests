package ivi.com.eivi.Next_View_Pageer;


import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import ivi.com.eivi.Admin.Admin_Add_Posts;
import ivi.com.eivi.Gold;
import ivi.com.eivi.Home;
import ivi.com.eivi.Home_Pages.View_Home_Comment_Adapter;
import ivi.com.eivi.Login.AC1___SET_PROFILE;
import ivi.com.eivi.Login.Login;
import ivi.com.eivi.Posts.panda;
import ivi.com.eivi.R;


public class Next_View extends AppCompatActivity {

    private RecyclerView mPandaList;
    private DatabaseReference MData_For_Home,mCoins, mDatabaseUsers;
    private Query MData_For_Limitet;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private static final int GALLERY_REQ = 1;
    private FirebaseUser mCurrentUsers;
    private ImageButton Story_image_add;
    private static final String TAG = "Home";
    private LinearLayoutManager layoutManager;
    private String post_key = null;
    private Button button14;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_view);
        Log.d(TAG, "onCreate: starting.");

        post_key = getIntent().getExtras().getString( "post");

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        layoutManager.findViewByPosition(4);
        layoutManager.isSmoothScrolling();
        mPandaList = (RecyclerView) findViewById(R.id.mPandaList);
        mPandaList.setLayoutManager(layoutManager);
        mPandaList.setHasFixedSize(true);
        mPandaList.stopNestedScroll(2);
        mPandaList.setNestedScrollingEnabled(false);

        button14 = (Button)findViewById(R.id.button14);
        button14.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        MData_For_Home = FirebaseDatabase.getInstance().getReference().child("Posts").child(post_key).child("posts");
        mCoins = FirebaseDatabase.getInstance().getReference();
        MData_For_Home.keepSynced(true);
        MData_For_Limitet = FirebaseDatabase.getInstance().getReference().child("Posts");
        MData_For_Limitet.keepSynced(true);
        mCurrentUsers = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");




        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child(mAuth.getUid()).hasChild("FerstName")) {

                    Intent loginIntent = new Intent(Next_View.this, AC1___SET_PROFILE.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }

                if (dataSnapshot.child(mAuth.getUid()).hasChild("Admin")){

                    button14.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {

                    LoginManager.getInstance().logOut();
                    Intent loginIntent = new Intent(Next_View.this, Login.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);

                }
            }
        };

    }



    @Override
    protected void onStart() {

        mAuth.addAuthStateListener(mAuthListner);
        super.onStart();

        final FirebaseRecyclerAdapter<panda, PandaViewHolder> FBRA = new FirebaseRecyclerAdapter<panda, PandaViewHolder>(
                panda.class,
                R.layout.view_toolls,
                PandaViewHolder.class,
                MData_For_Home



        ) {
            @Override
            protected void populateViewHolder(final PandaViewHolder viewHolder, final panda model, final int position) {

                final String post_key2 = getRef( position ).getKey().toString();

                viewHolder.setUsername(model.getUsername());
                viewHolder.setImage(getApplicationContext() , model.getImage());


                MData_For_Home.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(post_key2).hasChild("Comments")){

                            int Com = (int) dataSnapshot.child(post_key2).child("Comments").getChildrenCount();

                            viewHolder.Comment.setText(" التعليقات " + Com);


                        }




                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        MData_For_Home.child(post_key2).removeValue();

                        return false;
                    }
                });

                viewHolder.Comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mCoins.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {



                                if (dataSnapshot.child("Users").child(mAuth.getUid()).hasChild("Admin")){

                                    Intent intent = new Intent(Next_View.this , View_Home_Comment_Adapter.class);
                                    intent.putExtra("b" , post_key2);
                                    intent.putExtra("a" , post_key);
                                    startActivity(intent);

                                }else {






                                    if (dataSnapshot.child("Gold").hasChild(mAuth.getUid())){

                                        Integer gold1 = Integer.parseInt(dataSnapshot.child("Gold").child(mAuth.getUid()).getValue(true).toString());

                                        if (gold1 >= 5){

                                            mCoins.child("Gold").child(mAuth.getUid()).setValue(gold1 - 5);
                                            Intent intent = new Intent(Next_View.this , View_Home_Comment_Adapter.class);
                                            intent.putExtra("b" , post_key2);
                                            intent.putExtra("a" , post_key);
                                            startActivity(intent);


                                        }else if (gold1 <= 4){

                                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    switch (which){
                                                        case DialogInterface.BUTTON_POSITIVE:
                                                            //Yes button clicked

                                                            Intent intent = new Intent(Next_View.this , Gold.class);
                                                            startActivity(intent);


                                                            break;
                                                        case DialogInterface.BUTTON_NEGATIVE:
                                                            //No button clicked

                                                            break;
                                                    }
                                                }
                                            };
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Next_View.this);
                                            builder.setMessage("لا تمتلك نقاط كافية للتعليق , هل ترغب في ربح بعض النقاط ؟").setPositiveButton("نعم", dialogClickListener)
                                                    .setNegativeButton("لا", dialogClickListener).show();




                                        }







                                    }else {

                                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which){
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        //Yes button clicked

                                                        Intent intent = new Intent(Next_View.this , Gold.class);
                                                        startActivity(intent);


                                                        break;
                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                        //No button clicked

                                                        break;
                                                }
                                            }
                                        };
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Next_View.this);
                                        builder.setMessage("لا تمتلك نقاط كافية للتعليق , هل ترغب في ربح بعض النقاط ؟").setPositiveButton("نعم", dialogClickListener)
                                                .setNegativeButton("لا", dialogClickListener).show();




                                    }

                                }



                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });




            }
        };


        mPandaList.setAdapter(FBRA);



    }



    public static class PandaViewHolder extends RecyclerView.ViewHolder {


        View mView;

        ImageView post_image;
        ImageView post_image1;
        boolean isView = false;
        boolean isselect = false;
        boolean IsLiked = false;
        TextView post_title;
        DatabaseReference mDatabase;
        DatabaseReference mUsersBase;
        FirebaseUser mCurrentUsers;
        FirebaseAuth mAuth;
        DatabaseReference mDatabaase;
        DatabaseReference Admin;
        ImageButton LikeButton;

        Typeface tf;
        TextView pandauserid;
        Button Comment;
        TextView Comments;
        TextView Like;
        TextView button;


        public PandaViewHolder(View itemView) {


            super(itemView);
            mView = itemView;

            button = (TextView)mView.findViewById(R.id.textView5);

            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users_Notify");
            mUsersBase = FirebaseDatabase.getInstance().getReference().child("Users");
            Admin = FirebaseDatabase.getInstance().getReference().child("Gold");
            mCurrentUsers = FirebaseAuth.getInstance().getCurrentUser();
            mDatabaase = FirebaseDatabase.getInstance().getReference().child("Posts");

            Comment = (Button)mView.findViewById(R.id.button7);

            IsLiked = false;

        }


        // post text view
        public void setUsername(final String username) {

            button.setText(username);

        }
        public void setImage (Context context , String image){

            ImageView imageView = (ImageView)mView.findViewById(R.id.ImagView);
            Picasso.with(context).load(image).into(imageView);


        }





    }

    public void add_button (View view ){

        Intent intent = new Intent(Next_View.this , Admin_Add_Posts.class);
        intent.putExtra("post" , post_key);
        startActivity(intent);


    }



    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Next_View.this , Home.class);
        startActivity(intent);


    }



   public void sheri (View view){

       Intent sendIntent = new Intent();
       sendIntent.setAction(Intent.ACTION_SEND);
       sendIntent.putExtra(Intent.EXTRA_TEXT , "https://play.google.com/store/apps/details?id=ivi.com.eivi" + " \n \n \n"  +
               "مرحبا صديقي , اعتقد ان هذه المواضيع قد تهمك , قم بتحميله وتجربته الان" );
       sendIntent.setType("text/plain");
       startActivity(sendIntent);

   }


}