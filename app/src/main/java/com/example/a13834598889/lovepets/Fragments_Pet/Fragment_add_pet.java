package com.example.a13834598889.lovepets.Fragments_Pet;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.JavaBean.Pet;
import com.example.a13834598889.lovepets.JavaBean.User;
import com.example.a13834598889.lovepets.LoginActivity;
import com.example.a13834598889.lovepets.R;
import com.example.a13834598889.lovepets.Tool.PictureUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 13834598889 on 2018/5/8.
 */

public class Fragment_add_pet extends Fragment implements View.OnClickListener {

    private View view;
    private EditText editText_pet_age;
    private EditText editText_pet_name;
    private EditText editText_pet_zhonglei;
    private ImageView imageView_back;
    private Button button_add_pet;
    private CircleImageView circleImageView_pet;
    private User administrator = new User();
    private ImageView imageView_choose_document;
    private static final int TAKE_PHOTO=1;
    private static final int CHOOSE_PHOTO=2;


    private CheckBox checkBox_xiong;
    private CheckBox checkBox_ci;

    private String imagePath = null;
    private Uri imageUri;
    private String chulihou_imagePath;
    private ProgressBar progressBar;

    public static Fragment_add_pet newInstance(){
        return new Fragment_add_pet();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_pet,container,false);

        preView();
        return view;
    }

    private void preView(){
        progressBar = (ProgressBar) view.findViewById(R.id.add_pet_progressbar);
        administrator = BmobUser.getCurrentUser(User.class);
        editText_pet_age = (EditText) view.findViewById(R.id.edit_add_pet_age);
        editText_pet_name = (EditText) view.findViewById(R.id.edit_add_pet_name);
        imageView_back = (ImageView) view.findViewById(R.id.fragment_add_pet_back);
        editText_pet_zhonglei = (EditText) view.findViewById(R.id.edit_add_pet_zhonglei);
        button_add_pet = (Button) view.findViewById(R.id.button_add_pet);
        imageView_choose_document = (ImageView)view.findViewById(R.id.choose_from_document_pet);
        circleImageView_pet = (CircleImageView) view.findViewById(R.id.add_pet_image);
        checkBox_xiong = (CheckBox) view.findViewById(R.id.checkbox_xiong);
        checkBox_ci = (CheckBox) view.findViewById(R.id.checkbox_ci);

        imageView_back.setOnClickListener(this);
        button_add_pet.setOnClickListener(this);
        imageView_choose_document.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_add_pet_back:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if(fragmentManager.findFragmentByTag("add_pet_fragment") !=null){
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("add_pet_fragment"))
                            .remove(fragmentManager.findFragmentByTag("add_pet_fragment"))
                            .show(fragmentManager.findFragmentByTag("fragment_pet"))
                            .commit();
                }
                break;
            case R.id.button_add_pet:
                if (editText_pet_name.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "内容不能为空", Toast.LENGTH_SHORT).show();
                } else if (!checkBox_ci.isChecked()&&!checkBox_xiong.isChecked()) {
                    Toast.makeText(getActivity(), "必须选一个宠物性别", Toast.LENGTH_SHORT).show();
                }else if (checkBox_ci.isChecked()&&checkBox_xiong.isChecked()) {
                    Toast.makeText(getActivity(), "只能选择一种性别", Toast.LENGTH_SHORT).show();
                } else if (editText_pet_name.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "内容不能为空", Toast.LENGTH_SHORT).show();
                } else if (editText_pet_zhonglei.getText().toString().equals("")){
                        Toast.makeText(getActivity(), "内容不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    final Pet pet = new Pet();

                    if(imagePath!=null){
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
                                    pet.setPicture(bmobFile);
                                    pet.setName(editText_pet_name.getText().toString());
                                    pet.setAge(Integer.parseInt(editText_pet_age.getText().toString()));
                                    if(checkBox_ci.isChecked()){
                                        pet.setSex("雌性");
                                    }else if(checkBox_xiong.isChecked()){
                                        pet.setSex("雄性");
                                    }
                                    pet.setZhongLei(editText_pet_zhonglei.getText().toString());
                                    pet.setIsFQQ(0);
                                    pet.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {

                                                User user = new User();
                                                user.setNickName(administrator.getNickName());
                                                user.setSignature(administrator.getSignature());
                                                user.setAge(administrator.getAge());
                                                user.setHadPet(1);
                                                user.setDianZan(administrator.getDianZan());
                                                user.setPet(pet);
                                                user.update(""+administrator.getObjectId(), new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        if(e==null){
                                                            Toast.makeText(getActivity(),"您已成功成果添加萌宠 ！2秒后将重新进入刷新重新进入登陆界面 。",Toast.LENGTH_LONG).show();
                                                            new Thread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try{
                                                                        Thread.sleep(2000);
                                                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                                                        startActivity(intent);
                                                                    }catch (InterruptedException e){
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }).start();

                                                        }else{

                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(getActivity(), "添加萌宠失败 ┭┮﹏┭┮   错误代码：" + e.getErrorCode() + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else{

                                }
                            }
                        });
                    }else{
                        pet.setName(editText_pet_name.getText().toString());
                        pet.setAge(Integer.parseInt(editText_pet_age.getText().toString()));
                        if(checkBox_ci.isChecked()){
                            pet.setSex("雌性");
                        }else if(checkBox_xiong.isChecked()){
                            pet.setSex("雄性");
                        }
                        pet.setZhongLei(editText_pet_zhonglei.getText().toString());
                        pet.setIsFQQ(0);
                        pet.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(getActivity(), "您已成功成果添加萌宠 ！但未上传照片 ！", Toast.LENGTH_SHORT).show();
                                    User user = new User();
                                    user.setNickName(administrator.getNickName());
                                    user.setSignature(administrator.getSignature());
                                    user.setAge(administrator.getAge());
                                    user.setHadPet(1);
                                    user.setDianZan(administrator.getDianZan());
                                    user.setPet(pet);
                                    user.update(""+administrator.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e==null){
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Toast.makeText(getActivity(),"您已成功成果添加萌宠 ！2秒后将重新进入刷新重新进入登陆界面 。",Toast.LENGTH_LONG).show();
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
                                                    }
                                                });
                                            }else{

                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getActivity(), "添加萌宠失败 ┭┮﹏┭┮   错误代码：" + e.getErrorCode() + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

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
    }


}
