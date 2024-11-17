package com.dicoding.picodiploma.loginwithanimation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryDetail
import com.dicoding.picodiploma.loginwithanimation.databinding.ItemStoryBinding
import com.dicoding.picodiploma.loginwithanimation.view.ui.detail.DetailActivity

class StoryListAdapter :
    ListAdapter<StoryDetail, StoryListAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StoryDetail) {
            binding.tvTitle.text = item.name
            binding.tvDescription.text = item.description
            Glide.with(binding.root.context)
                .load(item.photoUrl)
                .into(binding.ivPhoto)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra("STORY_ID", item.id)
                    putExtra("STORY_TITLE", item.name)
                    putExtra("STORY_DESCRIPTION", item.description)
                    putExtra("STORY_PHOTO_URL", item.photoUrl)
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryDetail>() {
            override fun areItemsTheSame(oldItem: StoryDetail, newItem: StoryDetail): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: StoryDetail,
                newItem: StoryDetail
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
