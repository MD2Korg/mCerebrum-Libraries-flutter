import 'author.dart';

class Module {
  String name;
  String version;
  List<Author> authors;
  Map<String, String> attributes;

  Module(String name, String version, List<Author> authors,
      Map<String, String> attributes) {
    this.name = name;
    this.version = version;
    this.authors = authors;
    this.attributes = attributes;
  }

  Map toJson() {
    Map m = new Map();
    m["name"] = name;
    m["version"] = version;
    m["authors"] = authors;
    m["attributes"] = attributes;
    return m;
  }

  factory Module.fromJson(Map<String, dynamic> json) {
    Map<String, dynamic> a = json["attributes"];
    Map<String, String> b = new Map();
    if(a!=null) {
      a.forEach((k, v) {
        b[k] = v.toString();
      });
    }
    return Module(json["name"], json["version"],
        (json["authors"] as List).map((i) => Author.fromJson(i)).toList(), b);
  }
}
