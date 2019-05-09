/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.md2k.phonesensor.enable;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.net.wifi.WifiManager;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.md2k.phonesensor.sensor.SensorType;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Consumer;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;
import pl.charmas.android.reactivelocation2.ReactiveLocationProviderConfiguration;

/**
 * Inner implementation of a dexter instance holding the state of the permissions request
 */
final class EnablerInstance {

    private final EnableCallback enableCallback;
    private final SensorEnabler sensorType;
    private WeakReference<Context> context;
    private Activity activity;

    protected EnablerInstance(Context context, SensorEnabler sensorType, EnableCallback enableCallback) {
        this.sensorType = sensorType;
        this.enableCallback = enableCallback;
        setContext(context);
    }

    protected void setContext(Context context) {
        this.context = new WeakReference<>(context);
    }

    /**
     * Method called whenever the inner activity has been created or restarted and is ready to be
     * used.
     */
    protected void onActivityReady(final Activity activity) {
        this.activity = activity;
        switch (sensorType) {
            case GPS:
                Dexter.withActivity(activity).withPermission(Manifest.permission.ACCESS_COARSE_LOCATION).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(activity, ReactiveLocationProviderConfiguration
                                .builder()
                                .setRetryOnConnectionSuspended(true)
                                .build()
                        );
                        final LocationRequest locationRequest = LocationRequest.create()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setNumUpdates(5)
                                .setInterval(100);
                        locationProvider.checkLocationSettings(
                                new LocationSettingsRequest.Builder()
                                        .addLocationRequest(locationRequest)
                                        .setAlwaysShow(true)  //Refrence: http://stackoverflow.com/questions/29824408/google-play-services-locationservices-api-new-option-never
                                        .build()
                        ).subscribe(new Consumer<LocationSettingsResult>() {
                            @Override
                            public void accept(LocationSettingsResult locationSettingsResult) throws Exception {
                                Status status = locationSettingsResult.getStatus();
                                if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                                    try {
                                        status.startResolutionForResult(activity, 1001);
                                    } catch (IntentSender.SendIntentException th) {
                                        enableCallback.onError();
//                                        Log.e("MainActivity", "Error opening settings activity.", th);
                                    }
                                } else
                                    enableCallback.onSuccess();
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        enableCallback.onError();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        enableCallback.onError();
                    }
                }).check();
                break;
            case BLUETOOTH:
                Dexter.withActivity(activity).withPermission(Manifest.permission.BLUETOOTH).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        activity.startActivityForResult(enableBtIntent, 1002);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        enableCallback.onError();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        enableCallback.onError();
                    }
                }).check();
                break;
            case WIFI:
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBtIntent, 1002);

                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                alertDialog.setTitle("Enable WIFI");
                alertDialog.setMessage("Please enable WIFI");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                WifiManager wifi = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                wifi.setWifiEnabled(true);
                                Enabler.onPermissionsRequested(true);
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Enabler.onPermissionsRequested(false);

                            }
                        });
                alertDialog.show();
        }

    }

    /**
     * Method called whenever the inner activity has been destroyed.
     */
    protected void onActivityDestroyed() {
        //Do nothing
    }

    private boolean isAlreadyEnabled() {
        switch (sensorType) {
            case GPS:
                final LocationManager manager = (LocationManager) context.get().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            case WIFI:
                return false;
            case BLUETOOTH:
                return false;
            default:
                return false;

        }
    }

    protected void requestEnable() {
        if (isAlreadyEnabled())
            enableCallback.onSuccess();
        else
            startTransparentActivityIfNeeded();
    }

    private void startTransparentActivityIfNeeded() {
        Context context = this.context.get();
        if (context == null) {
            return;
        }

        Intent intent = new Intent(context, EnablerActivity.class);
        if (context instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(SensorType.class.getSimpleName(), sensorType.name());
        context.startActivity(intent);
    }


    protected void onPermissionsChecked(boolean success) {
        activity.finish();
        activity = null;
        if (success)
            enableCallback.onSuccess();
        else enableCallback.onError();
    }

}