package com.android.aniviewer_kun

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.aniviewer_kun.databinding.RowMediaBinding
import com.android.aniviewer_kun.glide.Glide
import com.android.aniviewer_kun.type.MediaStatus


class MediaRowAdapter(private val viewModel: MainViewModel)
    : ListAdapter<MediaListQuery.Medium?, MediaRowAdapter.VH>(RedditDiff()) {

    private var media = listOf<MediaListQuery.Medium?>()

    inner class VH(val binding: RowMediaBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.romajiTitle.setOnClickListener {
                val anime = getItem(adapterPosition)
                viewModel.doAnimeDetails(it.context, anime)
            }
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
        val binding = holder.binding
        val anime = media[position]
        binding.romajiTitle.text = anime?.title?.romaji
        if (anime?.averageScore != null) {
            binding.averageScore.text = "Score: ${anime?.averageScore}%"
        }

        if (anime?.status == MediaStatus.RELEASING) {
            var episodeText = ""
            if (anime?.nextAiringEpisode?.episode != null && anime?.episodes != null) episodeText = "${anime?.nextAiringEpisode?.episode - 1}/${anime?.episodes} episodes - "
            else if (anime?.nextAiringEpisode?.episode != null) episodeText = "${anime?.nextAiringEpisode.episode} episodes - "
            else if (anime?.episodes != null) episodeText = "${anime?.episodes} episodes - "
            binding.mediaInfo.text =
                "${episodeText}${anime?.season.toString()} ${anime?.seasonYear}"

            if (anime.coverImage?.medium != null) {
                Glide.glideFetch(
                    anime.coverImage?.medium,
                    binding.subRowPic
                )
            } else {
                binding.subRowPic.setImageResource(R.drawable.ic_launcher_foreground)
            }
        }

        viewModel.fetchDone.postValue(true)
    }

    fun setMedia(list: List<MediaListQuery.Medium?>?) {
        media = list ?: listOf()
    }

    class RedditDiff : DiffUtil.ItemCallback<MediaListQuery.Medium?>() {
        override fun areItemsTheSame(
            oldItem: MediaListQuery.Medium,
            newItem: MediaListQuery.Medium
        ): Boolean {
            return false
        }
        override fun areContentsTheSame(
            oldItem: MediaListQuery.Medium,
            newItem: MediaListQuery.Medium
        ): Boolean {
            return false

        }
    }
}

