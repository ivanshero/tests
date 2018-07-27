package ivi.com.eivi.Home_Pages;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import ivi.com.eivi.Login.Login;
import ivi.com.eivi.Posts.panda;
import ivi.com.eivi.R;


public class View_Home_Comment_Adapter extends AppCompatActivity {

    private String a = null;
    private String b = null;
    private RecyclerView mPandaList;
    private DatabaseReference mDatabase , oo;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseUser mCurrentUsers;
    private DatabaseReference mDatabaseUsers , User;
    private EditText UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.video_comment_view);

        a = getIntent().getExtras().getString( "a" );
        b = getIntent().getExtras().getString( "b" );

        mPandaList = (RecyclerView) findViewById(R.id.mPandaList);
        mPandaList.setHasFixedSize(true);
        mPandaList.setLayoutManager(new LinearLayoutManager(this));

        UserName  = (EditText)findViewById(R.id.addTextfd0941);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").child(a).child("posts").child(b).child("Comments");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUsers = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child( mAuth.getUid());
        User = FirebaseDatabase.getInstance().getReference().child("Users");


       mDatabaseUsers.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               String u = String.valueOf(dataSnapshot.child("username").getValue(true).toString());

               UserName.setHint( "  اهلا  "+ u + "  هل لديك تعليق على هذه الصورة ؟  ");
               UserName.setTextSize(13);

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

    }

    @Override
    protected void onStart() {

        super.onStart();
        FirebaseRecyclerAdapter <panda, PandaViewHolder> FBRA = new FirebaseRecyclerAdapter <panda, PandaViewHolder>(
                panda.class,
                R.layout.video_comment_tools,
                PandaViewHolder.class,
                mDatabase


        ) {
            @Override
            protected void populateViewHolder(final PandaViewHolder viewHolder, final panda model, final int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setUsername(model.getUsername());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked

                                        mDatabase.child(post_key).removeValue();

                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        User.child(model.getUid().toString()).child("Block").setValue("Block");


                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(View_Home_Comment_Adapter.this);
                        builder.setMessage("هل ترغب في حظر المستخدم , ام حزف تعليق").setPositiveButton("حزف التعليق", dialogClickListener).setNegativeButton("حظر ", dialogClickListener).show();


                    }
                });
            }
        };
        mPandaList.setAdapter( FBRA );
    }


    public static class PandaViewHolder extends RecyclerView.ViewHolder {


            View mView;

        public PandaViewHolder(View itemView) {

            super(itemView);
             mView = itemView;


        }

        // post text view
        public void setTitle(String title) {

            TextView post_title = (TextView) mView.findViewById(R.id.Comment_View_TextView052);
            post_title.setText(title);

        }

        // user post name view
        public void setUsername(String username){

            TextView usernamehomepost = (TextView) mView.findViewById(R.id.Comment_Name_View052);
            usernamehomepost.setText(username);
        }


    }

    public void CommentBTN0941 (View view){


        final  String titleaddtext = UserName.getText().toString().trim();

            if (!TextUtils.isEmpty(titleaddtext)){

                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                final DatabaseReference newPost = mDatabase.push();
                                String user = dataSnapshot.child("username").getValue(true).toString();

                                newPost.child("title"   ).setValue(titleaddtext);
                                newPost.child("uid"     ).setValue(mAuth.getUid());
                                newPost.child("username").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){

                                           // UserName.setText(null);

                                        }

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {}});

            }

    }

}



