package com.ssw.kast.model

import com.ssw.kast.model.component.PickerElement

class Gender(
    var id: Any,
    var type: String
) {
    companion object {
        fun fromDropDown(element: PickerElement): Gender {
            val gender: Gender = element.value as Gender
            return gender
        }
    }
}

fun abstractListOfGender(): List<Gender> {
    return listOf(
        Gender("GEN0001", "Male"),
        Gender("GEN0002", "Female")
    )
}