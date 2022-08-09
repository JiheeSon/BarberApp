package com.example.barberapp.view.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.barberapp.databinding.ItemServiceCategoryBinding
import com.example.barberapp.model.Constants.BASE_IMAGE_URL
import com.example.barberapp.model.remote.response.service.ServiceCategory

class CategoryAdapter(private val categoryList: List<ServiceCategory>,): RecyclerView.Adapter<CategoryAdapter.ImageViewHolder>() {
    private lateinit var binding: ItemServiceCategoryBinding
    inner class ImageViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind (category: ServiceCategory) {
            binding.apply {
                textCategory.text = category.category
                Glide.with(view.context)
                    .load(BASE_IMAGE_URL + category.categoryImage)
//                    .error(R.drawable.ic_baseline_image_not_supported_24)
//                    .fallback(R.drawable.ic_baseline_image_not_supported_24)
                    .into(imgBackground)
                Glide.with(view.context)
                    .load(BASE_IMAGE_URL + category.categoryImage)
//                    .error(R.drawable.ic_baseline_image_not_supported_24)
//                    .fallback(R.drawable.ic_baseline_image_not_supported_24)
                    .into(imgMain)
            }
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemServiceCategoryBinding.inflate(layoutInflater, parent, false)
        return ImageViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.apply {
            val image = categoryList[position]
            bind(image)
        }
    }


}