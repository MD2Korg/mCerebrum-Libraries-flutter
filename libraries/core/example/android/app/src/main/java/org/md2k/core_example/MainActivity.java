package org.md2k.core_example;

import android.os.Bundle;

import org.md2k.mcerebrumapi.data.MCDataType;
import org.md2k.mcerebrumapi.data.MCSampleType;
import org.md2k.mcerebrumapi.datakitapi.datasource.MCDataSource;

import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);
      MCDataSource.registerBuilder()
              .setDataType(MCDataType.POINT, MCSampleType.INT_ARRAY)
              .setColumnNames(new String[]{"abc", "def"})
              .setDataSourceType("abc")
              .build();
  }
}
