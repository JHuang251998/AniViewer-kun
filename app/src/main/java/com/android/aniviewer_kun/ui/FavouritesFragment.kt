package com.android.aniviewer_kun.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.constraintlayout.motion.widget.OnSwipe
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.aniviewer_kun.MainViewModel
import com.android.aniviewer_kun.MediaRowAdapter
import com.android.aniviewer_kun.R
import com.android.aniviewer_kun.ViewerMediaRowAdapter
import com.android.aniviewer_kun.api.AniListUser
import com.android.aniviewer_kun.databinding.FragmentRvBinding
import com.android.aniviewer_kun.databinding.FragmentRvSortBinding
import com.android.aniviewer_kun.type.MediaSort
import com.apollographql.apollo3.api.and

class FavouritesFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentRvBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ViewerMediaRowAdapter
    private lateinit var recyclerView: RecyclerView

    private fun initAdapter(binding: FragmentRvBinding): ViewerMediaRowAdapter {
        val adapter = ViewerMediaRowAdapter(viewModel, this)
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        val manager = LinearLayoutManager(binding.root.context)
        recyclerView.layoutManager = manager

        return adapter
    }

    private fun initSwipeLayout(swipe: SwipeRefreshLayout) {
        swipe.setOnRefreshListener {
            viewModel.getViewerData()
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

        val cityAdapter = ArrayAdapter.createFromResource(
            binding.root.context,
            R.array.sortOptions,
            android.R.layout.simple_spinner_item
        )
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

//        viewModel.getViewerData()

        viewModel.observeViewer().observe(viewLifecycleOwner) {
            adapter.setMedia(AniListUser.favourites)
            adapter.submitList(AniListUser.favourites)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.swipeRefreshLayout.setOnRefreshListener(null)
    }
}