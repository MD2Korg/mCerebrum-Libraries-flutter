import 'package:phonesensor/phonesensor.dart';

class Configuration{

  Map _configuration;
  Future<void> getSettings() async {
    _configuration = await Phonesensor.getSettings;
  }
  Future<void> save() async {
    Phonesensor.setSettings(_configuration);
  }

  bool getEnabled(String id){
    return _configuration["phonesensor_"+id+"_enable"];
  }
  void setEnabled(String id, bool enable){
    _configuration["phonesensor_"+id+"_enable"]=enable;
    save();
  }
  double getSampleRate(String id){
    return _configuration["phonesensor_"+id+"_sample_rate"];
  }
  void setSampleRate(String id, double newRating) {
    _configuration["phonesensor_"+id+"_sample_rate"]=newRating;
    save();
  }
  String getSampleRateUnit(String id){
    return _configuration["phonesensor_"+id+"_sample_rate_unit"];
  }
  void setSampleRateUnit(String id, String value){
    _configuration["phonesensor_"+id+"_sample_rate_unit"]=value;
    save();
  }

  String getWriteType(String id){
    return _configuration["phonesensor_"+id+"_write_type"];
  }
  void setWriteType(String id, String value){
    _configuration["phonesensor_"+id+"_write_type"]=value;
    save();
  }
  String getWriteOnChangeType(String id){
    return _configuration["phonesensor_"+id+"_write_on_change_type"];
  }
  void setWriteOnChangeType(String id, String value){
    _configuration["phonesensor_"+id+"_write_on_change_type"]=value;
    save();
  }
  double getWriteOnChangeValue(String id){
    return _configuration["phonesensor_"+id+"_write_on_change_value"];
  }
  void setWriteOnChangeValue(String id, double value){
    _configuration["phonesensor_"+id+"_write_on_change_value"]=value;
    save();
  }
  Future<void> setToDefault(String id) async{
    _configuration.remove("phonesensor_"+id+"_enable");
    _configuration.remove("phonesensor_"+id+"_sample_rate");
    _configuration.remove("phonesensor_"+id+"_sample_rate_unit");
    _configuration.remove("phonesensor_"+id+"_write_type");
    _configuration.remove("phonesensor_"+id+"_write_on_change_type");
    _configuration.remove("phonesensor_"+id+"_write_on_change_value");
    await save();
  }

}
