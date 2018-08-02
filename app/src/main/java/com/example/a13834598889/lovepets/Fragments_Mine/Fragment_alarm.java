package com.example.a13834598889.lovepets.Fragments_Mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a13834598889.lovepets.JavaBean.Alarm;
import com.example.a13834598889.lovepets.JavaBean.Decode_json;
import com.example.a13834598889.lovepets.JavaBean.LongRunningService;
import com.example.a13834598889.lovepets.JavaBean.User;
import com.example.a13834598889.lovepets.R;
import com.example.a13834598889.lovepets.RegisterActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 13834598889 on 2018/5/15.
 */

public class Fragment_alarm extends Fragment {

    private View view;
    private EditText editText_time;
    private EditText editText_text;
    private ImageView imageView_back;
    private Button button_creat_alarm;


    public static Fragment_alarm newInstance(){
        return  new Fragment_alarm();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alerm,container,false);
        preView();
        return view;
    }


    private void preView(){
        editText_time = (EditText) view.findViewById(R.id.edit_alarm_time);
        editText_text = (EditText) view.findViewById(R.id.edit_alarm_text);
        imageView_back = (ImageView) view.findViewById(R.id.fragment_alarm_back);
        button_creat_alarm = (Button) view.findViewById(R.id.button_alarm);



        button_creat_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(editText_text.getText().toString().equals("00")||editText_time.getText().toString().equals("")||editText_text.getText().toString().equals("")){
                     Toast.makeText(getActivity(), "无法创建提醒，请完善信息", Toast.LENGTH_SHORT).show();
                 }else{
                     Intent intent = new Intent(getActivity(), LongRunningService.class);
                     intent.putExtra("time",editText_time.getText().toString());
                     intent.putExtra("text",editText_text.getText().toString());
                     getActivity().startService(intent);
                     final AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
                     dialog.setTitle("提示信息 ：")
                             .setMessage("新提醒已建立 。")
                             .setCancelable(false);
                     dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {
                             dialog.create().dismiss();
                         }
                     });
                     dialog.show();
                }
            }
        });


        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if(fragmentManager.findFragmentByTag("alarm_fragment") !=null){
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("alarm_fragment"))
                            .remove(fragmentManager.findFragmentByTag("alarm_fragment"))
                            .show(fragmentManager.findFragmentByTag("fragment_account"))
                            .commit();
                }
            }
        });
    }


}
