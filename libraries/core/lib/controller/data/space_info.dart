class SpaceInfo{
  int size;
  int other;
  int available;
  int total;

  SpaceInfo({this.size=0, this.other=0, this.available=0, this.total=0});

  SpaceInfo.name(Map<String, dynamic> map){
    size = map["size"];
    other = map["other"];
    available = map["available"];
    total = map["total"];
  }
  static String toSizeString(int value){
    if(value<1024) return value.toString()+" B";
    if(value<1024*1024) return (value/1024).toStringAsFixed(1)+" KB";
    if(value<1024*1024*1024) return (value/(1024*1024)).toStringAsFixed(1)+" MB";
    return (value/(1024*1024*1024)).toStringAsFixed(1)+" MB";
  }
/*
  String getSizeStr(int value) {
    String str;
    double v = value.toDouble();
    if (value > 1024 * 1024 * 1024) {
      str = (v / (1024 * 1024 * 1024)).toStringAsFixed(1) + " GB";
    } else if (value > 1024 * 1024) {
      str = (v / (1024 * 1024)).toStringAsFixed(1) + " MB";
    } else if (value > 1024) {
      str = (v / (1024)).toStringAsFixed(1) + " KB";
    } else {
      str = v.toString() + " B";
    }
    return str;
  }
*/

}