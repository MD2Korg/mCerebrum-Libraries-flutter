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
  String _id;
  Widget Function() _widget;
  UserInterface(String id, Function() widget){
    this._id = id;
    this._widget = widget;
  }

  Function get widget => _widget;

  String get id => _id;

}
