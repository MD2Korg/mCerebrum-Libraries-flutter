package rekab.app.background_locator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import io.flutter.view.FlutterMain;

public class LocatorBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        FlutterMain.ensureInitializationComplete(context, null);
        LocatorService.enqueueWork(context, intent);
    }
}