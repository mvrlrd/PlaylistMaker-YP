package ru.mvrlrd.playlistmaker.player.di

import android.media.MediaPlayer
import org.koin.dsl.module
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer
import ru.mvrlrd.playlistmaker.player.data.PlayerClient

val playerDataModule = module {
    single<PlayerClient> {
        MyMediaPlayer(mediaPlayer = get())
    }
    single {
        MediaPlayer()
    }
}