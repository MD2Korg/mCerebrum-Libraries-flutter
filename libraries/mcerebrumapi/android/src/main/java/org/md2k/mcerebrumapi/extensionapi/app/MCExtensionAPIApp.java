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

package org.md2k.mcerebrumapi.extensionapi.app;

public class MCExtensionAPIApp /*implements Parcelable */{
/*
    private String packageName;
    private String name;
    private String description;
    private String version;
    private Bitmap icon;
    private IConfigure iConfigure;
    private IBackgroundProcess iBackgroundProcess;
    private IPermission iUserPermission;
    private ArrayList<UserInterface> userInterfaces;
    private ArrayList<Action> actions;
    private ArrayList<Param> listOfOperations;

    private MCExtensionAPIApp() {
        userInterfaces = new ArrayList<>();
        actions = new ArrayList<>();
    }

    protected MCExtensionAPIApp(Parcel in) {
        name = in.readString();
        description = in.readString();
        version = in.readString();
        packageName = in.readString();
        icon = in.readParcelable(getClass().getClassLoader());
        listOfOperations = in.readArrayList(null);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(version);
        parcel.writeString(packageName);
        parcel.writeParcelable(icon, i);
        parcel.writeList(listOfOperations);
    }

    public static final Creator<MCExtensionAPIApp> CREATOR = new Creator<MCExtensionAPIApp>() {
        @Override
        public MCExtensionAPIApp createFromParcel(Parcel in) {
            return new MCExtensionAPIApp(in);
        }

        @Override
        public MCExtensionAPIApp[] newArray(int size) {
            return new MCExtensionAPIApp[size];
        }
    };

    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public String getPackageName() {
        return packageName;
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

    public IPermission getiUserPermission() {
        return iUserPermission;
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

    @Override
    public int describeContents() {
        return 0;
    }


    private void createList() {
        listOfOperations = new ArrayList<>();
        if (iConfigure != null) {
            listOfOperations.add(new Param(IConfigure.TYPE, IConfigure.ID_GET_STATE, "Configuration Status", null));
            listOfOperations.add(new Param(IConfigure.TYPE, IConfigure.ID_SET, "Set Configuration", null));
        }
        if (iBackgroundProcess != null) {
            listOfOperations.add(new Param(IBackgroundProcess.TYPE, IBackgroundProcess.ID_START, "Start Process", null));
            listOfOperations.add(new Param(IBackgroundProcess.TYPE, IBackgroundProcess.ID_STOP, "Stop Process", null));
            listOfOperations.add(new Param(IBackgroundProcess.TYPE, IBackgroundProcess.ID_IS_RUNNING, "Process Status", null));
        }
        if (iUserPermission != null) {
            listOfOperations.add(new Param(IPermission.TYPE, IPermission.ID_HAS_PERMISSION, "Permission Status", null));
            listOfOperations.add(new Param(IPermission.TYPE, IPermission.ID_REQUEST_PERMISSION, "Request Permission", null));
        }
        for (int i = 0; userInterfaces != null && i < userInterfaces.size(); i++) {
            listOfOperations.add(userInterfaces.get(i).getParam());
        }
        for (int i = 0; actions != null && i < actions.size(); i++) {
            listOfOperations.add(actions.get(i).getParam());
        }
    }
*/

}

