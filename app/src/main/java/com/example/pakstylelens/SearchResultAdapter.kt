package com.example.pakstylelens

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SearchResultAdapter(
    private val results: List<SearchResult>,
    private val caption: String?,
    private val inputBitmap: Bitmap?
) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val caption: TextView = view.findViewById(R.id.captionText)
        val brand: TextView = view.findViewById(R.id.brandText)
        val fabric: TextView = view.findViewById(R.id.fabricText)
        val color: TextView = view.findViewById(R.id.colorText)
        val url: TextView = view.findViewById(R.id.urlText)
        val imageView: ImageView = view.findViewById(R.id.resultImage)

        val inputHeaderContainer: View = view.findViewById(R.id.inputHeaderContainer)
        val inputHeader: TextView = view.findViewById(R.id.inputHeader)
        val inputHeaderImage: ImageView = view.findViewById(R.id.inputHeaderImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.caption.text = result.caption
        holder.brand.text = result.brand
        holder.fabric.text = "Fabric: ${result.fabric}"
        holder.color.text = "Color: ${result.color}"
        holder.url.text = result.url

        Glide.with(holder.imageView.context)
            .load(result.image_url)
            .placeholder(R.drawable.fashion_logo)
            .into(holder.imageView)

        if (position == 0) {
            holder.inputHeaderContainer.visibility = View.VISIBLE

            if (inputBitmap != null) {
                if (inputBitmap != null) {
                    holder.inputHeader.text = "You asked:"
                    holder.inputHeader.visibility = View.VISIBLE
                    holder.inputHeaderImage.setImageBitmap(inputBitmap)
                    holder.inputHeaderImage.visibility = View.VISIBLE
                }

            } else if (!caption.isNullOrBlank()) {
                holder.inputHeader.text = "You asked: $caption"
                holder.inputHeader.visibility = View.VISIBLE
                holder.inputHeaderImage.visibility = View.GONE
            } else {
                holder.inputHeaderContainer.visibility = View.GONE
            }
        } else {
            holder.inputHeaderContainer.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = results.size
}
