package org.md2k.phonesensor.mcerebrum;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.md2k.mcerebrumapi.extensionapi.ConfigState;
import org.md2k.mcerebrumapi.extensionapi.library.ExtensionCallback;
import org.md2k.mcerebrumapi.extensionapi.library.IBackgroundProcess;
import org.md2k.mcerebrumapi.extensionapi.library.IPermissionInterface;
import org.md2k.mcerebrumapi.extensionapi.library.MCExtensionAPILibrary;
import org.md2k.phonesensor.mcerebrum.PhoneSensorManager;

import java.util.List;

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
public class PhoneSensorExtension {
    private static final String ID = "phonesensor";
    private static final String NAME = "Phone Sensor";
    private static final String DESCRIPTION = "Library for collecting phone sensor data";
    private static final int VERSION_CODE = org.md2k.phonesensor.BuildConfig.VERSION_CODE;
    private static final String VERSION_NAME = org.md2k.phonesensor.BuildConfig.VERSION_NAME;
    private static String[] permissions= new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.INTERNET
    };

    public static MCExtensionAPILibrary createExtensionAPI(final Context context) {
        return MCExtensionAPILibrary.builder()
                .setId(ID)
                .setName(NAME)
                .setDescription(DESCRIPTION)
                .setIcon(null)
                .setVersion(VERSION_CODE, VERSION_NAME)
                .setCustomPermissionInterface(new IPermissionInterface() {
                    @Override
                    public boolean hasPermission() {
                        for (String permission : permissions) {
                            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                                return false;
                        }
                        return true;
                    }

                    @Override
                    public void requestPermission(Activity activity, final ExtensionCallback extensionCallback) {
                        Dexter.withActivity(activity).withPermissions(permissions).withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if(report.areAllPermissionsGranted())
                                    extensionCallback.onSuccess(true);
                                else extensionCallback.onError("Permission not granted");
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                            }
                        }).check();
                    }
                })
                .noConfiguration()
                .setBackgroundExecutionInterface(new IBackgroundProcess() {
                    @Override
                    public void start(Object param) {
                        //TODO:
                        PhoneSensorManager.getInstance(context).startBackground();
//                        phoneSensorManager.startBackground(param);
                    }

                    @Override
                    public void stop() {
                        PhoneSensorManager.getInstance(context).stopBackground();
                    }

                    @Override
                    public boolean isRunning() {
                        long runningTime = PhoneSensorManager.getInstance(context).getRunningTime();
                        return runningTime != -1;
                    }
                })
                .build();
    }
    protected static ConfigState getState(Context context){
        return ConfigState.CONFIGURED;
/*
        Configuration current = Configuration.read(context);
        Configuration def=Configuration.readDefault(context);
        if(current==null || current.size()==0) return ConfigState.NOT_CONFIGURED;
        if(def==null || def.size()==0){
            return ConfigState.CONFIGURED;
        }else{
            if(def.equals(current)) return ConfigState.CONFIGURED;
            else return ConfigState.PARTIALLY_CONFIGURED;
        }
*/
    }

}
