package com.example.a13834598889.lovepets.Fragments_Mine;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.example.a13834598889.lovepets.JavaBean.DianZan;
import com.example.a13834598889.lovepets.JavaBean.User;
import com.example.a13834598889.lovepets.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by 13834598889 on 2018/5/3.
 */

public class Fragment_friends extends Fragment implements View.OnClickListener{

    private RecyclerView recyclerView_friends;
    private ContactsAdapter adapter;
    private List<User> users = new ArrayList<>();
    private CircleImageView circleImageView;
    private ImageView imageView_back;
    private View view;
    private String picture_path;
    private Integer max = 1;


    private User administor = new User();



    public static Fragment_friends newInstance(String path){
        Fragment_friends fragment_friends = new Fragment_friends();
        fragment_friends.picture_path = path;
        return fragment_friends;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends,container,false);
        administor = BmobUser.getCurrentUser(User.class);
        preView();
        return view;
    }

    public class ContactsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView text_view_ni_cheng;
        private TextView text_view_dian_zan_shu;
        private TextView textView_qianming;
        private CircleImageView imageView_TouXiang;
        private ImageView imageView_dianzan;
        private LinearLayout layout_caozuo;
        private User user_friend;
        private DianZan dianZan_friend;
        private Integer dianzanshu_1 = 0;
        private String path = "";
        public ContactsHolder (View view){
            super(view);
            text_view_dian_zan_shu=(TextView)view.findViewById(R.id.dian_zan_shu_text_view);
            text_view_ni_cheng=(TextView)view.findViewById(R.id.ni_cheng_text_view);
            textView_qianming=(TextView)view.findViewById(R.id.ge_xing_qian_ming_text_view);
            imageView_TouXiang=(CircleImageView)view.findViewById(R.id.tou_xiang_image_view);
            imageView_dianzan = (ImageView)view.findViewById(R.id.dian_zan_button);
            layout_caozuo = (LinearLayout)view.findViewById(R.id.layout_caozuo_friend);
            imageView_dianzan.setOnClickListener(this);
            layout_caozuo.setOnClickListener(this);
        }

        public void bindView(User user){
            this.user_friend = user;
            text_view_ni_cheng.setText(user.getNickName());
            textView_qianming.setText(user.getSignature());
            if(user.getPicture()!=null){
                downloadFile_picture(user.getPicture(),imageView_TouXiang);
            }

            BmobQuery<DianZan> query=new BmobQuery<>();
            query.getObject(user.getDianZan().getObjectId(), new QueryListener<DianZan>() {
                @Override
                public void done(DianZan dianZan, cn.bmob.v3.exception.BmobException e) {
                    if (e==null){
                        dianZan_friend = dianZan;
                        final int a=dianZan.getmDianZanShu().intValue();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text_view_dian_zan_shu.setText(a+"");
                            }
                        });
                    }else{
                        Toast.makeText(getActivity(),""+e.getMessage()+e.getErrorCode(),Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

        private void downloadFile_picture(BmobFile file, final CircleImageView view) {
            file.download(new DownloadFileListener() {
                @Override
                public void done(final String s, cn.bmob.v3.exception.BmobException e) {
                    if(e==null){
                        if(s!=null){
                            try{
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{
                                            path = s;
                                            Glide.with(getActivity().getApplication()).load(s).into(view);
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

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case  R.id.dian_zan_button:
                    dianzanshu_1++;
                    if(max < dianzanshu_1 ){
                        Toast.makeText(getActivity(),"每次只能给此帖子点赞一次哦 ！",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    actionDaianzanCartoon(imageView_dianzan);
                    dianZan_friend.setmDianZanShu(dianZan_friend.getmDianZanShu()+1);
                    dianZan_friend.update(dianZan_friend.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(cn.bmob.v3.exception.BmobException e) {
                            if(e==null){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{
                                            text_view_dian_zan_shu.setText(""+dianZan_friend.getmDianZanShu());
                                        }catch (Exception c){
                                            c.printStackTrace();
                                        }
                                    }
                                });
                            }else{

                            }
                        }
                    });
                    actionDaianzanCartoon(imageView_dianzan);
                break;
                case R.id.layout_caozuo_friend:
                    FragmentManager fragmentManager;
                    fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                            .add(R.id.fragment_container,Fragment_im.newInstance(user_friend),"im_fragment")
                            .commit();
                    break;


            }
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

    public class ContactsAdapter extends RecyclerView.Adapter<ContactsHolder> {
        private List<User> users;

        public ContactsAdapter(List<User> users) {
            this.users = users;
        }

        @Override
        public ContactsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.friend_item, parent, false);
            return new ContactsHolder(view);
        }

        @Override
        public void onBindViewHolder(ContactsHolder holder, int position) {
            User user = users.get(position);
            holder.bindView(user);
        }

        @Override
        public int getItemCount() {
            return users.size();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_friends_back:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if(fragmentManager.findFragmentByTag("friends_fragment") !=null){
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("friends_fragment"))
                            .remove(fragmentManager.findFragmentByTag("friends_fragment"))
                            .show(fragmentManager.findFragmentByTag("fragment_account"))
                            .commit();
                }
                break;
                default:
                    break;
        }
    }


    //以下为测试代码
    private void addData(){
        BmobQuery<User> query = new BmobQuery<User>();
//likes是Post表中的字段，用来存储所有喜欢该帖子的用户
        query.addWhereRelatedTo("friends", new BmobPointer(administor));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, cn.bmob.v3.exception.BmobException e) {
                if(e==null){
                    Toast.makeText(getActivity(),"您共有  "+list.size()+"  个朋友",Toast.LENGTH_LONG).show();
                    adapter.users = list;
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void preView(){
        circleImageView = (CircleImageView)view.findViewById(R.id.circleImageView_mine88);
        if(picture_path.equals("")){
            Glide.with(getActivity().getApplication()).load(R.drawable.test_touxiang3).into(circleImageView);
        }else{
            Glide.with(getActivity().getApplication()).load(picture_path).into(circleImageView);
        }

        recyclerView_friends = (RecyclerView) view.findViewById(R.id.contacts_list_recycler_view);
        imageView_back=(ImageView)view.findViewById(R.id.fragment_friends_back);
        imageView_back.setOnClickListener(this);

        addData();
        adapter = new ContactsAdapter(users);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView_friends.setLayoutManager(layoutManager);
        recyclerView_friends.setAdapter(adapter);
    }


}
