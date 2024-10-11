package com.ssw.kast.model.entity

import com.ssw.kast.model.component.PickerElement

class MusicGenre(
    var id: Any,
    var type: String
) {
    companion object {
        fun listFromItemPickers(items: List<PickerElement>): List<MusicGenre> {
            var listMusicGenre = mutableListOf<MusicGenre>()
            items.forEach { item ->
                val musicGenre: MusicGenre = item.value as MusicGenre
                listMusicGenre.add(musicGenre)
            }

            return listMusicGenre
        }

        fun abstractListOfMusicGenre(): List<MusicGenre> {
            return listOf(
                MusicGenre("MGR0001", "Alternative"),
                MusicGenre("MGR0001", "Classic"),
                MusicGenre("MGR0001", "Electronic"),
                MusicGenre("MGR0001", "Metal"),
                MusicGenre("MGR0001", "Pop"),
                MusicGenre("MGR0001", "Rap"),
                MusicGenre("MGR0001", "Rock")
            )
        }
    }
}