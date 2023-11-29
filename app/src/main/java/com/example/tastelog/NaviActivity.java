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
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import android.os.Bundle;
import android.util.Log;

import com.example.tastelog.databinding.ActivityNaviBinding;

import java.util.ArrayList;
import java.util.List;

public class NaviActivity extends AppCompatActivity {

    private static final String TAG = "TasteLog[NaviActivity]";
    private static final String TAG_HOME = "HomeFragment";
    private static final String TAG_FRIEND = "FriendFragment";
    private static final String TAG_BOOKMARK = "BookmarkFragment";
    private static final String TAG_SETTING = "SettingFragment";
    private static final String TAG_REQUEST = "FriendRequestFragment";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userName;
    private List<String> friendList;

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

    @Override
    protected void onStart() {
        super.onStart();
        getUserName(mAuth.getCurrentUser(), new NaviActivity.OnUserNameFetchedListener() {
            @Override
            public void onUserNameFetched(String name) {
                if (name != null) {
                    userName = name;
                    Log.d(TAG, "User name found : " + userName);
                } else {
                    Log.d(TAG, "User name not found");
                }
            }
        });
        getFriendList(mAuth.getCurrentUser(), new NaviActivity.OnFriendListFetchedListener() {
            @Override
            public void onFriendListFetched(List<String> list) {
                if(list != null){
                    friendList = list;
                    Log.d(TAG, "Friend : ");
                }else {
                    Log.d(TAG, "Friend");
                }
            }
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
            Bundle args = new Bundle();
            args.putString("userName", userName);
            if (friendList != null) {
                args.putStringArrayList("friendList", new ArrayList<>(friendList));
            }
            fragment.setArguments(args);
            fragmentTransaction.add(R.id.mainFrameLayout, fragment, tag);
        }

        Fragment home = fragmentManager.findFragmentByTag(TAG_HOME);
        Fragment friend = fragmentManager.findFragmentByTag(TAG_FRIEND);
        Fragment bookmark = fragmentManager.findFragmentByTag(TAG_BOOKMARK);
        Fragment setting = fragmentManager.findFragmentByTag(TAG_SETTING);
        Fragment request = fragmentManager.findFragmentByTag(TAG_REQUEST);

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
        if(request != null){
            fragmentTransaction.hide(request);
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
        }else if(tag.equals(TAG_REQUEST)){
            if(request != null){
                fragmentTransaction.show(request);
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
    public void getFriendList(FirebaseUser user, final OnFriendListFetchedListener listener){
        String uid = user.getUid().toString();
        CollectionReference friendCollection = db.collection("friend");
        CollectionReference UserCollection = db.collection("user");

//        Query query = friendCollection.whereEqualTo("uid1", uid);
        List<String> friendList = new ArrayList<>();
        List<String> friendName = new ArrayList<>();
        Query query = friendCollection.where(Filter.or(
                Filter.equalTo("uid1", uid),
                Filter.equalTo("uid2", uid)
        ));
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String uid1 = document.getString("uid1");
                                String uid2 = document.getString("uid2");
                                if(uid.equals(uid1)){
                                    friendList.add(uid2);
                                    Log.d(TAG, "Success add : " + uid2);
                                }else if(uid.equals(uid2)){
                                    friendList.add(uid1);
                                    Log.d(TAG, "Success add : " + uid1);
                                }

                            }
                            for(String uid : friendList){
                                UserCollection.document(uid).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    DocumentSnapshot userDocument = task.getResult();
                                                    if (userDocument.exists()) {
                                                        String userName = userDocument.getString("name");
                                                        friendName.add(userName);
                                                        Log.d(TAG, "User Name: " + userName);
                                                    } else {
                                                        Log.d(TAG, "User document not found.");
                                                        listener.onFriendListFetched(null);
                                                    }
                                                } else {
                                                    Log.w(TAG, "Error getting user document.", task.getException());
                                                    listener.onFriendListFetched(null);
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                            listener.onFriendListFetched(null);
                        }
                    }
                });
        listener.onFriendListFetched(friendName);
    }

    public interface OnFriendListFetchedListener {
        void onFriendListFetched(List<String> friendList);
    }
}