package com.example.a13834598889.lovepets.Tool;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.a13834598889.lovepets.Fragments_Share.Fragment_share_1;
import com.example.a13834598889.lovepets.Fragments_Share.Fragment_share_2;

/**
 * Created by 13834598889 on 2018/5/8.
 */

public class Fragment_card_Adapter extends FragmentPagerAdapter{

    private final String[] TITLES = {"生活分享","专家指导"};
    private Fragment[] fragments;

    public Fragment_card_Adapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[TITLES.length];
        fragments[0] = Fragment_share_1.newInstance();
        fragments[1] = Fragment_share_2.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[position] = Fragment_share_1.newInstance();
                    break;
                case 1:
                    fragments[position] = Fragment_share_2.newInstance();
                    break;
                default:
                    break;
            }
        }
        return fragments[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

}
