package com.example.tastelog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tastelog.databinding.FragmentFriendBinding;
import com.example.tastelog.databinding.FriendlistBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String TAG = "TasteLog[FriendFragment]";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FriendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendFragment newInstance(String param1, String param2) {
        FriendFragment fragment = new FriendFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentFriendBinding binding = FragmentFriendBinding.inflate(getLayoutInflater());

        initFirebaseAuthAndFireStore();




        binding.friendRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.friendRecyclerview.setAdapter(new FriendAdapter(getData()));

        return binding.getRoot();
    }

    private void initFirebaseAuthAndFireStore() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private class FriendViewHolder extends RecyclerView.ViewHolder{
        private FriendlistBinding binding;
        private FriendViewHolder(FriendlistBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
        private void bind(String text){ binding.friendNameView.setText(text); }
    }

    private class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder>{
        private List<String> list;
        private FriendAdapter(List<String> list) {this.list = list;}


        @NonNull
        @Override
        public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            FriendlistBinding binding = FriendlistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new FriendViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
            String text = list.get(position);
            holder.bind(text);
        }

        @Override
        public int getItemCount() { return list.size();}
    }

    private List<String> getData() {
        CollectionReference friendsCollection = db.collection("friend");
        CollectionReference UserCollection = db.collection("user");

        String currentUserUid = mAuth.getCurrentUser().getUid();

        Query query = friendsCollection.whereEqualTo("uid1", currentUserUid);

        List<String> friendList = new ArrayList<>();
        List<String> friendName = new ArrayList<>();
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String uid2 = document.getString("uid2");
                                friendList.add(uid2);
                                Log.d(TAG, "Success add : " + uid2);
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
                                                    }
                                                } else {
                                                    Log.w(TAG, "Error getting user document.", task.getException());
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return friendName;
    }


}