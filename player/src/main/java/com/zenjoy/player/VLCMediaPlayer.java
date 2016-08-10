package com.zenjoy.player;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

/*
 * ZenMediaLib has hooked MediaPlayer of vlc, but modify MediaPlayer directly is not encouraged.
  * Please add feature here.
 */
public class VLCMediaPlayer extends MediaPlayer {
    public VLCMediaPlayer(LibVLC libVLC) {
        super(libVLC);
    }

    public VLCMediaPlayer(Media media) {
        super(media);
    }
}
