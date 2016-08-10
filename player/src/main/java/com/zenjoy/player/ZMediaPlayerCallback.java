package com.zenjoy.player;

/**
 * Created by zhaomingliang on 15/6/28.
 */
public interface ZMediaPlayerCallback {
    void onPlay(long duration);
    void onPaused(float position);
    void onStopped();
    void onComplete(ZMediaPlayer player);
    void onPostPosition(float position);
    void onError(Exception throwable);
    void onHardwareAccelerationError();
}
