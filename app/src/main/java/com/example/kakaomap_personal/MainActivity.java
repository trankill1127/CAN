package com.example.kakaomap_personal;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener, MapView.POIItemEventListener {

    //xml
    private MapView mMapView;
    private ViewGroup mMapViewContainer;

    //value
    MapPoint startMapPoint;
    MapPoint canMapPoint;

    Double startLatitude; //앱 실행 시 사용자 위도
    Double startLongitude; //앱 실행 시 사용자 경도
    Double latitude; //가장 가까운 쓰레기통 위도
    Double longitude; //가장 가까운 쓰레기통 경도

    MapPOIItem canMarker = new MapPOIItem();
    MapPOIItem startMarker = new MapPOIItem();

    private Intent intent;
    private TextView layout_userName;
    private int now_step_count;

    private String userID;
    private String userPassword;
    private String userName;
    private int step_count;
    private int trash_count;
    private int total;
    private int best_rank;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        initView(); //화면 초기화



        startLatitude = 37.5666805;
        startLongitude = 126.9784147;




        ImageButton arrive_button = findViewById(R.id.main_arriveButton);
        arrive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                now_step_count=10; //임의로 걸음수 설정

                //데베 업뎃을 위해 회원정보 업뎃
                step_count=step_count+now_step_count;
                trash_count=trash_count+1;
                total=step_count+trash_count*1000;

                //데베 업뎃
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            boolean success = jsonObject.getBoolean("success");

                            if (success) { // 데베 업뎃을 성공한 경우
                                intent = new Intent(MainActivity.this,RankingActivity.class);

                                //RankingActivity에 회원정보 전달
                                intent.putExtra("userID", userID);
                                intent.putExtra("userPassword", userPassword);
                                intent.putExtra("userName", userName);
                                intent.putExtra("step_count", step_count);
                                intent.putExtra("trash_count", trash_count);
                                intent.putExtra("total", total);
                                intent.putExtra("best_rank", best_rank);
                                intent.putExtra("now_step_count", now_step_count);

                                startActivity(intent);

                            } else { // 데베 업뎃에 실패한 경우
                                Toast.makeText(getApplicationContext(),"데이터베이스 업데이트에 실패했습니다.",Toast.LENGTH_SHORT).show(); //데베 업뎃 실패 알림
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                UpdateRequest updateRequest = new UpdateRequest(userID,step_count,trash_count,total,responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(updateRequest);

            }
        });

    }

    private void initView() {

        //맵 바인딩
        mMapView = new MapView(this);
        mMapViewContainer = findViewById(R.id.map_view);
        mMapViewContainer.addView(mMapView);

        //맵 리스너
        mMapView.setMapViewEventListener(this); // this에 MapView.MapViewEventListener 구현.
        mMapView.setPOIItemEventListener(this);

        //맵 리스너 (현재위치 업데이트)
        mMapView.setCurrentLocationEventListener(this);
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading); //현재위치 추적

        //LoginActivity에서 전달한 정보들 받아오기
        intent = getIntent();
        userID = intent.getStringExtra("userID");
        userPassword = intent.getStringExtra("userPassword");
        userName = intent.getStringExtra("userName");
        step_count = intent.getIntExtra("step_count", 0);
        trash_count = intent.getIntExtra("trash_count", 0);
        total = intent.getIntExtra("total", 0);
        best_rank = intent.getIntExtra("best_rank", 0);

        //회원 정보 적용
        layout_userName = findViewById(R.id.main_userName);
        layout_userName.setText(userName);


        Response.Listener<String> c_responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(getApplicationContext(),"가장 가까운 쓰레기통의 위치를 찾는 데 성공했습니다.",Toast.LENGTH_SHORT).show();

                    latitude=Double.parseDouble(jsonObject.getString("latitude"));
                    longitude=Double.parseDouble(jsonObject.getString("longitude"));

                    canMapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                    canMarker.setItemName("쓰레기통 위치");
                    canMarker.setMapPoint(canMapPoint);//마커 위치 설정
                    canMarker.setMarkerType(MapPOIItem.MarkerType.BluePin); //마커 모습(기본)
                    canMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); //마커 모습(클릭)

                    mMapView.addPOIItem(canMarker); //지도 위에 마커 표시

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        TrashCanRequest trashCanRequest = new TrashCanRequest(startLatitude, startLongitude, c_responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(trashCanRequest);

    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float accuracyInMeters) {
        //MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        //startMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);
        //startLatitude = mapPointGeo.latitude;
        //startLongitude = mapPointGeo.longitude;

        //사용자 현재 위치 좌표로 지도 중심 이동
        //mMapView.setMapCenterPoint(currentMapPoint, true);
        //mMapView.setZoomLevel(1, true);//맵 배율 설정

        //userMarker.setMapPoint(currentMapPoint);//마커 위치 설정
        //userMarker.setMarkerType(MapPOIItem.MarkerType.BluePin); //마커 모습(기본)
        //userMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); //마커 모습(클릭)

        //mapView.addPOIItem(userMarker); //지도 위에 마커 표시



    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}