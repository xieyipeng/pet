package com.example.a13834598889.lovepets.Fragments_Pet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.AddStoryActivity;
import com.example.a13834598889.lovepets.Fragments_Mine.Fragment_card;
import com.example.a13834598889.lovepets.JavaBean.Pet;
import com.example.a13834598889.lovepets.JavaBean.User;
import com.example.a13834598889.lovepets.R;

import java.io.File;
import java.io.IOException;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by 13834598889 on 2018/5/7.
 */

public class Fragment_pet extends Fragment implements View.OnClickListener{
    private ImageView imageView_back;
    private CircleImageView circleImageView;
    private CircleImageView circleImageView_00;
    private View view;
    private User administrator = new User();
    public  LinearLayout y_pet_layout;
    public  LinearLayout n_pet_layout;
    private TextView textView_age;
    private TextView textView_name;
    private TextView textView_sex;
    private TextView textView_pet_id;

    private TextView textView_button_change;
    private TextView textView_button_add_life;
    private TextView textView_button_find_life;


    private FloatingActionButton add_pet;
    private String picture_path;
    private String image_pet ="";
    private Pet Pet00;


    public static Fragment_pet newInstance(String image_path){
        Fragment_pet fragment_pet = new Fragment_pet();
        fragment_pet.picture_path = image_path;
        return fragment_pet;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pet,container,false);
        administrator = BmobUser.getCurrentUser(User.class);
        preView();
        return view;
    }

    private void preView(){
        textView_pet_id = (TextView)view.findViewById(R.id.text_pet_id);
        imageView_back=(ImageView)view.findViewById(R.id.image_back_pet);
        textView_button_change = (TextView)view.findViewById(R.id.text_pet_change);
        textView_button_add_life = (TextView)view.findViewById(R.id.text_pet_life_add);
        textView_button_find_life = (TextView)view.findViewById(R.id.text_pet_find_life);
        circleImageView = (CircleImageView) view.findViewById(R.id.circleImageView_mine33);
        circleImageView_00 = (CircleImageView) view.findViewById(R.id.circleImageView_mine30);
        textView_button_change.setOnClickListener(this);
        textView_button_add_life.setOnClickListener(this);
        textView_button_find_life.setOnClickListener(this);
        if(picture_path.equals("")){
            Glide.with(getActivity().getApplication()).load(R.drawable.test_touxiang3).into(circleImageView);
        }else{
            Glide.with(getActivity().getApplication()).load(picture_path).into(circleImageView);
        }
        add_pet = (FloatingActionButton)view.findViewById(R.id.float_button_add_pet);
        textView_age = (TextView)view.findViewById(R.id.test_pet_age);
        textView_name  =(TextView) view.findViewById(R.id.test_pet_name);
        textView_sex = (TextView)view.findViewById(R.id.test_pet_sex);
        y_pet_layout = (LinearLayout)view.findViewById(R.id.y_pet_layout);
        n_pet_layout = (LinearLayout)view.findViewById(R.id.n_pet_layout);
        add_pet.setOnClickListener(this);


        if(administrator.getHadPet()!=0){
            y_pet_layout.setVisibility(View.VISIBLE);
            n_pet_layout.setVisibility(View.GONE);
        }else{
            y_pet_layout.setVisibility(View.GONE);
            n_pet_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        preView2();
    }


    private void preView2(){
        if(picture_path.equals("")){
            Glide.with(getActivity().getApplication()).load(R.drawable.test_touxiang3).into(circleImageView);
        }else{
            Glide.with(getActivity().getApplication()).load(picture_path).into(circleImageView);
        }
        if(BmobUser.getCurrentUser(User.class).getPet()!=null){
            BmobQuery<Pet> query = new BmobQuery<>();
            query.getObject(BmobUser.getCurrentUser(User.class).getPet().getObjectId(), new QueryListener<Pet>() {
                @Override
                public void done(final Pet pet, BmobException e) {
                    if (e == null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Pet00 = pet;
                                    BmobFile file = pet.getPicture();
                                    textView_age.setText(pet.getAge()+"");
                                    textView_name.setText(pet.getName());
                                    textView_sex.setText(pet.getSex());
                                    textView_pet_id.setText("ID："+pet.getObjectId());
                                    if(file!=null){
                                        downloadFile_picture(file,circleImageView_00,1);

                                    }else{
                                        int resource=R.drawable.test_touxiang2;
                                        Glide.with(getActivity().getApplication()).load(resource).into(circleImageView_00);
                                        Glide.with(getActivity())
                                                .load(resource)
                                                .bitmapTransform(new BlurTransformation(getActivity(), 14, 3))
                                                .into(imageView_back);
                                    }
                                }catch (Exception c){
                                    c.printStackTrace();
                                }

                            }
                        });
                    }else{

                    }
                }
            });
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.float_button_add_pet:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out).hide(this).commit();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                        .add(R.id.fragment_container,Fragment_add_pet.newInstance(),"add_pet_fragment")
                        .commit();

                break;
            case R.id.text_pet_change:
                FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
                fragmentManager1.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out).hide(this).commit();
                fragmentManager1.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                        .add(R.id.fragment_container,Fragment_pet_change.newInstance(image_pet,Pet00.getObjectId()
                                ,Pet00),"change_pet_fragment")
                        .commit();
                break;
            case R.id.text_pet_life_add:
                Intent intent = AddStoryActivity.actionAddStoryActivity(getActivity(),"新建萌宠生活录");
                startActivity(intent);
                break;
            case R.id.text_pet_find_life:
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out).hide(this).commit();

                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                        .add(R.id.fragment_container, Fragment_petLife.newInstance(image_pet),"pet_life_fragment")
                        .commit();

                break;
        }
    }

    private void downloadFile_picture(BmobFile file, final CircleImageView view, final int way) {
        file.download(new DownloadFileListener() {
            @Override
            public void done(final String s, BmobException e) {
                if(e==null){
                    if(s!=null){

                        try{
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        Glide.with(getActivity().getApplication()).load(s).into(view);
                                        if(way ==1){
                                            image_pet = s;
                                            Glide.with(getActivity())
                                                    .load(s)
                                                    .bitmapTransform(new BlurTransformation(getActivity(), 14, 3))
                                                    .into(imageView_back);

                                        }
                                    }catch (Exception c){
                                        c.printStackTrace();
                                    }
                                }
                            });
                        }catch (Exception c){
                            c.printStackTrace();
                        }
                    }else {
                    }
                }

            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });
    }

}
