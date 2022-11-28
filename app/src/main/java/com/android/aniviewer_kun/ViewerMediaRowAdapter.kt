package com.android.aniviewer_kun

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.aniviewer_kun.databinding.FragmentRvBinding
import com.android.aniviewer_kun.databinding.RowMediaBinding
import com.android.aniviewer_kun.glide.Glide
import com.android.aniviewer_kun.ui.Top100Fragment

class ViewerMediaRowAdapter(private val viewModel: MainViewModel, private val parentFragment: Fragment) :
    ListAdapter<ViewerQuery.Node?, ViewerMediaRowAdapter.VH>(RedditDiff()) {

    private var media = listOf<ViewerQuery.Node?>()

    inner class VH(val binding: RowMediaBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val anime = getItem(adapterPosition)
                viewModel.doAnimeDetails(it.context, anime)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val rowPostBinding =
            RowMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    override fun onBindViewHolder(holder: ViewerMediaRowAdapter.VH, position: Int) {
        if (position >= media.size) return
        val binding = holder.binding
        val anime = media[position]

        var season: String? = null
        if (anime?.season != null) season = anime?.season.toString()

        var year: String? = null
        if (anime?.seasonYear != null) year = anime?.seasonYear.toString()

        if (parentFragment is Top100Fragment) {
            binding.romajiTitle.text = "${position + 1}. ${anime?.title?.romaji}"
        } else {
            binding.romajiTitle.text = anime?.title?.romaji
        }


        if (anime?.averageScore != null) {
            binding.averageScore.text = "Score: ${anime?.averageScore}%"
        }

        var episodeText = ""
        Log.d("YYY", "${anime?.episodes}")
        if (anime?.nextAiringEpisode?.episode != null && anime?.episodes != null) episodeText =
            "${anime?.nextAiringEpisode?.episode - 1}/${anime?.episodes} episodes"
        else if (anime?.nextAiringEpisode?.episode != null) episodeText =
            "${anime?.nextAiringEpisode.episode} episodes"
        else if (anime?.episodes != null) {
            episodeText = "${anime?.episodes} episode"
            if (anime?.episodes > 1) episodeText += "s"
        }

        if (season != null && year != null) {
            binding.mediaInfo.text = "${episodeText} - ${season} ${year}"
        } else if (season != null) {
            binding.mediaInfo.text = "${episodeText} - ${season}"
        } else if (year != null) {
            binding.mediaInfo.text = "${episodeText} - ${year}"
        }

        if (anime?.coverImage?.medium != null) {
            Glide.glideFetch(
                anime.coverImage.medium,
                binding.subRowPic
            )
        }

        viewModel.fetchDone.postValue(true)
    }

    fun setMedia(list: List<ViewerQuery.Node?>?) {
        media = list ?: listOf()
    }

    class RedditDiff : DiffUtil.ItemCallback<ViewerQuery.Node?>() {
        override fun areItemsTheSame(
            oldItem: ViewerQuery.Node,
            newItem: ViewerQuery.Node
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: ViewerQuery.Node,
            newItem: ViewerQuery.Node
        ): Boolean {
            return false
        }
    }
}