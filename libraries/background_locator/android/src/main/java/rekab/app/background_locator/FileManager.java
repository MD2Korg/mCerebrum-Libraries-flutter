package rekab.app.background_locator;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class FileManager {
    static void writeToLogFile(Context context, String parentDirectory, Location location) {
        long length=-1;
        String path = parentDirectory+"/gps_log.txt";
        try {
            FileOutputStream fOut = new FileOutputStream(path, true);
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(path, Context.MODE_APPEND));
            String log = formatLog(location);
            Log.d("location","log = "+log);
            fOut.write(log.getBytes());
            fOut.close();
//            outputStreamWriter.write(log);
//            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        File f = new File(path);
        if(f.exists()) {
            length = f.length();
            Log.d("location", "filesize = "+length);
            if(length>10000) {
                createMessagePack(context, parentDirectory, path);
                f.delete();
            }
        }
    }
    private static void createMessagePack(Context context, String parentDirectory, String logFilePath){
        Log.d("location", "createMsgPack...");
        File file = new File(logFilePath);
        BufferedReader reader;
        FileInputStream is;
        ArrayList<String> lines = new ArrayList<>();
        try {
            if (file.exists()) {
                is = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();
                while (line != null) {
                    lines.add(line);
                    line = reader.readLine();
                }
                writeMsgPackFile(context, parentDirectory, lines);
            }
        }catch (Exception e){
            Log.e("location","error on creating msgpack e="+e.getMessage());
        }
    }
    private static List<String> ppg_headers = new ArrayList<>(Arrays.asList("timestamp", "localtime", "latitude", "longitude", "accuracy", "altitude", "speed", "speedAccuracy", "heading"));
    private static String getLocationMetaData(){
        return "    {\n" +
                "      \"name\": \"location--org.md2k.mcontain--phone\",\n" +
                "      \"modules\":[\n" +
                "        {\n" +
                "          \"name\": \"org.md2k.mcontain\",\n" +
                "          \"authors\":[\n" +
                "            {\n" +
                "              \"name\":\"Syed Monowar Hossain\",\n" +
                "              \"email\":\"dev@md2k.org\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"version\":\"\",\n" +
                "          \"attributes\":{\n" +
                "        \"platform_type\": \"phone\"\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"annotations\":[],\n" +
                "      \"description\":\"None\",\n" +
                "  \"input_streams\": [],\n" +
                "  \"data_descriptor\": [\n" +
                "        {\n" +
                "          \"name\":\"latitude\",\n" +
                "          \"type\":\"double\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\":\"longitude\",\n" +
                "          \"type\":\"double\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\":\"accuracy\",\n" +
                "          \"type\":\"double\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\":\"altitude\",\n" +
                "          \"type\":\"double\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\":\"speed\",\n" +
                "          \"type\":\"double\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\":\"speedAccuracy\",\n" +
                "          \"type\":\"double\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\":\"heading\",\n" +
                "          \"type\":\"double\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n";

    }
    private static void writeMsgPackFile(Context context, String parentDirectory, ArrayList<String> lines) {
        long time = System.currentTimeMillis();
        File dir = new File(parentDirectory+"/archive/");
        dir.mkdirs();

        String metaDataPath = parentDirectory+"/archive/location_"+time+".json";
            try {
                FileOutputStream fOut = new FileOutputStream(metaDataPath);
                fOut.write(getLocationMetaData().getBytes());
                fOut.close();
//                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(metaDataPath, Context.MODE_APPEND));
//                outputStreamWriter.write(getLocationMetaData());
//                outputStreamWriter.close();
            }
            catch (IOException e) {
                Log.e("location", "metadata File write failed: " + e.toString());
            }

        try {

        File o = new File(parentDirectory+"/archive/location_"+time+".gzip");
        MessagePacker packer = MessagePack.newDefaultPacker(new GZIPOutputStream(new FileOutputStream(o)));
        int offset = Calendar.getInstance().getTimeZone().getRawOffset();
        packer.packArrayHeader(ppg_headers.size());
                for (String header : ppg_headers) {
                    packer.packString(header);
                }

                for (int i = 0; i < lines.size(); i++) {
                    HashMap<String, Object> values=retrieveValues(lines.get(i));
                    if(values.size()!=8) continue;
                    packer.packArrayHeader(ppg_headers.size());
                    packer.packLong(((long)values.get("timestamp")) * 1000);
                    packer.packLong((((long)values.get("timestamp")) - offset) * 1000); //localtime

                    packer.packDouble((double)values.get(Keys.ARG_LATITUDE));
                    packer.packDouble((double)values.get(Keys.ARG_LONGITUDE));
                    packer.packDouble((double)values.get(Keys.ARG_ACCURACY));
                    packer.packDouble((double)values.get(Keys.ARG_ALTITUDE));
                    packer.packDouble((double)values.get(Keys.ARG_SPEED));
                    packer.packDouble((double)values.get(Keys.ARG_SPEED_ACCURACY));
                    packer.packDouble((double)values.get(Keys.ARG_HEADING));
                }
                packer.close();

        } catch (IOException e) {
            Log.e("location", "error in creating msgpack file e = "+e.getMessage());
        }
    }
    private static String formatLog(Location locationDto) {
        double speedAccuracy = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            speedAccuracy = locationDto.getSpeedAccuracyMetersPerSecond();
        }
        return locationDto.getTime() +","
                +locationDto.getLatitude()+","
                +locationDto.getLongitude()+","
                +locationDto.getAccuracy()+","
                +locationDto.getAltitude()+","
                +locationDto.getSpeed()+","
                +speedAccuracy+","
                +locationDto.getBearing()
                +"\n";
    }
    private static HashMap<String, Object> retrieveValues(String line) {
        HashMap<String, Object> result = new HashMap<>();
        String[] parts = line.split(",");
        if(parts.length<8) return result;
        result.put("timestamp", Long.parseLong(parts[0]));
        result.put(Keys.ARG_LATITUDE, Double.parseDouble(parts[1]));
        result.put(Keys.ARG_LONGITUDE, Double.parseDouble(parts[2]));
        result.put(Keys.ARG_ACCURACY, Double.parseDouble(parts[3]));
        result.put(Keys.ARG_ALTITUDE, Double.parseDouble(parts[4]));
        result.put(Keys.ARG_SPEED, Double.parseDouble(parts[5]));
        result.put(Keys.ARG_SPEED_ACCURACY, Double.parseDouble(parts[6]));
        result.put(Keys.ARG_HEADING, Double.parseDouble(parts[7]));
        return result;
    }

}
