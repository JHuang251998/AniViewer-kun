package com.android.aniviewer_kun.ui

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.aniviewer_kun.AnimeDetails
import com.android.aniviewer_kun.R
import com.android.aniviewer_kun.databinding.FragmentAnimeDetailsBinding
import com.android.aniviewer_kun.databinding.FragmentSingleImageBinding
import com.android.aniviewer_kun.glide.Glide

class AnimeDetailsFragment : Fragment() {
    private var _binding: FragmentAnimeDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnimeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animeDetailsActivity = requireActivity() as AnimeDetails

        animeDetailsActivity.setActionBarBack()

        arguments?.getString(AnimeDetails.animeTitleRomajiKey).let {
            binding.romajiTitle.text = it
            if (it != null) {
                animeDetailsActivity.setActionBarTitle(it)
            }
        }
        arguments?.getString(AnimeDetails.animeAverageScore).let {
            binding.averageScore.text = "Score: ${it}%"
        }
        arguments?.getString(AnimeDetails.animeFavorites).let {
            binding.favorites.text = it
        }
        arguments?.getString(AnimeDetails.animePopularity).let {
            binding.popularity.text = it
        }

        arguments?.getString(AnimeDetails.animeDescription).let {
            binding.detailedDescription.text = it
        }
        binding.detailedDescription.movementMethod = ScrollingMovementMethod()

        arguments?.getString(AnimeDetails.animeFormat).let {
            binding.mediaFormat.text = it
        }
        arguments?.getString(AnimeDetails.animeStartDate).let {
            binding.startDate.text = it
        }
        arguments?.getString(AnimeDetails.animeEndDate).let {
            binding.endDate.text = it
        }
        arguments?.getString(AnimeDetails.animeOtherNames).let {
            binding.otherNames.text = it
        }
        arguments?.getString(AnimeDetails.animeCountryOfOrigin).let {
            binding.origin.text = it
        }
        arguments?.getString(AnimeDetails.animeGenres).let {
            binding.genres.text = it
        }

        val imageURL = arguments?.getString(AnimeDetails.animePicKey)
        Log.d("XXX", "AnimeDetailsFragment: imageURL = $imageURL")
        if (imageURL != null) {
            Glide.glideFetch(
                imageURL,
                binding.subRowPic
            )
        } else {
            binding.subRowPic.setImageResource(R.drawable.ic_launcher_foreground)
        }

        binding.subRowPic.setOnClickListener {
            if (imageURL != null) {
                val action = AnimeDetailsFragmentDirections.actionAnimeDetailsFragmentToSingleImageFragment(imageURL)
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}