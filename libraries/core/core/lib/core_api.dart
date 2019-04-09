import 'package:core/ui/page/login_page.dart';
import 'package:core/ui/page/main_page.dart';
import 'package:extensionapi/extensionapi.dart';

class CoreAPI {
  static MCExtensionAPI get() {
    MCExtensionAPI m;
    m = new MCExtensionAPI(
      "core",
      title: "Core",
      userInterfaces: [
        UserInterface(
          "main",
          title: 'Main Page',
          description: 'Main Page',
          widget: (a) {
            return MainPage(a);
          },
        ),
        UserInterface("login",
            title: 'Login',
            description: 'Login to Cerebral Cortex', widget: (map) {
          return LoginPage(map);
        }),
      ],
    );

    return m;
  }
}
