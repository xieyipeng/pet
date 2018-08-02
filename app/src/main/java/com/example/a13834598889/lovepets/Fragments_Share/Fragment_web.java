package com.example.a13834598889.lovepets.Fragments_Share;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.JavaBean.Expert;
import com.example.a13834598889.lovepets.JavaBean.Web;
import com.example.a13834598889.lovepets.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 13834598889 on 2018/5/13.
 */

public class Fragment_web extends Fragment {

    private View view;
    private   Expert expert;
    private CircleImageView circleImageView;
    private TextView textView_title;
    private WebView webView;
    private ImageView imageView_back;
    private String image_path;

    public static Fragment_web newInstance(Expert expert, String image_path){
        Fragment_web fragment_web = new Fragment_web();
        fragment_web.expert = expert;
        fragment_web.image_path = image_path;
        return fragment_web;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_web,container,false);
        preView();
        return view;
    }

    private void preView(){

        webView = (WebView) view.findViewById(R.id.web_view);
        circleImageView = (CircleImageView)view.findViewById(R.id.circleImageView_web);
        textView_title = (TextView) view.findViewById(R.id.title_web);
        imageView_back = (ImageView)view.findViewById(R.id.fragment_web_back);

        if(!image_path.equals("")){
            Glide.with(getActivity().getApplication()).load(image_path).into(circleImageView);
        }else{
            Glide.with(getActivity().getApplication()).load(R.drawable.test_touxiang3).into(circleImageView);
        }


        textView_title.setText(expert.getTitle());

        //WebView加载web资源
        webView.loadUrl(expert.getUri());
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if(fragmentManager.findFragmentByTag("web_fragment") !=null){
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("web_fragment"))
                            .remove(fragmentManager.findFragmentByTag("web_fragment"))
                            .show(fragmentManager.findFragmentByTag("fragment_share"))
                            .commit();
                }
            }
        });

    }
}
