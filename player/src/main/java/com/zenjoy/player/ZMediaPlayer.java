package com.zenjoy.player;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.AndroidUtil;
import org.videolan.libvlc.util.HWDecoderUtil;
import org.videolan.libvlc.util.VLCUtil;

import java.util.ArrayList;

public abstract class ZMediaPlayer implements IVLCVout.Callback {
    private final static String TAG = "ZMediaPlayer";

    public static final int AOUT_AUDIOTRACK = 0;
    public static final int AOUT_OPENSLES = 1;
    public static final int HW_ACCELERATION_AUTOMATIC = -1;
    public static final int HW_ACCELERATION_DISABLED = 0;
    public static final int HW_ACCELERATION_DECODING = 1;
    public static final int HW_ACCELERATION_FULL = 2;
    public final static int MEDIA_NO_VIDEO = 0x01;
    public final static int MEDIA_NO_HWACCEL = 0x02;
    public final static int MEDIA_PAUSED = 0x4;

    public static final int PLAYER_BEST_FIT = 0;
    public static final int PLAYER_FIT_HORIZONTAL = 1;
    public static final int PLAYER_FIT_VERTICAL = 2;
    public static final int PLAYER_FILL = 3;
    public static final int PLAYER_16_9 = 4;
    public static final int PLAYER_4_3 = 5;
    public static final int PLAYER_ORIGINAL = 6;
    private static final String DEFAULT_CODEC_LIST = "mediacodec_ndk,mediacodec_jni,iomx,all";

    private final static boolean HDMI_AUDIO_ENABLED = false;

    private VLCMediaPlayer mediaPlayer;
    protected LibVLC libVLC = null;
    private String fileFullName;
    protected View playerContainer;
    protected View playerView;
    protected ZMediaPlayerCallback callBack;
    public int currentSize = PLAYER_BEST_FIT;

    //TODO:
    private int mVideoHeight;
    private int videoHeight;

    private int videoWidth;
    private int videoVisibleHeight;
    private int videoVisibleWidth;

    private int sarNum;
    private int sarDen;

    private Handler eventHandler;
    private long mediaDuration = 0;
    private float position = 0f;
    private Context context;

    private volatile boolean stopped;

    public ZMediaPlayer(Context context, String fileFullName, final ZMediaPlayerCallback callback) {
        this.context = context;

        libVLC = createVLC(context);
        mediaPlayer = new VLCMediaPlayer(libVLC);

        this.fileFullName = fileFullName;
        callBack = callback;

        eventHandler = new VideoPlayerEventHandler(callBack);

        init();
    }

    public ZMediaPlayer(View playerContainer, View playerView, String fileFullName,
                        final int playerSizeMode, final ZMediaPlayerCallback callback) {
        this.playerContainer = playerContainer;
        this.context = playerContainer.getContext();

        libVLC = createVLC(context);

        mediaPlayer = new VLCMediaPlayer(libVLC);

        this.playerView = playerView;
        this.fileFullName = fileFullName;
        callBack = callback;

        eventHandler = new VideoPlayerEventHandler(callBack);
        if (playerSizeMode >= 0) {
            currentSize = playerSizeMode;
        }

        init();
    }

    public ZMediaPlayer(View playerContainer, View playerView, String fileFullName,
                        final int playerSizeMode, final ZMediaPlayerCallback callback, int videoHeight) {
        this(playerContainer, playerView, fileFullName, playerSizeMode, callback);
        this.videoHeight = videoHeight;
    }

    private void init() {
        final IVLCVout vlcVOut = mediaPlayer.getVLCVout();
        vlcVOut.setCallback(this);
        EventHandler.getInstance().addHandler(eventHandler);

        setVideoView(vlcVOut);
    }

    protected void attachView() {
        IVLCVout vlcVOut = mediaPlayer.getVLCVout();
        if (!vlcVOut.areViewsAttached()) {
            vlcVOut.attachViews();
        }
    }

    protected abstract void setVideoView(IVLCVout ivlcVout);

    protected abstract void addAttachListener(View playerView);

    private static LibVLC createVLC(Context applicationContext) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        if (!VLCUtil.hasCompatibleCPU(applicationContext)) {
            Log.e(TAG, VLCUtil.getErrorMsg());
            throw new IllegalStateException("LibVLC initialisation failed: " + VLCUtil.getErrorMsg());
        }

        LibVLC libVLC = new LibVLC(getLibOptions(pref));
        LibVLC.setOnNativeCrashListener(new LibVLC.OnNativeCrashListener() {
            @Override
            public void onNativeCrash() {
            }
        });

        return libVLC;
    }

    public static ArrayList<String> getLibOptions(SharedPreferences pref) {
        ArrayList<String> options = new ArrayList<>(50);

        final boolean timeStreching = pref.getBoolean("enable_time_stretching_audio", false);
        final String subtitlesEncoding = pref.getString("subtitle_text_encoding", "");
        final boolean frameSkip = pref.getBoolean("enable_frame_skip", false);
        String chroma = pref.getString("chroma_format", "");
        chroma = chroma.equals("YV12") && !AndroidUtil.isGingerbreadOrLater() ? "" : chroma;
        final boolean verboseMode = pref.getBoolean("enable_verbose_mode", true);

        //if (pref.getBoolean("equalizer_enabled", false))
        //setEqualizer(Preferences.getFloatArray(pref, "equalizer_values"));

        int aout = -1;
        try {
            aout = Integer.parseInt(pref.getString("aout", "-1"));
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        aout = getAout(aout);

        int deblocking = -1;
        try {
            deblocking = getDeblocking(Integer.parseInt(pref.getString("deblocking", "-1")));
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        int networkCaching = pref.getInt("network_caching_value", 0);
        if (networkCaching > 60000)
            networkCaching = 60000;
        else if (networkCaching < 0)
            networkCaching = 0;

        /* CPU intensive plugin, setting for slow devices */
        options.add(timeStreching ? "--audio-time-stretch" : "--no-audio-time-stretch");
        options.add("--avcodec-skiploopfilter");
        options.add("" + deblocking);
        options.add("--avcodec-skip-frame");
        options.add(frameSkip ? "2" : "0");
        options.add("--avcodec-skip-idct");
        options.add(frameSkip ? "2" : "0");
        options.add("--subsdec-encoding");
        options.add(subtitlesEncoding);
        options.add("--stats");
        /* XXX: why can't the default be fine ? #7792 */
        if (networkCaching > 0)
            options.add("--network-caching=" + networkCaching);
        options.add(aout == AOUT_OPENSLES ? "--aout=opensles" : (aout == AOUT_AUDIOTRACK ? "--aout=android_audiotrack" : "--aout=dummy"));
        options.add("--androidwindow-chroma");
        options.add(chroma.indexOf(0) != 0 ? chroma : "RV32");

        if (HDMI_AUDIO_ENABLED) {
            options.add("--spdif");
            options.add("--audiotrack-audio-channels");
            options.add("8"); // 7.1 maximum
        }
        options.add(verboseMode ? "-vvv" : "-vv");
        return options;
    }

    private static int getAout(int aout) {
        final HWDecoderUtil.AudioOutput hwaout = HWDecoderUtil.getAudioOutputFromDevice();
        if (hwaout == HWDecoderUtil.AudioOutput.AUDIOTRACK || hwaout == HWDecoderUtil.AudioOutput.OPENSLES)
            return hwaout == HWDecoderUtil.AudioOutput.OPENSLES ? AOUT_OPENSLES : AOUT_AUDIOTRACK;

        return aout == AOUT_OPENSLES ? AOUT_OPENSLES : AOUT_AUDIOTRACK;
    }

    private static int getDeblocking(int deblocking) {
        int ret = deblocking;
        if (deblocking < 0) {
            /**
             * Set some reasonable sDeblocking defaults:
             *
             * Skip all (4) for armv6 and MIPS by default
             * Skip non-ref (1) for all armv7 more than 1.2 Ghz and more than 2 cores
             * Skip non-key (3) for all devices that don't meet anything above
             */
            VLCUtil.MachineSpecs m = VLCUtil.getMachineSpecs();
            if (m == null)
                return ret;
            if ((m.hasArmV6 && !(m.hasArmV7)) || m.hasMips)
                ret = 4;
            else if (m.frequency >= 1200 && m.processors > 2)
                ret = 1;
            else if (m.bogoMIPS >= 1200 && m.processors > 2) {
                ret = 1;
                Log.d(TAG, "Used bogoMIPS due to lack of frequency info");
            } else
                ret = 3;
        } else if (deblocking > 4) { // sanity check
            ret = 3;
        }
        return ret;
    }

    protected void play() throws RuntimeException {
        play(true);
    }

    /**
     * 播放音/视频文件
     *
     * @param hasVideo 是否有video
     */
    protected void play(boolean hasVideo) throws RuntimeException {
        if (mediaPlayer == null)
            return;

        final Media media = new Media(libVLC, fileFullName);
        media.parse();
        if (!hasVideo) {
            media.addOption(":no-video");
        }

        mediaPlayer.setMedia(media);
        media.release();

        //VLCInstance.getMainMediaPlayer(this.getApplicationContext()).setEqualizer(VLCOptions.getEqualizer());
        mediaPlayer.setVideoTitleDisplay(MediaPlayer.Position.Disable, 0);

        mediaPlayer.play();
    }

    public boolean isStopped() {
        return stopped;
    }

    public void stop() {
        if (stopped)
            return;

        stopped = true;

        EventHandler.getInstance().removeHandler(eventHandler);
        eventHandler.removeCallbacksAndMessages(null);
        callBack = null;
        if (playerView != null)
            playerView.setKeepScreenOn(false);

//        if (mediaPlayer.isPlaying())
        mediaPlayer.stop();

        final IVLCVout vlcVOut = mediaPlayer.getVLCVout();
        vlcVOut.detachViews();
        vlcVOut.setCallback(null);

        mediaPlayer.release();
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            position = mediaPlayer.getPosition();
        }
    }

    public void resume() {
        if (mediaPlayer != null) {
            if (position > 0) {
                mediaPlayer.setPosition(position);
            }
            mediaPlayer.play();
        }
    }

    public void simplePause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void seekTo(double position) {
        mediaPlayer.setPosition((float) position);
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public long getDuration() {
        return mediaPlayer.getLength();
    }

    public void release() {
        stop();
//        VLCInstance.release();
    }

    @Override
    public void onNewLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int
            visibleHeight, int sarNum, int sarDen) {
        videoWidth = width;
        mVideoHeight = height;
        videoVisibleWidth = visibleWidth;
        videoVisibleHeight = visibleHeight;
        this.sarNum = sarNum;
        this.sarDen = sarDen;
        changePlayerLayout();
    }

    private class VideoPlayerEventHandler extends WeakHandler<ZMediaPlayerCallback> {

        private boolean isOnPlayCallback = false;

        public VideoPlayerEventHandler(ZMediaPlayerCallback owner) {
            super(owner);
        }

        @Override
        public void handleMessage(Message msg) {
            ZMediaPlayerCallback callback = getOwner();
            if (callback == null) return;

            switch (msg.getData().getInt("event")) {
                case EventHandler.MediaParsedChanged:
                    System.out.println("MediaPlayer: " + "MediaParsedChanged");
                    break;
                case EventHandler.MediaPlayerPlaying:
                    System.out.println("MediaPlayer: " + "MediaPlayerPlaying");
                    break;
                case EventHandler.MediaPlayerPaused:
                    if (callBack != null)
                        callBack.onPaused(mediaPlayer.getPosition());
                    break;
                case EventHandler.MediaPlayerStopped:
                    synchronized (mediaPlayer) {
                        if (!mediaPlayer.isReleased()) {
                            if (callBack != null)
                                callBack.onStopped();
                        }
                    }
                    isOnPlayCallback = false;
                    break;
                case EventHandler.MediaPlayerEndReached:
                    if (!isOnPlayCallback) {
                        mediaDuration = mediaPlayer.getLength();
                        if (callBack != null)
                            callBack.onPlay(mediaDuration);
                    }
                    if (callBack != null)
                        callBack.onComplete(ZMediaPlayer.this);
                    break;
                case EventHandler.MediaPlayerVout:
                    break;
                case EventHandler.MediaPlayerPositionChanged:
                    mediaDuration = mediaPlayer.getLength();
                    if (!isOnPlayCallback) {
                        if (callBack != null)
                            callBack.onPlay(mediaDuration);
                        isOnPlayCallback = true;
                    }
                    callBack.onPostPosition(mediaPlayer.getPosition());//TODO:warning: may produce NPE
                    break;
                case EventHandler.MediaPlayerEncounteredError:
                    callBack.onError(new UnknownException("ZMediaPlayer"));
                    break;
                case EventHandler.HardwareAccelerationError:
                    callBack.onHardwareAccelerationError();
                    break;
                case EventHandler.MediaPlayerTimeChanged:
                    // avoid useless error logs
                    break;
                case EventHandler.MediaPlayerESAdded:
                    break;
                case EventHandler.MediaPlayerESDeleted:
                    break;
                default:
                    break;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void changePlayerLayout() {
        System.out.println("MediaPlayer: " + "changePlayerLayout");
        if (playerContainer == null || playerView == null)
            return;

        int sw = playerContainer.getWidth();
        int sh = playerContainer.getHeight();

//        if (Build.MODEL.equalsIgnoreCase("SM-N9100")) { //兼容samsung note4
//            sh = playerContainer.getHeight() - 600;
//        } else {
//            sh = playerContainer.getHeight() - 400;
//        }

        if (videoHeight > 0) {
            sh = videoHeight;
        }
        final IVLCVout vlcVout = mediaPlayer.getVLCVout();
        vlcVout.setWindowSize(sw, sh);

        double dw = sw, dh = sh;
        boolean isPortrait;

        // getWindow().getDecorView() doesn't always take orientation into account, we have to correct the values
        isPortrait = playerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        if (sw > sh && isPortrait || sw < sh && !isPortrait) {
            dw = sh;
            dh = sw;
        }

        // sanity check
        if (dw * dh == 0 || videoWidth * mVideoHeight == 0) {
            Log.e(TAG, "Invalid mediaPlayer size");
            return;
        }

        // compute the aspect ratio
        double ar, vw;
        if (sarDen == sarNum) {
            /* No indication about the density, assuming 1:1 */
            vw = videoVisibleWidth;
            ar = (double) videoVisibleWidth / (double) videoVisibleHeight;
        } else {
            /* Use the specified aspect ratio */
            vw = videoVisibleWidth * (double) sarNum / sarDen;
            ar = vw / videoVisibleHeight;
        }

        // compute the display aspect ratio
        double dar = dw / dh;

        switch (currentSize) {
            case PLAYER_BEST_FIT:
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case PLAYER_FIT_HORIZONTAL:
                dh = dw / ar;
                break;
            case PLAYER_FIT_VERTICAL:
                dw = dh * ar;
                break;
            case PLAYER_FILL:
                break;
            case PLAYER_16_9:
                ar = 16.0 / 9.0;
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case PLAYER_4_3:
                ar = 4.0 / 3.0;
                if (dar < ar)
                    dh = dw / ar;
                else
                    dw = dh * ar;
                break;
            case PLAYER_ORIGINAL:
                dh = videoVisibleHeight;
                dw = vw;
                break;
        }

        if (playerView != null) {
            // set display size
            ViewGroup.LayoutParams lp = playerView.getLayoutParams();
            lp.width = (int) Math.ceil(dw * videoWidth / videoVisibleWidth);
            lp.height = (int) Math.ceil(dh * mVideoHeight / videoVisibleHeight);
            if (lp instanceof RelativeLayout.LayoutParams) {
                int marginLeft = (sw - lp.width) / 2;
                //不加这一行sumsung的布局不对, CENTTER_IN_PARENT不生效
                ((RelativeLayout.LayoutParams) lp).addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                ((RelativeLayout.LayoutParams) lp).setMargins(marginLeft, 0, 0, 0);
            }

            playerView.setLayoutParams(lp);
            playerView.invalidate();
        }
    }

    public String getFileName() {
        return fileFullName;
    }
}
