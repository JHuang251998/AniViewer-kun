package com.android.aniviewer_kun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.aniviewer_kun.databinding.FragmentRvBinding

class AiringFragment: Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentRvBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MediaRowAdapter
    private lateinit var recyclerView: RecyclerView

    private fun initAdapter(binding: FragmentRvBinding) : MediaRowAdapter {
        val adapter = MediaRowAdapter(viewModel)
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        val manager = LinearLayoutManager(binding.root.context)
        recyclerView.layoutManager = manager
        return adapter
    }

    private fun initSwipeLayout(swipe : SwipeRefreshLayout) {
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
        println("AIRING FRAGMENT CREATED")
        adapter = initAdapter(binding)
        initSwipeLayout(binding.swipeRefreshLayout)
    }
}