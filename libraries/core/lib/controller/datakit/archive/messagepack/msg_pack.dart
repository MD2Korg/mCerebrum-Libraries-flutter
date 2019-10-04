import 'package:archive/archive.dart';
import 'package:mcerebrumapi/datakitapi/mc_data.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource_result.dart';
import 'package:mcerebrumapi/datakitapi/mc_metadata.dart';
import 'package:msgpack_dart/msgpack_dart.dart';

import 'author.dart';
import 'data_descriptor.dart';
import 'module.dart';
import 'stream_metadata.dart';

class MessagePack {
  static StreamMetadata buildStreamMetadata(MCDataSource dataSource) {
    List<MCDataDescriptor> mcDataSourceDescriptors = dataSource.dataDescriptors;

    List<DataDescriptor> dataDescriptors = new List();
    for (int i = 0; i < mcDataSourceDescriptors.length; i++) {
      Map<String, dynamic> attr = mcDataSourceDescriptors[i].metaData;
      String type = mcDataSourceDescriptors[i].dataType.toString();
      String name = mcDataSourceDescriptors[i].id;
      String description = mcDataSourceDescriptors[i].description;
      Map<String, dynamic> attributes = Map.from(attr);
      attributes.remove("id");
      attributes.remove("description");
      DataDescriptor dd =
          new DataDescriptor(type, name, description, attributes);
      dataDescriptors.add(dd);
    }

    String dataSourceId = dataSource.dataSourceId;
    String dataSourceType = dataSource.dataSourceType;

    String applicationId = dataSource.applicationId;
    String applicationType = dataSource.applicationType;

    String platformId = dataSource.platformId;
    String platformType = dataSource.platformType;

    String platformAppId = dataSource.platformAppId;
    String platformAppType = dataSource.platformAppType;

    List<String> nameComponents = new List();
    nameComponents.add(dataSourceType);
    nameComponents.add(dataSourceId);
    nameComponents.add(applicationType);
    nameComponents.add(applicationId);
    nameComponents.add(platformType);
    nameComponents.add(platformId);
    nameComponents.add(platformAppId);
    nameComponents.add(platformAppType);
    nameComponents.removeWhere((e) {
      return e == null;
    });

    String streamName = nameComponents[0];
    nameComponents.remove(streamName);
    nameComponents.forEach((s) {
      streamName += "--" + s;
    });

    String description;
    if (dataSource.dataSourceMetaData != null)
      description = dataSource.dataSourceMetaData.description;
    if (description == null) {
      description = "None";
    }

    String moduleVersion = dataSource.applicationMetaData.version;

    List<Author> moduleAuthors = new List();
    Author a = new Author("Syed Monowar Hossain",
        "dev@md2k.org"); //TODO: Change this once stream generating modules are required to insert this information.
    moduleAuthors.add(a);

    Map<String, String> moduleAttributes = new Map();
    moduleAttributes["datasource_type"] = dataSourceType;
    moduleAttributes["datasource_id"] = dataSourceId;
    moduleAttributes["application_type"] = applicationType;
    moduleAttributes["application_id"] = applicationId;
    moduleAttributes["platform_type"] = platformType;
    moduleAttributes["platform_id"] = platformId;
    moduleAttributes["platformapp_type"] = platformAppType;
    moduleAttributes["platformapp_id"] = platformAppId;

    Module module = new Module(
        applicationType, moduleVersion, moduleAuthors, moduleAttributes);
    List<Module> modules = new List();
    modules.add(module);

    StreamMetadata streamMetadata =
        new StreamMetadata(streamName, description, dataDescriptors, modules);
    return streamMetadata;
  }

  static List<int> createZippedMessagePack(
      MCDataSourceResult dsc, List<MCData> data) {
    GZipEncoder gZipEncoder = new GZipEncoder();
    List<int> array = new List();
    array.addAll(serialize(_generateHeaders(dsc)));
    for (int i = 0; i < data.length; i++) {
      List<dynamic> values = new List();
      values.add(data[i].timestamp * 1000);
      values.add((data[i].timestamp -
              DateTime.fromMillisecondsSinceEpoch(data[i].timestamp)
                  .timeZoneOffset
                  .inMilliseconds) *
          1000);
      for (int j = 0; j < dsc.dataSource.dataDescriptors.length; j++)
        values.add(data[i].data[dsc.dataSource.dataDescriptors[j].id]);
      array.addAll(serialize(values));
    }
    return gZipEncoder.encode(array);
  }

  static List<String> _generateHeaders(MCDataSourceResult dsc) {
    List<MCDataDescriptor> dataDescList = dsc.dataSource.dataDescriptors;
    List<String> headers = new List();
    headers.add("Timestamp");
    headers.add("Localtime");
    dataDescList.forEach((e) {
      String name = e.id;
      name = name.replaceAll(" ", "_");
      headers.add(name);
    });
    return headers;
  }
}
