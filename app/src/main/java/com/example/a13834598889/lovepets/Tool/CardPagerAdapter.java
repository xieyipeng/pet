package com.example.a13834598889.lovepets.Tool;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import com.example.a13834598889.lovepets.Fragments_Mine.Fragment_card_mine;
import com.example.a13834598889.lovepets.Fragments_Mine.Fragment_card_star;


/**
 * Created by Wangtianrui on 2018/5/1.
 */

public class CardPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = {"我的帖子","我的收藏"};
    private Fragment[] fragments;

    public CardPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[TITLES.length];
        fragments[0] = Fragment_card_mine.newInstance();
        fragments[1] = Fragment_card_star.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[position] = Fragment_card_mine.newInstance();
                    break;
                case 1:
                    fragments[position] = Fragment_card_star.newInstance();
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
