package com.cookandroid.korconversationapp;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class CustomExoPlayerView extends PlayerView {

    private SimpleExoPlayer player;
    private MediaSource mediaSource;
    private Boolean playWhenReady = true;
    private int currentWindow = 0;
    private Long playbackPosition = 0L;

    public CustomExoPlayerView(Context context) {
        super(context);
    }

    public CustomExoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomExoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    void initializePlayer(String url1) {

        //ConcatenatingMediaSource mediaSource = new ConcatenatingMediaSource();

        mediaSource = buildMediaSource(Uri.parse(url1));
        //MediaSource mediaSource2 = buildMediaSource(Uri.parse(url2));

        //mediaSource.addMediaSource(mediaSource1);
        //mediaSource.addMediaSource(mediaSource2);

        player = ExoPlayerFactory.newSimpleInstance(getContext());

        setPlayer(player);
        setUseController(false);

        //prepare
        player.prepare(mediaSource, true, false);

        //start,stop
        player.setPlayWhenReady(playWhenReady);
    }

    private MediaSource buildMediaSource(Uri uri) {

        String userAgent = Util.getUserAgent(getContext(), "korconversation");

        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri);
    }


    void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();

            setPlayer(null);
            player.release();
            player = null;

        }
    }
}
