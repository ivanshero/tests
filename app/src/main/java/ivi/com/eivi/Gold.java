package ivi.com.eivi;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Gold extends AppCompatActivity implements RewardedVideoAdListener {


    private RewardedVideoAd mRewardedVideoAd;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private boolean isOk;
    private TextView textView , textView24 , textView21 , textView20;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544/5224354917");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(Gold.this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Gold");

        isOk= false;

        textView24 = (TextView)findViewById(R.id.textView24);
        button = (Button) findViewById(R.id.button9);

        textView21 = (TextView)findViewById(R.id.textView21);
        textView20 = (TextView)findViewById(R.id.textView20);

        button.setEnabled(false);
        button.setText("انتظر ليتم تحميل الاعلان");


        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {




                if (dataSnapshot.hasChild(mAuth.getUid())){

                    String Gold = dataSnapshot.child(mAuth.getUid()).getValue(true).toString();

                    int i = Integer.parseInt(dataSnapshot.child(mAuth.getUid()).getValue(true).toString());

                    int f = i/5;

                    String Alt = dataSnapshot.child("AltsGeld").child(mAuth.getUid()).getValue(true).toString();

                    textView21.setText("لديك "+ Gold +" من النقاط .");
                    textView20.setText("اي ما يعادل " + f + " مرات التعليق ");
                    textView24.setText("لقد حصلت حتى الان على " + Alt + " نقطة , قم بتجميع المزيد للحصول على 2000 نقطة مجانا ");

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }



    @Override
    public void onRewardedVideoAdLoaded() {

        button.setEnabled(true);
        button.setText("انقر لعرض الاعلان والربح منه");

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

        button.setText("انقر لربح المزيد");
        button.setEnabled(true);
    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {


    }

    @Override
    public void onRewardedVideoAdLeftApplication() {



    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

        Toast.makeText(Gold.this, "فشل في تحميل الاعلان , اعد التحميل مجددا", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRewardedVideoCompleted() {

        if (isOk){

            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    int Gold5 = Integer.parseInt(dataSnapshot.child(mAuth.getUid()).getValue(true).toString());

                    if (Gold5 >= 95555){

                        Toast.makeText(Gold.this, "  لديك العديد من النقاط , لايمكنك ربح المزيد  ", Toast.LENGTH_SHORT).show();

                    }else {

                        if (dataSnapshot.hasChild(mAuth.getUid())){

                            int Gold1 = Integer.parseInt(dataSnapshot.child(mAuth.getUid()).getValue(true).toString());
                            int Gold2 = 15;

                            int Alt1 = Integer.parseInt(dataSnapshot.child("AltsGeld").child(mAuth.getUid()).getValue(true).toString());
                            int Alt2 = 15;

                            mDatabaseReference.child("AltsGeld").child(mAuth.getUid()).setValue(Alt1 + Alt2);

                            if (Alt1 >= 985 && Alt1 <= 1005){

                                mDatabaseReference.child(mAuth.getUid()).setValue(Gold1 + 1000);
                                mDatabaseReference.child("AltsGeld").child(mAuth.getUid()).setValue(2000);

                            }else if (Alt1 != 985 ){

                                mDatabaseReference.child(mAuth.getUid()).setValue(Gold1 + Gold2);
                            }




                        }else {

                            mDatabaseReference.child("AltsGeld").child(mAuth.getUid()).setValue(15);
                            mDatabaseReference.child(mAuth.getUid()).setValue(15);

                        }



                    }





                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }else {


        }



    }


    @Override
    public void onResume() {
        mRewardedVideoAd.resume(Gold.this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(Gold.this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(Gold.this);
        super.onDestroy();
    }


    public void ADD_COINS (View view){

        isOk =true;
        mRewardedVideoAd.show();

    }





}
