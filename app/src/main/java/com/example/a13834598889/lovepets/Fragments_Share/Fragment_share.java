package com.example.a13834598889.lovepets.Fragments_Share;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.JavaBean.Card;
import com.example.a13834598889.lovepets.JavaBean.User;
import com.example.a13834598889.lovepets.MainActivity;
import com.example.a13834598889.lovepets.R;
import com.example.a13834598889.lovepets.Tool.CardPagerAdapter;
import com.example.a13834598889.lovepets.Tool.Fragment_card_Adapter;
import com.flyco.tablayout.SlidingTabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 13834598889 on 2018/5/7.
 */

public class Fragment_share extends Fragment{
    private View view;
    private CircleImageView circleImageView;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager ;
    private User administrator;

    public String picture_path;


    public static Fragment_share newInstance(String image_path){
        Fragment_share fragment_share = new Fragment_share();
        fragment_share.picture_path = image_path;
        return fragment_share;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        administrator = BmobUser.getCurrentUser(User.class);
        view = inflater.inflate(R.layout.fragment_share,container,false);
        preView();
        return view;
    }





    public void preView(){
        slidingTabLayout=(SlidingTabLayout)view.findViewById(R.id.sliding_tabs_2);
        slidingTabLayout.setTextSelectColor(R.color.title_text);
        slidingTabLayout.setTextUnselectColor(R.color.colorAccent);
        slidingTabLayout.setUnderlineColor(R.color.colorAccent);
        slidingTabLayout.setDividerColor(R.color.colorAccent);
        slidingTabLayout.setIndicatorColor(R.color.colorAccent);
        circleImageView = (CircleImageView)view.findViewById(R.id.circleImageView_mine11);
        if(picture_path.equals("")){
            Glide.with(getActivity().getApplication()).load(R.drawable.tubiao_start).into(circleImageView);
        }else{
            Glide.with(getActivity().getApplication()).load(picture_path).into(circleImageView);
        }
        viewPager = (ViewPager)view.findViewById(R.id.viewPager_container5);

        Fragment_card_Adapter adapter1 = new Fragment_card_Adapter(getChildFragmentManager());
        viewPager.setAdapter(adapter1);
        slidingTabLayout.setViewPager(viewPager);
    }


}
