package ivi.com.eivi;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ivi.com.eivi.Admin.Admin;
import ivi.com.eivi.Login.AC1___SET_PROFILE;
import ivi.com.eivi.Login.AC5__SET_PROFILE;
import ivi.com.eivi.Login.Login;
import ivi.com.eivi.Next_View_Pageer.Next_View;
import ivi.com.eivi.Posts.panda;


public class Home extends AppCompatActivity {

    private RecyclerView mPandaList;
    private DatabaseReference MData_For_Home, mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseUser mCurrentUsers;
    private static final String TAG = "Home";
    private LinearLayoutManager layoutManager;
    private TextView textView , textView6;
    private Button button13;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Log.d(TAG, "onCreate: starting.");


        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        layoutManager.isSmoothScrolling();
        mPandaList = (RecyclerView) findViewById(R.id.mPandaList);
        mPandaList.setLayoutManager(layoutManager);
        mPandaList.setHasFixedSize(true);


        mAuth = FirebaseAuth.getInstance();


        MData_For_Home = FirebaseDatabase.getInstance().getReference().child("Posts");


        mCurrentUsers = mAuth.getCurrentUser();


        mDatabaseUsers = FirebaseDatabase.getInstance().getReference();


        textView = (TextView)findViewById(R.id.textView);
        textView6 = (TextView)findViewById(R.id.textView6);
        button13 = (Button)findViewById(R.id.button3);






        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("Users").child(mAuth.getUid()).hasChild("username")) {

                    Intent loginIntent = new Intent(Home.this, AC1___SET_PROFILE.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);

                }else {

                    String name = dataSnapshot.child("Users").child(mAuth.getUid()).child("username").getValue(true).toString();
                    textView.setText("اهلا بك : !" + name);
                }

                if (dataSnapshot.child("Users").child(mAuth.getUid()).hasChild("Block")){

                    Intent loginIntent = new Intent(Home.this, AC5__SET_PROFILE.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);

                }
                if (!dataSnapshot.child("Users").child(mAuth.getUid()).hasChild("Block")){}


                if (dataSnapshot.child("Gold").hasChild(mAuth.getUid())){

                    String Gold = dataSnapshot.child("Gold").child(mAuth.getUid()).getValue(true).toString();


                    textView6.setText(" لديك " + Gold + " نقطة حاليا");
                }


                if (dataSnapshot.child("Users").child(mAuth.getUid()).hasChild("Admin")){

                    button13.setText("اضافة ازرار");


                }
                if (!dataSnapshot.child("Users").child(mAuth.getUid()).hasChild("Admin")){


                    button13.setText("تواصل مع الدعم");

                }



                button13.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (dataSnapshot.child("Users").child(mAuth.getUid()).hasChild("Admin")){

                            Intent intent = new Intent(Home.this , Admin.class);
                            startActivity(intent);

                        }

                        if (!dataSnapshot.child("Users").child(mAuth.getUid()).hasChild("Admin")){


                            ShareCompat.IntentBuilder.from(Home.this)
                                    .setType("message/rfc822")
                                    .addEmailTo("Eivi.de@Hotmail.com")
                                    .setSubject("اكتب رسالتك وارسلها للدعم الفني")
                                    .setText("الدعم الفني ليس له اي علاقة بالمنشورات التي تتم النشر او حتى بالحظر , نقوم فقط بالرد على  الرسائل التي تتعلق بالامان والمشاكل المخصصة للتطبيق برمجيا و من ناحية التصميم , اذا كانت لديك مشاكل مع احد المشرفين يرجة كتابته , سوف نقوم بحل هذه الامور ايضا")
                                    .setChooserTitle("فريق الدعم الفني")
                                    .startChooser();


                        }



                    }
                });



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

                    FirebaseAuth.getInstance().signOut();
                    Intent loginIntent = new Intent(Home.this, Login.class);
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
                R.layout.home_post_adapter,
                PandaViewHolder.class,
                MData_For_Home

        ) {
            @Override
            protected void populateViewHolder(final PandaViewHolder viewHolder, final panda model, final int position) {

                final String post_key = getRef( position ).getKey().toString();

                viewHolder.setUsername(model.getUsername());

                viewHolder.button.setText(post_key);

                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        MData_For_Home.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String ff = dataSnapshot.child(post_key).child("username").getValue(true).toString();

                                Intent intent = new Intent(Home.this , Next_View.class);
                                intent.putExtra("post" , post_key );
                                startActivity(intent);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }
                });

                viewHolder.button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        MData_For_Home.child(post_key).removeValue();

                        return false;
                    }
                });


            }
        };


        mPandaList.setAdapter(FBRA);



    }





    public static class PandaViewHolder extends RecyclerView.ViewHolder {


        View mView;

        DatabaseReference mDatabase;
        DatabaseReference mUsersBase;
        FirebaseUser mCurrentUsers;
        FirebaseAuth mAuth;
        DatabaseReference mDatabaase;
        DatabaseReference Admin;
        Button button;

        public PandaViewHolder(View itemView) {


            super(itemView);
            mView = itemView;

            button = (Button)mView.findViewById(R.id.button2);

            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users_Notify");
            mUsersBase = FirebaseDatabase.getInstance().getReference().child("Users");
            Admin = FirebaseDatabase.getInstance().getReference().child("Gold");
            mCurrentUsers = FirebaseAuth.getInstance().getCurrentUser();
            mDatabaase = FirebaseDatabase.getInstance().getReference().child("Posts");

        }


        // post text view
        public void setUsername(final String username) {



            button.setText(username);


        }



    }


    public void AddCoins (View view){

        Intent intent = new Intent(Home.this , Gold.class);
        startActivity(intent);


    }

    @Override
    public void onBackPressed() {

        Home.this.finish();

    }


}