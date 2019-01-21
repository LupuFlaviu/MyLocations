package com.flaviul.test.mylocations.data.local.db.model

import android.os.Parcelable
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import kotlinx.android.parcel.Parcelize

@RealmClass
@Parcelize
open class Location(
    @PrimaryKey
    open var id: Long = 0,
    open var latitude: Double = 0.0,
    open var longitude: Double = 0.0,
    open var label: String? = "",
    open var address: String? = ""
) : RealmModel, Parcelable {
    constructor(latitude: Double, longitude: Double, tag: String, address: String) : this(
        0,
        latitude,
        longitude,
        tag,
        address
    )
}