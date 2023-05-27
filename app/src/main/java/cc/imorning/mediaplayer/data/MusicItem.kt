package cc.imorning.mediaplayer.data

import android.os.Parcel
import android.os.Parcelable

data class MusicItem(
    val id: String? = "",
    val name: String? = "",
    val artists: String? = "",
    val duration: Long = 0L,
    val path: String? = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(artists)
        parcel.writeLong(duration)
        parcel.writeString(path)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MusicItem> {
        override fun createFromParcel(parcel: Parcel): MusicItem {
            return MusicItem(parcel)
        }

        override fun newArray(size: Int): Array<MusicItem?> {
            return arrayOfNulls(size)
        }
    }
}