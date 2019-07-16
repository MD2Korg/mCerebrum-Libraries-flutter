package org.md2k.phonesensor_example;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.md2k.core.Core;
import org.md2k.mcerebrumapi.MCerebrumAPI;
import org.md2k.mcerebrumapi.extensionapi.MCExtensionAPI;
import org.md2k.phonesensor.mcerebrum.PhoneSensorExtension;

import java.util.List;

import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);
    Core.init(this);
    MCExtensionAPI x = PhoneSensorExtension.createExtensionAPI(this);
    Dexter.withActivity(this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.BLUETOOTH).withListener(new MultiplePermissionsListener() {
      @Override
      public void onPermissionsChecked(MultiplePermissionsReport report) {
        x.getBackgroundProcess().start();
      }

      @Override
      public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

      }
    }).check();
/*
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
*/
    Log.d("abc","here");
  }
}
