package com.example.whoappproxy;

import java.lang.reflect.Method;
import androidx.annotation.NonNull;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ProxyInfo;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** WhoappproxyPlugin */
public class WhoappproxyPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private Registrar registrar;
  private ConnectivityManager manager;
  private MethodChannel channel;

  public static void registerWith(Registrar registrar) {

    WhoappproxyPlugin plugin = new WhoappproxyPlugin();
    plugin.setupObjects(registrar);
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    setupChannels(flutterPluginBinding.getBinaryMessenger(), flutterPluginBinding.getApplicationContext());
  }

  private void setupObjects(Registrar registrar)
  {
    this.registrar = registrar;
    this.manager = (ConnectivityManager) registrar
    .context()
    .getApplicationContext()
    .getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  private void setupChannels(BinaryMessenger messenger, Context context)
  {
    channel = new MethodChannel(messenger, "whoappproxy");
    channel.setMethodCallHandler(this);
    this.manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    String proxy, proxyHost, proxyPort;

    if (call.method.equals("getProxy")) {
      try 
      {
        if(manager == null)
        {
          throw(new Exception("Manager is null"));
        }
        ProxyInfo proxyInfo = this.manager.getDefaultProxy();
        proxyHost = proxyInfo.getHost();
        proxyPort = String.valueOf(proxyInfo.getPort());
        if(proxyHost != null && proxyPort != null)
        {
          proxy = proxyHost + ":" + proxyPort;
        } else {
          proxy = "No proxyHost or proxtPort";
        }
        result.success(proxy);
      }
      catch (Exception e)
      {
        result.success("exception - " + e.toString());
      }
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
