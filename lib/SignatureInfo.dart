import 'dart:io' show Platform;

import 'package:flutter/services.dart';

class SignatureInfo {
  static const MethodChannel _channel = MethodChannel('app/signature');

  static Future<String?> getSHA1() async {
    if (!Platform.isAndroid) {
      throw UnsupportedError('Only Android is supported');
    }

    try {
      final String signature = await _channel.invokeMethod('getSignature');
      return signature;
    } on PlatformException catch (e) {
      throw Exception('Failed to get signature: ${e.message}');
    }
  }
}
