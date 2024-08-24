package com.ssw.kast.model.entity

import com.ssw.kast.model.component.PickerElement

class Country(
    var id: Any,
    var name: String
) {
    companion object {
        fun fromDropDown(element: PickerElement): Country {
            val country: Country = element.value as Country
            return country
        }
    }
}

fun abstractListOfCountry(): List<Country> {
    return listOf(
        Country("CNT0001", "Austria"),
        Country("CNT0001", "Belgium"),
        Country("CNT0001", "Brazilia"),
        Country("CNT0001", "Canada"),
        Country("CNT0001", "China"),
        Country("CNT0001", "Danemark"),
        Country("CNT0001", "England"),
        Country("CNT0002", "Finland"),
        Country("CNT0002", "France"),
        Country("CNT0003", "Germany"),
        Country("CNT0003", "Greece"),
        Country("CNT0004", "Iceland"),
        Country("CNT0004", "Ireland"),
        Country("CNT0004", "Italia"),
        Country("CNT0005", "Japan"),
        Country("CNT0006", "Madagascar"),
        Country("CNT0006", "Morocco"),
        Country("CNT0007", "Netherlands"),
        Country("CNT0007", "Nigeria"),
        Country("CNT0007", "Norwegia"),
        Country("CNT0008", "Saudi Arabia"),
        Country("CNT0008", "Serbia"),
        Country("CNT0008", "Slovenia"),
        Country("CNT0008", "South Korea"),
        Country("CNT0009", "South Africa"),
        Country("CNT0010", "Spain"),
        Country("CNT0010", "Sweden"),
        Country("CNT0010", "Switzerland"),
        Country("CNT0010", "Tunisia"),
        Country("CNT0010", "Turkish"),
        Country("CNT0011", "United States"),
        Country("CNT0011", "Wales")
    )
}