package com.example.tastelog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tastelog.databinding.FragmentSettingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private static final String TAG = "TasteLog[SettingFragment]";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG_SETTING = "SettingFragment";
    private static final String TAG_REQUEST = "FriendRequestFragment";
    private FragmentSettingBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    // TODO: Rename and change types of parameters
    private String userName;
    private String mParam2;

    private List<String> friendList;


    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString("userName");
            friendList = getArguments().getStringArrayList("friendList");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_setting, container, false);
        binding = FragmentSettingBinding.inflate(getLayoutInflater());
        initFirebaseAuthAndFireStore();
        binding.userId.setText(userName);

        binding.changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.changePasswordView.setVisibility(View.VISIBLE);
            }
        });
        binding.closeChangePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.changePasswordView.setVisibility(View.INVISIBLE);
            }
        });
        binding.concatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.concatView.setVisibility(View.VISIBLE);
            }
        });
        binding.closeConcatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.concatView.setVisibility(View.INVISIBLE);
            }
        });

        binding.singOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        binding.addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFriendPopup();
            }
        });


        return binding.getRoot();
    }

    private void initFirebaseAuthAndFireStore() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private void signOut(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void showAddFriendPopup() {
        Dialog popupDialog = new Dialog(getActivity());


        popupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupDialog.setContentView(R.layout.popup_layout);

        Button closeButton = popupDialog.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog.dismiss();
            }
        });
        Button addButton = popupDialog.findViewById(R.id.add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText friendName = popupDialog.findViewById(R.id.friend_name);

                RequestFriend(mAuth.getCurrentUser(), friendName.getText().toString());

                popupDialog.dismiss();
            }
        });

        popupDialog.show();
    }
    private void RequestFriend(FirebaseUser user, String friendName){
        String uid = user.getUid().toString();

        CollectionReference friendCollection = db.collection("friend");
        CollectionReference userCollection = db.collection("user");

        Query query = userCollection.whereEqualTo("name", friendName);

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot snapshots = task.getResult();
                            if(!snapshots.isEmpty()){
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    String uid2 = document.getString("uid");
                                    Query query2 = friendCollection.where(Filter.or(
                                            Filter.and(Filter.equalTo("uid1", uid), Filter.equalTo("uid2", uid2)),
                                            Filter.and(Filter.equalTo("uid1", uid2), Filter.equalTo("uid2", uid))
                                    ));
                                    query2.get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        QuerySnapshot snapshots1 = task.getResult();
                                                        if(!snapshots1.isEmpty()){
                                                            Toast.makeText(getActivity(), "이미 친구입니다.", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else{
                                                            Map<String, Object> data = new HashMap<>();
                                                            data.put("name1", userName);
                                                            data.put("uid1", uid);
                                                            data.put("name2", friendName);
                                                            data.put("uid2", uid2);
                                                            data.put("timestamp", FieldValue.serverTimestamp());
                                                            friendCollection.document().set(data);
                                                            Toast.makeText(getActivity(), "친구가 되었습니다.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            });
                                }
                            }else{
                                Toast.makeText(getActivity(), "해당 이름이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}