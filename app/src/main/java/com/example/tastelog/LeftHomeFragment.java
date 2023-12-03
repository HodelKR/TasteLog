package com.example.tastelog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tastelog.databinding.FragmentLefthomeBinding;


public class LeftHomeFragment extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("tmp", "leftHome create");
        return inflater.inflate(R.layout.fragment_lefthome, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView book = view.findViewById(R.id.book);
        ImageView music = view.findViewById(R.id.music);
        ImageView tv = view.findViewById(R.id.tv);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.d("tmp", "book clicked");
                Intent intent = new Intent(getActivity(), FavoriteActivity.class);
                intent.putExtra("category", "book");
                startActivity(intent);
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.d("tmp", "tv clicked");
                Intent intent = new Intent(getActivity(), FavoriteActivity.class);
                intent.putExtra("category", "tv");
                startActivity(intent);
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.d("tmp", "music clicked");
                Intent intent = new Intent(getActivity(), FavoriteActivity.class);
                intent.putExtra("category", "music");
                startActivity(intent);
            }
        });
    }
}