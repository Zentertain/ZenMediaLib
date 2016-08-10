package com.zenjoy.player;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import org.videolan.libvlc.IVLCVout;

public class SurfaceViewPlayer extends BaseViewPlayer {

    public SurfaceViewPlayer(View playerView, SurfaceView surfaceView, String fileFullName, int surfaceSizeMode, ZMediaPlayerCallback callback) {
        super(playerView, surfaceView, fileFullName, surfaceSizeMode, callback);

        handleAttach(surfaceView);
    }

    public SurfaceViewPlayer(View playerView, SurfaceView surfaceView, String fileFullName, int surfaceSizeMode, ZMediaPlayerCallback callback, int videoHeight) {
        super(playerView, surfaceView, fileFullName, surfaceSizeMode, callback, videoHeight);

        handleAttach(surfaceView);
    }

    @Override
    protected void setVideoView(IVLCVout ivlcVout) {
        ivlcVout.setVideoView((SurfaceView) playerView);
    }

    protected void handleAttach(SurfaceView surfaceView) {
        setAttached(surfaceView.getHolder().getSurface() != null);
        if(isAttached()) {
            attachView();
        }else {
            addAttachListener(surfaceView);
        }
    }

    @Override
    protected void addAttachListener(View playerView) {
        ((SurfaceView)playerView).getHolder().addCallback(new SurfaceHolder.Callback2() {
            @Override
            public void surfaceRedrawNeeded(SurfaceHolder holder) {

            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                handleSurfaceAvailable();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }
}
