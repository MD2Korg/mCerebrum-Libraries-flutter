import 'data_descriptor.dart';
import 'module.dart';

class StreamMetadata {
  String name;
  String description;
  List<DataDescriptor> dataDescriptor;
  List<Module> modules;

  StreamMetadata(String name, String description,
      List<DataDescriptor> dataDescriptors, List<Module> modules) {
    this.name = name;
    this.description = description;
    this.dataDescriptor = dataDescriptors;
    this.modules = modules;
  }

  Map toJson() {
    Map<String, dynamic> m = new Map();
    m["name"] = name;
    m["description"] = description;
    m["data_descriptor"] =
        dataDescriptor; //jsonEncode(data_descriptor.map((e) => e.toJson()).toList());
    m["modules"] =
        modules; //jsonEncode(modules.map((e) => e.toJson()).toList());
    return m;
  }

  factory StreamMetadata.fromJson(Map<String, dynamic> json) {
    return StreamMetadata(
        json["name"],
        json["description"],
        (json["data_descriptor"] as List)
            .map((i) => DataDescriptor.fromJson(i))
            .toList(),
        (json["modules"] as List).map((i) => Module.fromJson(i)).toList());
  }
}
