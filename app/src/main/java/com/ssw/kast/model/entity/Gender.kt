package com.ssw.kast.model.entity

import com.ssw.kast.model.component.PickerElement

class Gender {
    var id: Any = ""
    var type: String = ""

    constructor()

    constructor(id: Any, type: String) {
        this.id = id
        this.type = type
    }

    companion object {
        fun fromDropDown(element: PickerElement): Gender {
            val gender: Gender = element.value as Gender
            return gender
        }

        fun abstractListOfGender(): List<Gender> {
            return listOf(
                Gender("GEN0001", "Male"),
                Gender("GEN0002", "Female")
            )
        }
    }
}