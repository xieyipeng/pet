package com.example.a13834598889.lovepets.Fragments_Share;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
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
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.JavaBean.Card;
import com.example.a13834598889.lovepets.JavaBean.User;
import com.example.a13834598889.lovepets.R;
import com.example.a13834598889.lovepets.Tool.PictureUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 13834598889 on 2018/5/9.
 */

public class Fragment_add_share extends Fragment implements View.OnClickListener{
    private FloatingActionButton button_add_back;
    private Button button_publish;
    private EditText editText_story_title;
    private EditText editText_story_content;
    private User administrator;
    private ImageView imageView_background;
    private boolean hadBackGround = false;
    private String imagePath = "";
    private String chulihou_image_path;
    private ImageView imageView_back;
    private View view;
    private NestedScrollView scrollView;
    private static final int CHOOSE_PHOTO = 2;

    public static Fragment_add_share newInstance(){
        Fragment_add_share fragment_add_share = new Fragment_add_share();
        return fragment_add_share;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_add_story,container,false);
        preView();
        return view;
    }

    private void preView(){
        administrator = BmobUser.getCurrentUser(User.class);
        button_add_back = (FloatingActionButton) view.findViewById(R.id.float_button_add_card_background);
        button_publish = (Button) view.findViewById(R.id.publish_button);
        editText_story_title = (EditText) view.findViewById(R.id.edit_text_story_title);
        editText_story_content = (EditText)view. findViewById(R.id.edit_text_story_content);
        imageView_background = (ImageView) view.findViewById(R.id.my_little_story_background);
        Glide.with(getActivity().getApplication()).load(R.drawable.test3).into(imageView_background);
        imageView_back = (ImageView)view.findViewById(R.id.fragment_new_card_back) ;
        scrollView = (NestedScrollView)view.findViewById(R.id.layout_2);
        scrollView.setOnClickListener(this);
        imageView_back.setOnClickListener(this);
        button_add_back.setOnClickListener(this);
        button_publish.setOnClickListener(this);
        imageView_background.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_little_story_background:
                break;
            case R.id.layout_2:
                break;

            case R.id.fragment_new_card_back:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if(fragmentManager.findFragmentByTag("fragment_add_share") !=null){
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("fragment_add_share"))
                            .remove(fragmentManager.findFragmentByTag("fragment_add_share"))
                            .show(fragmentManager.findFragmentByTag("fragment_share"))
                            .commit();
                }
                break;
            case R.id.float_button_add_card_background:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest
                        .permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.
                            WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
                break;
            case R.id.publish_button:
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("温馨提醒");
                alertDialog.setMessage("是否确认发表？");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (hadBackGround) {
                            if (editText_story_title.getText().toString().equals("")) {
                                Toast.makeText(getActivity(), "您还未输入标题", Toast.LENGTH_LONG).show();
                                alertDialog.create().dismiss();
                            }
                            if (editText_story_content.getText().toString().equals("")) {
                                Toast.makeText(getActivity(), "内容不能为空", Toast.LENGTH_LONG).show();
                                alertDialog.create().dismiss();
                            } else {
                                final Card card = new Card();
                                card.setContext(editText_story_content.getText().toString());
                                card.setTitle(editText_story_title.getText().toString());
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                card.setTime(df.format(new Date()));
                                card.setUser(administrator);
                                card.setIsJScard(0);
                                card.setNum_dianzan(0);
                                card.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(getActivity(), "您已成功上传您的小故事 , 宠物社区中下拉刷新即可看到您的小故事 。", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "上传失败" + e.getErrorCode() + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                if (hadBackGround) {
                                    long t = System.currentTimeMillis();//获得当前时间的毫秒数
                                    Random rd = new Random(t);//作为种子数传入到Random的构造器中
                                    String targetPath = getActivity().getExternalCacheDir() + "compressPic" + rd + ".jpg";
                                    chulihou_image_path = PictureUtil.compressImage(imagePath, targetPath, 30);
                                    File file = new File(chulihou_image_path);
                                    final BmobFile bmobFile = new BmobFile(file);
                                    bmobFile.uploadblock(new UploadFileListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                card.setPicture(bmobFile);
                                                card.update(card.getObjectId(), new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        if (e == null) {

                                                        } else {
                                                            Toast.makeText(getActivity(), "setImage失败" + e.getMessage() + e.getErrorCode(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(getActivity(), "上传背景失败" + e.getMessage() + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                if(fragmentManager.findFragmentByTag("fragment_add_share") !=null){
                                    fragmentManager.beginTransaction()
                                            .hide(fragmentManager.findFragmentByTag("fragment_add_share"))
                                            .remove(fragmentManager.findFragmentByTag("fragment_add_share"))
                                            .show(fragmentManager.findFragmentByTag("fragment_share"))
                                            .commit();
                                }
                            }
                        } else {
                            if (editText_story_title.getText().toString().equals("")) {
                                Toast.makeText(getActivity(), "您还未输入标题", Toast.LENGTH_LONG).show();
                            } else if (editText_story_content.getText().toString().equals("")) {
                                Toast.makeText(getActivity(), "内容不能为空", Toast.LENGTH_LONG).show();
                            } else {
                                final Card card = new Card();
                                card.setContext(editText_story_content.getText().toString());
                                card.setTitle(editText_story_title.getText().toString());
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                card.setTime(df.format(new Date()));
                                card.setUser(administrator);
                                card.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(getActivity(), "您已成功上传您的小故事", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "上传失败" + e.getErrorCode() + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                if(fragmentManager.findFragmentByTag("fragment_add_share") !=null){
                                    fragmentManager.beginTransaction()
                                            .hide(fragmentManager.findFragmentByTag("fragment_add_share"))
                                            .remove(fragmentManager.findFragmentByTag("fragment_add_share"))
                                            .show(fragmentManager.findFragmentByTag("fragment_share"))
                                            .commit();
                                }
                            }
                        }
                    }
                });
                alertDialog.show();
                break;
            default:
                break;
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                }
            default:
                break;
        }
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        if (imagePath != null) {
            displayImage(imagePath);
        } else {
        }

    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        Uri uri;
        uri = data.getData();
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Log.d("test", "getImagePath: " + path);
            }
            cursor.close();
        }
        return path;

    }

    private void displayImage(String imagePath000) {
        if (imagePath != null) {
            Toast.makeText(getActivity(), "" + imagePath000, Toast.LENGTH_LONG).show();
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath000);
            imageView_background.setImageBitmap(bitmap);
            hadBackGround = true;
        } else {
            Toast.makeText(getActivity(), "Filed to get image .", Toast.LENGTH_SHORT).show();
        }

    }

}
