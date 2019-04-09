package org.md2k.phonesensor.sensor.gps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import org.md2k.phonesensor.SensorType;
import org.md2k.phonesensor.sensor.Comparison;
import org.md2k.phonesensor.sensor.Frequency;
import org.md2k.phonesensor.sensor.MCAbstractSensor;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;
import pl.charmas.android.reactivelocation2.ReactiveLocationProviderConfiguration;

/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public class MCGps extends MCAbstractSensor {
    private Frequency readFrequency;

//    private ReactiveLocationProvider locationProvider;
    private Observable<Location> locationUpdatesObservable;
    private Observable<Location> lastKnownLocationObservable;

    private Disposable updatableLocationDisposable;


    public void setWriteAsReceived(){
        super.setWriteAsReceived();
    }

    public MCGps(Context context) {
        super(context, SensorType.GPS, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        });
        setWriteFixed(1, TimeUnit.MINUTES);
    }
    public void setReadFrequency(double frequency, TimeUnit timeUnit) {
        this.readFrequency = new Frequency(frequency, timeUnit);
    }

    @Override
    protected void startSensing() {
        create();
        updatableLocationDisposable = Observable.merge(lastKnownLocationObservable, locationUpdatesObservable)
                .map(new Function<Location, double[]>() {
                    @Override
                    public double[] apply(Location location) {
                        return new double[]{location.getLatitude(), location.getLongitude()};
                    }
                })
                .subscribe(new Consumer<double[]>() {
                    @Override
                    public void accept(double[] locationData) throws Exception {
                        setSample(System.currentTimeMillis(), locationData);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.d("abc","error");
                    }
                });

    }
/*
    @SuppressLint("MissingPermission")
    public void getLastKnownLocation(final EventListener eventListener){
        final ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(context);
        Observable<Location> lastKnownLocationObservable = locationProvider
                .getLastKnownLocation()
                .observeOn(AndroidSchedulers.mainThread());

        Disposable disposable = lastKnownLocationObservable
                .map(new Function<Location, double[]>() {
                    @Override
                    public double[] apply(Location location) {
                        return new MCLocationData(
                                location.getLatitude(),
                                location.getLongitude(),
                                location.getAltitude(),
                                location.getSpeed(),
                                location.getBearing(),
                                location.getAccuracy(),
                                location.getProvider()
                        );
                    }
                })
                .subscribe(new Consumer<MCLocationData>() {
                    @Override
                    public void accept(MCLocationData locationData) throws Exception {
                        eventListener.onChange(System.currentTimeMillis(), locationData);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.d("abc","error");
                    }
                });

    }
*/

    @SuppressLint("MissingPermission")
    private void create() {
        final ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(context, ReactiveLocationProviderConfiguration
                .builder()
                .setRetryOnConnectionSuspended(true)
                .build()
        );


        lastKnownLocationObservable = locationProvider
                .getLastKnownLocation()
                .observeOn(AndroidSchedulers.mainThread());
        final LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval((long) (1000.0/readFrequency.as(TimeUnit.SECONDS).getFrequency()));
        locationUpdatesObservable = locationProvider
                .checkLocationSettings(
                        new LocationSettingsRequest.Builder()
                                .addLocationRequest(locationRequest)
                                .setAlwaysShow(true)  //Reference: http://stackoverflow.com/questions/29824408/google-play-services-locationservices-api-new-option-never
                                .build()
                )
                .flatMap(new Function<LocationSettingsResult, Observable<Location>>() {
                    @Override
                    public Observable<Location> apply(LocationSettingsResult locationSettingsResult) {
                            return locationProvider.getUpdatedLocation(locationRequest);
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    protected void stopSensing() {
        if (updatableLocationDisposable != null && !updatableLocationDisposable.isDisposed()) {
            updatableLocationDisposable.dispose();
        }
    }


    @Override
    public boolean isSupported() {
        PackageManager packMan = context.getPackageManager();
        return packMan.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
    }

    @Override
    public HashMap<String, String> getSensorInfo() {
        HashMap<String, String> h = new HashMap<>();
        return h;
    }

    @Override
    protected boolean isChanged(Object prevSample, Object curSample, Comparison comparison) {
        return true;
    }
}
