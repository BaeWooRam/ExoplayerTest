package com.example.exoplayertest

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView

interface PlayerManager {
    interface Init {
        fun init()
        fun clear()
    }

    interface Listener {
        fun addPlayerListener(listener: Player.EventListener): Target
    }

    interface Target {
        fun target(target: PlayerView): Video
    }

    interface Video {
        fun video(uri: Uri): Handling
        fun video(uriList: List<Uri>): Handling
    }

    interface Handling {
        fun next()
        fun previous()
        fun play()
        fun stop()
        fun pause()
    }
}