package com.zenjoy.player;

import android.content.Context;
import android.view.View;

import org.videolan.libvlc.IVLCVout;

public class AudioPlayer extends BaseViewPlayer {
    public AudioPlayer(Context context, String fileFullName, ZMediaPlayerCallback callback) {
        super(context, fileFullName, callback);

        setAttached(true);
    }

    @Override
    protected void setVideoView(IVLCVout ivlcVout) {
    }

    @Override
    protected void addAttachListener(View playerView) {
    }
}
