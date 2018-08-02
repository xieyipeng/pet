package com.example.a13834598889.lovepets.Fragments_Mine;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.JavaBean.Card;
import com.example.a13834598889.lovepets.JavaBean.Expert;
import com.example.a13834598889.lovepets.JavaBean.User;
import com.example.a13834598889.lovepets.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by 13834598889 on 2018/5/4.
 */

public class Fragment_card_mine extends Fragment {
    private RecyclerView recyclerView_share;
    private ShareAdapter adapter;
    private List<Card> cards = new ArrayList<>();
    private View view;
    private User administor;
    private RelativeLayout relativeLayout_no_card;


    public static Fragment_card_mine newInstance(){
        return new Fragment_card_mine();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        administor = BmobUser.getCurrentUser(User.class);
        view = inflater.inflate(R.layout.fragment_card_mine,container,false);
        preView();
        return view;
    }

    public class ShareHolder extends RecyclerView.ViewHolder{
        private TextView textView_idName;
        private TextView textView_ID;
        private TextView textView_signture;
        private TextView textView_time;
        private TextView textView_title;
        private TextView textView_context;
        private TextView textView_dianzanshu;
        private CircleImageView circleImageView;
        private ImageView imageView;
        private Integer dianzanshu_1;
        private LinearLayout layout;



        private Card card0;

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
            textView_dianzanshu = (TextView)view.findViewById(R.id.text_dianzanshu);
            layout = (LinearLayout) view.findViewById(R.id.caozuo_layout);
        }


        public void bindView(final Card card) {
            layout.setVisibility(View.GONE);
            card0 = card;
            textView_idName.setText(card.getUser().getNickName());
            textView_ID.setText("ID：" + card.getUser().getObjectId());
            textView_signture.setText(card.getUser().getSignature());
            textView_time.setText(card.getTime());
            textView_title.setText(card.getTitle());
            textView_context.setText(card.getContext());
            dianzanshu_1 = card.getNum_dianzan();
            textView_dianzanshu.setText("点赞数："+dianzanshu_1);
            BmobQuery<User> query = new BmobQuery<>();
            query.getObject(card.getUser().getObjectId(), new QueryListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if(e==null){
                        downloadFile_picture(user.getPicture(),circleImageView);
                    }
                }
            });
            if(card.getPicture()!=null){
                imageView.setVisibility(View.VISIBLE);
                downloadFile_picture(card.getPicture(),imageView);
            }
        }




        private void downloadFile_picture(BmobFile file, final ImageView view) {
            file.download(new DownloadFileListener() {
                @Override
                public void done(final String s, BmobException e) {
                    if(e==null){
                        if(s!=null){
                            try{
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Glide.with(getActivity().getApplication()).load(s).into(view);
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
        recyclerView_share = (RecyclerView)view.findViewById(R.id.recycler_View_mine_tiezi_1);
        relativeLayout_no_card = (RelativeLayout)view.findViewById(R.id.layout_no_card);

        adapter=new ShareAdapter(cards);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView_share.setLayoutManager(layoutManager);
        recyclerView_share.setAdapter(adapter);
        AddData();
    }

    private void AddData(){
        BmobQuery<Card> query = new BmobQuery<>();
        query.addWhereEqualTo("user",administor);
        query.setLimit(100);
        query.findObjects(new FindListener<Card>() {
            @Override
            public void done(final List<Card> list, BmobException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(list.size()==0){
                            relativeLayout_no_card.setVisibility(View.VISIBLE);
                        }else{
                            Toast.makeText(getActivity(),"您共发表了  "+list.size()+"  个帖子",Toast.LENGTH_LONG).show();
                            adapter.cards = list;
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }
}
