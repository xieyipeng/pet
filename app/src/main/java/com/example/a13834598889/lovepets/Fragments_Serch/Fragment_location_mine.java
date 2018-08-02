package com.example.a13834598889.lovepets.Fragments_Serch;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.JavaBean.Decode_json;
import com.example.a13834598889.lovepets.JavaBean.User;
import com.example.a13834598889.lovepets.R;
import com.example.a13834598889.lovepets.RegisterActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by 13834598889 on 2018/5/14.
 */

public class Fragment_location_mine extends Fragment {

    private ImageView imageView_back;
    private EditText editText_jingdu;
    private EditText editText_weidu;
    private CircleImageView circleImageView_image;
    private String image_path;
    public LocationClient mLocationClient;

    private List<CheckBox> checkBoxes = new ArrayList<>();


    private CheckBox checkBox_1;
    private CheckBox checkBox_2;
    private CheckBox checkBox_3;
    private CheckBox checkBox_4;
    private CheckBox checkBox_5;
    private CheckBox checkBox_6;
    private CheckBox checkBox_7;
    private CheckBox checkBox_8;
    private CheckBox checkBox_9;
    private CheckBox checkBox_10;
    private CheckBox checkBox_11;



    private Button button_upLoad;

    private View view;


    private String data;

    private User administor;

    public static Fragment_location_mine newInstance(String image_path){
        Fragment_location_mine fragment_location_mine = new Fragment_location_mine();
        fragment_location_mine.image_path = image_path;
        return fragment_location_mine;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        administor = BmobUser.getCurrentUser(User.class);
        mLocationClient = new LocationClient(getActivity());
        mLocationClient.registerLocationListener(new MyLocationListener());
        view = inflater.inflate(R.layout.activity_2,container,false);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission
                .READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String permission[] = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permission, 1);
        }else {
            requestLocation();
        }
        preView();
        return view;
    }

    private void preView(){

        editText_jingdu = (EditText) view.findViewById(R.id.edit_jingdu);
        editText_weidu = (EditText) view.findViewById(R.id.edit_weidu);
        imageView_back = (ImageView) view.findViewById(R.id.fragment_location_back) ;
        circleImageView_image = (CircleImageView)view.findViewById(R.id.circleImageView_location);


        if(image_path.equals("")){
            Glide.with(getActivity().getApplication()).load(R.drawable.test_touxiang3).into(circleImageView_image);
        }else{
            Glide.with(getActivity().getApplication()).load(image_path).into(circleImageView_image);
        }

        button_upLoad = (Button) view.findViewById(R.id.button_upload_location);


        checkBox_1 = (CheckBox) view.findViewById(R.id.checkbox_1);
        checkBox_2 = (CheckBox) view.findViewById(R.id.checkbox_2);
        checkBox_3 = (CheckBox) view.findViewById(R.id.checkbox_3);
        checkBox_4 = (CheckBox) view.findViewById(R.id.checkbox_4);
        checkBox_5 = (CheckBox) view.findViewById(R.id.checkbox_5);
        checkBox_6 = (CheckBox) view.findViewById(R.id.checkbox_6);
        checkBox_7 = (CheckBox) view.findViewById(R.id.checkbox_7);
        checkBox_8 = (CheckBox) view.findViewById(R.id.checkbox_8);
        checkBox_9 = (CheckBox) view.findViewById(R.id.checkbox_9);
        checkBox_10 = (CheckBox) view.findViewById(R.id.checkbox_10);
        checkBox_11 = (CheckBox) view.findViewById(R.id.checkbox_11);
        checkBoxes.add(checkBox_1);checkBoxes.add(checkBox_2);checkBoxes.add(checkBox_3);
        checkBoxes.add(checkBox_4);checkBoxes.add(checkBox_5);checkBoxes.add(checkBox_6);
        checkBoxes.add(checkBox_7);checkBoxes.add(checkBox_8);checkBoxes.add(checkBox_9);
        checkBoxes.add(checkBox_10);


        button_upLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!check()){

                    final AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
                    dialog.setTitle("提示信息 ：")
                            .setMessage("      能且只能同时选中一种宠物 !")
                            .setCancelable(true);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialog.create().dismiss();
                        }
                    });
                    dialog.show();
                }else{
                    final AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
                    dialog.setTitle("提示信息 ：")
                            .setMessage("      是否确认上上传 ？")
                            .setCancelable(false);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            data = editText_weidu.getText().toString()+","+editText_jingdu.getText().toString()+",";
                            if(checkBox_1.isChecked()){
                                data = data+"猫咪"+",";
                            }
                            if(checkBox_2.isChecked()){
                                data = data+"狗狗"+",";
                            }
                            if(checkBox_3.isChecked()){
                                data = data+"小羊驼"+",";
                            }
                            if(checkBox_4.isChecked()){
                                data = data+"小胖猪"+",";
                            }
                            if(checkBox_5.isChecked()){
                                data = data+"鹦鹉"+",";
                            }
                            if(checkBox_6.isChecked()){
                                data = data+"仓鼠"+",";
                            }
                            if(checkBox_7.isChecked()){
                                data = data+"小乌龟"+",";
                            }
                            if(checkBox_8.isChecked()){
                                data = data+"流氓兔"+",";
                            }
                            if(checkBox_9.isChecked()){
                                data = data+"变色龙"+",";
                            }
                            if(checkBox_10.isChecked()){
                                data = data+"其他"+",";
                            }
                            data = data+ administor.getObjectId()+",";
                            if(checkBox_11.isChecked()){
                                data = data+"y";
                            }else{
                                data = data+"n";
                            }
                            User user = new User();
                            user.setData(data);
                            user.setHadPet(administor.getHadPet());
                            user.setSignature(administor.getSignature());
                            user.update(administor.getObjectId(),new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e == null){
                                        try{
                                            Toast.makeText(getActivity(), "已成功上传您的宠物位置，下一次地图界面，您的数据显示。", Toast.LENGTH_SHORT).show();
                                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            if(fragmentManager.findFragmentByTag("mine_location_fragment") !=null){
                                                fragmentManager.beginTransaction()
                                                        .hide(fragmentManager.findFragmentByTag("mine_location_fragment"))
                                                        .remove(fragmentManager.findFragmentByTag("mine_location_fragment"))
                                                        .show(fragmentManager.findFragmentByTag("fragment_serch"))
                                                        .commit();
                                            }

                                        }catch (Exception c){
                                            c.printStackTrace();
                                        }
                                    }else{
                                        try{
                                            Toast.makeText(getActivity(), ""+e.getMessage()+e.getErrorCode(), Toast.LENGTH_SHORT).show();

                                        }catch (Exception c){
                                            c.printStackTrace();
                                        }
                                    }
                                }
                            });


                        }
                    });
                    dialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            if(fragmentManager.findFragmentByTag("mine_location_fragment") !=null){
                                fragmentManager.beginTransaction()
                                        .hide(fragmentManager.findFragmentByTag("mine_location_fragment"))
                                        .remove(fragmentManager.findFragmentByTag("mine_location_fragment"))
                                        .show(fragmentManager.findFragmentByTag("fragment_serch"))
                                        .commit();
                            }
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
                if(fragmentManager.findFragmentByTag("mine_location_fragment") !=null){
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("mine_location_fragment"))
                            .remove(fragmentManager.findFragmentByTag("mine_location_fragment"))
                            .show(fragmentManager.findFragmentByTag("fragment_serch"))
                            .commit();
                }
            }
        });

    }



    private boolean check(){

        int number = 0 ;


        for(CheckBox checkBox:checkBoxes){
            if(checkBox.isChecked()){
                number++;
            }
        }


        if(number == 1){
            return true;
        }else{
            return false;
        }
    }



    private void requestLocation(){
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0){
                    for (int result:grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(getActivity(), "请同意权限", Toast.LENGTH_SHORT).show();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            if(fragmentManager.findFragmentByTag("mine_location_fragment") !=null){
                                fragmentManager.beginTransaction()
                                        .hide(fragmentManager.findFragmentByTag("mine_location_fragment"))
                                        .remove(fragmentManager.findFragmentByTag("mine_location_fragment"))
                                        .show(fragmentManager.findFragmentByTag("fragment_serch"))
                                        .commit();
                            }
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    Toast.makeText(getActivity(), "发生未知错误", Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    if(fragmentManager.findFragmentByTag("mine_location_fragment") !=null){
                        fragmentManager.beginTransaction()
                                .hide(fragmentManager.findFragmentByTag("mine_location_fragment"))
                                .remove(fragmentManager.findFragmentByTag("mine_location_fragment"))
                                .show(fragmentManager.findFragmentByTag("fragment_serch"))
                                .commit();
                    }
                }
                break;
            default:

        }
    }

    public class  MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder builder1 = new StringBuilder();
            builder1.append((Double)bdLocation.getLongitude());
            editText_jingdu.setText(builder1);
            StringBuilder builder2 = new StringBuilder();
            builder2.append((Double)bdLocation.getLatitude());
            editText_weidu.setText(builder2);

        }
    }


}
