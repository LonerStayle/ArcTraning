package kr.loner.arctraining.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(val id: String, val name: String) : Parcelable