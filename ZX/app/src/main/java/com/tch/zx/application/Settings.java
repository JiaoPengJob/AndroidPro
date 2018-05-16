package com.tch.zx.application;

import android.content.Context;

import com.tch.zx.R;
import com.tch.zx.util.SharedPrefsUtil;

/**
 * IJKPlayer设置器
 */

public class Settings {

    private Context mAppContext;

    public static final int PV_PLAYER__Auto = 0;
    public static final int PV_PLAYER__AndroidMediaPlayer = 1;
    public static final int PV_PLAYER__IjkMediaPlayer = 2;
    public static final int PV_PLAYER__IjkExoMediaPlayer = 3;

    public Settings(Context context) {
        mAppContext = context.getApplicationContext();
    }

    public boolean getEnableBackgroundPlay() {
        String key = mAppContext.getString(R.string.pref_key_enable_background_play);
        return SharedPrefsUtil.getValue(mAppContext, key, false);
    }

    public int getPlayer() {
        String key = mAppContext.getString(R.string.pref_key_player);
        String value = SharedPrefsUtil.getValue(mAppContext, key, "");
        try {
            return Integer.valueOf(value).intValue();
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public boolean getUsingMediaCodec() {
        String key = mAppContext.getString(R.string.pref_key_using_media_codec);
        return SharedPrefsUtil.getValue(mAppContext, key, false);
    }

    public boolean getUsingMediaCodecAutoRotate() {
        String key = mAppContext.getString(R.string.pref_key_using_media_codec_auto_rotate);
        return SharedPrefsUtil.getValue(mAppContext, key, false);
    }

    public boolean getMediaCodecHandleResolutionChange() {
        String key = mAppContext.getString(R.string.pref_key_media_codec_handle_resolution_change);
        return SharedPrefsUtil.getValue(mAppContext, key, false);
    }

    public boolean getUsingOpenSLES() {
        String key = mAppContext.getString(R.string.pref_key_using_opensl_es);
        return SharedPrefsUtil.getValue(mAppContext, key, false);
    }

    public String getPixelFormat() {
        String key = mAppContext.getString(R.string.pref_key_pixel_format);
        return SharedPrefsUtil.getValue(mAppContext, key, "");
    }

    public boolean getEnableNoView() {
        String key = mAppContext.getString(R.string.pref_key_enable_no_view);
        return SharedPrefsUtil.getValue(mAppContext, key, false);
    }

    public boolean getEnableSurfaceView() {
        String key = mAppContext.getString(R.string.pref_key_enable_surface_view);
        return SharedPrefsUtil.getValue(mAppContext, key, false);
    }

    public boolean getEnableTextureView() {
        String key = mAppContext.getString(R.string.pref_key_enable_texture_view);
        return SharedPrefsUtil.getValue(mAppContext, key, false);
    }

    public boolean getEnableDetachedSurfaceTextureView() {
        String key = mAppContext.getString(R.string.pref_key_enable_detached_surface_texture);
        return SharedPrefsUtil.getValue(mAppContext, key, false);
    }

    public boolean getUsingMediaDataSource() {
        String key = mAppContext.getString(R.string.pref_key_using_mediadatasource);
        return SharedPrefsUtil.getValue(mAppContext, key, false);
    }

    public String getLastDirectory() {
        String key = mAppContext.getString(R.string.pref_key_last_directory);
        return SharedPrefsUtil.getValue(mAppContext, key, "/");
    }

    public void setLastDirectory(String path) {
        String key = mAppContext.getString(R.string.pref_key_last_directory);
        SharedPrefsUtil.putValue(mAppContext, key, path);
    }

}
