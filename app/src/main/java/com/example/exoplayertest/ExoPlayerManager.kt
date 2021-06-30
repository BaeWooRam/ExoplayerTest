package com.example.exoplayertest

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util

class ExoPlayerManager(private val context: Context) :
    PlayerManager.Init,
    PlayerManager.Listener,
    PlayerManager.Target,
    PlayerManager.Video,
    PlayerManager.Handling {
    private val debugTag = javaClass.simpleName
    private var mTarget: PlayerView? = null
    private var mSimpleExoPlayer: SimpleExoPlayer? = null
    private val mUserAgent: String = Util.getUserAgent(context, context.applicationInfo.name)

    @Synchronized
    override fun init() {
        if (mSimpleExoPlayer == null)
            mSimpleExoPlayer = SimpleExoPlayer.Builder(context).build()
    }

    override fun clear() {
        mSimpleExoPlayer?.release()
    }

    override fun addPlayerListener(listener: Player.EventListener): PlayerManager.Target {
        mSimpleExoPlayer?.addListener(listener)
        return this@ExoPlayerManager
    }

    override fun target(target: PlayerView): PlayerManager.Video {
        val verify = checkPlayer()

        if (verify != ExoPlayerVerificationType.OK) {
            Log.e(debugTag, "mSimpleExoPlayer is Null")
            return this@ExoPlayerManager
        }

        if(mTarget == null)
            target.player = mSimpleExoPlayer
        else
            PlayerView.switchTargetView(mSimpleExoPlayer!!, mTarget, target)

        this@ExoPlayerManager.mTarget = target
        return this@ExoPlayerManager
    }

    @Synchronized
    override fun video(uri: Uri): PlayerManager.Handling {
        mSimpleExoPlayer?.run {
            clearMediaItems()

            //Uri 정보로부터 미디어 들고 오기
            val mediaItem = MediaItem.fromUri(uri)
            val factory = DefaultDataSourceFactory(context, mUserAgent)
            val progressiveMediaSource =
                ProgressiveMediaSource.Factory(factory).createMediaSource(mediaItem)

            setMediaSource(progressiveMediaSource)
        }
        return this@ExoPlayerManager
    }

    @Synchronized
    override fun video(uriList: List<Uri>): PlayerManager.Handling {
        mSimpleExoPlayer?.run {
            clearMediaItems()
            addMediaSources(convertProgressiveMediaSources(uriList).toList())
        }

        return this@ExoPlayerManager
    }

    /**
     * Uri 배열로부터 ProgressiveMediaSources 생성
     */
    private fun convertProgressiveMediaSources(uriList: List<Uri>): ArrayList<ProgressiveMediaSource> {
        val list = arrayListOf<ProgressiveMediaSource>()

        uriList.forEach {
            val mediaItem = MediaItem.fromUri(it)
            val factory = DefaultDataSourceFactory(context, mUserAgent)
            list.add(ProgressiveMediaSource.Factory(factory).createMediaSource(mediaItem))
        }

        return list
    }

    override fun next() {
        val hasNext = mSimpleExoPlayer?.hasNext() == true
        Log.d(debugTag, "next() hasNext = $hasNext, currentPeriodIndex = ${mSimpleExoPlayer?.currentPeriodIndex}")

        if (hasNext)
            mSimpleExoPlayer?.next()
    }

    override fun previous() {
        val hasPrevious = mSimpleExoPlayer?.hasPrevious() == true
        Log.d(debugTag, "previous() hasPrevious = $hasPrevious, currentPeriodIndex = ${mSimpleExoPlayer?.currentPeriodIndex}")

        if (hasPrevious)
            mSimpleExoPlayer?.previous()
    }

    @Synchronized
    override fun play() {
        mSimpleExoPlayer?.run {
            prepare()
            play()
        }
    }

    override fun stop() {
        mSimpleExoPlayer?.stop()
    }

    override fun pause() {
        mSimpleExoPlayer?.pause()
    }

    private fun checkPlayer(): ExoPlayerVerificationType {
        if (mSimpleExoPlayer == null)
            return ExoPlayerVerificationType.PLAYER_IS_NULL

        return ExoPlayerVerificationType.OK
    }
}