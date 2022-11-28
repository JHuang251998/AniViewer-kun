package com.android.aniviewer_kun.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.aniviewer_kun.*
import com.android.aniviewer_kun.databinding.FragmentRvBinding
import com.android.aniviewer_kun.databinding.FragmentRvSearchBinding
import com.android.aniviewer_kun.databinding.FragmentRvSortBinding
import com.android.aniviewer_kun.type.MediaSort
import com.android.aniviewer_kun.type.MediaStatus
import com.android.aniviewer_kun.type.MediaType
import com.apollographql.apollo3.api.Optional
import com.google.android.material.internal.ViewUtils.hideKeyboard

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

    private fun actionBarSearch() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) {
                    val mainActivity = requireActivity() as MainActivity
                    mainActivity.hideKeyboard()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
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
        actionBarSearch()

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
                val mainActivity = requireActivity() as MainActivity
                mainActivity.hideKeyboard()
            }
        }

        binding.searchBar.setOnKeyListener(View.OnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN) {
                val text = binding.searchBar.text.toString()
                if (text.isNotEmpty()) {
                    viewModel.searchMedia(text, 20)
                    val mainActivity = requireActivity() as MainActivity
                    mainActivity.hideKeyboard()
                }
                return@OnKeyListener true
            }
            return@OnKeyListener false
        })

        viewModel.observeSearchMediaResults().observe(viewLifecycleOwner) {
            adapter.setMedia(it)
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }
    }
}