package com.example.a13834598889.lovepets.Fragments_Share;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.AddStoryActivity;
import com.example.a13834598889.lovepets.JavaBean.Card;
import com.example.a13834598889.lovepets.JavaBean.User;
import com.example.a13834598889.lovepets.R;

import java.net.CacheRequest;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by 13834598889 on 2018/5/8.
 */

public class Fragment_share_1 extends Fragment implements View.OnClickListener{

    private View view;
    private RecyclerView recyclerView_share;
    private ShareAdapter adapter;
    private List<Card> cards = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Integer max = 1;

    private User administor;

    public static Fragment_share_1 newInstance(){
        Fragment_share_1 fragment_share_1 = new Fragment_share_1();
        return fragment_share_1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_share_1,container,false);
        administor = BmobUser.getCurrentUser(User.class);
        preView();
        return view;
    }

    public class ShareHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textView_idName;
        private TextView textView_ID;
        private TextView textView_signture;
        private TextView textView_time;
        private TextView textView_title;
        private TextView textView_context;
        private TextView textView_dianzanshu;
        private CircleImageView circleImageView;
        private ImageView imageView;
        private Integer dianzanshu_1 = 0;
        private LinearLayout layout_caozuo_card;
        private String path_account_image;
        private String path_back_image;


        private View imageView_dianzan;
        private View imageView_shoucang;
        private View imageView_jiahaoyou;

        private Card card0;

        private User user_card = new User();

        public ShareHolder(View view){
            super(view);
            textView_idName=(TextView)view.findViewById(R.id.text_share_item_idName);
            textView_ID=(TextView)view.findViewById(R.id.text_share_item_ID);
            textView_signture=(TextView)view.findViewById(R.id.text_share_item_signture);
            textView_time=(TextView)view.findViewById(R.id.text_share_item_time);
            textView_title=(TextView)view.findViewById(R.id.text_share_item_title);
            textView_context=(TextView)view.findViewById(R.id.text_share_item_context);
            circleImageView=(CircleImageView)view.findViewById(R.id.image_share_item_circleView);
            imageView=(ImageView)view.findViewById(R.id.image_share_item_image);
            imageView_dianzan=(ImageView)view.findViewById(R.id.image_button_dianzan);
            imageView_shoucang=(ImageView)view.findViewById(R.id.image_button_shoucang);
            imageView_jiahaoyou=(ImageView)view.findViewById(R.id.image_button_jiahaoyou);
            textView_dianzanshu = (TextView)view.findViewById(R.id.text_dianzanshu);
            layout_caozuo_card = (LinearLayout)view.findViewById(R.id.caozuo_layout_card);
            imageView_dianzan.setOnClickListener(this);
            imageView_shoucang.setOnClickListener(this);
            imageView_jiahaoyou.setOnClickListener(this);
            layout_caozuo_card.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.caozuo_layout_card:

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                            .add(R.id.fragment_container,Fragment_see_card.newInstance(card0,path_account_image,path_back_image),"fragment_see_card")
                            .commit();
                    break;

                case R.id.image_button_dianzan:
                    dianzanshu_1++;
                    if(max < dianzanshu_1 ){
                        Toast.makeText(getActivity(),"每次只能给此帖子点赞一次哦 ！",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    actionDaianzanCartoon(imageView_dianzan);
                    final Card card = new Card();
                    card.setNum_dianzan(card0.getNum_dianzan()+1);
                    card.update(card0.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try{
                                            textView_dianzanshu.setText("点赞数："+card.getNum_dianzan());
                                        }catch (Exception c){
                                            c.printStackTrace();
                                        }
                                    }
                                });
                            }else{

                            }
                        }
                    });
                    break;
                case R.id.image_button_shoucang:
                    BmobRelation relation = new BmobRelation();
                    relation.add(card0);
                    administor.setCards(relation);
                    administor.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){

                                try{
                                    Toast.makeText(getActivity(),"您已成功收藏该帖子 ！",Toast.LENGTH_SHORT).show();
                                }catch (Exception c){
                                    c.printStackTrace();
                                }
                            }else{

                            }
                        }

                    });
                    actionDaianzanCartoon(imageView_shoucang);
                    break;
                case R.id.image_button_jiahaoyou:
                    BmobRelation relation1 = new BmobRelation();
                    User user = new User();
                    user.setObjectId(user_card.getObjectId());
                    relation1.add(user);
                    administor.setFriends(relation1);
                    administor.update(administor.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){

                            }else{

                            }
                        }
                    });
                    actionDaianzanCartoon(imageView_jiahaoyou);
                    Toast.makeText(getActivity(),"您已临时加对方为好友，可在我的朋友中找到次联系人 ！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        public void bindView(final Card card) {
            card0 = card;
            textView_idName.setText(card.getUser().getNickName());
            textView_ID.setText("ID：" + card.getUser().getObjectId());
            textView_signture.setText(card.getUser().getSignature());
            textView_time.setText(card.getTime());
            textView_title.setText(card.getTitle());
            textView_context.setText(card.getContext());
            textView_dianzanshu.setText("点赞数："+card.getNum_dianzan());
            BmobQuery<User> query = new BmobQuery<>();
            query.getObject(card.getUser().getObjectId(), new QueryListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if(e==null){
                        try{
                            user_card = user;
                            downloadFile_picture(user.getPicture(),circleImageView,1);
                        }catch (Exception c){
                            c.printStackTrace();
                        }

                    }
                }
            });
            if(card.getPicture()!=null){
                imageView.setVisibility(View.VISIBLE);
                downloadFile_picture(card.getPicture(),imageView,2);
            }
        }




        private void downloadFile_picture(BmobFile file, final ImageView view, final int way) {
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
                                            if(way == 1){
                                                path_account_image = s;
                                            }else if(way == 2){
                                                path_back_image = s;
                                            }
                                        }catch (Exception c){
                                            c.printStackTrace();
                                        }

                                    }
                                });
                            }catch (Exception d){
                                d.printStackTrace();
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

        private void actionDaianzanCartoon(View imageView){
            ObjectAnimator button_dianzan0=ObjectAnimator.ofFloat(imageView,"scaleX",1f,1.7f,1f);
            ObjectAnimator button_dianzan1=ObjectAnimator.ofFloat(imageView,"scaleY",1f,1.7f,1f);
            ObjectAnimator button_dianzan2=ObjectAnimator.ofFloat(imageView,"rotation",0f,360f);
            AnimatorSet animatorSet=new AnimatorSet();
            animatorSet.setDuration(1000)
                    .play(button_dianzan0)
                    .with(button_dianzan1)
                    .with(button_dianzan2);
            animatorSet.start();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.float_button_add_0_background:
                Intent intent = AddStoryActivity.actionAddStoryActivity(getActivity(),"新建帖子");
                startActivity(intent);
                break;
        }
    }

    public class ShareAdapter extends RecyclerView.Adapter<ShareHolder>{
        private List<Card> cards;

        public ShareAdapter (List<Card> cards){
            this.cards = cards;
        }


        @Override
        public ShareHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.share_item, parent, false);
            return new ShareHolder(view);
        }

        @Override
        public void onBindViewHolder(ShareHolder holder, int position) {
            Card card = cards.get(position);
            holder.bindView(card);
        }

        @Override
        public int getItemCount() {
            return cards.size();
        }

    }


    public void preView(){
        recyclerView_share = (RecyclerView)view.findViewById(R.id.recycler_View_share_1);
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.float_button_add_0_background) ;
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout_card);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        floatingActionButton.setOnClickListener(this);

        adapter=new ShareAdapter(cards);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView_share.setLayoutManager(layoutManager);
        recyclerView_share.setAdapter(adapter);
        AddData();
    }

    public void refresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            AddData();
                            swipeRefreshLayout.setRefreshing(false);
                        }catch (Exception c){
                            c.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    public void AddData(){
        BmobQuery<Card> query = new BmobQuery<>();
        query.addWhereEqualTo("isJScard",0);
        query.setLimit(10);
        query.order("-createdAt");
        query.findObjects(new FindListener<Card>() {
            @Override
            public void done(final List<Card> list, BmobException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Toast.makeText(getActivity(),"为您推荐  "+list.size()+"  个帖子",Toast.LENGTH_LONG).show();
                            ShareAdapter adapter1 = new ShareAdapter(list);
                            recyclerView_share.setAdapter(adapter1);
                            adapter1.notifyDataSetChanged();
                        }catch (Exception c){
                            c.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
