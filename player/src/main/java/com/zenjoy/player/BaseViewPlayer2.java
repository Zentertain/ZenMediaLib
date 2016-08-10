package com.zenjoy.player;

import android.content.Context;
import android.view.View;

public abstract class BaseViewPlayer2 extends ZMediaPlayer2 {
    protected boolean attached = false;
    protected boolean requestPlay = false;
    protected boolean hasVideo;

    public BaseViewPlayer2(Context context, String fileFullName, ZMediaPlayerCallback2 callback) {
        super(context, fileFullName, callback);
    }

    public BaseViewPlayer2(Context context, View playerView, String fileFullName, ZMediaPlayerCallback2 callback) {
        super(context, playerView, fileFullName, callback);
    }

    public boolean requestPlay() {
        return requestPlay(true);
    }

    public boolean requestPlay(boolean hasVideo) {
        try {

            setHasVideo(hasVideo);
            setRequestPlay(true);
            if (isAttached()) {
                play(hasVideo);
            }

            return isAttached();
        } catch (Exception e) {
            e.printStackTrace();
            if (callBack != null) {
                callBack.onError(e);
            }
        }

        return false;
    }

    protected void handleSurfaceAvailable() {
        if (isStopped())
            return;

        try {
            setAttached(true);
            attachView();
            if (isRequestPlay()) {
                play(hasVideo());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (callBack != null) {
                callBack.onError(e);
            }
        }
    }

    protected boolean isAttached() {
        return attached;
    }

    protected void setAttached(boolean attached) {
        this.attached = attached;
    }

    protected boolean isRequestPlay() {
        return requestPlay;
    }

    protected void setRequestPlay(boolean requestPlay) {
        this.requestPlay = requestPlay;
    }

    protected boolean hasVideo() {
        return hasVideo;
    }

    protected void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }
}
