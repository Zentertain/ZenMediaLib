package com.zenjoy.player;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.TextureView;

public class PlayerTextureView extends TextureView implements ZMediaPlayer2.ZPlayerView {

    private ZDisplayMode displayMode = ZDisplayMode.BEST_FIT;
    private int videoWith = -1;
    private int videoHeight = -1;

    public PlayerTextureView(Context context) {
        super(context);
    }

    public PlayerTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlayerTextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ZDisplayMode getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(ZDisplayMode displayMode) {
        this.displayMode = displayMode;

        requestLayout();
    }

    @Override
    public void setVideoSize(int width, int height) {

        this.videoWith = width;
        this.videoHeight = height;

        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] measure = displayMode.measure(widthMeasureSpec, heightMeasureSpec, videoWith, videoHeight);
        setMeasuredDimension(measure[0], measure[1]);
    }
}
