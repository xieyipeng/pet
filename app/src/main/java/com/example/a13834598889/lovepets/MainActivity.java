package com.example.a13834598889.lovepets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.a13834598889.lovepets.Fragments_Mine.Fragment_mine;
import com.example.a13834598889.lovepets.Fragments_Pet.Fragment_pet;
import com.example.a13834598889.lovepets.Fragments_Serch.Fragment_serch;
import com.example.a13834598889.lovepets.Fragments_Share.Fragment_share;
import com.example.a13834598889.lovepets.JavaBean.User;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.util.NIMUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;

    private Fragment save_fragment_mine;
    private Fragment save_fragment_serch;
    private Fragment save_fragment_share;
    private Fragment save_fragment_pet;
    private Fragment fragment = null;
    private FragmentManager fragmentManager;
    private User administrator;
    private String image_download_path = "";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    boolean isRember;

    private  Observer<List<IMMessage>> incomingMessageObserver;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            find_jude();
            switch (item.getItemId()) {
                case R.id.navigation_share:
                    fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out).hide(fragment).commit();
                    if(save_fragment_share == null){
                        fragment = Fragment_share.newInstance(image_download_path);
                        save_fragment_share = fragment;
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                                .add(R.id.fragment_container,fragment,"fragment_share")
                                .commit();
                    }else{
                        fragment = save_fragment_share;
                    }
                    fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out).show(fragment).commit();
                    return true;
                case R.id.navigation_serch:
                    fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out).hide(fragment).commit();
                    if(save_fragment_serch == null){
                        fragment = Fragment_serch.newInstance(image_download_path);
                        save_fragment_serch = fragment;
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                                .add(R.id.fragment_container,fragment,"fragment_serch")
                                .commit();
                    }else{
                        fragment = save_fragment_serch;
                    }
                    fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out).show(fragment).commit();
                    return true;
                case R.id.navigation_pet:
                    fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out).hide(fragment).commit();
                    if(save_fragment_pet == null){
                        fragment = Fragment_pet.newInstance(image_download_path);
                        save_fragment_pet = fragment;
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                                .add(R.id.fragment_container,fragment,"fragment_pet")
                                .commit();
                    }else{
                        fragment = save_fragment_pet;
                    }
                    fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out).show(fragment).commit();
                    return true;
                case R.id.navigation_mine:
                    fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out).hide(fragment).commit();
                    if(save_fragment_mine == null){
                        fragment = Fragment_mine.newInstance(image_download_path);
                        save_fragment_mine = fragment;
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                                .add(R.id.fragment_container,fragment,"fragment_account")
                                .commit();
                    }else{
                        fragment = save_fragment_mine;
                    }
                    fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out).show(fragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        administrator = BmobUser.getCurrentUser(User.class);
        getAppCacheDir(MainActivity.this);
        if(administrator.getPicture()!=null){
            downloadFile_picture(administrator.getPicture());
        }
////////////////////////////////////////////////////////////////////////
        NIMClient.init(this, loginInfo(), options());


        //信息接受监听
        incomingMessageObserver =
                new Observer<List<IMMessage>>() {
                    @Override
                    public void onEvent(List<IMMessage> messages) {
                        Toast.makeText(MainActivity.this,"收到新信息",Toast.LENGTH_LONG).show();
//                        // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
//                        Toast.makeText(MainActivity.this,""+messages.get(messages.size()-1),Toast.LENGTH_LONG).show();
                    }
                };
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);

        //信息接受监听

        doLogin();

        ////////////////////////////////////////////////////////////////////////

        pref= PreferenceManager.getDefaultSharedPreferences(this);
        isRember=pref.getBoolean("image_path",false);

        try{
            File sdCard = Environment.getExternalStorageDirectory();
            File path = new File(sdCard, "LovePets");
            path.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }

        preView();

    }


    private void preView(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setNavigationBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked}
        };
        int[] colors = new int[]{ContextCompat.getColor(this, R.color.toolbar_and_menu_color),
                ContextCompat.getColor(this, R.color.testColor)
        };
        ColorStateList colorStateList = new ColorStateList(states, colors);
        navigation.setItemTextColor(colorStateList);
        navigation.setItemIconTintList(colorStateList);
        fragmentManager = getSupportFragmentManager();
        fragment = Fragment_share.newInstance(image_download_path);
        save_fragment_share = fragment;
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                .add(R.id.fragment_container,fragment,"fragment_share")
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void find_jude(){
        show_view("friends_fragment");
        show_view("card_fragment");
        show_view("account_fragment");
        show_view("change_fragment");
        show_view("add_pet_fragment");
        show_view("fragment_add_share");
        show_view("fragment_see_card");
        show_view("pet_life_fragment");
        show_view("change_pet_fragment");
        show_view("im_fragment");
        show_view("web_fragment");
        show_view("video_fragment");
        show_view("mine_location_fragment");
        show_view("alarm_fragment");

    }

    private void show_view(String fragment_tag){
        if(fragmentManager.findFragmentByTag(fragment_tag) !=null){
            fragmentManager.beginTransaction()
                    .hide(fragmentManager.findFragmentByTag(fragment_tag))
                    .remove(fragmentManager.findFragmentByTag(fragment_tag))
                    .commit();
        }
    }

    private void downloadFile_picture(BmobFile file) {
        file.download(new DownloadFileListener() {
            @Override
            public void done(final String s, BmobException e) {
                if(e==null){
                    editor=pref.edit();
                    editor.putBoolean("image_path",true);
                    editor.putString("image",s);
                    editor.apply();
                    image_download_path = s;
                    if(s!=null){
                        image_download_path = s;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                image_download_path = s;
                            }
                        });
                    }else {
                    }
                }

            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });
    }









    // 如果返回值为 null，则全部使用默认参数。
    private SDKOptions options() {
        SDKOptions options = new SDKOptions();
        return options;
    }

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    private LoginInfo loginInfo() {
        return null;
    }

    /**
     * 配置 APP 保存图片/语音/文件/log等数据的目录
     * 这里示例用SD卡的应用扩展存储目录
     */
    static String getAppCacheDir(Context context) {
        String storageRootPath = null;
        try {
            // SD卡应用扩展存储区(APP卸载后，该目录下被清除，用户也可以在设置界面中手动清除)，请根据APP对数据缓存的重要性及生命周期来决定是否采用此缓存目录.
            // 该存储区在API 19以上不需要写权限，即可配置 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="18"/>
            if (context.getExternalCacheDir() != null) {
                storageRootPath = context.getExternalCacheDir().getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(storageRootPath)) {
            // SD卡应用公共存储区(APP卸载后，该目录不会被清除，下载安装APP后，缓存数据依然可以被加载。SDK默认使用此目录)，该存储区域需要写权限!
            storageRootPath = Environment.getExternalStorageDirectory()+"";
        }

        return storageRootPath;
    }


    public void doLogin() {
//        Toast.makeText(MainActivity.this,administrator.getIm_id()+""+administrator.getIm_token()+"",Toast.LENGTH_LONG).show();
        LoginInfo info = new LoginInfo(administrator.getIm_id()+"",administrator.getIm_token()+""); // config...
        NIMClient.getService(AuthService.class).login(info);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NIMClient.getService(AuthService.class).logout();
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, false);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("提示信息 ：")
                .setMessage("是否确认返回登录界面 ？\n 如果需要返回上一层，可点击右上角返回键。")
                .setCancelable(true);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.create().dismiss();
                finish();

            }
        });
        dialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.create().dismiss();
            }
        });
        dialog.show();
    }

}
