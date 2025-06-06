package com.example.hangout_hub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RestaurantAdapter(
    private val restaurants: List<Restaurant>,
    private val onClick: (Restaurant) -> Unit
) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    inner class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.txtRestaurantName)
        private val location: TextView = itemView.findViewById(R.id.txtRestaurantLocation)
        private val image: ImageView = itemView.findViewById(R.id.imgRestaurant)
        val btnBookNow: Button = itemView.findViewById(R.id.btnBookNow)

        fun bind(restaurant: Restaurant) {
            name.text = restaurant.name
            location.text = restaurant.location
            Glide.with(itemView.context).load(restaurant.imageUrl).into(image)
            itemView.setOnClickListener { onClick(restaurant) }

            // Click anywhere on item
            itemView.setOnClickListener {
                onClick(restaurant)
            }

            // Click only the Book Now button
            btnBookNow.setOnClickListener {
                onClick(restaurant)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(restaurants[position])
    }

    override fun getItemCount(): Int = restaurants.size
}
