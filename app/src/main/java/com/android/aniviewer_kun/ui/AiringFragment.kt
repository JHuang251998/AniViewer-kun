package com.android.aniviewer_kun.ui

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
import com.android.aniviewer_kun.databinding.FragmentRvSortBinding
import com.android.aniviewer_kun.type.MediaSort
import com.android.aniviewer_kun.type.MediaStatus
import com.android.aniviewer_kun.type.MediaType

class AiringFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentRvSortBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MediaRowAdapter
    private lateinit var recyclerView: RecyclerView

    private var currentSort = listOf(MediaSort.POPULARITY_DESC)

    private fun initAdapter(binding: FragmentRvSortBinding): MediaRowAdapter {
        val adapter = MediaRowAdapter(viewModel, this)
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        val manager = LinearLayoutManager(binding.root.context)
        recyclerView.layoutManager = manager
        return adapter
    }

    private fun initSwipeLayout(swipe: SwipeRefreshLayout) {
        swipe.setOnRefreshListener {
            viewModel.getMediaListByStatus(
                MediaStatus.RELEASING,
                MediaType.ANIME,
                currentSort,
                1,
                50
            )
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
        _binding = FragmentRvSortBinding.inflate(inflater, container, false)
        viewModel.clearMedia()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = initAdapter(binding)
        initSwipeLayout(binding.swipeRefreshLayout)

        val cityAdapter = ArrayAdapter.createFromResource(
            binding.root.context,
            R.array.sortOptions,
            android.R.layout.simple_spinner_item
        )
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sortSpinner.adapter = cityAdapter


        binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                val sortOptions = resources.getStringArray(R.array.sortOptions)
                when (sortOptions[position]) {
                    "Popularity" -> currentSort = listOf(MediaSort.POPULARITY_DESC)
                    "Score" -> currentSort = listOf(MediaSort.SCORE_DESC)
                    "Start Date" -> currentSort = listOf(MediaSort.START_DATE_DESC)
                    "Title" -> currentSort = listOf(MediaSort.TITLE_ROMAJI_DESC)
                }

                println(currentSort)
                viewModel.getMediaListByStatus(
                    MediaStatus.RELEASING,
                    MediaType.ANIME,
                    currentSort,
                    1,
                    50
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

//        viewModel.getMediaListByStatus(
//            MediaStatus.RELEASING,
//            MediaType.ANIME,
//            currentSort,
//            1,
//            50
//        )
        viewModel.observeMedia().observe(viewLifecycleOwner) {
            adapter.setMedia(it)
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.swipeRefreshLayout.setOnRefreshListener(null)
    }
}