package com.example.hangout_hub

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide

class ImageFlipperAdapter(
    private val context: Context,
    private val imageUrls: List<String>
) : BaseAdapter() {

    override fun getCount(): Int = imageUrls.size

    override fun getItem(position: Int): Any = imageUrls[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView = convertView as? ImageView ?: ImageView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        Glide.with(context)
            .load(imageUrls[position])
            .placeholder(R.drawable.placeholder) // Make sure this drawable exists
            .into(imageView)

        return imageView
    }
}
