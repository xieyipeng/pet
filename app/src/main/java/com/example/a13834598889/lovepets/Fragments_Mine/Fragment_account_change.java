package com.example.a13834598889.lovepets.Fragments_Mine;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.JavaBean.User;
import com.example.a13834598889.lovepets.R;
import com.example.a13834598889.lovepets.Tool.PictureUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 13834598889 on 2018/5/8.
 */

public class Fragment_account_change extends Fragment implements View.OnClickListener{
    private View view;
    private ImageView imageView_back;
    private Button button_change;
    private CircleImageView circleImageView;
    private EditText editText_age;
    private EditText editText_emile;
    private EditText editText_nickName;
    private EditText editText_signature;
    private EditText editText_phone;
    private Boolean isBaoCun = false;
    private LinearLayout layout_choose_image;
    private String imagePath = null;
    private Uri imageUri;
    private static final int TAKE_PHOTO=1;
    private static final int CHOOSE_PHOTO=2;
    private User administrator;
    private String chulihou_imagePath="";
    private String picture_path;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    boolean isRember;
    private ImageView imageView_choose_document;
    private ProgressBar progressBar;

    public static Fragment_account_change newInstance(String imagePath){
       Fragment_account_change fragment_account_change = new Fragment_account_change();
       fragment_account_change.picture_path = imagePath;
       return fragment_account_change;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account_change,container,false);
        administrator = BmobUser.getCurrentUser(User.class);
        preView();
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
                    editText_phone.setEnabled(false);
                    button_change.setText("编        辑");
                    isBaoCun = false;
                    final AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
                    dialog.setTitle("提示信息 ：")
                            .setMessage("是否确定修改个人账户信息 ？")
                            .setCancelable(false);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            if(editText_phone.getText().toString().equals("")||editText_signature.getText().toString().equals("")
                                    ||editText_emile.getText().toString().equals("")||editText_age.getText().toString().equals("")){
                                Toast.makeText(getActivity(),"除照片之外，所有信息均不能为空 ！" ,Toast.LENGTH_LONG).show();
                                dialog.create().dismiss();
                            }else{

                                progressBar.setVisibility(View.VISIBLE);
                                imageView_back.setEnabled(false);
                                if(imagePath!=null){
                                    if(administrator.getPicture()!=null){
                                        BmobFile yuan_FILE=administrator.getPicture();
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
                                    String targetPath = getActivity().getExternalCacheDir()+"compressPic"+rd+".jpg";
                                    chulihou_imagePath= PictureUtil.compressImage(imagePath,targetPath,30);
                                    File file=new File(chulihou_imagePath);
                                    final BmobFile bmobFile=new BmobFile(file);
                                    bmobFile.uploadblock(new UploadFileListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e==null){
                                                progressBar.setVisibility(View.INVISIBLE);
                                                imageView_back.setEnabled(true);
                                                Toast.makeText(getActivity(),"上传成功",Toast.LENGTH_SHORT).show();
                                                editor=pref.edit();
                                                editor.putBoolean("image_path",true);
                                                editor.putString("image",imagePath);
                                                editor.apply();
                                                User user=new User();
                                                user.setNickName(editText_nickName.getText().toString());
                                                user.setMobilePhoneNumber(editText_phone.getText().toString());
                                                user.setSignature(editText_signature.getText().toString());
                                                user.setEmail(editText_emile.getText().toString());
                                                user.setPicture(bmobFile);
                                                user.setAge(Integer.parseInt(editText_age.getText().toString()));
                                                user.setHadPet(administrator.getHadPet());
                                                user.setDianZan(administrator.getDianZan());
                                                user.update(BmobUser.getCurrentUser(User.class).getObjectId(), new UpdateListener() {
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
                                                            Toast.makeText(getActivity(),"修改失败"+e.getMessage()+e.getErrorCode(),Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });

                                            }else{
                                                Toast.makeText(getActivity(),"上传失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else{

                                    User user=new User();
                                    user.setNickName(editText_nickName.getText().toString());
                                    user.setMobilePhoneNumber(editText_phone.getText().toString());
                                    user.setSignature(editText_signature.getText().toString());
                                    user.setEmail(editText_emile.getText().toString());
                                    user.setAge(Integer.parseInt(editText_age.getText().toString()));
                                    user.setHadPet(administrator.getHadPet());
                                    user.setDianZan(administrator.getDianZan());
                                    user.update(BmobUser.getCurrentUser(User.class).getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e==null){
                                                progressBar.setVisibility(View.INVISIBLE);
                                                imageView_back.setEnabled(true);
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
                                                Toast.makeText(getActivity(),"修改失败"+e.getMessage()+e.getErrorCode(),Toast.LENGTH_LONG).show();
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
                            progressBar.setVisibility(View.INVISIBLE);
                            imageView_back.setEnabled(true);
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
                    layout_choose_image.setVisibility(View.VISIBLE);
                    editText_age.setEnabled(true);
                    editText_emile.setEnabled(true);
                    editText_nickName.setEnabled(true);
                    editText_signature.setEnabled(true);
                    editText_phone.setEnabled(true);
                    button_change.setText("保        存");
                    isBaoCun = true;
                }
                break;
            case R.id.fragment_mine_back:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if(fragmentManager.findFragmentByTag("change_fragment") !=null){
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("change_fragment"))
                            .remove(fragmentManager.findFragmentByTag("change_fragment"))
                            .show(fragmentManager.findFragmentByTag("fragment_account"))
                            .commit();
                }
                break;
            case R.id.choose_from_document:
                if(ContextCompat.checkSelfPermission(getActivity(), Manifest
                        .permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.
                            WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
                break;
            default:
                break;

        }

    }

    private void preView(){
        progressBar = (ProgressBar) view.findViewById(R.id.account_change_progressbar);
        pref= PreferenceManager.getDefaultSharedPreferences(getActivity());
        isRember=pref.getBoolean("image_path",false);
        circleImageView = (CircleImageView) view.findViewById(R.id.circleImageView_mine1010);
        if(picture_path.equals("")){
            Glide.with(getActivity().getApplication()).load(R.drawable.test_touxiang2).into(circleImageView);
        }else{
            Glide.with(getActivity().getApplication()).load(picture_path).into(circleImageView);
        }
        editText_age = (EditText)view.findViewById(R.id.edit_mine_age);
        editText_emile = (EditText)view.findViewById(R.id.edit_mine_emile);
        editText_nickName = (EditText)view.findViewById(R.id.edit_mine_nickname);
        editText_signature = (EditText)view.findViewById(R.id.edit_mine_signature);
        editText_phone = (EditText)view.findViewById(R.id.edit_mine_phoneNumber);
        button_change = (Button)view.findViewById(R.id.button_mine_change) ;
        imageView_back = (ImageView)view.findViewById(R.id.fragment_mine_back);
        layout_choose_image = (LinearLayout) view.findViewById(R.id.choose_image_layout);
        imageView_choose_document = (ImageView)view.findViewById(R.id.choose_from_document);
        button_change.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        imageView_back.setOnClickListener(this);
        imageView_choose_document.setOnClickListener(this);
        editText_age.setText(administrator.getAge()+"");
        editText_emile.setText(administrator.getEmail());
        editText_nickName.setText(administrator.getNickName());
        editText_signature.setText(administrator.getSignature());
        editText_phone.setText(administrator.getMobilePhoneNumber());


        //设置初始状态为不可编辑
        editText_age.setEnabled(false);
        editText_emile.setEnabled(false);
        editText_nickName.setEnabled(false);
        editText_signature.setEnabled(false);
        editText_phone.setEnabled(false);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    try{
                        Bitmap bitmap= BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        circleImageView.setImageBitmap(bitmap);
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
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
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
                String selection=MediaStore.Images.Media._ID+"="+id;
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
            circleImageView.setImageBitmap(bitmap);
        }
        else {
            Toast.makeText(getActivity(),"Filed to get image .",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        layout_choose_image.setVisibility(View.GONE);
    }
}
