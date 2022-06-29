package com.payback.ui.mainFragment

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.payback.data.local.Image
import com.payback.ui.R
import com.payback.ui.adapter.ImagesAdapter
import com.payback.ui.adapter.StateAdapter
import com.payback.ui.databinding.FragmentMainBinding
import com.payback.ui.mainActivity.MainActivityViewModel
import com.payback.ui.utilities.ItemOffsetDecoration
import com.payback.ui.utilities.changeStatusBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {


    private lateinit var binding: FragmentMainBinding
    private val adapter = ImagesAdapter { image, imageView -> navigate(image, imageView) }
    private val viewModel: MainActivityViewModel by activityViewModels()
    private var gridLayoutSpan = 2
    private var isInitiated = false
    private var job: Job? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        setSearchViewListener()
        setUpAdapter()

        if (!isInitiated) init()

//        binding.retryBtn.setOnClickListener {
//            adapter.refresh()
//        }


        binding.appCompatButton.setOnClickListener {

            searchImages(binding.textInputSearch.text.toString()  , true)
        }

    }

    private fun setUpAdapter() {
        val itemDecoration =
            ItemOffsetDecoration(requireContext(), R.dimen.item_margin)
        binding.recyclerview.addItemDecoration(itemDecoration)


        val currentOrientation = resources.configuration.orientation
        gridLayoutSpan = if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            3
        } else {
            2
        }

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = adapter
        binding.recyclerview.adapter = adapter.withLoadStateFooter(
            footer = StateAdapter { retry() }
        )

        adapter.addLoadStateListener { state ->
            binding.waitingProgress.isVisible = state.refresh is LoadState.Loading

            binding.empty.isVisible =
                state.refresh is LoadState.NotLoading && adapter.itemCount == 0
            binding.errorSection.isVisible = state.refresh is LoadState.Error

        }
    }

    private fun init() {
        isInitiated = true
        searchImages(viewModel.currentSearch)
        binding.textInputSearch.apply {
            setText(viewModel.currentSearch)
            text?.length?.let { setSelection(it) }
        }
    }

    private fun searchImages(searchString: String, isUserInitiated: Boolean = false) {
        job?.cancel()
        job = viewLifecycleOwner.lifecycleScope.launch {
            if (isUserInitiated) adapter.submitData(PagingData.empty())
            viewModel.searchForImage(searchString).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun setSearchViewListener() {
        binding.textInputSearch.apply {
            addTextChangedListener { _: Editable? ->
//                binding.se.isVisible = text.toString().isNotEmpty()
            }
            setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = text.toString().trim()
                    if (query.isNotEmpty()) {
                        searchImages(query, true)
                    } else {
                        binding.textInputSearch.text = null
                    }
                    hideSoftKeyboard()
                    return@OnEditorActionListener true
                }
                false
            })
        }

    }

    private fun navigate(image: Image, imageView: ImageView) {
        viewModel.image = image
        val extras = FragmentNavigatorExtras(
            imageView to image.largeImageURL
        )
        val action = MainFragmentDirections.actionMainFragmentToDetailFragment()
        findNavController().navigate(action, extras)
        changeStatusBarColorToBlack()
    }

    private fun changeStatusBarColorToBlack() {
        requireActivity().apply {
            changeStatusBar(false)
        }
    }

    private fun retry() {
        adapter.retry()
    }

    private fun hideSoftKeyboard() {
        requireActivity().apply {
            WindowInsetsControllerCompat(window, window.decorView).hide(WindowInsetsCompat.Type.ime())
        }
    }


    override fun onResume() {
        super.onResume()
        requireActivity().changeStatusBar(true)
        hideSoftKeyboard()
    }

}