package com.example.a13834598889.lovepets;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.JavaBean.User;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import java.util.prefs.Preferences;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {
    private EditText editText_account;
    private EditText editText_passWord;
    private Button button_login;
    private CheckBox checkBox_remberPassWord;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Button register_login_button;
    private CircleImageView circleImageView_login;
    boolean isRember;
    boolean isRember1;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.login_layout);
        BmobConfig config=new BmobConfig.Builder(this)
                .setApplicationId("6b774f422fc1322846e3ef04da03e05c")
                .setConnectTimeout(30)
                .setUploadBlockSize(1024*1024)
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);


        preView();



        if(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.READ_PHONE_STATE},1);

        }else{
            if(ContextCompat.checkSelfPermission(LoginActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},3);

            }else{
                if(ContextCompat.checkSelfPermission(LoginActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},4);

                }else{
                    if(ContextCompat.checkSelfPermission(LoginActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                    }else{
                        Toast.makeText(this, "请给定位权限", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(LoginActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);

                    }
                }else {
                    Toast.makeText(LoginActivity.this,"拒绝了拨打电话权限权限",Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(LoginActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},4);

                    }
                }else {
                    Toast.makeText(LoginActivity.this,"拒绝了存储权限",Toast.LENGTH_SHORT).show();
                }
            case 3:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(LoginActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},4);

                    }
                }else {
                    Toast.makeText(LoginActivity.this,"拒绝了定位权限",Toast.LENGTH_SHORT).show();
                }
            case 4:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                }else {
                    Toast.makeText(LoginActivity.this,"拒绝了定位权限",Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }



    private void login(){
        progressBar.setVisibility(View.VISIBLE);
        button_login.setEnabled(false);
        register_login_button.setEnabled(false);
        final String account0=editText_account.getText().toString();
        final String passWord0=editText_passWord.getText().toString();
        User user=new User();
        user.setUsername(account0);
        user.setPassword(passWord0);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    editor=pref.edit();
                    if(checkBox_remberPassWord.isChecked()){
                        editor.putBoolean("rember_passWord",true);
                        editor.putString("account",account0);
                        editor.putString("password",passWord0);
                    }else {
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,""+e.getErrorCode()+e.getMessage(),Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.INVISIBLE);
                    button_login.setEnabled(true);
                    register_login_button.setEnabled(true);
                }
            }
        });
    }

    private void preView(){
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        isRember=pref.getBoolean("rember_passWord",false);
        isRember1 = pref.getBoolean("image_path",false);

        progressBar = (ProgressBar)findViewById(R.id.main_progress);
        editText_account=(EditText)findViewById(R.id.login_account_Edit_text);
        editText_passWord=(EditText)findViewById(R.id.login_passWord_Edit_text);
        button_login=(Button)findViewById(R.id.login_button);
        register_login_button=(Button)findViewById(R.id.register_button);
        checkBox_remberPassWord=(CheckBox)findViewById(R.id.rember_passWord_Check_Box);
        circleImageView_login = (CircleImageView)findViewById(R.id.circleImageView_login);
        if(isRember1){
            String path = pref.getString("image","");
            Glide.with(getApplication()).load(path).into(circleImageView_login);
        }


        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        if(isRember){
            String account=pref.getString("account","");
            String passWord=pref.getString("password","");
            editText_account.setText(account);
            editText_passWord.setText(passWord);
            checkBox_remberPassWord.setChecked(true);
        }
        register_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }






    @Override
    protected void onRestart() {
        preView();
        super.onRestart();
        preView();
    }

    @Override
    protected void onResume() {
        preView();
        super.onResume();
        preView();
        progressBar.setVisibility(View.INVISIBLE);
        button_login.setEnabled(true);
        register_login_button.setEnabled(true);
    }

}

















