import 'package:core/controller/datakit/archive/messagepack/stream_metadata.dart';
import 'package:core/controller/utils/file_utils.dart';

import 'cerebral_cortex.dart';
import 'login_response.dart';
import 'register_response.dart';

class UploadManager {
  CerebralCortex cerebralCortex;

  Future<void> init({String url = "https://odin.md2k.org"}) async {
    cerebralCortex = new CerebralCortex();
    cerebralCortex.init(url);
  }

  Future<bool> upload(String userId, String password, String filePrefix) async {
    LoginResponse loginResponse = await cerebralCortex.login(userId, password);
    Map<String, dynamic> json = await FileUtils.readAsMap(filePrefix + ".json");
    StreamMetadata streamMetaData = StreamMetadata.fromJson(json);
    RegisterResponse registerResponse =
        await cerebralCortex.registerDataSource(loginResponse, streamMetaData);
    return cerebralCortex.uploadData(
        loginResponse, registerResponse, filePrefix + ".gzip");
  }
}
