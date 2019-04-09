import 'package:flushbar/flushbar.dart';
import 'package:flutter/material.dart';

class CommonFlushBar {
  Flushbar<bool> showError(BuildContext context, String message) {
    return Flushbar<bool>(
      message: message,
      icon: Icon(
        Icons.error_outline,
        size: 28.0,
        color: Colors.white,
      ),
      mainButton: FlatButton(
        onPressed: () {},
        child: Text(
          "Dismiss",
          style: TextStyle(color: Colors.amber),
        ),
      ),
      backgroundGradient: new LinearGradient(
          colors: [Colors.red.shade900, Colors.red.shade300]),
      duration: Duration(seconds: 3),
      leftBarIndicatorColor: Colors.red.shade900,
      isDismissible: true,
    )..show(context);
  }

  Flushbar<bool> showSuccess(BuildContext context, String message) {
    return Flushbar<bool>(
        message: message,
        icon: Icon(
          Icons.check_circle_outline,
          size: 28.0,
          color: Colors.white,
        ),
        backgroundGradient: new LinearGradient(
            colors: [Colors.green.shade900, Colors.green.shade300]),
        duration: Duration(seconds: 3),
        leftBarIndicatorColor: Colors.green.shade900)
      ..show(context);
  }

/*
  void showProgress(BuildContext context, String message){
    Flushbar()
      ..message = message
      ..icon = Icon(
        Icons.error_outline,
        size: 28.0,
        color: Colors.white,
      )
      ..backgroundGradient = new LinearGradient(colors: [Colors.red])
      ..backgroundColor=Colors.red
      ..shadowColor = Colors.white
      ..duration = Duration(seconds: 5)
      ..leftBarIndicatorColor = Colors.red.shade900
      ..show(context);

  }
*/
  Flushbar<bool> showProgress(BuildContext context, String msg) {
    return Flushbar<bool>(
      message: msg,
      showProgressIndicator: true,
      progressIndicatorBackgroundColor: Colors.grey[800],
    )..show(context);
  }
}
