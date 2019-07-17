package org.md2k.motionsense;

import android.Manifest;
import android.content.Context;

import org.md2k.mcerebrumapi.extensionapi.IBackgroundProcess;
import org.md2k.mcerebrumapi.extensionapi.IConfigure;
import org.md2k.mcerebrumapi.extensionapi.MCExtensionAPI;

public class MotionSenseExtension {
        private static final String ID = "motionsense";
        private static final String NAME = "Phone Sensor";
        private static final String DESCRIPTION = "Library for collecting motion sensor data";
        private static final int VERSION_CODE = org.md2k.motionsense.BuildConfig.VERSION_CODE;
        private static final String VERSION_NAME = org.md2k.motionsense.BuildConfig.VERSION_NAME;
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

        public static MCExtensionAPI createExtensionAPI(final Context context) {
            return MCExtensionAPI.builder()
                    .asLibrary()
                    .setId(ID)
                    .setName(NAME)
                    .setDescription(DESCRIPTION)
                    .setVersion(VERSION_CODE, VERSION_NAME)
                    .setPermissionList(permissions)
                    .noConfiguration()
                    .setBackgroundExecutionInterface(new IBackgroundProcess() {
                        @Override
                        public void start() {
                            //TODO:
//                            PhoneSensorManager.getInstance(context).startBackground();
//                        phoneSensorManager.startBackground(param);
                        }

                        @Override
                        public void stop() {
//                            PhoneSensorManager.getInstance(context).stopBackground();
                        }

                        @Override
                        public boolean isRunning() {
//                            long runningTime = PhoneSensorManager.getInstance(context).getRunningTime();
//                            return runningTime != -1;
                            return false;
                        }

                        @Override
                        public long getRunningTime() {
                            return 0;
                        }
                    })
                    .build();
        }
        protected static IConfigure.ConfigState getState(Context context){
            return IConfigure.ConfigState.CONFIGURED;
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

