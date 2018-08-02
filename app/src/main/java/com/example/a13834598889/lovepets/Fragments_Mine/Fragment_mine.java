package com.example.a13834598889.lovepets.Fragments_Mine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.JavaBean.User;
import com.example.a13834598889.lovepets.R;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 13834598889 on 2018/5/7.
 */

public class Fragment_mine extends Fragment implements View.OnClickListener{
    private CircleImageView circleImageView;
    private View view;
    private TextView textView_button_tiezi;
    private TextView textView_button_friends;
    private ImageView imageView_edit;
    private FragmentManager fragmentManager;
    private User administrator;
    private TextView textView_nickName;
    private TextView textView_id;
    private TextView textView_signature;
    public String picture_path;
    private TextView textView_bangzhu;
    private TextView textView_button_tixing;

    public static Fragment_mine newInstance(String image_path){
        Fragment_mine fragment_mine = new Fragment_mine();
        fragment_mine.picture_path = image_path;
        return fragment_mine;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account,container,false);
        administrator = BmobUser.getCurrentUser(User.class);
        preView();
        return view;
    }

    private void preView(){
        circleImageView = (CircleImageView) view.findViewById(R.id.circleImageView_mine40);
        if(picture_path.equals("")){
            Glide.with(getActivity().getApplication()).load(R.drawable.test_touxiang3).into(circleImageView);
        }else{
            Glide.with(getActivity().getApplication()).load(picture_path).into(circleImageView);
        }
        textView_button_tiezi = (TextView)view.findViewById(R.id.text_button_wodetiezi) ;
        textView_button_friends = (TextView)view.findViewById(R.id.text_button_wodepengyou) ;
        textView_button_tixing =(TextView) view.findViewById(R.id.text_button_wodeyuding);
        imageView_edit =(ImageView)view.findViewById(R.id.image_button_edit);
        textView_nickName = (TextView)view.findViewById(R.id.text_mine_nickname);
        textView_id = (TextView)view.findViewById(R.id.text_mine_id);
        textView_signature = (TextView)view.findViewById(R.id.text_mine_signature);
        textView_bangzhu = (TextView) view.findViewById(R.id.text_button_bangzhu);
        textView_bangzhu.setOnClickListener(this);

        textView_nickName.setText("昵称："+administrator.getNickName()+"");
        textView_id.setText("ID："+administrator.getObjectId()+"");
        textView_signature.setText(administrator.getSignature()+"");

        textView_button_tiezi.setOnClickListener(this);
        textView_button_friends.setOnClickListener(this);
        imageView_edit.setOnClickListener(this);
        textView_button_tixing.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {


        switch (view.getId()){
            case R.id.text_button_wodetiezi:
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out).hide(this).commit();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                        .add(R.id.fragment_container,Fragment_card.newInstance(picture_path),"card_fragment")
                        .commit();
                break;
            case R.id.text_button_wodepengyou:
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out).hide(this).commit();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                        .add(R.id.fragment_container,Fragment_friends.newInstance(picture_path),"friends_fragment")
                        .commit();
                break;
            case R.id.image_button_edit:
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out).hide(this).commit();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                        .add(R.id.fragment_container,Fragment_account_change.newInstance(picture_path),"change_fragment")
                        .commit();
                break;
            case R.id.text_button_bangzhu:
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("温馨提醒");
                alertDialog.setMessage("如果有任何建议，请发送信息至邮箱 971106029@qq.com\nApp由中北大学Android实验室Century小组完成，未经允许不得进行任何商业活动");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.show();
                break;
            case R.id.text_button_wodeyuding:
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out).hide(this).commit();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                        .add(R.id.fragment_container,Fragment_alarm.newInstance(),"alarm_fragment")
                        .commit();
                break;
        }
    }
}
