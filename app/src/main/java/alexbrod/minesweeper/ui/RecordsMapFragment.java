package alexbrod.minesweeper.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

import alexbrod.minesweeper.R;
import alexbrod.minesweeper.bl.RecordsList;
import alexbrod.minesweeper.bl.ScoreRecord;
import alexbrod.minesweeper.bl.SharedPrefManager;


public class RecordsMapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private final int MAX_MARKERS_IN_MAP = 10;
    private final double ISRAEL_HERZELIA_N_E_LONG = 34.88;
    private final double ISRAEL_HERZELIA_N_E_LAT = 32.205;
    private final double ISRAEL_HERZELIA_S_W_LONG = 34.8;
    private final double ISRAEL_HERZELIA_S_W_LAT = 32.165;
    private final double MAP_PADDING = 0.01;

    private MapView mapView;
    private GoogleMap recordsMap;
    private SharedPrefManager prefs;

    public RecordsMapFragment() {
        // Required empty public constructor
    }

    public static RecordsMapFragment newInstance() {
        RecordsMapFragment fragment = new RecordsMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Activity fragAct = getActivity();
        prefs = SharedPrefManager.getInstance(fragAct);
        prefs.setNumOfMaxRecordsToShow(MAX_MARKERS_IN_MAP);


        View v = inflater.inflate(R.layout.records_map_fragment_layout, container, false);

        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        recordsMap = googleMap;
        recordsMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        enableMyLocation();
        updateMapToLevel(0);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void updateMapToLevel(int level) {
        RecordsList list = prefs.getRecordList(level);
        recordsMap.clear(); //clear all previous markers if exist
        double NELat = ISRAEL_HERZELIA_N_E_LAT;
        double NELong = ISRAEL_HERZELIA_N_E_LONG;
        double SWLat = ISRAEL_HERZELIA_S_W_LAT;
        double SWLong = ISRAEL_HERZELIA_S_W_LONG;
        if(list != null) {
            for (int i = 0; i < list.size() && i < MAX_MARKERS_IN_MAP; i++) {
                ScoreRecord rec = list.get(i);
                //Double.MAX_VALUE returned when there is no location parameters
                if (rec.getLatitude() != Double.MAX_VALUE
                        && rec.getLongitude() != Double.MAX_VALUE) {
                    // create marker
                    MarkerOptions marker = new MarkerOptions();
                    marker.position(new LatLng(rec.getLatitude(), rec.getLongitude()));
                    marker.title("Name: " + rec.getName());
                    marker.snippet("Time: " + rec.getTime()
                            + "\nDate: " + formatDate(rec.getDate()));

                    // Changing marker icon
                    marker.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

                    // adding marker
                    recordsMap.addMarker(marker);
                    if (rec.getLatitude() < SWLat) {
                        SWLat = rec.getLatitude();
                    } else if (rec.getLatitude() > NELat) {
                        NELat = rec.getLatitude();
                    }
                    if (rec.getLongitude() < SWLong) {
                        SWLong = rec.getLongitude();
                    } else if (rec.getLongitude() > NELong) {
                        NELong = rec.getLongitude();
                    }

                }
            }
        }

        // Create a LatLngBounds
        LatLngBounds latLngBounds = new LatLngBounds(
                new LatLng(SWLat - MAP_PADDING, SWLong - MAP_PADDING),
                new LatLng(NELat + MAP_PADDING, NELong + MAP_PADDING));

        // Set the camera to the greatest possible zoom level that includes the
        // bounds
        recordsMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));

    }

    private String formatDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.format(new Date(milliSeconds));
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        recordsMap.setMyLocationEnabled(true);
    }
}
