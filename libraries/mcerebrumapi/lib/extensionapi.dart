library extensionapi;
import 'package:flutter/material.dart';

class MCExtensionAPI {
  List<UserInterface> _userInterfaces;
  MCExtensionAPI(List<UserInterface> userInterfaces){
    this._userInterfaces = userInterfaces;
  }

  List<UserInterface> get userInterfaces => _userInterfaces;

}
class UserInterface{
  final Widget Function() widget;
  UserInterface(this.widget);
}
