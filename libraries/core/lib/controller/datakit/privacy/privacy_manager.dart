import 'package:mcerebrumapi/datakitapi/mc_data.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource.dart';
import 'package:mcerebrumapi/datakitapi/mc_datasource_result.dart';

import 'privacy_data.dart';

class PrivacyManager {
  PrivacyData privacyData;
  Set<String> uuIds;

  void start(PrivacyData privacyData, List<MCDataSource> dataSources) {
    uuIds = new Set();
    this.privacyData = privacyData;
  }

  void stop() {
    uuIds.clear();
  }

  bool checkData(MCData data) {
/*
    if(data.uuid==privacyData.uuid){

    }
*/
    if (privacyData == null) return true;
    if (privacyData.endTimestamp <= DateTime.now().millisecondsSinceEpoch) {
      privacyData = null;
      return true;
    }
    return !uuIds.contains(data.uuid);
  }

  void checkDataSource(MCDataSourceResult dataSourceResult) {
    if (privacyData != null &&
        privacyData.enable == true &&
        _isMatchWithPrivacyList(dataSourceResult, privacyData.dataSources))
      uuIds.add(dataSourceResult.uuid);
  }

  bool _isMatchWithPrivacyList(
      MCDataSourceResult mcDataSourceResult, List<String> privacyUuid) {
    for (int i = 0; i < privacyUuid.length; i++) {
      if (_isMatch(mcDataSourceResult.dataSource,
          MCDataSource.fromUUID(privacyUuid[i]))) {
        return true;
      }
    }
    return false;
  }

  bool _isMatch(MCDataSource full, MCDataSource partial) {
    if (partial.dataSourceType != null &&
        (full.dataSourceType == null ||
            full.dataSourceType != partial.dataSourceType)) return false;
    if (partial.dataSourceId != null &&
        (full.dataSourceId == null ||
            full.dataSourceId != partial.dataSourceId)) return false;
    if (partial.platformType != null &&
        (full.platformType == null ||
            full.platformType != partial.platformType)) return false;
    if (partial.platformId != null &&
        (full.platformId == null || full.platformId != partial.platformId))
      return false;
    if (partial.platformAppType != null &&
        (full.platformAppType == null ||
            full.platformAppType != partial.platformAppType)) return false;
    if (partial.platformAppId != null &&
        (full.platformAppId == null ||
            full.platformAppId != partial.platformAppId)) return false;
    if (partial.applicationType != null &&
        (full.applicationType == null ||
            full.applicationType != partial.applicationType)) return false;
    if (partial.applicationId != null &&
        (full.applicationId == null ||
            full.applicationId != partial.applicationId)) return false;
    return true;
  }
}
