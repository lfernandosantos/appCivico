package com.example.ferna.appcivico;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

/**
 * Created by ferna on 13/10/2016.
 */

public class ConfiguradorLocation implements GoogleApiClient.ConnectionCallbacks {

    private Activity activity;

    public ConfiguradorLocation(Activity activity){
        this.activity = activity;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        LocationRequest request = LocationRequest.create();
        request.setInterval(2000);
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        request.setSmallestDisplacement(50);



    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
