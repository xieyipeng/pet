package com.example.a13834598889.lovepets.Fragments_Pet;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.AddStoryActivity;
import com.example.a13834598889.lovepets.JavaBean.Record;
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
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 13834598889 on 2018/5/12.
 */

public class Fragment_petLife extends Fragment{

    private String image_path;
    private View view;
    private CircleImageView circleImageView;
    private ImageView imageView_back;
    private RecyclerView recyclerView_life;
    private FloatingActionButton floatingActionButton_add_life;

    private List<Record> records = new ArrayList<>();
    private LifeAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private User administor;

    private RelativeLayout layout_no_life;


    public static Fragment_petLife newInstance(String path){
        Fragment_petLife fragment_petLife = new Fragment_petLife();
        fragment_petLife.image_path = path;
        return fragment_petLife;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_life,container,false);
        administor = BmobUser.getCurrentUser(User.class);
        preView();
        return view;
    }

    private void preView(){
        imageView_back = (ImageView)view.findViewById(R.id.fragment_life_recode_back);
        recyclerView_life = (RecyclerView)view.findViewById(R.id.recycler_View_life);
        circleImageView = (CircleImageView) view.findViewById(R.id.circleImageView_life);
        floatingActionButton_add_life = (FloatingActionButton)view.findViewById(R.id.float_button_add_life);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout_life);
        layout_no_life = (RelativeLayout) view.findViewById(R.id.layout_no_life);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if(fragmentManager.findFragmentByTag("pet_life_fragment") !=null){
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("pet_life_fragment"))
                            .remove(fragmentManager.findFragmentByTag("pet_life_fragment"))
                            .show(fragmentManager.findFragmentByTag("fragment_pet"))
                            .commit();
                }
            }
        });

        floatingActionButton_add_life.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddStoryActivity.actionAddStoryActivity(getActivity(),"新建萌宠生活录");
                startActivity(intent);
            }
        });


        if(image_path.equals("")){
            Glide.with(getActivity().getApplication()).load(R.drawable.tubiao_start).into(circleImageView);
        }else{
            Glide.with(getActivity().getApplication()).load(image_path).into(circleImageView);
        }

        addData();
        adapter = new LifeAdapter(records);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView_life.setLayoutManager(layoutManager);
        recyclerView_life.setAdapter(adapter);
    }

    public class LifeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imageView_back;
        private TextView textView_time;
        private TextView textView_context;

        public LifeHolder(View view){
            super(view);
            imageView_back = (ImageView)view.findViewById(R.id.life_item_back);
            textView_time = (TextView)view.findViewById(R.id.life_item_time);
            textView_context = (TextView) view.findViewById(R.id.life_item_text);
            imageView_back.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.life_item_back:
                    break;
            }
        }

        private void bindView(Record record){
            textView_time.setText(record.getTime());
            textView_context.setText(record.getContext());

            if(record.getImage_back()!=null){
                downloadFile_picture(record.getImage_back(),imageView_back);
            }else{
                Glide.with(getActivity().getApplication()).load(image_path).into(imageView_back);
            }
        }

        private void downloadFile_picture(BmobFile file, final ImageView view) {
            file.download(new DownloadFileListener() {
                @Override
                public void done(final String s, BmobException e) {
                    if(e==null){
                        if(s!=null){
                            try{
                                Glide.with(getActivity().getApplication()).load(s).into(view);
                            }catch (Exception exception){
                                exception.printStackTrace();
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

    public class LifeAdapter extends RecyclerView.Adapter<LifeHolder>{

        public List<Record> records;


        public LifeAdapter (List<Record> records){
            this.records = records;
        }

        @Override
        public LifeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.life_item, parent, false);
            return new LifeHolder(view);
        }

        @Override
        public void onBindViewHolder(LifeHolder holder, int position) {
            Record record = records.get(position);
            holder.bindView(record);
        }

        @Override
        public int getItemCount() {
            return records.size();
        }

    }

    public void refresh(){
        addData();
        swipeRefreshLayout.setRefreshing(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                addData();
                                swipeRefreshLayout.setRefreshing(false);
                            }catch (Exception c){
                                c.printStackTrace();
                            }

                        }
                    });
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void addData(){
        BmobQuery<Record> query = new BmobQuery<>();
        query.addWhereEqualTo("user",administor);
        query.setLimit(20);
        query.order("-createdAt");
        query.findObjects(new FindListener<Record>() {
            @Override
            public void done(final List<Record> list, BmobException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            if(list.size()==0){
                                layout_no_life.setVisibility(View.VISIBLE);
                            }
                            Toast.makeText(getActivity(),"为您找到  "+list.size()+"  生活录",Toast.LENGTH_LONG).show();
                            adapter.records = list;
                            adapter.notifyDataSetChanged();
                        }catch (Exception c){
                            c.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        layout_no_life.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }
}
