package com.example.a13834598889.lovepets.Fragments_Pet;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.JavaBean.Pet;
import com.example.a13834598889.lovepets.JavaBean.User;
import com.example.a13834598889.lovepets.R;
import com.example.a13834598889.lovepets.Tool.PictureUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 13834598889 on 2018/5/12.
 */

public class Fragment_pet_change extends Fragment implements View.OnClickListener{

    private View view;
    private String image_path;
    private ImageView imageView_back;
    private CircleImageView circleImageView_pet;
    private EditText editText_pet_age;
    private EditText editText_pet_name;
    private EditText editText_pet_zhonglei;
    private Button button_change;
    private LinearLayout linearLayout_caozuo_pet_change;
    private ImageView imageView_choose_document;
    private CheckBox checkBox_isFQQ;
    private CheckBox checkBox_xiong;
    private CheckBox checkBox_ci;
    private ProgressBar progressBar;

    private static final int TAKE_PHOTO=1;
    private static final int CHOOSE_PHOTO=2;
    private Uri imageUri;
    private String imagePath = null;

    private Boolean isBaoCun = false;
    private String chulihou_imagePath;

    private Pet pet0 = new Pet();
    private String pet_id;

    public static Fragment_pet_change newInstance(String image_path,String id,Pet pet){
        Fragment_pet_change fragment_pet_change = new Fragment_pet_change();
        fragment_pet_change.image_path = image_path;
        fragment_pet_change.pet_id = id;
        fragment_pet_change.pet0 = pet;
        return fragment_pet_change;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_pet_change,container,false);
        preView();
        return view;
    }

    private void preView(){
        progressBar= (ProgressBar)view.findViewById(R.id.pet_change_progressbar);
        imageView_back = (ImageView) view.findViewById(R.id.fragment_pet_change_back);
        circleImageView_pet = (CircleImageView) view.findViewById(R.id.circleImageView_pet_change);
        editText_pet_age = (EditText)view.findViewById(R.id.edit_pet1_age);
        editText_pet_name = (EditText)view.findViewById(R.id.edit_pet1_name);
        editText_pet_zhonglei = (EditText)view.findViewById(R.id.edit_pet1_zhonglei);
        button_change = (Button)view.findViewById(R.id.button_pet_change);
        linearLayout_caozuo_pet_change = (LinearLayout)view.findViewById(R.id.choose_pet_image_layout);
        imageView_choose_document = (ImageView)view.findViewById(R.id.choose_from_document_pet);
        checkBox_isFQQ = (CheckBox) view.findViewById(R.id.checkbox_isFQQ);
        checkBox_ci = (CheckBox) view.findViewById(R.id.checkbox_ci_change);
        checkBox_xiong = (CheckBox) view.findViewById(R.id.checkbox_xiong_change);

        imageView_back.setOnClickListener(this);
        button_change.setOnClickListener(this);
        imageView_choose_document.setOnClickListener(this);
        checkBox_isFQQ.setChecked(false);



        if(!image_path.equals("")){
            Glide.with(getActivity().getApplication()).load(image_path).into(circleImageView_pet);
        }else{
            Glide.with(getActivity().getApplication()).load(R.drawable.tubiao_start).into(circleImageView_pet);
        }
        editText_pet_age.setText(pet0.getAge()+"");
        editText_pet_name.setText(pet0.getName());
        editText_pet_zhonglei.setText(pet0.getZhongLei());
        if(pet0.getIsFQQ() == 1){
            checkBox_isFQQ.setChecked(true);
        }
        if(pet0.getSex().equals("雄性")){
            checkBox_xiong.setChecked(true);
            checkBox_ci.setChecked(false);
        }else if(pet0.getSex().equals("雌性")){
            checkBox_xiong.setChecked(false);
            checkBox_ci.setChecked(true);
        }

        editText_pet_age.setEnabled(false);
        editText_pet_name.setEnabled(false);
        editText_pet_zhonglei.setEnabled(false);




    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_pet_change_back:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if(fragmentManager.findFragmentByTag("change_pet_fragment") !=null){
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("change_pet_fragment"))
                            .remove(fragmentManager.findFragmentByTag("change_pet_fragment"))
                            .show(fragmentManager.findFragmentByTag("fragment_pet"))
                            .commit();
                }
                break;
            case R.id.button_pet_change:
                if(isBaoCun){
                    imageView_back.setEnabled(false);
                    button_change.setEnabled(false);
                    editText_pet_age.setEnabled(false);
                    editText_pet_name.setEnabled(false);
                    editText_pet_zhonglei.setEnabled(false);
                    button_change.setText("编        辑");
                    isBaoCun = false;
                    final AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
                    dialog.setTitle("提示信息 ：")
                            .setMessage("是否确定修改个人账户信息 ？")
                            .setCancelable(false);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {



                            if(editText_pet_age.getText().toString().equals("")
                                    ||editText_pet_name.getText().toString().equals("")||editText_pet_zhonglei.getText().toString().equals("")){
                                Toast.makeText(getActivity(),"除照片之外，所有信息均不能为空 ！" ,Toast.LENGTH_LONG).show();
                                dialog.create().dismiss();
                            }else if (!checkBox_ci.isChecked()&&!checkBox_xiong.isChecked()) {
                                Toast.makeText(getActivity(), "必须选一个宠物性别", Toast.LENGTH_SHORT).show();
                            }else if (checkBox_ci.isChecked()&&checkBox_xiong.isChecked()) {
                                Toast.makeText(getActivity(), "只能选择一种性别", Toast.LENGTH_SHORT).show();
                            }else{

                                progressBar.setVisibility(View.VISIBLE);

                                if(imagePath!=null){
                                    if(pet0.getPicture()!=null){
                                        BmobFile yuan_FILE=pet0.getPicture();
                                        if(yuan_FILE!=null){
                                            yuan_FILE.setUrl(yuan_FILE.getUrl());
                                            yuan_FILE.delete(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {

                                                }
                                            });
                                        }
                                    }

                                    long t = System.currentTimeMillis();//获得当前时间的毫秒数
                                    Random rd = new Random(t);//作为种子数传入到Random的构造器中
                                    String targetPath = getActivity().getExternalCacheDir()+"compressPic"+rd.nextInt()+".jpg";
                                    chulihou_imagePath= PictureUtil.compressImage(imagePath,targetPath,30);
                                    File file=new File(chulihou_imagePath);
                                    final BmobFile bmobFile=new BmobFile(file);
                                    bmobFile.uploadblock(new UploadFileListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e==null){
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Pet pet= new Pet();
                                                pet.setAge(Integer.parseInt(editText_pet_age.getText().toString()));
                                                if(checkBox_ci.isChecked()){
                                                    pet.setSex("雌性");
                                                }else if(checkBox_xiong.isChecked()){
                                                    pet.setSex("雄性");
                                                }
                                                pet.setName(editText_pet_name.getText().toString());
                                                pet.setZhongLei(editText_pet_zhonglei.getText().toString());
                                                pet.setPicture(bmobFile);

                                                if(checkBox_isFQQ.isChecked()){
                                                    pet.setIsFQQ(1);
                                                }else{
                                                    pet.setIsFQQ(0);
                                                }

                                                pet.update(pet0.getObjectId(), new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        if(e==null){
                                                            Toast.makeText(getActivity(),"修改成功,2秒后将重新进入登陆界面 。",Toast.LENGTH_LONG).show();
                                                            new Thread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try{
                                                                        Thread.sleep(2000);
                                                                        getActivity().finish();
                                                                    }catch (InterruptedException e){
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }).start();
                                                        }else{

                                                        }
                                                    }
                                                });

                                            }else{
                                            }
                                        }
                                    });
                                }else{

                                    Pet pet= new Pet();
                                    pet.setAge(Integer.parseInt(editText_pet_age.getText().toString()));
                                    if(checkBox_ci.isChecked()){
                                        pet.setSex("雌性");
                                    }else if(checkBox_xiong.isChecked()){
                                        pet.setSex("雄性");
                                    }
                                    pet.setName(editText_pet_name.getText().toString());
                                    pet.setZhongLei(editText_pet_zhonglei.getText().toString());
                                    if(checkBox_isFQQ.isChecked()){
                                        pet.setIsFQQ(1);
                                    }else{
                                        pet.setIsFQQ(0);
                                    }

                                    progressBar.setVisibility(View.VISIBLE);

                                    pet.update(pet0.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e==null){
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getActivity(),"修改成功,2秒后将重新进入登陆界面 。",Toast.LENGTH_LONG).show();
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try{
                                                            Thread.sleep(2000);
                                                            getActivity().finish();
                                                        }catch (InterruptedException e){
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }).start();
                                            }else{
                                                Toast.makeText(getActivity(),"修改失败"+e.getMessage()+e.getErrorCode()+"*2*",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }

                            }

                        }
                    });
                    dialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            button_change.setEnabled(true);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            if(fragmentManager.findFragmentByTag("change_fragment") !=null){
                                fragmentManager.beginTransaction()
                                        .hide(fragmentManager.findFragmentByTag("change_fragment"))
                                        .remove(fragmentManager.findFragmentByTag("change_fragment"))
                                        .show(fragmentManager.findFragmentByTag("fragment_account"))
                                        .commit();
                            }
                        }
                    });
                    dialog.show();
                }else{
                    linearLayout_caozuo_pet_change.setVisibility(View.VISIBLE);
                    editText_pet_age.setEnabled(true);
                    editText_pet_name.setEnabled(true);
                    editText_pet_zhonglei.setEnabled(true);
                    button_change.setText("保        存");
                    isBaoCun = true;
                }
                break;
            case R.id.choose_from_document_pet:
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest
                        .permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.
                            WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    try{
                        Bitmap bitmap= BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        circleImageView_pet.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                }
            default:
                break;
        }
    }

    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(getActivity(),"Sorry,you denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        if(imagePath!=null){
            displayImage(imagePath);
        }
        else{
        }

    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        Uri uri;
        uri = data.getData();
        if (DocumentsContract.isDocumentUri(getActivity(),uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }
            else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=getImagePath(uri,null);
        }
        else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri,String selection){
        String path=null;
        Cursor cursor=getActivity().getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Log.d("test", "getImagePath: "+path);
            }
            cursor.close();
        }
        return path;

    }
    private void displayImage(String imagePath){
        if(imagePath!=null){
            Toast.makeText(getActivity(),""+imagePath,Toast.LENGTH_LONG).show();
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            circleImageView_pet.setImageBitmap(bitmap);
        }
        else {
            Toast.makeText(getActivity(),"Filed to get image .",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        linearLayout_caozuo_pet_change.setVisibility(View.GONE);
    }
}
