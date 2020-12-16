package com.example.bloodbank;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

public class LocationFragment extends Fragment implements OnMapReadyCallback {
    private MapView mMapView;
    private DatabaseReference mydatabase;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private GoogleMap myGoogleMap;
    private LatLngBounds myBounds;
    private double latitude,longitude;
    private ClusterManager myClusterManager;
    private ClusterRenderer myRenderer;
    private ArrayList<ClusterMarker> myClusterMarker = new ArrayList<>();
    private int[] avatars = {R.drawable.ap2,R.drawable.an2,R.drawable.abp2,R.drawable.abn2,R.drawable.bp2,R.drawable.bn2,R.drawable.op2,R.drawable.on2};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.location_fragment,null);
        mMapView = (MapView)view.findViewById(R.id.mapView);
        mydatabase = FirebaseDatabase.getInstance().getReference().child("Data").child("Users");
        initGoogleMap(savedInstanceState);
        return view;
    }

    public void initGoogleMap(Bundle savedInstanceState){
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            Toast.makeText(getContext(),"Please EnableLocation", Toast.LENGTH_LONG).show();
        }
        final double[] lat = new double[1];
        final double lang[] = new double[1];
        myClusterManager = new ClusterManager<ClusterMarker>(getActivity().getApplicationContext(),map);
        myRenderer = new ClusterRenderer(getActivity(),map,myClusterManager);
        myClusterManager.setRenderer(myRenderer);
        mydatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dc:dataSnapshot.getChildren()){
                    User current = dc.getValue(User.class);
                    String email = current.getEmail();
                    if(email.equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        myGoogleMap = map;
                        lat[0] = current.getLatitude();
                        lang[0] = current.getLongitude();
                        double bottom_boundary = lat[0] - 1;
                        double left_boundary = lang[0] - 1;
                        double top_boundary = lat[0] + 1;
                        double right_boundary = lang[0] + 1;
                        String snippet = "Yours";
                        int avatar = 0;
                        switch(current.getBlood()){
                            case "A+":
                                avatar = avatars[0];
                                break;
                            case "A-":
                                avatar = avatars[1];
                                break;
                            case "B+":
                                avatar = avatars[4];
                                break;
                            case "B-":
                                avatar = avatars[5];
                                break;
                            case "O+":
                                avatar = avatars[6];
                                break;
                            case "0-":
                                avatar = avatars[7];
                                break;
                            case "AB+":
                                avatar = avatars[2];
                                break;
                            case "AB-":
                                avatar = avatars[3];
                                break;
                        }

                        myBounds = new LatLngBounds(new LatLng(bottom_boundary,left_boundary),new LatLng(top_boundary,right_boundary));
                        LatLng usr_loc = new LatLng(lat[0],lang[0]);
                        ClusterMarker clusterMarker = new ClusterMarker(usr_loc,current.getFirstname(),snippet+" "+current.getBlood()+"\n"+current.getAddress(),avatar,current);
                        myClusterManager.addItem(clusterMarker);
                        myClusterMarker.add(clusterMarker);
                        map.moveCamera(CameraUpdateFactory.newLatLngBounds(myBounds,0));
                    }
                    else{
                        String snippet = current.getFirstname();
                        int avatar = 0;
                        switch(current.getBlood()){
                            case "A+":
                                avatar = avatars[0];
                                break;
                            case "A-":
                                avatar = avatars[1];
                                break;
                            case "B+":
                                avatar = avatars[4];
                                break;
                            case "B-":
                                avatar = avatars[5];
                                break;
                            case "O+":
                                avatar = avatars[6];
                                break;
                            case "0-":
                                avatar = avatars[7];
                                break;
                            case "AB+":
                                avatar = avatars[2];
                                break;
                            case "AB-":
                                avatar = avatars[3];
                                break;
                        }
                        lat[0] = current.getLatitude();
                        lang[0] = current.getLongitude();
                        LatLng usr_loc = new LatLng(lat[0],lang[0]);
                        ClusterMarker clusterMarker = new ClusterMarker(usr_loc,current.getFirstname(),snippet+" "+current.getBlood()+"\n"+current.getAddress(),avatar,current);
                        myClusterManager.addItem(clusterMarker);
                        myClusterMarker.add(clusterMarker);
                    }
                }
                myClusterManager.cluster();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}