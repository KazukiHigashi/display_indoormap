package com.example.smasashi.display_indoormap;

import jp.co.mapion.android.maps.MapActivity;

        import jp.co.mapion.android.maps.Map;
        import jp.co.mapion.android.maps.MapActivity;
        import jp.co.mapion.android.maps.MapView;
        import android.os.Bundle;
        import android.view.Window;

public class MainActivity extends MapActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapView mapView = new MapView(this, new MyMap("0c517f9ffc55368fbff97908d680dfd2", R.drawable.map));
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        setContentView(mapView);
    }
}