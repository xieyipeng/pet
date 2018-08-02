package com.example.a13834598889.lovepets;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a13834598889.lovepets.JavaBean.Decode_json;
import com.example.a13834598889.lovepets.JavaBean.DianZan;
import com.example.a13834598889.lovepets.JavaBean.User;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterActivity extends AppCompatActivity {
    private EditText register_account;
    private EditText register_phone;
    private EditText register_emile;
    private EditText register_passWord;
    private Button register_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            //让应用主题内容占用系统状态栏的空间,注意:下面两个参数必须一起使用 stable 牢固的
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //设置状态栏颜色为透明
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        super.onCreate(savedInstanceState);
        final DianZan dianZan=new DianZan();
        dianZan.setmDianZanShu(new Integer(0));
        dianZan.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
            }
        });
        setContentView(R.layout.activity_register);
        register_account=(EditText)findViewById(R.id.register_name_editText);
        register_phone=(EditText)findViewById(R.id.register_phone_editText);
        register_emile=(EditText)findViewById(R.id.register_emile_editText);
        register_passWord=(EditText)findViewById(R.id.register_mima_editText);
        register_button=(Button)findViewById(R.id.register_account_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialog=new AlertDialog.Builder(RegisterActivity.this);
                dialog.setTitle("提示信息 ：")
                        .setMessage("注册后将使用注册账号登陆，且注册账号将不可修改。\n 是否确认注册 ？")
                        .setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        if(register_account.getText().toString().equals("")||register_emile.
                                getText().toString().equals("")||register_passWord.getText().toString
                                ().equals("")||register_phone.getText().toString().equals("")){
                            Toast.makeText(RegisterActivity.this,"所有注册信息均不能为空",Toast.LENGTH_SHORT).show();
                            dialog.create().dismiss();
                        }else if(register_account.getText().toString().length()<6||register_passWord.getText().length()<6){
                            Toast.makeText(RegisterActivity.this,"账号或密码长度不能小于六位",Toast.LENGTH_SHORT).show();
                            dialog.create().dismiss();
                        }else{

                            //实现网上注册:
                            User user=new User();
                            user.setUsername(register_account.getText().toString());
                            user.setEmail(register_emile.getText().toString());
                            user.setPassword(register_passWord.getText().toString());
                            user.setMobilePhoneNumber(register_phone.getText().toString());
                            user.setDianZan(dianZan);
                            user.setNickName("Century");
                            user.setSignature("这是一只小懒猪。");
                            user.setHadPet(0);
                            user.signUp(new SaveListener<User>() {
                                @Override
                                public void done(final User user, BmobException e) {
                                    if(e==null){


                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String [] data= Decode_json.getLOGIN(user.getObjectId()+"");
                                                String im_id = data[0];
                                                String im_token = data[1];
                                                user.setIm_id(im_id);
                                                user.setIm_token(im_token);
                                                user.update(""+user.getObjectId(), new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e){
                                                        if (e !=null){
                                                            Toast.makeText(RegisterActivity.this,"GGLE!!!!!",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }).start();



                                        Toast.makeText(RegisterActivity.this,"注册成功"+user.getObjectId(),Toast.LENGTH_SHORT).show();
                                        AlertDialog.Builder dialog=new AlertDialog.Builder(RegisterActivity.this);
                                        dialog.setTitle("温馨提醒")
                                                .setMessage("您已成功注册账号，请使用账号和密码进行登陆。")
                                                .setCancelable(false);
                                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                            }
                                        });
                                        dialog.show();
                                    }else{
                                        Toast.makeText(RegisterActivity.this,"注册失败"+e.getMessage()+"Error code:"+e.getErrorCode(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }


                    }
                });
                dialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();

            }
        });
    }

}
