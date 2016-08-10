package com.zenjoy.player;

import android.view.View.MeasureSpec;

public enum ZDisplayMode {

    BEST_FIT {
        @Override
        public int[] measure(int widthMeasureSpec, int heightMeasureSpec, int videoWidth, int videoHeight) {

            if (videoWidth < 1 || videoHeight < 1) {
                return measureDefault(widthMeasureSpec, heightMeasureSpec);
            }

            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int[] measure = new int[2];

            float scaleWidth = width / (float) videoWidth;
            float scaleHeight = height / (float) videoHeight;

            if (scaleWidth < scaleHeight) {
                measure[0] = width;
                measure[1] = (int) (scaleWidth*videoHeight);
            }else {
                measure[1] = height;
                measure[0] = (int) (scaleHeight*videoWidth);
            }

            return measure;
        }
    },
    PLAYER_FIT_HORIZONTAL {
        @Override
        public int[] measure(int widthMeasureSpec, int heightMeasureSpec, int videoWidth, int videoHeight) {

            if (videoWidth < 1 || videoHeight < 1) {
                return measureDefault(widthMeasureSpec, heightMeasureSpec);
            }

            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int[] measure = new int[2];
            measure[0] = width;

            float scale = width / (float) videoWidth;
            measure[1] = (int) (scale * videoHeight);

            return measure;
        }
    },
    PLAYER_FIT_VERTICAL {
        @Override
        public int[] measure(int widthMeasureSpec, int heightMeasureSpec, int videoWidth, int videoHeight) {

            if (videoWidth < 1 || videoHeight < 1) {
                return measureDefault(widthMeasureSpec, heightMeasureSpec);
            }

            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int[] measure = new int[2];
            measure[1] = height;

            float scale = height / (float) videoHeight;
            measure[0] = (int) (scale * videoWidth);

            return measure;
        }
    },
    PLAYER_FILL {
        @Override
        public int[] measure(int widthMeasureSpec, int heightMeasureSpec, int videoWidth, int videoHeight) {

            if (videoWidth < 1 || videoHeight < 1) {
                return measureDefault(widthMeasureSpec, heightMeasureSpec);
            }

            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int[] measure = new int[2];
            measure[0] = width;
            measure[1] = height;
            return measure;
        }
    };

    private static int[] measureDefault(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int[] measure = new int[2];
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        if (modeWidth == MeasureSpec.AT_MOST || modeWidth == MeasureSpec.EXACTLY) {
            measure[0] = width;
        }
        if (modeHeight == MeasureSpec.AT_MOST || modeHeight == MeasureSpec.EXACTLY) {
            measure[1] = height;
        }

        return measure;
    }

    public abstract int[] measure(int widthMeasureSpec, int heightMeasureSpec, int videoWidth, int videoHeight);
}
