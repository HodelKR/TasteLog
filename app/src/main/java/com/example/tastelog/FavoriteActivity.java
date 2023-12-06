package com.example.tastelog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.tastelog.databinding.ActivityFavoriteBinding;
import com.example.tastelog.databinding.RecyclerviewItemBinding;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        Toolbar toolbar = findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String title = intent.getExtras().getString("category");

        getSupportActionBar().setTitle(title + " 추가하기");

        Button addFavoriteBtn = findViewById(R.id.add_favorite_btn);

        addFavoriteBtn.setOnClickListener(view -> {
            Intent intent1 = new Intent(getApplicationContext(), AddActivity.class);
            Log.d("tmp", "add clicked");
            intent1.putExtra("category", title);
            startActivity(intent1);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String title = intent.getExtras().getString("category");

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select category, comment from " + "tb_favorite"
                + " order by _id desc", null);
        int cnt = cursor.getCount();

        List<String> list = new ArrayList<>();

        for (int i = 0; i < cnt; i++) {
            cursor.moveToNext();
            String category = cursor.getString(0);
            String comment = cursor.getString(1);
            if(title.equals(category)) list.add("카테고리 : " + category + ", 설명 : " + comment);
        }

        RecyclerView recyclerView = findViewById(R.id.favorite_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ItemAdapter(list));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private RecyclerviewItemBinding binding;

        private MyViewHolder(RecyclerviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<String> list;

        private ItemAdapter(List<String> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerviewItemBinding binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MyViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String text = list.get(position);
            holder.binding.textView.setText(text);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}