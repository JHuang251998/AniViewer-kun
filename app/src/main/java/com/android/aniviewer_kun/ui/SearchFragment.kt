package com.android.aniviewer_kun.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.aniviewer_kun.MainViewModel
import com.android.aniviewer_kun.MediaRowAdapter
import com.android.aniviewer_kun.R
import com.android.aniviewer_kun.databinding.FragmentRvBinding
import com.android.aniviewer_kun.databinding.FragmentRvSearchBinding
import com.android.aniviewer_kun.databinding.FragmentRvSortBinding
import com.android.aniviewer_kun.type.MediaSort
import com.android.aniviewer_kun.type.MediaStatus
import com.android.aniviewer_kun.type.MediaType
import com.apollographql.apollo3.api.Optional

class SearchFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentRvSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MediaRowAdapter
    private lateinit var recyclerView: RecyclerView

    private fun initAdapter(binding: FragmentRvSearchBinding): MediaRowAdapter {
        val adapter = MediaRowAdapter(viewModel, this)
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        val manager = LinearLayoutManager(binding.root.context)
        recyclerView.layoutManager = manager
        return adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRvSearchBinding.inflate(inflater, container, false)
        viewModel.clearMedia()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = initAdapter(binding)

        val cityAdapter = ArrayAdapter.createFromResource(
            binding.root.context,
            R.array.sortOptions,
            android.R.layout.simple_spinner_item
        )
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.searchButton.setOnClickListener {
            val text = binding.searchBar.text.toString()
            if (text.isNotEmpty()) {
                viewModel.searchMedia(text, 20)
            }
        }

        viewModel.observeSearchMediaResults().observe(viewLifecycleOwner) {
            adapter.setMedia(it)
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }
    }
}