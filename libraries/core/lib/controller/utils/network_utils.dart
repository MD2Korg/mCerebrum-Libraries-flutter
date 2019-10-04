import 'package:connectivity/connectivity.dart';

class NetworkUtils {
  Future<ConnectivityResult> getNetworkType() async {
    return await (Connectivity().checkConnectivity());
  }
}
