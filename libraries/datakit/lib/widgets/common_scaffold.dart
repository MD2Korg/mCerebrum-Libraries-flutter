import 'package:flutter/material.dart';
import 'common_drawer.dart';
import 'common_bottom_navigation.dart';

class CommonScaffold extends StatelessWidget {
  final appTitle;
  final Widget bodyData;
  final showFAB;
  final showDrawer;
  final backGroundColor;
  final actionFirstIcon;
  final scaffoldKey;
  final showBottomNav;
  final floatingIcon;
  final centerDocked;
  final elevation;

  CommonScaffold(
      {this.appTitle,
      this.bodyData,
      this.showFAB = false,
      this.showDrawer = false,
      this.backGroundColor,
      this.actionFirstIcon,
      this.scaffoldKey,
      this.showBottomNav = false,
      this.centerDocked = false,
      this.floatingIcon,
      this.elevation = 4.0});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      key: scaffoldKey != null ? scaffoldKey : null,
      backgroundColor: backGroundColor != null ? backGroundColor : null,
      appBar: AppBar(
        elevation: elevation,
        backgroundColor: Colors.black,
        title: Text(appTitle),
        actions: <Widget>[
          SizedBox(
            width: 5.0,
          ),
          IconButton(
            onPressed: () {},
            icon: Icon(Icons.location_on, color: Colors.green,),

          ),
          IconButton(
            onPressed: () {},
            icon: Icon(Icons.watch, color: Colors.red,),

          ),
          IconButton(
            onPressed: () {},
            icon: Icon(Icons.cloud, color: Colors.yellow,),

          ),
/*
          IconButton(
            onPressed: () {
            },
            icon: Icon(Icons.close),
          )
*/
        ],
      ),
      drawer: showDrawer ? CommonDrawer() : null,
      body: Container(
        // Add box decoration
          decoration: BoxDecoration(
            // Box decoration takes a gradient
            gradient: LinearGradient(
              // Where the linear gradient begins and ends
              begin: Alignment.topLeft,
              end: Alignment.bottomLeft,
              // Add one stop for each color. Stops should increase from 0 to 1
//        stops: [0.1, 0.5, 0.7, 0.9],
              colors: [
                // Colors are easy thanks to Flutter's Colors class.
                Colors.black,
                Colors.teal.shade900,
              ],
            ),
          ),
        child: bodyData,
      ),
      floatingActionButton: null,
      floatingActionButtonLocation: centerDocked
          ? FloatingActionButtonLocation.centerDocked
          : FloatingActionButtonLocation.endFloat,
      bottomNavigationBar: showBottomNav ? CommonBottomNavigation() : null,
    );
  }
}
