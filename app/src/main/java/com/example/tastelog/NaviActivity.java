package com.example.tastelog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.tastelog.databinding.ActivityNaviBinding;

public class NaviActivity extends AppCompatActivity {

    private static final String TAG_HOME = "HomeFragment";
    private static final String TAG_FRIEND = "FriendFragment";
    private static final String TAG_BOOKMARK = "BookmarkFragment";
    private static final String TAG_SETTING = "SettingFragment";

    private ActivityNaviBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNaviBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setFragment(TAG_HOME, new HomeFragment());

        binding.navigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                setFragment(TAG_HOME, new HomeFragment());
            } else if (itemId == R.id.friend) {
                setFragment(TAG_FRIEND, new FriendFragment());
            } else if (itemId == R.id.bookmark) {
                setFragment(TAG_BOOKMARK, new BookmarkFragment());
            } else if (itemId == R.id.setting) {
                setFragment(TAG_SETTING, new SettingFragment());
            }

            return true;
        });
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
}