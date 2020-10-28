
import 'dart:async';

import 'package:flutter/services.dart';

class Whoappproxy {

  Whoappproxy._();

  static const MethodChannel _channel =
      const MethodChannel('whoappproxy');

  static Future<String> get getProxy async {
    final String version = await _channel.invokeMethod('getProxy');
    return version;
  }
}
