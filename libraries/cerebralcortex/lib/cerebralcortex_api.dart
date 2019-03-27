import 'package:extensionapi/extensionapi.dart';
import 'login_page.dart';
import 'server_page.dart';
class CerebralCortexAPI {
  static MCExtensionAPI get() {
    MCExtensionAPI m;
    m = new MCExtensionAPI("cerebralcortex",
      title: "Cerebral Cortex",
      userInterfaces: [
        UserInterface(
          "main", title: 'Main Page', description: 'Main Page',
          widget: ServerPage(),
        ),
        UserInterface(
          "login", title: 'Login', description: 'Login to Cerebral Cortex',
          widget: LoginPage(),
        ),
      ],
    );
    return m;
  }
}