package com.example.a13834598889.lovepets.Fragments_Share;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.JavaBean.Card;
import com.example.a13834598889.lovepets.JavaBean.User;
import com.example.a13834598889.lovepets.R;

import org.w3c.dom.Text;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 13834598889 on 2018/5/10.
 */

public class Fragment_see_card extends Fragment implements View.OnClickListener{

    private String path_account;
    private String path_back;
    private Card card;
    private FloatingActionButton floatingActionButton_star;
    private TextView textView_title;
    private TextView textView_context;
    private TextView textView_time;
    private ImageView imageView_background;
    private ImageView imageView_back;
    private NestedScrollView nestedScrollView_see_card;
    private CircleImageView circleImageView;
    private User administor;




    private View view;

    public static Fragment_see_card newInstance(Card card,String path_account,String path_back){
        Fragment_see_card fragment_see_card = new Fragment_see_card();
        fragment_see_card.path_account = path_account;
        fragment_see_card.path_back = path_back;
        fragment_see_card.card = card;
        return fragment_see_card;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_scrolling,container,false);
        administor = BmobUser.getCurrentUser(User.class);
        preView();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.float_button_star_card:
                BmobRelation relation = new BmobRelation();
                relation.add(card);
                administor.setCards(relation);
                administor.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Toast.makeText(getActivity(),"您已成功收藏该帖子 ！",Toast.LENGTH_SHORT).show();
                        }else{
                        }
                    }

                });
                break;
            case R.id.fragment_see_card_back:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if(fragmentManager.findFragmentByTag("fragment_see_card") !=null){
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("fragment_see_card"))
                            .remove(fragmentManager.findFragmentByTag("fragment_see_card"))
                            .show(fragmentManager.findFragmentByTag("fragment_share"))
                            .commit();
                }
                break;
            case R.id.nestedScrollView_see_card:
                break;
            case R.id.image_card_see_back:
                break;
        }
    }


    private void preView(){
        circleImageView = (CircleImageView)view.findViewById(R.id.circleImageView_web);
        floatingActionButton_star = (FloatingActionButton)view.findViewById(R.id.float_button_star_card);
        textView_title = (TextView)view.findViewById(R.id.card_view_title);
        textView_context = (TextView) view.findViewById(R.id.card_view_context);
        textView_time = (TextView)view.findViewById(R.id.card_view_time);
        imageView_background = (ImageView)view.findViewById(R.id.image_card_see_back);
        imageView_back = (ImageView)view.findViewById(R.id.fragment_see_card_back);
        circleImageView= (CircleImageView)view.findViewById(R.id.circleImageView_mine444);
        nestedScrollView_see_card = (NestedScrollView) view.findViewById(R.id.nestedScrollView_see_card);
        floatingActionButton_star.setOnClickListener(this);
        imageView_back.setOnClickListener(this);
        nestedScrollView_see_card.setOnClickListener(this);
        imageView_background.setOnClickListener(this);
        textView_time.setText(card.getTime());
        textView_title.setText(card.getTitle());
        textView_context.setText(card.getContext());
        Glide.with(getActivity().getApplication()).load(path_account).into(circleImageView);
        if(path_back==null){
            Glide.with(getActivity().getApplication()).load(R.drawable.test3).into(imageView_background);
        }else{
            Glide.with(getActivity().getApplication()).load(path_back).into(imageView_background);
        }
    }
}
