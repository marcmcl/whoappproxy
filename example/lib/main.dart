import 'dart:io';
import 'dart:convert';
import 'package:flutter/material.dart';
import 'dart:async';
import 'package:http/http.dart';
import 'package:flutter/services.dart';
import 'package:whoappproxy/whoappproxy.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _proxy = 'Unknown';
  String _response;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String proxy;

    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      proxy = await Whoappproxy.getProxy;
    } on PlatformException {
      proxy = 'Failed to get proxy.';
    }

    
    var client = new HttpClient();
    client.findProxy = (uri) { return "PROXY " + proxy; };
    var request = await client.getUrl(Uri.parse("https://darthttptest.azurewebsites.net/ip.js"));
    var response = await request.close();
    _response = '';
    await for (var contents in response.transform(Utf8Decoder())) {
      _response = _response + contents;
    };
    
    

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _proxy = proxy;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('WHO App Proxy Plugin'),
        ),
        body: Center(
          child: Text('Proxy: $_proxy\nResponse: $_response\n'),
        ),
      ),
    );
  }
}
