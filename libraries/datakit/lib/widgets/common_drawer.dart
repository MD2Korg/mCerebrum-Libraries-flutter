import 'package:flutter/material.dart';

class CommonDrawer extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: ListView(
        padding: EdgeInsets.zero,
        children: <Widget>[
          UserAccountsDrawerHeader(
            accountName: Text(
              "Nusrat Nasrin",
            ),
            accountEmail: Text(
              "nusrat27@gmail.com",
            ),
            currentAccountPicture: new CircleAvatar(
//              backgroundImage: new AssetImage(UIData.blankImage),
            ),
          ),
          new ListTile(
            title: Text(
              "Profile",
              style: TextStyle(fontSize: 18.0),
            ),
            leading: Icon(
              Icons.person,
              color: Colors.blue,
            ),
          ),
          new ListTile(
            title: Text(
              "Bug Report",
              style: TextStyle(fontSize: 18.0),
            ),
            leading: Icon(
              Icons.bug_report,
              color: Colors.green,
            ),
          ),
          new ListTile(
            title: Text(
              "Feedback",
              style: TextStyle(fontWeight: FontWeight.w700, fontSize: 18.0),
            ),
            leading: Icon(
              Icons.feedback,
              color: Colors.red,
            ),
          ),
          new ListTile(
            title: Text(
              "Contact Us",
              style: TextStyle(fontWeight: FontWeight.w700, fontSize: 18.0),
            ),
            leading: Icon(
              Icons.contact_mail,
              color: Colors.cyan,
            ),
          ),
          Divider(),
          new ListTile(
            title: Text(
              "Tutorial",
              style: TextStyle(fontWeight: FontWeight.w700, fontSize: 18.0),
            ),
            leading: Icon(
              Icons.help,
              color: Colors.brown,
            ),
          ),
          Divider(),
        ],
      ),
    );
  }
}
