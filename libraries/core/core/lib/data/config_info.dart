class ConfigInfo{
  String fileName="";
  double fileSize=0;
  int createTimestamp=0;
  bool isFromServer=false;
  bool isValidConfig = false;
  int serverPublishedTime = 0;

  ConfigInfo({this.fileName="", this.fileSize=0, this.createTimestamp=0, this.isFromServer=false, this.isValidConfig=false, this.serverPublishedTime=0});

  ConfigInfo.name(Map<String, dynamic> map){
    fileName = map["fileName"];
    fileSize = map["fileSize"];
    createTimestamp = map["createTimestamp"];
    isFromServer = map["isFromServer"];
    isValidConfig = map["isValidConfig"];
    serverPublishedTime = map["serverPublishedTime"];
  }
  toJson() {
    return {
      'fileName': fileName,
      'fileSize': fileSize,
      'createTimestamp': createTimestamp,
      'isFromServer': isFromServer,
      'isValidConfig': isValidConfig,
      'serverPublishedTime': serverPublishedTime,
    };
  }
}