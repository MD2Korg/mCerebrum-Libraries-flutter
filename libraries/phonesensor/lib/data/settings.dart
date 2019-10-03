class Settings{

  Map<String, dynamic> _settings;
  Map<String, dynamic> _defaultSettings;
  Map<String, dynamic> _internalSettings;
  Settings(this._settings, this._defaultSettings){
    _setInternalSettings();
    if(this._settings==null) this._settings=new Map<String, dynamic>();
  }

  Map<String, dynamic> get settings => _settings;

  bool isEnabled(String id){
    if(_settings==null || _settings["phonesensor_"+id+"_enable"]==null)
      return isDefaultEnabled(id);
    else return _settings["phonesensor_"+id+"_enable"];
  }
  bool isDefaultEnabled(String id){
    if(_defaultSettings==null || _defaultSettings["phonesensor_"+id+"_enable"]==null) return _internalSettings["phonesensor_"+id+"_enable"];
    else return _defaultSettings["phonesensor_"+id+"_enable"];
  }
  void setEnabled(String id, bool enable){
    _settings["phonesensor_"+id+"_enable"]=enable;
  }
  double getSampleRate(String id){
    if(_settings==null ||_settings["phonesensor_"+id+"_sampleRate"]==null) return getDefaultSampleRate(id);
    else return _settings["phonesensor_"+id+"_sampleRate"];
  }
  double getDefaultSampleRate(String id){
    if(_defaultSettings==null ||_defaultSettings["phonesensor_"+id+"_sampleRate"]==null) return _internalSettings["phonesensor_"+id+"_sampleRate"];
    else return _defaultSettings["phonesensor_"+id+"_sampleRate"];
  }
  void setSampleRate(String id, double newRating) {
    _settings["phonesensor_"+id+"_sampleRate"]=newRating;
  }
  String getSampleRateUnit(String id){
    if(_settings==null ||_settings["phonesensor_"+id+"_sampleRateUnit"]==null) return getDefaultSampleRateUnit(id);
    else return _settings["phonesensor_"+id+"_sampleRateUnit"];
  }
  String getDefaultSampleRateUnit(String id){
    if(_defaultSettings==null ||_defaultSettings["phonesensor_"+id+"_sampleRateUnit"]==null) return _internalSettings["phonesensor_"+id+"_sampleRateUnit"];
    else return _defaultSettings["phonesensor_"+id+"_sampleRateUnit"];
  }
  void setSampleRateUnit(String id, String value){
    _settings["phonesensor_"+id+"_sampleRateUnit"]=value;
  }

  String getWriteType(String id){
    if(_settings==null ||_settings["phonesensor_"+id+"_writeType"]==null) return getDefaultWriteType(id);
    else return _settings["phonesensor_"+id+"_writeType"];
  }
  String getDefaultWriteType(String id){
    if(_defaultSettings==null ||_defaultSettings["phonesensor_"+id+"_writeType"]==null) return _internalSettings["phonesensor_"+id+"_writeType"];
    else return _defaultSettings["phonesensor_"+id+"_writeType"];
  }
  void setWriteType(String id, String value){
    _settings["phonesensor_"+id+"_writeType"]=value;
  }
  String getWriteOnChangeType(String id){
    if(_settings==null ||_settings["phonesensor_"+id+"_writeOnChangeType"]==null) return getDefaultWriteOnChangeType(id);
    else return _settings["phonesensor_"+id+"_writeOnChangeType"];
  }
  String getDefaultWriteOnChangeType(String id){
    if(_defaultSettings==null ||_defaultSettings["phonesensor_"+id+"_writeOnChangeType"]==null) return _internalSettings["phonesensor_"+id+"_writeOnChangeType"];
    else return _defaultSettings["phonesensor_"+id+"_writeOnChangeType"];
  }
  void setWriteOnChangeType(String id, String value){
    _settings["phonesensor_"+id+"_writeOnChangeType"]=value;
  }
  double getWriteOnChangeValue(String id){
    if(_settings==null ||_settings["phonesensor_"+id+"_writeOnChangeValue"]==null) return getDefaultWriteOnChangeValue(id);
    return _settings["phonesensor_"+id+"_writeOnChangeValue"];
  }
  double getDefaultWriteOnChangeValue(String id){
    if(_defaultSettings==null ||_defaultSettings["phonesensor_"+id+"_writeOnChangeValue"]==null) return _internalSettings["phonesensor_"+id+"_writeOnChangeValue"];
    else return _defaultSettings["phonesensor_"+id+"_writeOnChangeValue"];
  }
  void setWriteOnChangeValue(String id, double value){
    _settings["phonesensor_"+id+"_writeOnChangeValue"]=value;
  }
  Future<void> setToDefault(String id, Settings defaultSettings) async{
    _settings = Map.from(defaultSettings._settings);
  }
  void _setInternalSettings(){
    _internalSettings = {
      "phonesensor_id": "phonesensor",
      "phonesensor_title": "Phone Sensor",
      "phonesensor_description": "Collects phone sensor data (accelerometer, gyroscope, GPS, Activity Type, Battery level, etc)",
      "phonesensor_enable": true,
      "phonesensor_accelerometer_enable": false,
      "phonesensor_accelerometer_sampleRate": 16.0,
      "phonesensor_accelerometer_sampleRateUnit": "SECONDS",
      "phonesensor_accelerometer_writeType": "FIXED",
      "phonesensor_accelerometer_writeOnChangeType": "SAMPLE_DIFFERENCE",
      "phonesensor_accelerometer_writeOnChangeValue": 0.1,
      "phonesensor_accelerometerLinear_enable": false,
      "phonesensor_accelerometerLinear_sampleRate": 16.0,
      "phonesensor_accelerometerLinear_sampleRateUnit": "SECONDS",
      "phonesensor_accelerometerLinear_writeType": "FIXED",
      "phonesensor_accelerometerLinear_writeOnChangeType": "SAMPLE_DIFFERENCE",
      "phonesensor_accelerometerLinear_writeOnChangeValue": 0.1,
      "phonesensor_gyroscope_enable": false,
      "phonesensor_gyroscope_sampleRate": 16.0,
      "phonesensor_gyroscope_sampleRateUnit": "SECONDS",
      "phonesensor_gyroscope_writeType": "FIXED",
      "phonesensor_gyroscope_writeOnChangeType": "SAMPLE_DIFFERENCE",
      "phonesensor_gyroscope_writeOnChangeValue": 0.1,
      "phonesensor_magnetometer_enable": false,
      "phonesensor_magnetometer_sampleRate": 16.0,
      "phonesensor_magnetometer_sampleRateUnit": "SECONDS",
      "phonesensor_magnetometer_writeType": "FIXED",
      "phonesensor_magnetometer_writeOnChangeType": "SAMPLE_DIFFERENCE",
      "phonesensor_magnetometer_writeOnChangeValue": 0.1,
      "phonesensor_gravity_enable": false,
      "phonesensor_gravity_sampleRate": 16.0,
      "phonesensor_gravity_sampleRateUnit": "SECONDS",
      "phonesensor_gravity_writeType": "FIXED",
      "phonesensor_gravity_writeOnChangeType": "SAMPLE_DIFFERENCE",
      "phonesensor_gravity_writeOnChangeValue": 0.1,
      "phonesensor_ambientLight_enable": false,
      "phonesensor_ambientLight_sampleRate": 16.0,
      "phonesensor_ambientLight_sampleRateUnit": "SECONDS",
      "phonesensor_ambientLight_writeType": "FIXED",
      "phonesensor_ambientLight_writeOnChangeType": "SAMPLE_DIFFERENCE",
      "phonesensor_ambientLight_writeOnChangeValue": 0.1,
      "phonesensor_airPressure_enable": false,
      "phonesensor_airPressure_sampleRate": 16.0,
      "phonesensor_airPressure_sampleRateUnit": "SECONDS",
      "phonesensor_airPressure_writeType": "FIXED",
      "phonesensor_airPressure_writeOnChangeType": "SAMPLE_DIFFERENCE",
      "phonesensor_airPressure_writeOnChangeValue": 0.1,
      "phonesensor_ambientTemperature_enable": false,
      "phonesensor_ambientTemperature_sampleRate": 16.0,
      "phonesensor_ambientTemperature_sampleRateUnit": "SECONDS",
      "phonesensor_ambientTemperature_writeType": "FIXED",
      "phonesensor_ambientTemperature_writeOnChangeType": "SAMPLE_DIFFERENCE",
      "phonesensor_ambientTemperature_writeOnChangeValue": 0.1,
      "phonesensor_proximity_enable": false,
      "phonesensor_proximity_sampleRate": 6.0,
      "phonesensor_proximity_sampleRateUnit": "SECONDS",
      "phonesensor_proximity_writeType": "ON_CHANGE",
      "phonesensor_proximity_writeOnChangeType": "NOT_EQUAL",
      "phonesensor_proximity_writeOnChangeValue": 0.1,
      "phonesensor_relativeHumidity_enable": false,
      "phonesensor_relativeHumidity_sampleRate": 6.0,
      "phonesensor_relativeHumidity_sampleRateUnit": "SECONDS",
      "phonesensor_relativeHumidity_writeType": "ON_CHANGE",
      "phonesensor_relativeHumidity_writeOnChangeType": "NOT_EQUAL",
      "phonesensor_relativeHumidity_writeOnChangeValue": 0.1,
      "phonesensor_significantMotion_enable": true,
      "phonesensor_significantMotion_writeType": "AS_RECEIVED",
      "phonesensor_chargingStatus_enable": true,
      "phonesensor_chargingStatus_writeType": "ON_CHANGE",
      "phonesensor_chargingStatus_writeOnChangeType": "NOT_EQUAL",
      "phonesensor_connectivityStatus_enable": true,
      "phonesensor_connectivityStatus_writeType": "ON_CHANGE",
      "phonesensor_connectivityStatus_writeOnChangeType": "NOT_EQUAL",
      "phonesensor_bluetoothStatus_enable": true,
      "phonesensor_bluetoothStatus_writeType": "ON_CHANGE",
      "phonesensor_bluetoothStatus_writeOnChangeType": "NOT_EQUAL",
      "phonesensor_gpsStatus_enable": true,
      "phonesensor_gpsStatus_writeType": "ON_CHANGE",
      "phonesensor_gpsStatus_writeOnChangeType": "NOT_EQUAL",
      "phonesensor_wifiStatus_enable": true,
      "phonesensor_wifiStatus_writeType": "ON_CHANGE",
      "phonesensor_wifiStatus_writeOnChangeType": "NOT_EQUAL",
      "phonesensor_battery_enable": true,
      "phonesensor_battery_writeType": "ON_CHANGE",
      "phonesensor_battery_sampleRate": 1.0,
      "phonesensor_battery_sampleRateUnit": "MINUTES",
      "phonesensor_battery_writeOnChangeType": "NOT_EQUAL",
      "phonesensor_battery_writeOnChangeValue": 0.1,
      "phonesensor_activityType_enable": true,
      "phonesensor_activityType_sampleRate": 1.0,
      "phonesensor_activityType_sampleRateUnit": "MINUTES",
      "phonesensor_activityType_writeType": "AS_RECEIVED",
      "phonesensor_activityType_writeOnChangeType": "NOT_EQUAL",
      "phonesensor_activityType_writeOnChangeValue": 0.1,
      "phonesensor_gps_enable": true,
      "phonesensor_gps_sampleRate": 12.0,
      "phonesensor_gps_sampleRateUnit": "MINUTES",
      "phonesensor_gps_writeType": "AS_RECEIVED",
      "phonesensor_gps_writeOnChangeType": "NOT_EQUAL",
      "phonesensor_gps_writeOnChangeValue": 0.1,
      "phonesensor_stepCount_enable": true,
      "phonesensor_stepCount_sampleRate": 1.0,
      "phonesensor_stepCount_sampleRateUnit": "MINUTES",
      "phonesensor_stepCount_writeType": "FIXED",
    };
  }
}
