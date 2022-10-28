package com.android.aniviewer_kun

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.aniviewer_kun.api.Media
import com.android.aniviewer_kun.databinding.RowMediaBinding


class MediaRowAdapter(private val viewModel: MainViewModel)
    : ListAdapter<Media, MediaRowAdapter.VH>(RedditDiff()) {

    inner class VH(val binding: RowMediaBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            // TODO: set onClickListener here
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val rowPostBinding = RowMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(rowPostBinding)
    }

    // Overrode these two methods as the Recyclerview
    // was unexpectedly duplicating rows
    // https://stackoverflow.com/questions/33316837/how-to-prevent-items-from-getting-duplicated-when-scrolling-recycler-view
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        println("ROW CREATED")
    }

    class RedditDiff : DiffUtil.ItemCallback<Media>() {
        override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
            return false
        }
        override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
            return false

        }
    }
}

