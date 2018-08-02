package com.example.a13834598889.lovepets.Fragments_Mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.JavaBean.Msg;
import com.example.a13834598889.lovepets.JavaBean.User;
import com.example.a13834598889.lovepets.R;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 13834598889 on 2018/5/13.
 */

public class Fragment_im extends Fragment {

    private View view;
    private ImageView imageView_back;
//    private String image_path;
    private TextView textView_name;

    private List<Msg> msgList = new ArrayList<>();
    private Button send;
    private EditText inputText;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private CircleImageView circleImageView_he;
    private CircleImageView circleImageView_me;

    private User im_He;
    private User im_Me;


    public static Fragment_im newInstance(User user){
        Fragment_im fragment_im = new Fragment_im();
//        fragment_im.image_path = image_path;
        fragment_im.im_He = user;
        return fragment_im;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.im_layout,container,false);
        preView();
        return view;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftLayout;

        LinearLayout rightLayout;

        TextView leftMsg;

        TextView rightMsg;

        public ViewHolder(View view) {
            super(view);
            leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            leftMsg = (TextView) view.findViewById(R.id.left_msg);
            rightMsg = (TextView) view.findViewById(R.id.right_msg);
        }
    }

    public class MsgAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<Msg> mMsgList;


        public MsgAdapter(List<Msg> msgList) {
            mMsgList = msgList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Msg msg = mMsgList.get(position);
            if (msg.getType() == Msg.TYPE_RECEIVED) {
                // 如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
                holder.leftLayout.setVisibility(View.VISIBLE);
                holder.rightLayout.setVisibility(View.GONE);
                holder.leftMsg.setText(msg.getContent());
            } else if(msg.getType() == Msg.TYPE_SENT) {
                // 如果是发出的消息，则显示右边的消息布局，将左边的消息布局隐藏
                holder.rightLayout.setVisibility(View.VISIBLE);
                holder.leftLayout.setVisibility(View.GONE);
                holder.rightMsg.setText(msg.getContent());
            }
        }

        @Override
        public int getItemCount() {
            return mMsgList.size();
        }

    }


    private void preView(){

        circleImageView_me = (CircleImageView)view.findViewById(R.id.image_im_me);
        circleImageView_he = (CircleImageView) view.findViewById(R.id.image_im_he);

        if(BmobUser.getCurrentUser(User.class).getPicture()!=null)
        {
            downloadFile_picture(BmobUser.getCurrentUser(User.class).getPicture(),circleImageView_me);
        }


//        if(!image_path.equals("")){
//            Glide.with(getActivity().getApplication()).load(image_path).into(circleImageView_he);
//        }

        im_Me = BmobUser.getCurrentUser(User.class);
        imageView_back = (ImageView)view.findViewById(R.id.fragment_im_back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if(fragmentManager.findFragmentByTag("im_fragment") !=null){
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("im_fragment"))
                            .remove(fragmentManager.findFragmentByTag("im_fragment"))
                            .show(fragmentManager.findFragmentByTag("friends_fragment"))
                            .commit();
                }
            }
        });

        textView_name = (TextView)view.findViewById(R.id.text_im_name);


        textView_name.setText(im_He.getNickName().toString());


        inputText = (EditText) view.findViewById(R.id.input_text);
        send = (Button) view.findViewById(R.id.send);
        msgRecyclerView = (RecyclerView) view.findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {


                    String account = im_He.getIm_id();
                    Toast.makeText(getActivity(),account,Toast.LENGTH_SHORT).show();
// 以单聊类型为例
                    SessionTypeEnum sessionType = SessionTypeEnum.P2P;
// 创建一个文本消息
                    IMMessage textMessage = MessageBuilder.createTextMessage(account, sessionType, content);
// 发送给对方
                    NIMClient.getService(MsgService.class).sendMessage(textMessage, true);

                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    inputText.setText(""); // 清空输入框中的内容
                }
            }
        });
    }

    private void initMsgs() {
        Msg msg1 = new Msg("Hello guy.", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("Hello. Who is that?", Msg.TYPE_SENT);
        msgList.add(msg2);
        Msg msg3 = new Msg("This is Tom. Nice talking to you. ", Msg.TYPE_RECEIVED);
        msgList.add(msg3);
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
}
