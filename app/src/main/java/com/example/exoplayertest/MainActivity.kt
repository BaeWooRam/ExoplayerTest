package com.example.exoplayertest

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


class MainActivity : AppCompatActivity() {
    private val debugTag = javaClass.simpleName
    lateinit var playerView: PlayerView
    lateinit var manager: ExoPlayerManager

    var btnExoPlay:ImageView? = null
    var btnExoNext:ImageView? = null
    var btnExoPause:ImageView? = null
    var btnExoPrevious:ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playerView = findViewById(R.id.exoPlayer)

        btnExoPlay = playerView.findViewById<ImageView>(R.id.btnExoPlay).apply { setOnClickListener { clickPlay() } }
        btnExoNext = playerView.findViewById<ImageView>(R.id.btnExoNext).apply { setOnClickListener { clickNext() } }
//        btnExoPause = playerView.findViewById<ImageView>(R.id.btnExoPause).apply { setOnClickListener { clickPause() } }
        btnExoPrevious = playerView.findViewById<ImageView>(R.id.btnExoPrevious).apply { setOnClickListener { clickPrevious() } }

        initNewPlayer()
    }

    @Deprecated("현재 안씀")
    private fun initPlayer() {
        val trackSelector = DefaultTrackSelector(this)
        trackSelector.setParameters(
            trackSelector.buildUponParameters()
                .setMaxVideoSizeSd() // 비디오 트랙 선택을 SD로 제한
                .setMaxAudioBitrate(1000) // 최대 오디오 bitrate를 1000으로 설정
        )

        val player = SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector).build()

        player.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)

                when (playbackState) {
                    Player.STATE_IDLE -> {
                        // 초기 상태, 플레이어가 중지 된 상태, 재생이 실패한 상태입니다.
                        Log.d(debugTag, "Player.STATE_IDLE")
                    }
                    Player.STATE_BUFFERING -> {
                        // 플레이어는 현재 위치에서 즉시 플레이 할 수 없습니다. 이것은 더 많은 데이터를로드해야하기 때문에 주로 발생합니다.
                        Log.d(debugTag, "Player.STATE_BUFFERING")
                    }
                    Player.STATE_READY -> {
                        // 플레이어는 현재 위치에서 즉시 플레이 할 수 있습니다.
                        Log.d(debugTag, "Player.STATE_READY")
                    }
                    Player.STATE_ENDED -> {
                        // 플레이어가 모든 미디어 재생을 마쳤습니다.
                        Log.d(debugTag, "Player.STATE_ENDED")
                    }
                }
            }
        })

        playerView.player = player

        // MediaItem을 만들고
        val mediaItem = MediaItem.fromUri(urls[0])

        // MediaSource를 만들고
        val userAgent = Util.getUserAgent(this, this.applicationInfo.name)
        val factory = DefaultDataSourceFactory(this, userAgent)
        val progressiveMediaSource =
            ProgressiveMediaSource.Factory(factory).createMediaSource(mediaItem)

        player.setMediaSource(progressiveMediaSource)
        player.prepare()
        player.play()
    }

    private fun initNewPlayer() {
        manager = ExoPlayerManager(this@MainActivity)
        manager.init()
        manager.target(playerView).video(urls.toList())

    }

    private fun clickPlay(){
        Log.d(debugTag, "clickPlay()")
        manager.play()
    }

    private fun clickNext(){
        Log.d(debugTag, "clickNext()")
        manager.next()
    }

    private fun clickPrevious(){
        Log.d(debugTag, "clickPrevious()")
        manager.previous()
    }

    private fun clickPause(){
        Log.d(debugTag, "clickPause()")
        manager.pause()
    }

    companion object {
        val urls = arrayOf(
            Uri.parse("https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_480_1_5MG.mp4"),
            Uri.parse("https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_640_3MG.mp4"),
            Uri.parse("https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_1280_10MG.mp4"),
            Uri.parse("https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_1920_18MG.mp4"),
            Uri.parse("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/VolkswagenGTIReview.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4"),
            Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4"),
            Uri.parse("https://firebasestorage.googleapis.com/v0/b/moimtest-a5863.appspot.com/o/9HvWpTmYbAhGgr5zuObGDtLnFo72%2FVideo%2F1589606971063?alt=media&token=a3cda8ed-2e85-4e27-9991-f52711c1372f")
        )
    }

}