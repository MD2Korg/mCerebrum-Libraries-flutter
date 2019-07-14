package org.md2k.mcerebrumapi.data;

public class Config {
    public static String get1(){
        return "{\n" +
                "  \"core_id\": \"core\",\n" +
                "  \"core_enable\": true,\n" +
                "  \"phonesensor_wifiStatus_enable\": true,\n" +
                "  \"phonesensor_battery_enable\": true,\n" +
                "  \"motionsense2_title\": \"MotionSense2\",\n" +
                "  \"motionsense2_enable\": true,\n" +
                "  \"motionsense1_devices\": [\n" +
                "    {\n" +
                "      \"version\": 1,\n" +
                "      \"accelerometer_enable\":true,\n" +
                "      \"rawMagnetometer_writeType\": \"AS_RECEIVED\"\n" +
                "    },\n" +
                "  ],\n" +
                "  \"motionsense2_devices\": [\n" +
                "    {\n" +
                "      \"platformType\": \"MOTION_SENSE_HRV\",\n" +
                "      \"version\": \"1.0.2.0\",\n" +
                "      \"accelerometer_enable\":true,\n" +
                "      \"rawMagnetometer_writeType\": \"AS_RECEIVED\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"platformType\": \"MOTION_SENSE_HRV_PLUS\",\n" +
                "      \"version\": \"1.0.3.0\",\n" +
                "      \"accelerometer_enable\":true,\n" +
                "      \"sequenceNumberMotion_enable\": true,\n" +
                "      \"battery_enable\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"platformType\": \"MOTION_SENSE_HRV_PLUS_GEN2\",\n" +
                "      \"version\": \"1.0.2.0\",\n" +
                "      \"minConnectionInterval\": 10,\n" +
                "      \"rawMagnetometer_writeType\": \"AS_RECEIVED\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
    }
    public static String get2(){
        return "{\n" +
                "  \"core_enable\": true,\n" +
                "  \"core_id\": \"core\",\n" +
                "  \"phonesensor_wifiStatus_enable\": true,\n" +
                "  \"phonesensor_battery_enable\": true,\n" +
                "  \"motionsense2_title\": \"MotionSense2\",\n" +
                "  \"motionsense2_enable\": true,\n" +
                "  \"motionsense1_devices\": [\n" +
                "    {\n" +
                "      \"accelerometer_enable\":true,\n" +
                "      \"version\": 1,\n" +
                "      \"rawMagnetometer_writeType\": \"AS_RECEIVED\"\n" +
                "    },\n" +
                "  ],\n" +
                "  \"motionsense2_devices\": [\n" +
                "    {\n" +
                "      \"version\": \"1.0.2.0\",\n" +
                "      \"accelerometer_enable\":true,\n" +
                "      \"platformType\": \"MOTION_SENSE_HRV\",\n" +
                "      \"rawMagnetometer_writeType\": \"AS_RECEIVED\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"sequenceNumberMotion_enable\": true,\n" +
                "      \"accelerometer_enable\":true,\n" +
                "      \"platformType\": \"MOTION_SENSE_HRV_PLUS\",\n" +
                "      \"version\": \"1.0.3.0\",\n" +
                "      \"battery_enable\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"minConnectionInterval\": 11,\n" +
                "      \"platformType\": \"MOTION_SENSE_HRV_PLUS_GEN2\",\n" +
                "      \"version\": \"1.0.2.0\",\n" +
                "      \"rawMagnetometer_writeType\": \"AS_RECEIVED\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
    }

    public static String get3(){
        return "{\n" +
                "  \"core_id\": \"core1\",\n" +
                "  \"core_enable\": true,\n" +
                "  \"phonesensor_wifiStatus_enable\": false,\n" +
                "  \"phonesensor_battery_enable\": true,\n" +
                "  \"motionsense2_title\": \"MotionSense2\",\n" +
                "  \"motionsense2_enable\": true,\n" +
                "  \"motionsense1_devices\": [\n" +
                "    {\n" +
                "      \"version\": 2,\n" +
                "      \"accelerometer_enable\":true,\n" +
                "      \"rawMagnetometer_writeType\": \"AS_RECEIVED\"\n" +
                "    },\n" +
                "  ],\n" +
                "  \"motionsense2_devices\": [\n" +
                "    {\n" +
                "      \"platformType\": \"MOTION_SENSE_HRV_PLUS\",\n" +
                "      \"version\": \"1.0.3.0\",\n" +
                "      \"accelerometer_enable\":true,\n" +
                "      \"sequenceNumberMotion_enable\": true,\n" +
                "      \"battery_enable\": true\n" +
                "    },\n" +
                "    {\n" +
                "      \"platformType\": \"MOTION_SENSE_HRV\",\n" +
                "      \"version\": \"1.0.2.0\",\n" +
                "      \"accelerometer_enable\":true,\n" +
                "      \"rawMagnetometer_writeType\": \"AS_RECEIVED\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"platformType\": \"MOTION_SENSE_HRV_PLUS_GEN2\",\n" +
                "      \"version\": \"1.0.2.0\",\n" +
                "      \"minConnectionInterval\": 20,\n" +
                "      \"rawMagnetometer_writeType\": \"AS_RECEIVED\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
    }

}
