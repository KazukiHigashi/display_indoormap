package com.example.smasashi.display_indoormap;

/**
 * Created by Smasashi on 15/12/31.
 */
import java.lang.reflect.Field;
import java.util.HashMap;

import jp.co.mapion.android.maps.GeoPoint;
import jp.co.mapion.android.maps.Map;
import jp.co.mapion.android.maps.Tile;
import android.graphics.Point;

import java.lang.reflect.Field;
import java.util.HashMap;

import jp.co.mapion.android.maps.GeoPoint;
import jp.co.mapion.android.maps.Map;
import jp.co.mapion.android.maps.Tile;
import android.graphics.Point;

public class MyMap extends Map {

    protected int tileWidth = 320;
    protected int tileHeight = 200;

    protected HashMap<Integer, Integer> cols = new HashMap<Integer, Integer>();
    protected HashMap<Integer, Integer> rows = new HashMap<Integer, Integer>();

    protected HashMap<Integer, Float> ratios = new HashMap<Integer, Float>();

    private HashMap<String, Integer> tileNameMap = new HashMap<String, Integer>();

    private String key;

    private int noimage;

    public MyMap(String key, int noimage) {
        this.key = key;
        this.noimage = noimage;
        init();
    }

    protected void init() {
        cols.put(1, 1);
        cols.put(2, 2);
        cols.put(3, 4);

        rows.put(1, 1);
        rows.put(2, 2);
        rows.put(3, 4);

        ratios.put(1, 4.0f);
        ratios.put(2, 2.0f);
        ratios.put(3, 1.0f);
    }

    @Override
    protected int getMaxZoomLevel() {
        return ratios.size();
    }

    @Override
    protected void setup() {
        setCenter(new GeoPoint(0, 0));
        setZoom(1);
    }

    @Override
    protected int getTileWidth() {
        return tileWidth;
    }

    @Override
    protected int getTileHeight() {
        return tileHeight;
    }

    @Override
    protected Point geoToPixel(GeoPoint geo) {
        int x = (int) (geo.getLongitudeE6() / getRatio());
        int y = (int) (geo.getLatitudeE6() / getRatio());
        return new Point(x, y);
    }

    @Override
    protected GeoPoint pixelToGeo(Point pixel) {
        int lat = (int) (pixel.y * getRatio());
        int lon = (int) (pixel.x * getRatio());
        return new GeoPoint(lat, lon);
    }

    @Override
    protected GeoPoint getOrigin() {
        double originRatio = ratios.get(1);
        int centerx = tileWidth * rows.get(1) / 2;
        int centery = tileHeight * cols.get(1) / 2;
        int lat = (int) (centery * originRatio);
        int lon = (int) (-centerx * originRatio);
        return new GeoPoint(lat, lon);
    }

    @Override
    protected String getURL(Tile tile) {
        if (isOut(tile)) {
            return noMap();
        }
        int x = (int) tile.getX();
        int y = (int) tile.getY();
        int tileNo = y * cols.get(getZoom()) + x;
        String name = "level" + getZoom() + "_" + tileNo;
        int id = getResourceInt(name);
        if (id == -1) {
            return noMap();
        } else {
            String ret = String.valueOf(id);
            return ret;
        }
    }

    @Override
    protected String getKey() {
        return key;
    }

    private double getRatio() {
        return ratios.get(getZoom());
    }

    private int getResourceInt(String name) {
        if (tileNameMap.containsKey(name)) {
            return tileNameMap.get(name);
        }
        try {
            Field field = R.drawable.class.getDeclaredField(name);
            int tileId = field.getInt(R.drawable.class);
            tileNameMap.put(name, tileId);
            return tileId;
        } catch (Exception e) {
        }
        return -1;
    }

    private String noMap() {
        return String.valueOf(noimage);
    }

    private boolean isOut(Tile tile) {
        int x = (int) tile.getX();
        int y = (int) tile.getY();
        if (x < 0 || y < 0) {
            return true;
        }
        if (x >= cols.get(getZoom())) {
            return true;
        }
        return false;
    }
}