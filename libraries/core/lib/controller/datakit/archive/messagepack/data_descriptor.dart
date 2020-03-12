class DataDescriptor {
  String type;
  String name;
  String description;
  Map<String, String> attributes = new Map();

  Map toJson() {
    Map m = new Map();
    m["type"] = type;
    m["name"] = name;
    m["description"] = description;
    m["attributes"] = attributes;
    return m;
  }

  factory DataDescriptor.fromJson(Map<String, dynamic> json) {
    Map<String, dynamic> a = json["attributes"];
    Map<String, String> b = new Map();
    if(a!=null) {
      a.forEach((k, v) {
        b[k] = v.toString();
      });
    }
    DataDescriptor d =
        DataDescriptor(json["type"], json["name"], json["description"], b);
    return d;
  }

  DataDescriptor(String type, String name, String description,
      Map<String, dynamic> attributes) {
    this.type = type;
    this.name = name;
    this.description = description;
    this.attributes = new Map<String, String>();
    if (attributes != null){
      attributes.forEach((k, v) {
        this.attributes[k] = v.toString();
      });
  }
  }
}
