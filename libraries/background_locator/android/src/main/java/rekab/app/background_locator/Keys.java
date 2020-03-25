package rekab.app.background_locator;

public class Keys {
    public static String SHARED_PREFERENCES_KEY = "SHARED_PREFERENCES_KEY";
    public static String CALLBACK_DISPATCHER_HANDLE_KEY = "CALLBACK_DISPATCHER_HANDLE_KEY";
    public static String CALLBACK_HANDLE_KEY = "CALLBACK_HANDLE_KEY";
    public static String DIRECTORY_HANDLE_KEY = "DIRECTORY_HANDLE_KEY";
    public static String NOTIFICATION_CALLBACK_HANDLE_KEY = "NOTIFICATION_CALLBACK_HANDLE_KEY";
    public static String CHANNEL_ID = "app.rekab/locator_plugin";
    public static String BACKGROUND_CHANNEL_ID = "app.rekab/locator_plugin_background";
    public static String METHOD_SERVICE_INITIALIZED = "LocatorService.initialized";
    public static String METHOD_PLUGIN_INITIALIZE_SERVICE = "LocatorPlugin.initializeService";
    public static String METHOD_PLUGIN_REGISTER_LOCATION_UPDATE = "LocatorPlugin.registerLocationUpdate";
    public static String METHOD_PLUGIN_UN_REGISTER_LOCATION_UPDATE = "LocatorPlugin.unRegisterLocationUpdate";
    public static String METHOD_PLUGIN_IS_REGISTER_LOCATION_UPDATE = "LocatorPlugin.isRegisterLocationUpdate";
    public static String ARG_LATITUDE = "latitude";
    public static String ARG_LONGITUDE = "longitude";
    public static String ARG_ACCURACY = "accuracy";
    public static String ARG_ALTITUDE = "altitude";
    public static String ARG_SPEED = "speed";
    public static String ARG_SPEED_ACCURACY = "speed_accuracy";
    public static String ARG_HEADING = "heading";
    public static String ARG_CALLBACK = "callback";
    public static String ARG_DIRECTORY = "directory";
    public static String ARG_LOCATION = "location";
    public static String ARG_SETTINGS = "settings";
    public static String ARG_CALLBACK_DISPATCHER = "callbackDispatcher";
    public static String ARG_INTERVAL = "interval";
    public static String ARG_DISTANCE_FILTER = "distanceFilter";
    public static String ARG_NOTIFICATION_TITLE = "notificationTitle";
    public static String ARG_NOTIFICATION_MSG = "notificationMsg";
    public static String ARG_NOTIFICATION_ICON = "notificationIcon";
    public static String ARG_WAKE_LOCK_TIME = "wakeLockTime";
    public static String ARG_NOTIFICATION_CALLBACK = "notificationCallback";
    public static String BCM_SEND_LOCATION = "BCM_SEND_LOCATION";
    public static String BCM_NOTIFICATION_CLICK = "BCM_NOTIFICATION_CLICK";
}