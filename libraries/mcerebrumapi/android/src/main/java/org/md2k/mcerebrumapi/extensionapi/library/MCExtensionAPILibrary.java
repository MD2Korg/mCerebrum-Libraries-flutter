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

import android.graphics.Bitmap;

import java.util.ArrayList;

public class MCExtensionAPILibrary {
    protected String id;
    protected String name;
    protected String description;
    protected int versionCode;
    protected String versionName;
    protected Bitmap icon;


    protected IPermissionInterface iPermissionInterface;
    protected String[] permissionList;


    protected IConfigure iConfigure;
    protected IConfigureWithUI iConfigureWithUI;
    protected IBackgroundProcess iBackgroundProcess;
    protected ArrayList<UserInterface> userInterfaces;
    protected ArrayList<Action> actions;
    protected ArrayList<Param> listOfOperations;

    protected MCExtensionAPILibrary() {
        userInterfaces = new ArrayList<>();
        actions = new ArrayList<>();
    }

    public static IMCExtensionBuilderLibrary.IId builder() {
        return new MCExtensionAPILibraryBuilder();
    }

    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getId() {
        return id;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public IConfigure getConfigure() {
        return iConfigure;
    }

    public IBackgroundProcess getBackgroundProcess() {
        return iBackgroundProcess;
    }

    public IPermissionInterface getPermissionInterface() {
        return iPermissionInterface;
    }


    public String[] getPermissionList() {
        return permissionList;
    }

    public IConfigure getiConfigure() {
        return iConfigure;
    }

    public IConfigureWithUI getiConfigureWithUI() {
        return iConfigureWithUI;
    }

    public IBackgroundProcess getiBackgroundProcess() {
        return iBackgroundProcess;
    }

    public ArrayList<Param> getListOfOperations() {
        return listOfOperations;
    }

    public ArrayList<UserInterface> getUserInterfaces() {
        return userInterfaces;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public ArrayList<Param> getUIParams() {
        ArrayList<Param> uiParams = new ArrayList<>();
        for (int i = 0; userInterfaces != null && i < userInterfaces.size(); i++) {
            uiParams.add(userInterfaces.get(i).getParam());
        }
        return uiParams;
    }
}

