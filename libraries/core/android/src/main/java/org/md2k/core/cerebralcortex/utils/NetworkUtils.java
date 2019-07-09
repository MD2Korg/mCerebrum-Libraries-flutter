package org.md2k.core.cerebralcortex.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import org.md2k.mcerebrumapi.exception.MCException;
import org.md2k.mcerebrumapi.status.MCStatus;

import java.net.InetAddress;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkUtils {
    public static boolean isInWifi(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
         return manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
    }
    public static boolean checkInternetConnection() {
        try {
            InetAddress inetAddress = InetAddress.getByName("google.com");
            return !inetAddress.toString().equals("");
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean isServerUp(String serverAddress){
        try {
            serverAddress = serverAddress.replace("https://", "");
            serverAddress = serverAddress.replace("http://", "");
            InetAddress inetAddress = InetAddress.getByName(serverAddress);
            return !inetAddress.toString().equals("");
        } catch (Exception e) {
            return false;
        }
    }

}
