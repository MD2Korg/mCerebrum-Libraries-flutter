import 'package:core/ui/page/login_page.dart';
import 'package:core/ui/page/main_page.dart';
import 'package:mcerebrumapi/extensionapi.dart';

class CoreAPI {
  static MCExtensionAPI get() {
    MCExtensionAPI m;
    m = new MCExtensionAPI([
        UserInterface(
          "main",
              () {
            return MainPage();
          },
        ),
      UserInterface("login", () {
        return LoginPage();
        }),
      ],
    );

    return m;
  }
}
