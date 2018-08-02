package com.example.a13834598889.lovepets.Fragments_Share;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.JavaBean.Web;
import com.example.a13834598889.lovepets.R;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 13834598889 on 2018/5/14.
 */

public class Fragment_video extends Fragment{

    private ImageView imageView_back;
    private CircleImageView circleImageView;
    private String image_path;
    private View view;
    private Web web;
    private TextView textView_title;

    public static Fragment_video newInstance(Web web,String image_path){
        Fragment_video fragment_video = new Fragment_video();
        fragment_video.web = web;
        fragment_video.image_path = image_path;
        return fragment_video;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.video_layout,container,false);
        preView();
        return view;
    }

    private void preView(){
        imageView_back = (ImageView) view.findViewById(R.id.fragment_video_back);
        circleImageView = (CircleImageView) view.findViewById(R.id.circleImageView_video);
        textView_title = (TextView)view.findViewById(R.id.title_video);


        textView_title.setText(web.getTitle());
        if(!image_path.equals("")){
            Glide.with(getActivity().getApplication()).load(image_path).into(circleImageView);
        }else{
            Glide.with(getActivity().getApplication()).load(R.drawable.test_touxiang3).into(circleImageView);
        }

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if(fragmentManager.findFragmentByTag("video_fragment") !=null){
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("video_fragment"))
                            .remove(fragmentManager.findFragmentByTag("video_fragment"))
                            .show(fragmentManager.findFragmentByTag("fragment_share"))
                            .commit();
                }
            }
        });


        JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) view.findViewById(R.id.videoplayer);
        jzVideoPlayerStandard.setUp(web.getUri(), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, web.getTitle());

    }

    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
        if (JZVideoPlayer.backPress()) {
            return;
        }
    }

}
