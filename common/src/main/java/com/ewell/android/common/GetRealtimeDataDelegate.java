package com.ewell.android.common;

import com.ewell.android.model.EMRealTimeReport;

import java.util.Map;

/**
 * Created by lillix on 11/2/16.
 */

public interface GetRealtimeDataDelegate {
    void GetRealtimeData(Map<String, EMRealTimeReport> realtimeData);

}

