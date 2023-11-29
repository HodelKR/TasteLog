package com.example.tastelog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tastelog.databinding.FragmentFriendRequestBinding;
import com.example.tastelog.databinding.FriendlistBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendRequestFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG_SETTING = "SettingFragment";
    private static final String TAG_REQUEST = "FriendRequestFragment";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userName;

    private FragmentFriendRequestBinding binding;
    private RequestAdapter adapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<String> friendList;

    public FriendRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendRequestFragment newInstance(String param1, String param2) {
        FriendRequestFragment fragment = new FriendRequestFragment();
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
        binding = FragmentFriendRequestBinding.inflate(getLayoutInflater());
        initFirebaseAuthAndFireStore();
        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });



        binding.requestRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new RequestAdapter(friendList);
        binding.requestRecyclerview.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.setData(friendList);
        adapter.notifyDataSetChanged();
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

    private class RequestAdapter extends RecyclerView.Adapter<FriendViewHolder>{
        private List<String> list;
        private RequestAdapter(List<String> list) { this.list = list;}


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

        public void setData(List<String> newData) {
            this.list = newData;
        }
    }


}