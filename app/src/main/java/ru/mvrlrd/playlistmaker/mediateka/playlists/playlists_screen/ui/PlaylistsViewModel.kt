package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.ui

import ru.mvrlrd.playlistmaker.FileOperatingViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.GetInternalFileUseCase
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.domain.PlaylistInteractor


class PlaylistsViewModel(useCase: PlaylistInteractor,fileUseCase: GetInternalFileUseCase) : FileOperatingViewModel(fileUseCase) {
    val playlists = useCase.getAllPlaylist()
}