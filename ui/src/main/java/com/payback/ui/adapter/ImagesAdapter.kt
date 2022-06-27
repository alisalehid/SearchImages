package com.payback.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.payback.data.local.Image
import com.payback.ui.databinding.ImageCardItemBinding
import com.payback.ui.utilities.imageCallBack


class ImagesAdapter(private val clicked: (Image, ImageView) -> Unit) :
    PagingDataAdapter<Image, ImagesAdapter.ImageViewHolder>(imageCallBack) {

    inner class ImageViewHolder(private val binding: ImageCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imagePassed: Image) {
            binding.apply {
                image = imagePassed
                tags.isSelected = true
                root.setOnClickListener {
//                    clicked.invoke(imagePassed, imageView)

                    Log.d("payback" , "click")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ImageCardItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val data = getItem(position)!!

        holder.bind(data)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            1
        } else {
            2
        }
    }
}