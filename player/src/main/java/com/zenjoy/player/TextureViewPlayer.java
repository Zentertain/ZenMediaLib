package com.zenjoy.player;

import android.graphics.SurfaceTexture;
import android.view.TextureView;
import android.view.View;

import org.videolan.libvlc.IVLCVout;

public class TextureViewPlayer extends BaseViewPlayer {
    public TextureViewPlayer(View playerView, TextureView textureView, String fileFullName, int surfaceSizeMode, ZMediaPlayerCallback callback) {
        super(playerView, textureView, fileFullName, surfaceSizeMode, callback);

        handleAttach(textureView);
    }

    public TextureViewPlayer(View playerView, TextureView textureView, String fileFullName, int surfaceSizeMode, ZMediaPlayerCallback callback, int videoHeight) {
        super(playerView, textureView, fileFullName, surfaceSizeMode, callback, videoHeight);

        handleAttach(textureView);
    }

    @Override
    protected void setVideoView(IVLCVout ivlcVout) {
        ivlcVout.setVideoView((TextureView) playerView);
    }

    protected void handleAttach(TextureView textureView) {
        setAttached(textureView.isAvailable());
        if(isAttached()) {
            attachView();
        }else {
            addAttachListener(textureView);
        }
    }

    @Override
    protected void addAttachListener(View playerView) {
        ((TextureView)playerView).setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                handleSurfaceAvailable();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }
}
