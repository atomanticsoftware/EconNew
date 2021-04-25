package com.widget.helpers;

import android.graphics.Bitmap;

import java.util.HashMap;

public class DataHolder {
    public static final String APPWIDGET_IDS = "ids";


    public static final String ME_LIST = "meList";
    public static final String TIMELINE_LIST = "timeLineList";
    public static HashMap<Integer, HashMap<String, Bitmap>> imageList = new HashMap<Integer, HashMap<String, Bitmap>>();
    public static final String UPDATE_INTERVAL_DEFAULT = "1800000";
}
