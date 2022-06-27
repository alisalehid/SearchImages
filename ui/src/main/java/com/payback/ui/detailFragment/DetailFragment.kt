package com.payback.ui.detailFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.payback.ui.R
import com.payback.ui.databinding.FragmentDetailBinding
import com.payback.ui.mainActivity.MainActivityViewModel
import com.payback.ui.utilities.changeStatusBar
import com.payback.ui.utilities.setToolbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {
    private lateinit var binding: FragmentDetailBinding
    private val viewModel: MainActivityViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)
        binding.image = viewModel.image
        loadTransition()

        binding.toolbar.apply {
            setToolbar(this)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    private fun loadTransition() {
        val imageUri = viewModel.image?.largeImageURL
        binding.imageView.apply {
            transitionName = imageUri
            Glide.with(requireContext())
                .load(imageUri)
                .into(this)
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().changeStatusBar(false)
    }

}