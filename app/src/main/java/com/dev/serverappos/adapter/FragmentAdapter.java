package com.dev.serverappos.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.dev.serverappos.fragment.Fragment_Cart;
import com.dev.serverappos.fragment.Fragment_Home;
import com.dev.serverappos.fragment.Fragment_Menu;
import com.dev.serverappos.fragment.Fragment_Notification;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new Fragment_Cart();
            case 0:

                return new Fragment_Home();

            case 2:

                return new Fragment_Notification();

            case 3:
                return new Fragment_Menu();

            default:
                return new Fragment_Cart();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }


}
