package org.md2k.mcerebrumapi.extensionapi;

import android.os.Parcel;
import android.os.Parcelable;

import org.md2k.mcerebrumapi.extensionapi.action.MCAction;
import org.md2k.mcerebrumapi.extensionapi.user_interface.MCUserInterface;

import java.util.ArrayList;

public class MCExtensionAPI implements Parcelable {
    String id;
    String name;
    String description;
    int versionCode;
    String versionName;
    IPermission.IPermissionInterface iPermissionInterface;
    String[] permissionList;

    IConfigure.IConfigureBasic iConfigure;
    IConfigure.IConfigureWithUI iConfigureWithUI;
    IBackgroundProcess iBackgroundProcess;
    ArrayList<MCUserInterface> MCUserInterfaces;
    ArrayList<MCAction> MCActions;
    ArrayList<Param> listOfOperations;

    MCExtensionAPI() {
        MCUserInterfaces = new ArrayList<>();
        MCActions = new ArrayList<>();
    }

    public static IMCExtensionBuilder.IIType builder() {
        return new MCExtensionAPIBuilder();
    }

    public String getId() {
        return id;
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


    public IBackgroundProcess getBackgroundProcess() {
        return iBackgroundProcess;
    }

    public IPermission.IPermissionInterface getPermissionInterface() {
        return iPermissionInterface;
    }


    public String[] getPermissionList() {
        return permissionList;
    }

    public IConfigure.IConfigureBasic getiConfigure() {
        return iConfigure;
    }

    public IConfigure.IConfigureWithUI getiConfigureWithUI() {
        return iConfigureWithUI;
    }

    public IBackgroundProcess getiBackgroundProcess() {
        return iBackgroundProcess;
    }

    public ArrayList<Param> getListOfOperations() {
        return listOfOperations;
    }

    public ArrayList<MCUserInterface> getMCUserInterfaces() {
        return MCUserInterfaces;
    }

    public ArrayList<MCAction> getMCActions() {
        return MCActions;
    }

    public ArrayList<Param> getUIParams() {
        ArrayList<Param> uiParams = new ArrayList<>();
        for (int i = 0; MCUserInterfaces != null && i < MCUserInterfaces.size(); i++) {
            uiParams.add(MCUserInterfaces.get(i).getParam());
        }
        return uiParams;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(versionCode);
        dest.writeString(versionName);
        dest.writeStringArray(permissionList);
    }
    protected MCExtensionAPI(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        versionCode = in.readInt();
        versionName = in.readString();
        permissionList = in.createStringArray();
    }

    public static final Creator<MCExtensionAPI> CREATOR = new Creator<MCExtensionAPI>() {
        @Override
        public MCExtensionAPI createFromParcel(Parcel in) {
            return new MCExtensionAPI(in);
        }

        @Override
        public MCExtensionAPI[] newArray(int size) {
            return new MCExtensionAPI[size];
        }
    };

}
