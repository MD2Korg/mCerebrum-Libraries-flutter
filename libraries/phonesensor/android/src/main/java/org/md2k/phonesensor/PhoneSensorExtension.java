package org.md2k.phonesensor;

import android.Manifest;
import android.content.Context;

import org.md2k.mcerebrumapi.core.extensionapi.ConfigState;
import org.md2k.mcerebrumapi.core.extensionapi.library.IBackgroundProcess;
import org.md2k.mcerebrumapi.core.extensionapi.library.MCExtensionAPILibrary;

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
    private static String[] permissions= new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET
    };

    public static MCExtensionAPILibrary createExtensionAPI(final Context context) {
        return MCExtensionAPILibrary.builder()
                .setId(Constants.ID)
                .setName(Constants.NAME)
                .setDescription(Constants.DESCRIPTION)
                .setIcon(null)
                .setVersion(Constants.VERSION_CODE, Constants.VERSION_NAME)
                .setPermissionList(permissions)
                .noConfiguration()
                .setBackgroundExecutionInterface(new IBackgroundProcess() {
                    @Override
                    public void start(Object param) {
                        //TODO:
                        PhoneSensorManager.getInstance(context).startBackground(param);
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
