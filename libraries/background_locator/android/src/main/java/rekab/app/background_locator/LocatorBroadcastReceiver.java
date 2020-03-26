package rekab.app.background_locator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.location.LocationResult;

import java.util.List;

import io.flutter.view.FlutterMain;

public class LocatorBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        FlutterMain.ensureInitializationComplete(context, null);
        intent.putExtra("timestamp", System.currentTimeMillis());
        LocationResult l = LocationResult.extractResult(intent);
        String directory = BackgroundLocatorPlugin.getDirectoryHandle(context, Keys.DIRECTORY_HANDLE_KEY);
        if(directory==null) return;
        if(l!=null){
            List<Location> locations = l.getLocations();
            for(int i =0;i<locations.size();i++){
                FileManager.writeToLogFile(context,directory, locations.get(i));
            }
        }
//        LocatorService.enqueueWork(context, intent);
    }
}