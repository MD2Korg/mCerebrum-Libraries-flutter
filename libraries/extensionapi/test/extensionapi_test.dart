import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:extensionapi/extensionapi.dart';
import 'login_page.dart';

void main() {

  Widget callback(Map<String, dynamic> params){
    return LoginPage(params["userName"]);
  }
  Widget callback1(int a){
    return LoginPage("abc");
  }

  test('adds one to input values', () {
    MCExtensionAPI m = new MCExtensionAPI("abc",userInterfaces: [
      UserInterface("abc",title:"ABC",widget: (a){

      }),
    ]
    );
//    m.userInterfaces[0].mcWidget(10,20,30);
//    m.userInterfaces[0].mcWidget.setCallback(callback);

//    final calculator = new MCExtensionAPILibrary("abc");
/*
    expect(calculator.addOne(2), 3);
    expect(calculator.addOne(-7), -6);
    expect(calculator.addOne(0), 1);
    expect(() => calculator.addOne(null), throwsNoSuchMethodError);
*/
  });
}
