package com.example.tastelog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import android.os.Bundle;
import android.util.Log;

import com.example.tastelog.databinding.ActivityNaviBinding;

public class NaviActivity extends AppCompatActivity {

    private static final String TAG = "TasteLog[NaviActivity]";
    private static final String TAG_HOME = "HomeFragment";
    private static final String TAG_FRIEND = "FriendFragment";
    private static final String TAG_BOOKMARK = "BookmarkFragment";
    private static final String TAG_SETTING = "SettingFragment";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private ActivityNaviBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNaviBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initFirebaseAuthAndFireStore();

        setFragment(TAG_HOME, new HomeFragment());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Home Screen");

        binding.navigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                setFragment(TAG_HOME, new HomeFragment());
                getSupportActionBar().setTitle("Home Screen");
            } else if (itemId == R.id.friend) {
                setFragment(TAG_FRIEND, new FriendFragment());
                getSupportActionBar().setTitle("Friend Screen");
            } else if (itemId == R.id.bookmark) {
                setFragment(TAG_BOOKMARK, new BookmarkFragment());
                getSupportActionBar().setTitle("Bookmark Screen");
            } else if (itemId == R.id.setting) {
                setFragment(TAG_SETTING, new SettingFragment());
                getSupportActionBar().setTitle("Setting Screen");
            }

            return true;
        });


    }
    private void initFirebaseAuthAndFireStore() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    protected void setFragment(String tag, Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(fragmentManager.findFragmentByTag(tag) == null){
            fragmentTransaction.add(R.id.mainFrameLayout, fragment, tag);
        }

        Fragment home = fragmentManager.findFragmentByTag(TAG_HOME);
        Fragment friend = fragmentManager.findFragmentByTag(TAG_FRIEND);
        Fragment bookmark = fragmentManager.findFragmentByTag(TAG_BOOKMARK);
        Fragment setting = fragmentManager.findFragmentByTag(TAG_SETTING);

        if(home != null){
            fragmentTransaction.hide(home);
        }
        if(friend != null){
            fragmentTransaction.hide(friend);
        }
        if(bookmark != null){
            fragmentTransaction.hide(bookmark);
        }
        if(setting != null){
            fragmentTransaction.hide(setting);
        }


        if(tag.equals(TAG_HOME)){
            if(home != null) {
                fragmentTransaction.show(home);
            }
        }else if(tag.equals(TAG_FRIEND)){
            if(friend != null){
                fragmentTransaction.show(friend);
            }
        }else if(tag.equals(TAG_BOOKMARK)){
            if(bookmark != null){
                fragmentTransaction.show(bookmark);
            }
        }else if(tag.equals(TAG_SETTING)){
            if(setting != null){
                fragmentTransaction.show(setting);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void getUserName(FirebaseUser user, final OnUserNameFetchedListener listener) {
        String uid = user.getUid().toString();
        CollectionReference userCollection = db.collection("user");

        userCollection.document(uid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot userDocument = task.getResult();
                            if (userDocument.exists()) {
                                String userName = userDocument.getString("name");
                                Log.d(TAG, "User Name: " + userName);
                                listener.onUserNameFetched(userName);
                            } else {
                                Log.d(TAG, "User document not found.");
                                listener.onUserNameFetched(null);
                            }
                        } else {
                            Log.w(TAG, "Error getting user document.", task.getException());
                            listener.onUserNameFetched(null);
                        }
                    }
                });
    }

    public interface OnUserNameFetchedListener {
        void onUserNameFetched(String userName);
    }
}