package com.example.a13834598889.lovepets.Fragments_Mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 13834598889 on 2018/5/7.
 */

public class Fragment_account extends Fragment implements View.OnClickListener{


    private CircleImageView circleImageView;
    private EditText editText_age;
    private EditText editText_emile;
    private EditText editText_nickName;
    private EditText editText_signature;
    private Button button_change;
    private ImageView imageView_back;
    private Boolean isBaoCun = false;



    public static Fragment_account newInstance(){
        return new Fragment_account();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account,container,false);
        circleImageView = (CircleImageView) view.findViewById(R.id.circleImageView_mine1010);
        editText_age = (EditText)view.findViewById(R.id.edit_mine_age);
        editText_emile = (EditText)view.findViewById(R.id.edit_mine_emile);
        editText_nickName = (EditText)view.findViewById(R.id.edit_mine_nickname);
        editText_signature = (EditText)view.findViewById(R.id.edit_mine_signature);
        button_change = (Button)view.findViewById(R.id.button_mine_change) ;
        imageView_back = (ImageView)view.findViewById(R.id.fragment_mine_back);
        button_change.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        imageView_back.setOnClickListener(this);



        //以下为数据初始化
        Glide.with(getActivity())
                .load(R.drawable.test_touxiang2)
                .into(circleImageView);


        //设置初始状态为不可编辑
        editText_age.setEnabled(false);
        editText_emile.setEnabled(false);
        editText_nickName.setEnabled(false);
        editText_signature.setEnabled(false);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.circleImageView_mine1010:
                break;
            case R.id.button_mine_change:
                if(isBaoCun){
                    editText_age.setEnabled(false);
                    editText_emile.setEnabled(false);
                    editText_nickName.setEnabled(false);
                    editText_signature.setEnabled(false);
                    button_change.setText("编        辑");
                    isBaoCun = false;
                }else{
                    editText_age.setEnabled(true);
                    editText_emile.setEnabled(true);
                    editText_nickName.setEnabled(true);
                    editText_signature.setEnabled(true);
                    button_change.setText("保        存");
                    isBaoCun = true;
                }
                break;
            case R.id.fragment_mine_back:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if(fragmentManager.findFragmentByTag("account_fragment") !=null){
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("account_fragment"))
                            .remove(fragmentManager.findFragmentByTag("account_fragment"))
                            .show(fragmentManager.findFragmentByTag("fragment_mine"))
                            .commit();
                }
                break;
                default:
                    break;

        }
    }
}
