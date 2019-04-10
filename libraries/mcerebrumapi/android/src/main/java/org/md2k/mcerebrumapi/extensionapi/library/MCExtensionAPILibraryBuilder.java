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

package org.md2k.mcerebrumapi.extensionapi.library;

import android.app.Activity;
import android.graphics.Bitmap;

import java.util.ArrayList;

class MCExtensionAPILibraryBuilder implements IMCExtensionBuilderLibrary.IId
        , IMCExtensionBuilderLibrary.IIName
        , IMCExtensionBuilderLibrary.IIDescription
        , IMCExtensionBuilderLibrary.IIVersion
        , IMCExtensionBuilderLibrary.IIPermission
        , IMCExtensionBuilderLibrary.IIIcon
        , IMCExtensionBuilderLibrary.IIConfigure
        , IMCExtensionBuilderLibrary.IIOperation {
    private MCExtensionAPILibrary mcExtensionAPILibrary;

    protected MCExtensionAPILibraryBuilder() {
        mcExtensionAPILibrary = new MCExtensionAPILibrary();
    }


    public static IMCExtensionBuilderLibrary.IId builder() {
        return new MCExtensionAPILibraryBuilder();
    }


    @Override
    public IMCExtensionBuilderLibrary.IIName setId(String id) {
        mcExtensionAPILibrary.id = id;
        return this;
    }

    @Override
    public IMCExtensionBuilderLibrary.IIDescription setName(String name) {
        mcExtensionAPILibrary.name = name;
        return this;
    }

    @Override
    public IMCExtensionBuilderLibrary.IIIcon setDescription(String description) {
        mcExtensionAPILibrary.description = description;
        return this;
    }
    @Override
    public IMCExtensionBuilderLibrary.IIVersion setIcon(Bitmap icon) {
        mcExtensionAPILibrary.icon = icon;
        return this;
    }

    @Override
    public IMCExtensionBuilderLibrary.IIPermission setVersion(int versionCode, String versionName) {
        mcExtensionAPILibrary.versionCode = versionCode;
        mcExtensionAPILibrary.versionName = versionName;
        return this;
    }

    @Override
    public IMCExtensionBuilderLibrary.IIConfigure noPermissionRequired() {
        mcExtensionAPILibrary.iPermissionInterface = null;
        mcExtensionAPILibrary.permissionList = null;
        return this;
    }
    @Override
    public IMCExtensionBuilderLibrary.IIConfigure setCustomPermissionInterface(IPermissionInterface iPermissionInterface) {
        mcExtensionAPILibrary.permissionList = null;
        mcExtensionAPILibrary.iPermissionInterface = iPermissionInterface;
        return this;
    }

    @Override
    public IMCExtensionBuilderLibrary.IIConfigure setPermissionList(String[] permissionList) {
        mcExtensionAPILibrary.permissionList = permissionList;
        mcExtensionAPILibrary.iPermissionInterface = null;
        return this;
    }

    @Override
    public IMCExtensionBuilderLibrary.IIOperation setConfiguration(IConfigure iConfigure) {
        mcExtensionAPILibrary.iConfigure = iConfigure;
        mcExtensionAPILibrary.iConfigureWithUI = null;
        return this;
    }

    @Override
    public IMCExtensionBuilderLibrary.IIOperation setConfigurationUI(Activity activity, IConfigureWithUI iConfigureWithUI) {
        mcExtensionAPILibrary.iConfigure = null;
        mcExtensionAPILibrary.iConfigureWithUI = iConfigureWithUI;
        return this;
    }

    @Override
    public IMCExtensionBuilderLibrary.IIOperation noConfiguration() {
        return this;
    }

    @Override
    public IMCExtensionBuilderLibrary.IIOperation setBackgroundExecutionInterface(IBackgroundProcess iBackgroundProcess) {
        mcExtensionAPILibrary.iBackgroundProcess = iBackgroundProcess;
        return this;
    }

    @Override
    public IMCExtensionBuilderLibrary.IIOperation addUserInterface(String id, String name, String description, MCUserInterface mcUserInterface) {
        UserInterface userInterface = new UserInterface(id, name, description, mcUserInterface);
        mcExtensionAPILibrary.userInterfaces.add(userInterface);
        return this;
    }

    @Override
    public IMCExtensionBuilderLibrary.IIOperation addAction(String id, String name, String description, MCAction mcAction) {
        Action action = new Action(id, name, description, mcAction);
        mcExtensionAPILibrary.actions.add(action);
        return this;
    }

    @Override
    public MCExtensionAPILibrary build() {
        createList();
        return mcExtensionAPILibrary;
    }

    private void createList() {
        mcExtensionAPILibrary.listOfOperations = new ArrayList<>();
        if (mcExtensionAPILibrary.iConfigure != null) {
            mcExtensionAPILibrary.listOfOperations.add(new Param(IConfigure.TYPE, IConfigure.ID_GET_STATE, "Configuration Status", null));
            mcExtensionAPILibrary.listOfOperations.add(new Param(IConfigure.TYPE, IConfigure.ID_SET, "Set Configuration", null));
        }
        if (mcExtensionAPILibrary.iBackgroundProcess != null) {
            mcExtensionAPILibrary.listOfOperations.add(new Param(IBackgroundProcess.TYPE, IBackgroundProcess.ID_START, "Start Process", null));
            mcExtensionAPILibrary.listOfOperations.add(new Param(IBackgroundProcess.TYPE, IBackgroundProcess.ID_STOP, "Stop Process", null));
            mcExtensionAPILibrary.listOfOperations.add(new Param(IBackgroundProcess.TYPE, IBackgroundProcess.ID_IS_RUNNING, "Process Status", null));
        }
        if (mcExtensionAPILibrary.iPermissionInterface != null) {
            mcExtensionAPILibrary.listOfOperations.add(new Param(IPermissionInterface.TYPE, IPermissionInterface.ID_HAS_PERMISSION, "Permission Status", null));
            mcExtensionAPILibrary.listOfOperations.add(new Param(IPermissionInterface.TYPE, IPermissionInterface.ID_REQUEST_PERMISSION, "Request Permission", null));
        }
        for (int i = 0; mcExtensionAPILibrary.userInterfaces != null && i < mcExtensionAPILibrary.userInterfaces.size(); i++) {
            mcExtensionAPILibrary.listOfOperations.add(mcExtensionAPILibrary.userInterfaces.get(i).getParam());
        }
        for (int i = 0; mcExtensionAPILibrary.actions != null && i < mcExtensionAPILibrary.actions.size(); i++) {
            mcExtensionAPILibrary.listOfOperations.add(mcExtensionAPILibrary.actions.get(i).getParam());
        }
    }

}

