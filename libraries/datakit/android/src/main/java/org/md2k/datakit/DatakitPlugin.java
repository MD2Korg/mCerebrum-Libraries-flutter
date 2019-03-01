package org.md2k.datakit;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.google.gson.Gson;

import org.md2k.datakit.exception.MCExceptionDataKitNotRunning;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSource;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSourceRegister;
import org.md2k.mcerebrumapi.core.datakitapi.datasource.MCDataSourceResult;
import org.md2k.mcerebrumapi.core.time.DateTime;

import java.util.ArrayList;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** DatakitPlugin */
public class DatakitPlugin implements MethodCallHandler {
  private static final String SPACE_INFO = "spaceInfo";
  private static final String DELETE = "delete";
  private static final String START = "start";
  private static final String STOP = "stop";
  private static final String IS_STARTED = "isStarted";
  private static final String DATASOURCES = "datasources";
  private static Context context;
  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "datakit");
    context = registrar.context();
    channel.setMethodCallHandler(new DatakitPlugin());
  }

  @Override
  public void onMethodCall(MethodCall call, final Result result) {
    switch(call.method){
      case DELETE:
          DataKitManager.getInstance(context).delete();
          result.success(true);
        break;
      case START:
        DataKitManager.getInstance(context).start();
          result.success(true);
        break;
        case IS_STARTED:
            result.success(DataKitManager.getInstance(context).isRunning());
            break;
      case STOP:
        DataKitManager.getInstance(context).stop();
          result.success(true);
        break;
      case SPACE_INFO:
          result.success(getSpaceInfo());
        break;
        case DATASOURCES:
            result.success(getDataSources());
            break;
        default:
          result.notImplemented();
          break;
    }
  }
  private String getDataSources(){
      Gson gson = new Gson();
      ArrayList<DataSourceInfo> dataSourceInfos=new ArrayList<>();
      DataKitManager d = DataKitManager.getInstance(context);
      long curTime = DateTime.getCurrentTime();
      int dataCount;
      int dataCountLastHour;
      try {
          ArrayList<MCDataSourceResult> r = d.queryDataSource((MCDataSource) MCDataSource.queryBuilder().build());
          for(int i =0;i<r.size();i++){
              dataCount = d.queryDataCount(r.get(i).getDsId(), 0, curTime);
              dataCountLastHour = d.queryDataCount(r.get(i).getDsId(), curTime-60*60*1000, curTime);
              DataSourceInfo dataSourceInfo = new DataSourceInfo(r.get(i).getDataSource().toUUID(), r.get(i).getDataSource().toString(), dataCount, dataCountLastHour, r.get(i).getLastDataTime());
              dataSourceInfos.add(dataSourceInfo);
          }
          return gson.toJson(dataSourceInfos);
      } catch (MCExceptionDataKitNotRunning mcExceptionDataKitNotRunning) {
          return null;
      }
  }
  private long[] getSpaceInfo(){
      long[] result = new long[4];
      StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
      long bytesAvailable;
      long totalBytes;
      if (android.os.Build.VERSION.SDK_INT >=
              android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
          bytesAvailable = stat.getAvailableBytes();
          totalBytes = stat.getTotalBytes();
      }
      else {
          bytesAvailable = 0;
          totalBytes = 0;
      }
      result[0]=totalBytes;
      result[1]=DataKitManager.getInstance(context).getSize();
      result[3]=bytesAvailable;
      result[2]=result[0]-result[1]-result[3];
      return result;
  }
/*
  private long[] getSize(){
    DataKitManager dataKitManager = DataKitManager.getInstance(context);
  }
*/
}
