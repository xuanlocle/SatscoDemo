package com.xuanlocle.satscodemo;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements
        OnMapReadyCallback {
    ListView lstView;
    EditText edtAddress;
    TextView tvDiscount;
    ConstraintLayout const1Dialog,const2Dialog,constraintMain;
    private GoogleMap mMap;
    double lattitude = 10.8139884, longitude = 106.6648174;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    public final static int MILLISECONDS_PER_SECOND = 10;
    public final static int MINUTESS = 60 * MILLISECONDS_PER_SECOND;
    boolean THE_FIRST_TIME_LOAD_LOCATION = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
//        startActivity(intent);

//            Fragment newFragment = new DebugExampleTwoFragment();
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.add(CONTENT_VIEW_ID, newFragment).commit();

        this.arrayAdapterListView();

        edtAddress = (EditText)findViewById(R.id.edtAddress);
        const1Dialog = (ConstraintLayout)findViewById(R.id.constDialog);
        const2Dialog = (ConstraintLayout)findViewById(R.id.const2Dialog);
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

        tvDiscount = (TextView)findViewById(R.id.tvDiscount);
        tvDiscount.setPaintFlags(tvDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
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
                const1Dialog.setVisibility(View.GONE);
                const2Dialog.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add a marker in Sydney and move the camera
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
//                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
//                checkLocationPermission();
            }
        } else {
//            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        LatLng sydney = new LatLng(lattitude,longitude);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Current"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMinZoomPreference(16);
    }
}
