package com.example.a13834598889.lovepets.Fragments_Mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.R;
import com.example.a13834598889.lovepets.Tool.CardPagerAdapter;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by 13834598889 on 2018/5/3.
 */

public class Fragment_card extends Fragment implements View.OnClickListener{

    private CircleImageView circleImageView;
    private ImageView imageView_back;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager ;
    private View view;
    private String picture_path;



    public static Fragment_card newInstance(String path){
        Fragment_card fragment_card = new Fragment_card();
        fragment_card.picture_path = path;
        return fragment_card;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_card,container,false);
        initViewPager();

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_card_back:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if(fragmentManager.findFragmentByTag("card_fragment") !=null){
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("card_fragment"))
                            .remove(fragmentManager.findFragmentByTag("card_fragment"))
                            .show(fragmentManager.findFragmentByTag("fragment_account"))
                            .commit();
                }
                break;
            default:
                break;
        }
    }



    public void initViewPager(){
        slidingTabLayout=(SlidingTabLayout)view.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setTextSelectColor(R.color.title_text);
        slidingTabLayout.setTextUnselectColor(R.color.colorAccent);
        slidingTabLayout.setUnderlineColor(R.color.colorAccent);
        slidingTabLayout.setDividerColor(R.color.colorAccent);
        slidingTabLayout.setIndicatorColor(R.color.colorAccent);
        circleImageView = (CircleImageView)view.findViewById(R.id.circleImageView_mine99);
        viewPager = (ViewPager)view.findViewById(R.id.viewPager_container3);

        imageView_back=(ImageView)view.findViewById(R.id.fragment_card_back);
        imageView_back.setOnClickListener(this);


        if(picture_path.equals("")){
            Glide.with(getActivity().getApplication()).load(R.drawable.test_touxiang3).into(circleImageView);
        }else{
            Glide.with(getActivity().getApplication()).load(picture_path).into(circleImageView);
        }

        CardPagerAdapter adapter1 = new CardPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter1);
        slidingTabLayout.setViewPager(viewPager);
    }
}
