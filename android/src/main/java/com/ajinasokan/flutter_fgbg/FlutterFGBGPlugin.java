package com.ajinasokan.flutter_fgbg;


import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterFGBGPlugin */
public class FlutterFGBGPlugin implements FlutterPlugin, ActivityAware, LifecycleObserver, EventChannel.StreamHandler, Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {
  EventChannel.EventSink lifecycleSink;
  private static boolean isInBackground = false;


  @Override
  public void onListen(Object o, EventChannel.EventSink eventSink) {
    lifecycleSink = eventSink;
  }

  @Override
  public void onCancel(Object o) {
    lifecycleSink = null;
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    new EventChannel(flutterPluginBinding.getBinaryMessenger(), "com.ajinasokan.flutter_fgbg/events")
            .setStreamHandler(this);
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {}

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    binding.getActivity().getApplication().registerActivityLifecycleCallbacks(this);
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  void onEvent1() {
    Log.d("FGBG", "ON_CREATE");
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  void onEvent2() {
    Log.d("FGBG", "ON_DESTROY");
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
  void onEvent3() {
    Log.d("FGBG", "ON_PAUSE");
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  void onEvent4() {
    Log.d("FGBG", "ON_RESUME");
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  void onEvent5() {
    Log.d("FGBG", "ON_START");
    if (lifecycleSink != null) {
      lifecycleSink.success("foreground");
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  void onEvent6() {
    Log.d("FGBG", "ON_STOP");
    if (lifecycleSink != null) {
      lifecycleSink.success("background");
    }
  }

//  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
//  void onAppBackgrounded() {
////    if (lifecycleSink != null) {
////      lifecycleSink.success("background");
////    }
//  }
//
//  @OnLifecycleEvent(Lifecycle.Event.ON_START)
//  void onAppForegrounded() {
////    if (lifecycleSink != null) {
////      lifecycleSink.success("foreground");
////    }
//  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {}

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {}

  @Override
  public void onDetachedFromActivity() {
    ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
  }

  @Override
  public void onActivityCreated(Activity activity, Bundle bundle) {
    Log.d("FGBG", "onActivityCreated | " + activity.getLocalClassName());
  }

  @Override
  public void onActivityStarted(Activity activity) {
    Log.d("FGBG", "onActivityStarted | " + activity.getLocalClassName());
  }

  @Override
  public void onActivityResumed(Activity activity) {
    Log.d("FGBG", "onActivityResumed | " + activity.getLocalClassName());

    if(isInBackground){
      if (lifecycleSink != null) {
        lifecycleSink.success("foreground");
      }
      isInBackground = false;
    }
  }

  @Override
  public void onActivityPaused(Activity activity) {
    Log.d("FGBG", "onActivityPaused | " + activity.getLocalClassName());
  }

  @Override
  public void onActivityStopped(Activity activity) {
    Log.d("FGBG", "onActivityStopped | " + activity.getLocalClassName());
  }

  @Override
  public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    Log.d("FGBG", "onActivitySaveInstanceState | " + activity.getLocalClassName());
  }

  @Override
  public void onActivityDestroyed(Activity activity) {
    Log.d("FGBG", "onActivityDestroyed | " + activity.getLocalClassName());
  }

  @Override
  public void onConfigurationChanged(Configuration configuration) {
    Log.d("FGBG", "onConfigurationChanged");
  }

  @Override
  public void onLowMemory() {
    Log.d("FGBG", "onLowMemory");
  }

  @Override
  public void onTrimMemory(int i) {
    Log.d("FGBG", "onTrimMemory");
    if(i == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN){
      if (lifecycleSink != null) {
        lifecycleSink.success("background");
      }
      isInBackground = true;
    }
  }
}