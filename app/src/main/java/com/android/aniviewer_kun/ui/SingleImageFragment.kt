package com.android.aniviewer_kun.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.aniviewer_kun.AnimeDetails
import com.android.aniviewer_kun.R
import com.android.aniviewer_kun.databinding.FragmentSingleImageBinding
import com.android.aniviewer_kun.glide.Glide

class SingleImageFragment : Fragment() {

    private var _binding: FragmentSingleImageBinding? = null
    private val binding get() = _binding!!
    private val args: SingleImageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingleImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animeDetailsActivity = requireActivity() as AnimeDetails
        animeDetailsActivity.hideBars()

        binding.singleImage.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.dummyIV1.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.dummyIV2.setOnClickListener {
            findNavController().popBackStack()
        }

        val imageURL = args.imageURL
        Log.d("XXX", "SingleImageFragment: imageURL = $imageURL")
        Glide.glideFetch(imageURL, binding.singleImage)
    }

    override fun onDestroyView() {
        val animeDetailsActivity = requireActivity() as AnimeDetails
        animeDetailsActivity.showBars()
        _binding = null
        super.onDestroyView()
    }
}