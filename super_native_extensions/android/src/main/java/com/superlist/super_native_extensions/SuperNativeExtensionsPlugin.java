package com.superlist.super_native_extensions;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;

/**
 * SuperNativeExtensionsPlugin
 */
public class SuperNativeExtensionsPlugin implements FlutterPlugin, ActivityAware {

    static final ClipDataHelper ClipDataHelper = new ClipDataHelper();
    static final DragDropHelper DragDropHelper = new DragDropHelper();

    private static boolean nativeInitialized = false;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        try {
            if (!nativeInitialized) {
                init(flutterPluginBinding.getApplicationContext(), ClipDataHelper, DragDropHelper);
                nativeInitialized = true;
            }
        } catch (Throwable e) {
            Log.e("flutter", e.toString());
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        final Activity activity = binding.getActivity();
        activity.getWindow().getDecorView().post(() ->
                DragDropHelper.attachDropHandler(
                        activity.findViewById(FlutterActivity.FLUTTER_VIEW_ID)));
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        onAttachedToActivity(binding);
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
    }

    @Override
    public void onDetachedFromActivity() {
    }

    public static native void init(Context context,
                                   ClipDataHelper ClipDataHelper,
                                   DragDropHelper DragDropHelper);

    static {
        System.loadLibrary("super_native_extensions");
    }
}
