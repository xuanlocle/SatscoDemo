package com.xuanlocle.satscodemo;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.zxing.WriterException;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MainActivity extends AppCompatActivity          {
    ListView lstView;
    EditText edtAddress;
    ImageView imvQRCode;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;
    TextView tvDiscount,tvDiemDen,tvInvolveNo;
    Button btnDSatsco,btnHuy;
    ConstraintLayout const1Dialog,const2Dialog,const3Dialog;
//    private GoogleMap mMap;
    double lattitude = 10.8139884, longitude = 106.6648174;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    public final static int MILLISECONDS_PER_SECOND = 10;
    public final static int MINUTESS = 60 * MILLISECONDS_PER_SECOND;
    boolean THE_FIRST_TIME_LOAD_LOCATION = true;




    private MapView mapView;
    private final String MAPKIT_API_KEY = "38e6cc01-366c-4616-a45b-7911a3c57d80";
    private final com.yandex.mapkit.geometry.Point TARGET_LOCATION = new com.yandex.mapkit.geometry.Point(10.816286, 106.664140);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.initialize(this);
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.activity_main);
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
//        startActivity(intent);

//            Fragment newFragment = new DebugExampleTwoFragment();
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.add(CONTENT_VIEW_ID, newFragment).commit();

        /**
         * Set the api key before calling initialize on MapKitFactory.
         * It is recommended to set api key in the Application.onCreate method,
         * but here we do it in each activity to make examples isolated.
         */
        /**
         * Initialize the library to load required native libraries.
         * It is recommended to initialize the MapKit library in the Activity.onCreate method
         * Initializing in the Application.onCreate method may lead to extra calls and increased battery use.
         */
        // Now MapView can be created.
//        setContentView(R.layout.activity_map_yandex);

        mapView = (MapView)findViewById(R.id.mapview);

        // And to show what can be done with it, we move the camera to the center of Saint Petersburg.
        mapView.getMap().move(
                new CameraPosition(TARGET_LOCATION, 14.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 5),
                null);



        this.arrayAdapterListView();
        tvDiscount = (TextView)findViewById(R.id.tvDiscount);
        tvDiemDen = (TextView)findViewById(R.id.tvDiemDen);
        tvInvolveNo = (TextView)findViewById(R.id.tvInvolveNo);
        imvQRCode=(ImageView)findViewById(R.id.imvQRCode);
        edtAddress = (EditText)findViewById(R.id.edtAddress);
        btnDSatsco = (Button)findViewById(R.id.btnDatSatsco);
        btnHuy   = (Button)findViewById(R.id.btnHuy);
        const1Dialog = (ConstraintLayout)findViewById(R.id.constDialog);
        const2Dialog = (ConstraintLayout)findViewById(R.id.const2Dialog);
        const3Dialog = (ConstraintLayout)findViewById(R.id.const3Involve);
//        constraintMain = (ConstraintLayout)findViewById(R.id.constMain);
//        edtAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ConstraintSet constraintSet = new ConstraintSet();
//                constraintSet.clone(constraintLayout);
//                constraintSet.connect(ConstraintSet.PARENT_ID,ConstraintSet.TOP,R.id.constDialog,ConstraintSet.TOP,0);
//                constraintSet.applyTo(constraintLayout);
//            }
//        });
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        tvDiscount.setPaintFlags(tvDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        btnDSatsco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String inputValue = tvInvolveNo.getText().toString().trim();
                if (inputValue.length() > 0) {
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        imvQRCode.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        Log.v("QRCODE", e.toString());
                    }
                }
                const2Dialog.setVisibility(View.GONE);
                const3Dialog.setVisibility(View.VISIBLE);
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);



    }
    @Override
    protected void onStop() {
        // Activity onStop call must be passed to both MapView and MapKit instance.
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        // Activity onStart call must be passed to both MapView and MapKit instance.
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }
    // This method use an ArrayAdapter to add data in ListView.
    private void arrayAdapterListView()
    {
        List<String> dataList = new ArrayList<String>();
        dataList.add("C.T Plaza");
        dataList.add("The Coffee House");
        dataList.add("Bud's Ice Cream");
        dataList.add("Chang Kang Kung");
        dataList.add("Kichi Kichi");

        ListView listView = (ListView)findViewById(R.id.lstView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Object clickItemObj = adapterView.getAdapter().getItem(index);
//                Toast.makeText(getApplicationContext(), "You clicked " + clickItemObj.toString(), Toast.LENGTH_SHORT).show();


                tvDiemDen.setText(clickItemObj.toString());

                const1Dialog.setVisibility(View.GONE);
                const2Dialog.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        Toast.makeText(getApplicationContext(), "Back Clicked", Toast.LENGTH_SHORT).show();
        if(const2Dialog.getVisibility()==View.VISIBLE)
        {
            const2Dialog.setVisibility(View.GONE);
            const1Dialog.setVisibility(View.VISIBLE);
        }
        else if(const3Dialog.getVisibility()==View.VISIBLE){
            const3Dialog.setVisibility(View.GONE);
            const2Dialog.setVisibility(View.VISIBLE);
        }
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        // Add a marker in Sydney and move the camera
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                //Location Permission already granted
////                buildGoogleApiClient();
//                mMap.setMyLocationEnabled(true);
//            } else {
//                //Request Location Permission
////                checkLocationPermission();
//            }
//        } else {
////            buildGoogleApiClient();
//            mMap.setMyLocationEnabled(true);
//        }
//        LatLng sydney = new LatLng(lattitude,longitude);
//
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Current"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,16));
////        mMap.setMinZoomPreference(16);
//
////        GMapV2Direction md = new GMapV2Direction();
////        LatLng sourcePosition, destPosition;
////        sourcePosition = new LatLng(lattitude,longitude);
////        destPosition = new LatLng(10.8114523,106.6642044); //the coffee house hong ha
////        Document doc = md.getDocument(sourcePosition, destPosition,
////                GMapV2Direction.MODE_DRIVING);
////
////        ArrayList<LatLng> directionPoint = md.getDirection(doc);
////        PolylineOptions rectLine = new PolylineOptions().width(3).color(
////                Color.RED);
////
////        for (int i = 0; i < directionPoint.size(); i++) {
////            rectLine.add(directionPoint.get(i));
////        }
////
////        Polyline polylin = mMap.addPolyline(rectLine);
//
//
//
//    }
}
