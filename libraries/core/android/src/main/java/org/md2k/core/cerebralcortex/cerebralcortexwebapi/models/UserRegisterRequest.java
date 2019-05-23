package org.md2k.core.cerebralcortex.cerebralcortexwebapi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Provides getters and setters for <code>UserRegisterRequest</code> objects.
 */
public class UserRegisterRequest {
    private AuthRequest authRequest;

    @SerializedName("user_role")
    @Expose
    private String userRole;

    @SerializedName("user_metadata")
    @Expose
    private ArrayList<UserMetadata> userMetadata;

    @SerializedName("user_settings")
    @Expose
    private ArrayList<UserSettings> userSettings;

    public UserRegisterRequest() {}

    public UserRegisterRequest(AuthRequest authRequest, String userRole,
                               ArrayList<UserMetadata> userMetadata,
                               ArrayList<UserSettings> userSettings) {
        this.authRequest = authRequest;
        this.userRole = userRole;
        this.userMetadata = userMetadata;
        this.userSettings = userSettings;
    }

    public ArrayList<UserMetadata> getUserMetadata() {
        return userMetadata;
    }

    public void setUserMetadata(ArrayList<UserMetadata> userMetadata) {
        this.userMetadata = userMetadata;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public AuthRequest getAuthRequest() {
        return authRequest;
    }

    public ArrayList<UserSettings> getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(ArrayList<UserSettings> userSettings) {
        this.userSettings = userSettings;
    }

    public void setAuthRequest(AuthRequest authRequest) {
        this.authRequest = authRequest;
    }
}