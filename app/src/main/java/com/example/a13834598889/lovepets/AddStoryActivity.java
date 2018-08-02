package com.example.a13834598889.lovepets;

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
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.JavaBean.Card;
import com.example.a13834598889.lovepets.JavaBean.Record;
import com.example.a13834598889.lovepets.JavaBean.User;
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

public class AddStoryActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton button_add_back;
    private Button button_publish;
    private EditText editText_story_title;
    private EditText editText_story_content;
    private User administrator;
    private ImageView imageView_background;
    private TextView textView_title;
    private boolean hadBackGround = false;
    private String imagePath = "";
    private String groupId = "";
    private String chulihou_image_path;
    private ImageView imageView_back;
    private String title;
    private ProgressBar progressBar;
    public static Intent actionAddStoryActivity(Context context ,String title_text) {
        Intent intent = new Intent(context, AddStoryActivity.class);
        intent.putExtra("title",title_text);
        return intent;
    }

    private static final int CHOOSE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            //让应用主题内容占用系统状态栏的空间,注意:下面两个参数必须一起使用 stable 牢固的
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            //设置状态栏颜色为透明
//            getWindow().setStatusBarColor(Color.WHITE);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setNavigationBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        title = getIntent().getStringExtra("title");

        setContentView(R.layout.activity_add_story);
        groupId = getIntent().getStringExtra("groupId_data");
        administrator = BmobUser.getCurrentUser(User.class);
        progressBar = (ProgressBar) findViewById(R.id.card_upload_progressbar);
        button_add_back = (FloatingActionButton) findViewById(R.id.float_button_add_card_background);
        button_publish = (Button) findViewById(R.id.publish_button);
        editText_story_title = (EditText) findViewById(R.id.edit_text_story_title);
        editText_story_content = (EditText) findViewById(R.id.edit_text_story_content);
        imageView_background = (ImageView) findViewById(R.id.my_little_story_background);
        textView_title = (TextView)findViewById(R.id.text_title_add_story);
        imageView_back = (ImageView)findViewById(R.id.fragment_new_card_back) ;
        button_add_back.setOnClickListener(this);
        button_publish.setOnClickListener(this);
        imageView_back.setOnClickListener(this);
        textView_title.setText(title);
        if(title.equals("新建帖子")){
            Glide.with(getApplication()).load(R.drawable.test2).into(imageView_background);
        }else if(title.equals("新建萌宠生活录")){
            Glide.with(getApplication()).load(R.drawable.test4).into(imageView_background);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_new_card_back:
                finish();
                break;
            case R.id.float_button_add_card_background:
                if (ContextCompat.checkSelfPermission(AddStoryActivity.this, Manifest
                        .permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddStoryActivity.this, new String[]{Manifest.permission.
                            WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
                break;
            case R.id.publish_button:
                if(title.equals("新建帖子")){
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddStoryActivity.this);
                    alertDialog.setTitle("温馨提醒");
                    alertDialog.setMessage("是否确认发表？");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (hadBackGround) {
                                if (editText_story_title.getText().toString().equals("")) {
                                    Toast.makeText(AddStoryActivity.this, "您还未输入标题", Toast.LENGTH_LONG).show();
                                    alertDialog.create().dismiss();
                                }
                                if (editText_story_content.getText().toString().equals("")) {
                                    Toast.makeText(AddStoryActivity.this, "内容不能为空", Toast.LENGTH_LONG).show();
                                    alertDialog.create().dismiss();
                                } else {
                                    progressBar.setVisibility(View.VISIBLE);
                                    imageView_back.setEnabled(false);
                                    button_add_back.setEnabled(false);
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
                                                try{
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    imageView_back.setEnabled(true);
                                                    button_add_back.setEnabled(true);
                                                }catch (Exception c){
                                                    c.printStackTrace();
                                                }
                                                Toast.makeText(AddStoryActivity.this, "您已成功上传您的小故事 , 宠物社区中下拉刷新即可看到您的小故事 。", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(AddStoryActivity.this, "上传失败" + e.getErrorCode() + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    if (hadBackGround) {
                                        long t = System.currentTimeMillis();//获得当前时间的毫秒数
                                        Random rd = new Random(t);//作为种子数传入到Random的构造器中
                                        String targetPath = getExternalCacheDir() + "compressPic" + rd + ".jpg";
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
                                                                try{
                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                    imageView_back.setEnabled(true);
                                                                    button_add_back.setEnabled(true);
                                                                }catch (Exception c){
                                                                    c.printStackTrace();
                                                                }
                                                            } else {
                                                                Toast.makeText(AddStoryActivity.this, "setImage失败" + e.getMessage() + e.getErrorCode(), Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });

                                                } else {
                                                    Toast.makeText(AddStoryActivity.this, "上传背景失败" + e.getMessage() + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                    finish();
                                }
                            } else {
                                if (editText_story_title.getText().toString().equals("")) {
                                    Toast.makeText(AddStoryActivity.this, "您还未输入标题", Toast.LENGTH_LONG).show();
                                } else if (editText_story_content.getText().toString().equals("")) {
                                    Toast.makeText(AddStoryActivity.this, "内容不能为空", Toast.LENGTH_LONG).show();
                                } else {
                                    progressBar.setVisibility(View.VISIBLE);
                                    imageView_back.setEnabled(false);
                                    button_add_back.setEnabled(false);
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
                                                try{
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    imageView_back.setEnabled(true);
                                                    button_add_back.setEnabled(true);
                                                }catch (Exception c){
                                                    c.printStackTrace();
                                                }
                                                Toast.makeText(AddStoryActivity.this, "您已成功上传您的小故事", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(AddStoryActivity.this, "上传失败" + e.getErrorCode() + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    finish();
                                }
                            }
                        }
                    });
                    alertDialog.show();
                }else if(title.equals("新建萌宠生活录")){
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddStoryActivity.this);
                    alertDialog.setTitle("温馨提醒");
                    alertDialog.setMessage("是否确认发表？");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (hadBackGround) {
                                if (editText_story_title.getText().toString().equals("")) {
                                    Toast.makeText(AddStoryActivity.this, "您还未输入标题", Toast.LENGTH_LONG).show();
                                    alertDialog.create().dismiss();
                                }
                                if (editText_story_content.getText().toString().equals("")) {
                                    Toast.makeText(AddStoryActivity.this, "内容不能为空", Toast.LENGTH_LONG).show();
                                    alertDialog.create().dismiss();
                                } else {
                                    progressBar.setVisibility(View.VISIBLE);
                                    imageView_back.setEnabled(false);
                                    button_add_back.setEnabled(false);
                                    final Record record = new Record();
                                    record.setContext(editText_story_content.getText().toString());
                                    record.setTitle(editText_story_title.getText().toString());
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                    record.setTime(df.format(new Date()));
                                    record.setUser(administrator);
                                    record.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                try{
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    imageView_back.setEnabled(true);
                                                    button_add_back.setEnabled(true);
                                                }catch (Exception c){
                                                    c.printStackTrace();
                                                }
                                                Toast.makeText(AddStoryActivity.this, "您已成功上传宠物生活录 , 生活录中下拉刷新即可看到您的记录 。", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(AddStoryActivity.this, "上传失败" + e.getErrorCode() + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    if (hadBackGround) {
                                        long t = System.currentTimeMillis();//获得当前时间的毫秒数
                                        Random rd = new Random(t);//作为种子数传入到Random的构造器中
                                        String targetPath = getExternalCacheDir() + "compressPic" + rd + ".jpg";
                                        chulihou_image_path = PictureUtil.compressImage(imagePath, targetPath, 30);
                                        File file = new File(chulihou_image_path);
                                        final BmobFile bmobFile = new BmobFile(file);
                                        bmobFile.uploadblock(new UploadFileListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    record.setImage_back(bmobFile);
                                                    record.update(record.getObjectId(), new UpdateListener() {
                                                        @Override
                                                        public void done(BmobException e) {
                                                            if (e == null) {
                                                                try{
                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                    imageView_back.setEnabled(true);
                                                                    button_add_back.setEnabled(true);
                                                                }catch (Exception c){
                                                                    c.printStackTrace();
                                                                }

                                                            } else {
                                                                Toast.makeText(AddStoryActivity.this, "setImage失败" + e.getMessage() + e.getErrorCode(), Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });

                                                } else {
                                                    Toast.makeText(AddStoryActivity.this, "上传背景失败" + e.getMessage() + e.getErrorCode(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                    finish();
                                }
                            } else {
                                if (editText_story_title.getText().toString().equals("")) {
                                    Toast.makeText(AddStoryActivity.this, "您还未输入标题", Toast.LENGTH_LONG).show();
                                } else if (editText_story_content.getText().toString().equals("")) {
                                    Toast.makeText(AddStoryActivity.this, "内容不能为空", Toast.LENGTH_LONG).show();
                                } else {
                                    progressBar.setVisibility(View.VISIBLE);
                                    imageView_back.setEnabled(false);
                                    button_add_back.setEnabled(false);
                                    final Record record = new Record();
                                    record.setContext(editText_story_content.getText().toString());
                                    record.setTitle(editText_story_title.getText().toString());
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                    record.setTime(df.format(new Date()));
                                    record.setUser(administrator);
                                    record.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                try{
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    imageView_back.setEnabled(true);
                                                    button_add_back.setEnabled(true);
                                                }catch (Exception c){
                                                    c.printStackTrace();
                                                }
                                                Toast.makeText(AddStoryActivity.this, "您已成功上传宠物生活录", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(AddStoryActivity.this, "上传失败" + e.getErrorCode() + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    finish();
                                }
                            }
                        }
                    });
                    alertDialog.show();
                }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        Log.d("MainActivity", "执行到handleImageBeforeKitKat方法了");
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        if (imagePath != null) {
            displayImage(imagePath);
        } else {
            Log.d("MainActivity", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        }

    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        Uri uri;
        uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
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
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
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
            Toast.makeText(AddStoryActivity.this, "" + imagePath000, Toast.LENGTH_LONG).show();
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath000);
            imageView_background.setImageBitmap(bitmap);
            hadBackGround = true;
        } else {
            Toast.makeText(this, "Filed to get image .", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(editText_story_title.getText().toString()) ||
                !TextUtils.isEmpty(editText_story_content.getText().toString())) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddStoryActivity.this);
            alertDialog.setTitle("温馨提醒");
            alertDialog.setMessage("未保存编辑信息，是否放弃编辑发表 ？");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            alertDialog.show();
        } else{
            super.onBackPressed();
        }
    }
}
