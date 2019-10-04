import 'package:flutter/material.dart';

class PrivacyPage extends StatefulWidget {
  @override
  _PrivacyPageState createState() => _PrivacyPageState();
}

class _PrivacyPageState extends State<PrivacyPage> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: AppBar(elevation: 4.0, title: Text("Storage")),
      body: bodyData(context),
    );
  }

  Widget bodyData(BuildContext context) {
    return Container();
  }
}
