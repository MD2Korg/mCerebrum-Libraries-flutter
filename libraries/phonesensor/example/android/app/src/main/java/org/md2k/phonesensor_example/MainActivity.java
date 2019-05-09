package org.md2k.phonesensor_example;

import android.os.Bundle;
import android.util.Log;

import org.md2k.mcerebrumapi.extensionapi.library.ExtensionCallback;
import org.md2k.mcerebrumapi.extensionapi.library.MCExtensionAPILibrary;
import org.md2k.phonesensor.mcerebrum.PhoneSensorExtension;

import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);
    DefaultConfig.write(this);
    MCExtensionAPILibrary x = PhoneSensorExtension.createExtensionAPI(this);
    boolean res = x.getPermissionInterface().hasPermission();
    x.getPermissionInterface().requestPermission(this, new ExtensionCallback() {
      @Override
      public <T> void onSuccess(T value) {
        x.getBackgroundProcess().start(null);
        Log.d("abc","abc");
      }

      @Override
      public void onError(String message) {
        Log.d("abc","message = "+message);
      }
    });
    Log.d("abc","here");
  }
}
