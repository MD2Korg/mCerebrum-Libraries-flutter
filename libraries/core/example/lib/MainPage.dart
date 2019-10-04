import 'package:flutter/material.dart';
import 'package:mcerebrumapi/mc_library.dart';

class MainPage extends StatelessWidget {
//  final MCExtensionAPI api = CoreAPI.get();
  final List<MCLibrary> mcLibraries;

  MainPage(this.mcLibraries);

  Widget bodyData(BuildContext context) => Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Column(
              // crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                RaisedButton(
                  child: Text("Main"),
                  onPressed: () async {
                    Navigator.pushNamed(context, "main");
//                await Navigator.push(context, MaterialPageRoute(builder: (context) => MainPage()));
                  },
                ),
                RaisedButton(
                  child: Text("Login"),
                  onPressed: () async {
                    Navigator.pushNamed(context, "login");
//                await Navigator.push(context, MaterialPageRoute(builder: (context) => MainPage()));
                  },
                ),
                RaisedButton(
                  child: Text("Storage"),
                  onPressed: () async {
                    Navigator.pushNamed(context, "storage");
//                await Navigator.push(context, MaterialPageRoute(builder: (context) => MainPage()));
                  },
                ),
                RaisedButton(
                  child: Text("Download Config"),
                  onPressed: () async {
//                    var res = await api.actions["download"]("decisions.json");
//                    Navigator.pushNamed(context, "storage");
//                await Navigator.push(context, MaterialPageRoute(builder: (context) => MainPage()));
                  },
                ),
                RaisedButton(
                  child: Text("PhoneSense Settings"),
                  onPressed: () async {
//                    var res = await api.actions["download"]("decisions.json");
                    await Navigator.push(
                        context,
                        MaterialPageRoute(
                            builder: (context) =>
                                mcLibraries[1].iExec.settings.ui(null)));
//                    Navigator.pushNamed(context, "storage");
//                await Navigator.push(context, MaterialPageRoute(builder: (context) => MainPage()));
                  },
                ),
              ],
            )
          ],
        ),
      );

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "Core Example",
          style: TextStyle(color: Colors.black),
        ),
      ),
      body: SingleChildScrollView(child: bodyData(context)),
    );
  }
}
