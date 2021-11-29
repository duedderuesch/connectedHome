package com.duedderuesch.placed.ui.login;

import androidx.annotation.NonNull;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.duedderuesch.placed.MainActivity;
import com.duedderuesch.placed.R;
import com.duedderuesch.placed.utils.startup;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LoginActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 1;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 2;
    private GoogleSignInClient mGoogleSignInClient;
    private LoginViewModel lvmodel;
    private ProgressBar pb_login;
    private SignInButton signInButton;
    private TextView tv_hello;
    public static TextView tv_loading;
    //private FirebaseAnalytics mFirebaseAnalytics;

    public static String local_db_status;
    public static String firebase_db_status;
    public static Date today;



    public static class dbStatus{
        /**
         * not reachable
         */
        public static String NOTREACHABLE = "not reachable";
        public static String ONLINE = "online";
        /**
         * na = no answer
         */
        public static String na = "unknown";
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        local_db_status = dbStatus.na;
        firebase_db_status = dbStatus.na;

        lvmodel = new ViewModelProvider(this).get(LoginViewModel.class);
        pb_login = findViewById(R.id.pb_login_loading);
        tv_hello = findViewById(R.id.tv_login_hello);
        tv_loading = findViewById(R.id.tv_login_loadingstate);

        pb_login.setVisibility(View.VISIBLE);
        tv_hello.setVisibility(View.GONE);
        tv_loading.setVisibility(View.GONE);

        basicNotificationChannel();

        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestId()
                .requestProfile()
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton = findViewById(R.id.bt_google_sign_in_button);
        signInButton.setVisibility(View.GONE);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.bt_google_sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        Calendar mCal = Calendar.getInstance();
        today = mCal.getTime();

        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account.getIdToken());
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        } else if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            //
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void basicNotificationChannel(){
        createNotificationChannel(getString(R.string.noti_channel_name_plant),
                getString(R.string.noti_channel_desc_plant),
                getString(R.string.noti_channel_id_plant));
        createNotificationChannel(getString(R.string.noti_channel_name_pollen),
                getString(R.string.noti_channel_desc_pollen),
                getString(R.string.noti_channel_id_pollen));

    }

    public static void registerCloudMessaging(Context ctx){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean getCMs = prefs.getBoolean("pref_sw_cm", true);
        if(getCMs) {
            if(prefs.getBoolean("pref_sw_cm_plant", true)) {
                FirebaseMessaging.getInstance().subscribeToTopic("mainz_plant")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    Log.e(TAG, "CM: Register unsuccessful");
                                } else {
                                    Log.d(TAG, "CM: Register successful");
                                }

                            }
                        });
            }
        }

    }

    private void createNotificationChannel(String channel_name, String channel_description, String channel_id) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id, channel_name, importance);
            channel.setDescription(channel_description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void updateUI(FirebaseUser user){
        if(user!=null){
//            NotificationItem testNoti = new NotificationItem(this.getString(R.string.noti_channel_id_pollen),
//                    "Pollen","logge heute deine Erfahrung",
//                    R.drawable.ic_pollen,
//                    NotificationCompat.PRIORITY_DEFAULT);
//            final int random = new Random().nextInt(100000000) + 100000000;
//            NotificationHandler.showNotification(NotificationHandler.getNotification(this, testNoti),random,this);

            //TODO: Event Logging
//            Bundle bundle = new Bundle();
//            bundle.putString(FirebaseAnalytics.Param.METHOD, "GoogleSignIn");
            //mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

            registerCloudMessaging(getApplicationContext());

            tv_loading.setVisibility(View.VISIBLE);
            startup.getStartup(getApplicationContext()); //data handling


        } else{
//            Bundle bundle = new Bundle();
//            bundle.putString(FirebaseAnalytics.Param.CONTENT, "login");
//            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            pb_login.setVisibility(View.GONE);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tv_hello.setAlpha(0f);
                    tv_hello.setVisibility(View.VISIBLE);
                    tv_hello.animate()
                            .alpha(1f)
                            .setDuration(1000)
                            .setListener(null);

                    signInButton.setAlpha(0f);
                    signInButton.setVisibility(View.VISIBLE);
                    signInButton.animate()
                            .alpha(1f)
                            .setDuration(1000)
                            .setListener(null);
                }
            }, 2000);
        }
    }

    public static void setLoadingInfo(String text){
        //LoginActivity.tv_loading.setText(text);
    }

    public static void finishLogin(Context ctx){
        Intent i = new Intent(ctx.getApplicationContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
    }
}