/*
 * Copyright (c) 2018, The University of Memphis, MD2K Center of Excellence
 *
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

package org.md2k.mcerebrumapi.extensionapi;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import org.md2k.mcerebrumapi.extensionapi.action.MCAction;
import org.md2k.mcerebrumapi.extensionapi.user_interface.MCUserInterface;

import java.util.ArrayList;

class MCExtensionAPIBuilder implements IMCExtensionBuilder.IIType
        , IMCExtensionBuilder.IIdLib
        , IMCExtensionBuilder.IINameLib
        , IMCExtensionBuilder.IIDescriptionLib
        , IMCExtensionBuilder.IIVersionLib
        , IMCExtensionBuilder.IIPermissionLib
        , IMCExtensionBuilder.IIConfigureLib
        , IMCExtensionBuilder.IIOperationLib
        , IMCExtensionBuilder.IIDescriptionApp
        , IMCExtensionBuilder.IIPermissionApp
        , IMCExtensionBuilder.IIConfigureApp
        , IMCExtensionBuilder.IIOperationApp {
    private MCExtensionAPI mcExtensionAPI;

    MCExtensionAPIBuilder() {
        mcExtensionAPI = new MCExtensionAPI();
    }


    @Override
    public MCExtensionAPIBuilder setId(@NonNull String id) {
        mcExtensionAPI.id = id;
        return this;
    }
    @Override
    public MCExtensionAPIBuilder setName(@NonNull String name) {
        mcExtensionAPI.name = name;
        return this;
    }

    @Override
    public MCExtensionAPIBuilder setDescription(@NonNull String description) {
        mcExtensionAPI.description = description;
        return this;
    }

    @Override
    public MCExtensionAPIBuilder setVersion(int versionCode, @NonNull String versionName) {
        mcExtensionAPI.versionCode = versionCode;
        mcExtensionAPI.versionName = versionName;
        return this;
    }

    @Override
    public MCExtensionAPIBuilder noPermissionRequired() {
        mcExtensionAPI.iPermissionInterface = null;
        mcExtensionAPI.permissionList = null;
        return this;
    }

    @Override
    public MCExtensionAPIBuilder setPermissionList(@NonNull String[] permissionList) {
        mcExtensionAPI.permissionList = permissionList;
        mcExtensionAPI.iPermissionInterface = null;
        return this;
    }
    @Override
    public MCExtensionAPIBuilder setCustomPermissionInterface(@NonNull IPermission.IPermissionInterface iPermission) {
        mcExtensionAPI.permissionList = null;
        mcExtensionAPI.iPermissionInterface = iPermission;
        return this;
    }


    @Override
    public MCExtensionAPIBuilder setConfiguration(@NonNull IConfigure.IConfigureBasic iConfigure) {
        mcExtensionAPI.iConfigure = iConfigure;
        mcExtensionAPI.iConfigureWithUI = null;
        return this;
    }

    @Override
    public MCExtensionAPIBuilder setConfigurationUI(@NonNull Activity activity, @NonNull IConfigure.IConfigureWithUI iConfigureWithUI) {
        mcExtensionAPI.iConfigure = null;
        mcExtensionAPI.iConfigureWithUI = iConfigureWithUI;
        return this;
    }

    @Override
    public MCExtensionAPIBuilder noConfiguration() {
        return this;
    }

    @Override
    public MCExtensionAPIBuilder setBackgroundExecutionInterface(@NonNull IBackgroundProcess iBackgroundProcess) {
        mcExtensionAPI.iBackgroundProcess = iBackgroundProcess;
        return this;
    }

    @Override
    public MCExtensionAPIBuilder addUserInterface(@NonNull MCUserInterface mcUserInterface) {
        mcExtensionAPI.MCUserInterfaces.add(mcUserInterface);
        return this;
    }

    @Override
    public MCExtensionAPIBuilder addAction(@NonNull MCAction mcAction) {
        mcExtensionAPI.MCActions.add(mcAction);
        return this;
    }


    @Override
    public MCExtensionAPI build() {
        createList();
        return mcExtensionAPI;
    }

    private void createList() {
        mcExtensionAPI.listOfOperations = new ArrayList<>();
        if (mcExtensionAPI.iConfigure != null) {
            mcExtensionAPI.listOfOperations.add(new Param(IConfigure.TYPE, IConfigure.ID_GET_STATE, "Configuration Status", null));
            mcExtensionAPI.listOfOperations.add(new Param(IConfigure.TYPE, IConfigure.ID_SET, "Set Configuration", null));
        }
        if (mcExtensionAPI.iBackgroundProcess != null) {
            mcExtensionAPI.listOfOperations.add(new Param(IBackgroundProcess.TYPE, IBackgroundProcess.ID_START, "Start Process", null));
            mcExtensionAPI.listOfOperations.add(new Param(IBackgroundProcess.TYPE, IBackgroundProcess.ID_STOP, "Stop Process", null));
            mcExtensionAPI.listOfOperations.add(new Param(IBackgroundProcess.TYPE, IBackgroundProcess.ID_IS_RUNNING, "Process Status", null));
        }
        if (mcExtensionAPI.iPermissionInterface != null) {
            mcExtensionAPI.listOfOperations.add(new Param(IPermission.IPermissionInterface.TYPE, IPermission.IPermissionInterface.ID_HAS_PERMISSION, "Permission Status", null));
            mcExtensionAPI.listOfOperations.add(new Param(IPermission.IPermissionInterface.TYPE, IPermission.IPermissionInterface.ID_REQUEST_PERMISSION, "Request Permission", null));
        }
        for (int i = 0; mcExtensionAPI.MCUserInterfaces != null && i < mcExtensionAPI.MCUserInterfaces.size(); i++) {
            mcExtensionAPI.listOfOperations.add(mcExtensionAPI.MCUserInterfaces.get(i).getParam());
        }
        for (int i = 0; mcExtensionAPI.MCActions != null && i < mcExtensionAPI.MCActions.size(); i++) {
            mcExtensionAPI.listOfOperations.add(mcExtensionAPI.MCActions.get(i).getParam());
        }
    }

    @Override
    public MCExtensionAPIBuilder asLibrary() {
        return this;
    }

    @Override
    public MCExtensionAPIBuilder asApp(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            mcExtensionAPI.id = pInfo.packageName;
            mcExtensionAPI.name = pInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
            mcExtensionAPI.versionName = pInfo.versionName;
            mcExtensionAPI.versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return this;
    }
}

