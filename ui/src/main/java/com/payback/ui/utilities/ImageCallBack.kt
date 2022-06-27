package com.payback.ui.utilities

import androidx.recyclerview.widget.DiffUtil
import com.payback.data.local.Image

val imageCallBack = object : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.imageId == newItem.imageId
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }
}