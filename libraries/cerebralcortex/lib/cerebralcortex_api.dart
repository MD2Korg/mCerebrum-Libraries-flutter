import 'package:extensionapi/extensionapi.dart';
import 'login_page.dart';

class CerebralCortexAPI {
  static MCExtensionAPI get() {
    MCExtensionAPI m;
    m = new MCExtensionAPI("cerebralcortex",
      title: "Cerebral Cortex",
      userInterfaces: [
        UserInterface(
          "login", title: 'Login', description: 'Login to Cerebral Cortex',
          widget: LoginPage(),
        ),
        UserInterface(
          "logout", title: 'Logout', description: 'Logout from Cerebral Cortex',
          widget: LoginPage(),
        ),
      ],
    );
    return m;
  }
}