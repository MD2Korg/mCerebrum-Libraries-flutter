import 'package:mcerebrumapi/datakitapi/mc_datasource.dart';
import 'package:mcerebrumapi/datakitapi/mc_datatype.dart';
import 'package:mcerebrumapi/datakitapi/mc_metadata.dart';

class PrivacyData {
  final int startTimestamp;
  final int endTimestamp;
  final bool enable;
  final List<String> dataSources;

  PrivacyData(
      this.startTimestamp, this.endTimestamp, this.dataSources, this.enable);

  Future<MCDataSource> createDataSource() async {
    return MCDataSource.create(
        dataSourceType: "PRIVACY",
        platformType: "PHONE",
        dataSourceMetaData: MCDataSourceMetaData.create("privacy", "Privacy",
            "Contains starttime, endtime , enable and list of datasources"),
        dataDescriptors: [
          MCDataDescriptor.create("starttimestamp", "StartTimestamp",
              "Contains start time of the privacy", MCDataType.INTEGER),
          MCDataDescriptor.create("endtimestamp", "EndTimestamp",
              "Contains end time of the privacy", MCDataType.INTEGER),
          MCDataDescriptor.create(
              "datasources",
              "DataSources",
              "Contains a list of datasources where privacy will be applied",
              MCDataType.OBJECT),
          MCDataDescriptor.create("enable", "Enable",
              "Enable/Disable the privacy", MCDataType.INTEGER)
        ]);
  }
}
