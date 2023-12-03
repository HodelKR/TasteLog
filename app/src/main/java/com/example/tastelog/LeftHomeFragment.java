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
import androidx.constraintlayout.widget.ConstraintLayout;
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

        CategoryOnClickListener onClickListener = new CategoryOnClickListener();

        ImageView book = view.findViewById(R.id.book);
        ImageView music = view.findViewById(R.id.music);
        ImageView tv = view.findViewById(R.id.tv);

        book.setOnClickListener(onClickListener);
        tv.setOnClickListener(onClickListener);
        music.setOnClickListener(onClickListener);
    }

    class CategoryOnClickListener implements View.OnClickListener {
        Intent intent;

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.book) {
                Log.d("tmp", "book clicked");
                intent = new Intent(getActivity(), FavoriteActivity.class);
                intent.putExtra("category", "book");
                startActivity(intent);
            }
            else if(view.getId() == R.id.music) {
                Log.d("tmp", "music clicked");
                intent = new Intent(getActivity(), FavoriteActivity.class);
                intent.putExtra("category", "music");
                startActivity(intent);
            }
            else if(view.getId() == R.id.tv) {
                Log.d("tmp", "tv clicked");
                intent = new Intent(getActivity(), FavoriteActivity.class);
                intent.putExtra("category", "tv");
                startActivity(intent);
            }
        }
    }
}