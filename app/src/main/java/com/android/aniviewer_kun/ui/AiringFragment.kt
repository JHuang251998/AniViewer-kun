package com.android.aniviewer_kun.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.aniviewer_kun.MainViewModel
import com.android.aniviewer_kun.MediaRowAdapter
import com.android.aniviewer_kun.R
import com.android.aniviewer_kun.databinding.FragmentRvBinding
import com.android.aniviewer_kun.type.MediaSort
import com.android.aniviewer_kun.type.MediaStatus
import com.android.aniviewer_kun.type.MediaType
import com.apollographql.apollo3.api.Optional

class AiringFragment: Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentRvBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MediaRowAdapter
    private lateinit var recyclerView: RecyclerView

    private var currentSort = listOf(MediaSort.POPULARITY_DESC)

    private fun initAdapter(binding: FragmentRvBinding) : MediaRowAdapter {
        val adapter = MediaRowAdapter(viewModel)
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        val manager = LinearLayoutManager(binding.root.context)
        recyclerView.layoutManager = manager
        return adapter
    }

    private fun initSwipeLayout(swipe : SwipeRefreshLayout) {
        swipe.setOnRefreshListener {
            viewModel.getMediaListByStatus(Optional.present(MediaStatus.RELEASING), MediaType.ANIME, currentSort)
        }

        viewModel.fetchDone.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRvBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = initAdapter(binding)
        initSwipeLayout(binding.swipeRefreshLayout)

        val cityAdapter = ArrayAdapter.createFromResource(binding.root.context,
            R.array.sortOptions,
            android.R.layout.simple_spinner_item)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sortSpinner.adapter = cityAdapter

        binding.sortImage.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
        binding.sortImage.tag = R.drawable.ic_baseline_arrow_downward_24
        binding.sortImage.setOnClickListener { changeSortDirection() }

        binding.sortSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                println("pos $position")
                val sortOptions = resources.getStringArray(R.array.sortOptions)
                println("item ${sortOptions[position]}")
                val desc = binding.sortImage.tag == R.drawable.ic_baseline_arrow_downward_24
                when(sortOptions[position]) {
                    "Popularity" -> currentSort = if (desc) listOf(MediaSort.POPULARITY_DESC) else listOf(MediaSort.POPULARITY)
                    "Score" -> currentSort = if (desc) listOf(MediaSort.SCORE_DESC) else listOf(MediaSort.SCORE)
                    "Start Date" -> currentSort = if (desc) listOf(MediaSort.START_DATE_DESC) else listOf(MediaSort.START_DATE)
                    "Title" -> currentSort = if (desc) listOf(MediaSort.TITLE_ROMAJI_DESC) else listOf(MediaSort.TITLE_ROMAJI)
                }

                println(currentSort)
                viewModel.getMediaListByStatus(Optional.present(MediaStatus.RELEASING), MediaType.ANIME, currentSort)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        viewModel.getMediaListByStatus(Optional.present(MediaStatus.RELEASING), MediaType.ANIME, currentSort)
        viewModel.observeMedia().observe(viewLifecycleOwner) {
            adapter.setMedia(it)
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun changeSortDirection() {
        if (binding.sortImage.tag == R.drawable.ic_baseline_arrow_downward_24) {
            when(currentSort) {
                listOf(MediaSort.POPULARITY_DESC) -> currentSort = listOf(MediaSort.POPULARITY)
                listOf(MediaSort.SCORE_DESC) -> currentSort = listOf(MediaSort.SCORE)
                listOf(MediaSort.START_DATE_DESC) -> currentSort = listOf(MediaSort.START_DATE)
                listOf(MediaSort.TITLE_ROMAJI_DESC) -> currentSort = listOf(MediaSort.TITLE_ROMAJI)
            }
            viewModel.getMediaListByStatus(Optional.present(MediaStatus.RELEASING), MediaType.ANIME, currentSort)
            binding.sortImage.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
            binding.sortImage.tag = R.drawable.ic_baseline_arrow_upward_24
        } else {
            when(currentSort) {
                listOf(MediaSort.POPULARITY) -> currentSort = listOf(MediaSort.POPULARITY_DESC)
                listOf(MediaSort.SCORE) -> currentSort = listOf(MediaSort.SCORE_DESC)
                listOf(MediaSort.START_DATE) -> currentSort = listOf(MediaSort.START_DATE_DESC)
                listOf(MediaSort.TITLE_ROMAJI) -> currentSort = listOf(MediaSort.TITLE_ROMAJI_DESC)
            }
            viewModel.getMediaListByStatus(Optional.present(MediaStatus.RELEASING), MediaType.ANIME, currentSort)
            binding.sortImage.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
            binding.sortImage.tag = R.drawable.ic_baseline_arrow_downward_24
        }
    }
}