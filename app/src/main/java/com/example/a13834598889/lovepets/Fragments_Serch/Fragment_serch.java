package com.example.a13834598889.lovepets.Fragments_Serch;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.Glide;
import com.example.a13834598889.lovepets.JavaBean.Pet;
import com.example.a13834598889.lovepets.JavaBean.User;
import com.example.a13834598889.lovepets.R;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.a13834598889.lovepets.Fragments_Serch.overlayutil.PoiOverlay;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by 13834598889 on 2018/5/7.
 */



public class Fragment_serch extends Fragment  implements SensorEventListener,
        OnGetGeoCoderResultListener,OnGetPoiSearchResultListener,
        OnGetSuggestionResultListener{
    private View view;
    private CircleImageView circleImageView;

    private String picture_path;



    ///////////////////////////////////////////

    private List<String> list_data = new ArrayList<>();


    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private LocationMode mCurrentMode;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;

    private Button button_serch;

    private LinearLayout linearLayout_map_info;




    private InfoWindow mInfoWindow;
    MapView mMapView;
    BaiduMap mBaiduMap;

    BitmapDescriptor mCurrentMarker;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private float direction;

    private EditText editText_serch_zhonglei;
    private ImageView imageView_serch_around;

    //地理检索
    GeoCoder mSearch = null;

    //poi
    private PoiSearch poiSearch = null;
    private SuggestionSearch suggestionSearch = null;
    private List<String> suggest;

    private EditText editCity = null;
    private AutoCompleteTextView keyWorldsView = null;
    private ArrayAdapter<String> sugAdapter = null;
    private int loadIndex = 0;
    int searchType = 0;
    LatLng center = new LatLng(39.92235, 116.380338);
    LatLng southwest = new LatLng( 39.92235, 116.380338 );
    LatLng northeast = new LatLng( 39.947246, 116.414977);
    LatLngBounds searchbound = new LatLngBounds.Builder().include(southwest).include(northeast).build();

    int radius = 500;
    private ProgressDialog dialog;

    //获取List<String>


    ////////////////////////////////////////////


    public static Fragment_serch newInstance(String image_path){
        Fragment_serch fragment_serch = new Fragment_serch();
        fragment_serch.picture_path = image_path;
        return fragment_serch;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        view = inflater.inflate(R.layout.fragment_order,container,false);
        myListener = new MyLocationListenner();
        preView();
        addData();
        return view;
    }

    private void preView(){



        button_serch = (Button) view.findViewById(R.id.serch_button);
        circleImageView = (CircleImageView) view.findViewById(R.id.circleImageView_mine22);
        imageView_serch_around = (ImageView) view.findViewById(R.id.image_button_serch_around);
        editText_serch_zhonglei = (EditText)view.findViewById(R.id.edit_serch_zhonglei);
        if(picture_path.equals("")){
            Glide.with(getActivity().getApplication()).load(R.drawable.test_touxiang3).into(circleImageView);
        }else{
            Glide.with(getActivity().getApplication()).load(picture_path).into(circleImageView);
        }




        imageView_serch_around.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), ""+list_data.size(), Toast.LENGTH_SHORT).show();
                if(editText_serch_zhonglei.getText().toString().equals("")){
                    initoverlay(list_data,"all");
                }else{
                    initoverlay(list_data,editText_serch_zhonglei.getText().toString());
                }
            }
        });




        ///////////////////////////////////////

        linearLayout_map_info = (LinearLayout)view.findViewById(R.id.layout_map_info);


        button_serch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchButtonProcess();
                initoverlay(list_data,"all");
            }
        });

        Button buttonAdd = (Button)view.findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(BmobUser.getCurrentUser(User.class).getHadPet() == 0){

                    final AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
                    dialog.setTitle("提示信息 ：")
                            .setMessage("      " +
                                    "您还没添加宠物呢 ! 请先添加您的宠物 。")
                            .setCancelable(true);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.create().dismiss();
                        }
                    });
                    dialog.show();
                }else{
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                            .add(R.id.fragment_container, Fragment_location_mine.newInstance(picture_path),"mine_location_fragment")
                            .commit();
                }
            }
        });


        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = LocationMode.NORMAL;

        // 地图初始化
        mMapView = (MapView) view.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

//        // 开启定位图层
         mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                // map view 销毁后不在处理新接收的位置
                if (location == null || mMapView == null) {
                    return;
                }
                mCurrentLat = location.getLatitude();
                mCurrentLon = location.getLongitude();
                mCurrentAccracy = location.getRadius();
                locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(mCurrentDirection).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);
                mBaiduMap.setMyLocationEnabled(true);
                if (isFirstLoc) {
                    isFirstLoc = false;
                    LatLng ll = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
                mBaiduMap
                        .setMyLocationConfiguration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.overlook(0);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll");
        option.setScanSpan(1);
        mLocClient.setLocOption(option);
        mLocClient.start();
        // 标记点和infoview
//        initoverlay(); //---------------------------------------------------->


        //地理编码
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);

        //poi

        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);

        // 初始化建议搜索模块，注册建议搜索事件监听
        suggestionSearch = SuggestionSearch.newInstance();
        suggestionSearch.setOnGetSuggestionResultListener(this);
        keyWorldsView = (AutoCompleteTextView)view.findViewById(R.id.geocodekey);
        sugAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line);
        keyWorldsView.setAdapter(sugAdapter);
        keyWorldsView.setThreshold(1);


        editCity = (EditText) view.findViewById(R.id.city);

        keyWorldsView.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                if (cs.length() <= 0) {
                    return;
                }

                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                suggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(cs.toString()).city(editCity.getText().toString()));
            }
        });

        Button dw_button = (Button)view.findViewById(R.id.dw_bt);
        dw_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng ll = new LatLng(mCurrentLat, mCurrentLon);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(ll));
            }

        });
    }


    private void addData(){
        BmobQuery<User> bmobQuery = new BmobQuery<User>();

        bmobQuery.addQueryKeys("data");
        bmobQuery.setLimit(200);
        bmobQuery.addWhereNotEqualTo("data","");
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if(e==null){
                 try{
                     List<String> data = new ArrayList<>();
                     Toast.makeText(getActivity(), "共有 " + object.size() +" 位朋友上传了宠物位置及种类", Toast.LENGTH_SHORT).show();
                     //注意：这里的Person对象中只有指定列的数据。
                     for(User obj:object){
                         list_data.add(obj.getData());
                         data.add(obj.getData());
                     }
                     initoverlay(data,"all");
                 }catch (Exception c){
                     c.printStackTrace();
                 }
                }else{

                }
            }
        });
    }


    private void requestLocation(){
        mLocClient.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0){
                    for (int result:grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(getActivity(), "请同意权限", Toast.LENGTH_SHORT).show();
//                            finish();-----------------------------------------------------------
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    Toast.makeText(getActivity(), "发生未知错误", Toast.LENGTH_SHORT).show();
//                    finish();----------------------------------------------------------------
                }
                break;
            default:

        }
    }





    public void initoverlay(List<String> list_data,String name) {

        mBaiduMap.clear();
        //添加marker
        //添加marker


        for (String i:list_data) {



            if(i!=null){

                String s[] = i.split(",");
                final String a = s[0];  //
                final String b = s[1];
                final String c = s[2];
                final String d = s[3];
                final String e = s[4];


                final LatLng ll = new LatLng(Double.valueOf(a), Double.valueOf(b));
                BitmapDescriptor bitmapDescriptor1 = BitmapDescriptorFactory.fromResource(
                        R.drawable.l1);
                BitmapDescriptor bitmapDescriptor2 = BitmapDescriptorFactory.fromResource(
                        R.drawable.l2);
                BitmapDescriptor bitmapDescriptor3 = BitmapDescriptorFactory.fromResource(
                        R.drawable.l3);
                BitmapDescriptor bitmapDescriptor4 = BitmapDescriptorFactory.fromResource(
                        R.drawable.l4);
                BitmapDescriptor bitmapDescriptor5 = BitmapDescriptorFactory.fromResource(
                        R.drawable.l5);
                BitmapDescriptor bitmapDescriptor6 = BitmapDescriptorFactory.fromResource(
                        R.drawable.l6);
                BitmapDescriptor bitmapDescriptor7 = BitmapDescriptorFactory.fromResource(
                        R.drawable.l7);
                BitmapDescriptor bitmapDescriptorfq = BitmapDescriptorFactory.fromResource(
                        R.drawable.tubiao_fqq);

                if(name.equals("all")){

                    if(e.equals("y")){
                        mark(ll,bitmapDescriptorfq,c,d);
                    }else{
                        if(c.equals("猫咪")){
                            mark(ll,bitmapDescriptor1,c,d);
                        }
                        else if(c.equals("狗狗")){
                            mark(ll,bitmapDescriptor2,c,d);
                        }
                        else if(c.equals("小羊驼")){
                            mark(ll,bitmapDescriptor3,c,d);
                        }
                        else if(c.equals("小胖猪")){
                            mark(ll,bitmapDescriptor4,c,d);
                        }
                        else if(c.equals("兔子")){
                            mark(ll,bitmapDescriptor5,c,d);
                        }
                        else if(c.equals("小乌龟")){
                            mark(ll,bitmapDescriptor6,c,d);
                        }
                        else if(c.equals("仓鼠")){
                            mark(ll,bitmapDescriptor7,c,d);
                        }else{
                            mark(ll,bitmapDescriptor7,c,d);
                        }
                    }

                }else if(name.contains("猫")&&c.contains("猫")){
                    if (e.equals("y")){
                        mark(ll,bitmapDescriptorfq,c,d);
                    }else{
                        mark(ll,bitmapDescriptor1,c,d);
                    }
                }else if(name.contains("狗")&&c.contains("狗")){
                    if (e.equals("y")){
                        mark(ll,bitmapDescriptorfq,c,d);
                    }else{
                        mark(ll,bitmapDescriptor2,c,d);
                    }
                }else if(name.contains("羊")&&c.contains("羊")){
                    if (e.equals("y")){
                        mark(ll,bitmapDescriptorfq,c,d);
                    }else{
                        mark(ll,bitmapDescriptor3,c,d);
                    }
                }else if(name.contains("猪")&&c.contains("猪")){
                    if (e.equals("y")){
                        mark(ll,bitmapDescriptorfq,c,d);
                    }else{
                        mark(ll,bitmapDescriptor4,c,d);
                    }
                }else if(name.contains("兔")&&c.contains("兔")){
                    if (e.equals("y")){
                        mark(ll,bitmapDescriptorfq,c,d);
                    }else{
                        mark(ll,bitmapDescriptor5,c,d);
                    }

                }else if(name.contains("乌龟")&&c.contains("乌龟")){
                    if (e.equals("y")){
                        mark(ll,bitmapDescriptorfq,c,d);
                    }else{
                        mark(ll,bitmapDescriptor6,c,d);
                    }
                }else if(name.contains("仓鼠")&&c.contains("仓鼠")){
                    if (e.equals("y")){
                        mark(ll,bitmapDescriptorfq,c,d);
                    }else{
                        mark(ll,bitmapDescriptor7,c,d);
                    }
                }


                BaiduMap.OnMarkerClickListener onMarkerClickListener = new
                        BaiduMap.OnMarkerClickListener(){

                            @Override
                            public boolean onMarkerClick(final Marker marker) {
                                String zhonglei = (String)marker.getExtraInfo().get("c");
                                String id = (String)marker.getExtraInfo().get("d");

                                LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
                                final View view1 = inflater.inflate(R.layout.a, null);
                                final CircleImageView circleImageView_map = (CircleImageView) view1.findViewById(R.id.map_image_1);
                                final CircleImageView imageView = (CircleImageView) view.findViewById(R.id.image_view);



                                BmobQuery<User> query = new BmobQuery<>();
                                query.getObject(id+"", new QueryListener<User>() {

                                    @Override
                                    public void done(final User object, BmobException e) {
                                        if(e==null){

                                            try{

                                                imageView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent();
                                                        intent.setAction(Intent.ACTION_DIAL);
                                                        intent.setData(Uri.parse("tel:"+object.getMobilePhoneNumber().toString()));
                                                        startActivity(intent);
                                                    }
                                                });


                                                BmobQuery<Pet> query1 = new BmobQuery<>();
                                                query1.getObject(object.getPet().getObjectId() + "", new QueryListener<Pet>() {
                                                    @Override
                                                    public void done(Pet pet, BmobException e) {

                                                        try{


                                                            if(pet.getPicture()!=null){
                                                                downloadFile_picture(pet.getPicture(),circleImageView_map);
                                                            }else{
                                                                Glide.with(getActivity().getApplication()).load(R.drawable.test_touxiang2).into(circleImageView_map);
                                                            }
                                                        }catch (Exception c){
                                                            c.printStackTrace();
                                                        }
                                                    }
                                                });
                                                if(object.getPicture()!=null){
                                                    downloadFile_picture(object.getPicture(),imageView);
                                                }else{
                                                    Glide.with(getActivity().getApplication()).load(R.drawable.test_touxiang3).into(imageView);
                                                }
                                            }catch (Exception c){
                                                c.printStackTrace();
                                            }
                                        }else{

                                        }
                                    }

                                });

                                LinearLayout linearLayout = (LinearLayout)view1.findViewById(R.id.map_caozuo);
                                TextView textView_zhonglei = (TextView)view1.findViewById(R.id.map_text) ;
                                textView_zhonglei.setText(zhonglei);

                                linearLayout_map_info.setVisibility(View.VISIBLE);
                                TextView textView = (TextView) view.findViewById(R.id.text_view);
                                textView.setText("经度：" + marker.getPosition().latitude + "纬度" + marker.getPosition().longitude);


                                LatLng ll = marker.getPosition();

                                InfoWindow infoWindow = new InfoWindow(view1, ll, -47);
                                mBaiduMap.showInfoWindow(infoWindow);
                                return false;
                            }
                        };
                mBaiduMap.setOnMarkerClickListener(onMarkerClickListener);
            }

        }


        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(getActivity(), "拖拽结束，新位置" + marker.getPosition().latitude
                        + "," + marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDragStart(Marker marker) {
            }
        });
    }

    private void downloadFile_picture(BmobFile file,final ImageView view) {
        file.download(new DownloadFileListener() {
            @Override
            public void done(final String s, BmobException e) {
                if(e==null){
                    if(s!=null){
                        try{
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Glide.with(getActivity().getApplication()).load(s).into(view);


                                }
                            });
                        }catch (Exception d){
                            d.printStackTrace();
                        }
                    }else {
                    }
                }

            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });
    }

    private void mark(LatLng L,BitmapDescriptor bitmapDescriptor,String c,String d) {
        OverlayOptions option = new MarkerOptions()
                .position(L)
                .icon(bitmapDescriptor)
                .draggable(true)
                .zIndex(9);
        mBaiduMap.addOverlay(option);
        Marker marker = (Marker) (mBaiduMap.addOverlay(option));

        Bundle bundle = new Bundle();
        bundle.putString("c", c);
        bundle.putString("d", d);
        marker.setExtraInfo(bundle);
    }

    public void searchButtonProcess() {
        EditText editCity = (EditText)view. findViewById(R.id.city);
        EditText editGeoCodeKey = (EditText) view.findViewById(R.id.geocodekey);
        // Geo搜索
        mSearch.geocode(new GeoCodeOption().city(
                editCity.getText().toString()).address(editGeoCodeKey.getText().toString()));
    }

    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_gcoding)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
        String strInfo = String.format("纬度：%f 经度：%f",
                result.getLocation().latitude, result.getLocation().longitude);
        Toast.makeText(getActivity(), strInfo, Toast.LENGTH_LONG).show();
    }



    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_gcoding)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
        Toast.makeText(getActivity(), result.getAddress()+" adcode: "+result.getAdcode(),
                Toast.LENGTH_LONG).show();

    }






    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        suggest = new ArrayList<String>();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                suggest.add(info.key);
            }
        }
        sugAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, suggest);
        keyWorldsView.setAdapter(sugAdapter);
        sugAdapter.notifyDataSetChanged();

    }

    @Override
    public void onGetPoiResult(PoiResult result) {

        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(getActivity(), "未找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();

            switch( searchType ) {
                case 2:
                    showNearbyArea(center, radius);
                    break;
                case 3:
                    showBound(searchbound);
                    break;
                default:
                    break;
            }

            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(getActivity(), strInfo, Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void showNearbyArea( LatLng center, int radius) {
        BitmapDescriptor centerBitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        MarkerOptions ooMarker = new MarkerOptions().position(center).icon(centerBitmap);
        mBaiduMap.addOverlay(ooMarker);

        OverlayOptions ooCircle = new CircleOptions().fillColor( 0xCCCCCC00 )
                .center(center).stroke(new Stroke(5, 0xFFFF00FF ))
                .radius(radius);
        mBaiduMap.addOverlay(ooCircle);
    }

    public void showBound( LatLngBounds bounds) {
        BitmapDescriptor bdGround = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);

        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds).image(bdGround).transparency(0.8f);
        mBaiduMap.addOverlay(ooGround);

        MapStatusUpdate u = MapStatusUpdateFactory
                .newLatLng(bounds.getCenter());
        mBaiduMap.setMapStatus(u);

        bdGround.recycle();
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {

        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getActivity(), result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }



    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        initoverlay(list_data,"all");
        super.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            mBaiduMap.setMyLocationEnabled(true);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            mBaiduMap
                    .setMyLocationConfiguration(new MyLocationConfiguration(
                            mCurrentMode, true, mCurrentMarker));
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.overlook(0);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));


        }
        public void onReceivePoi(BDLocation poiLocation) {
        }
    }


    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            poiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));
            // }
            return true;
        }
    }

}
