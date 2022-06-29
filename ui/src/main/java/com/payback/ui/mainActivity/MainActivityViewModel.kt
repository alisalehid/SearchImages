package com.payback.ui.mainActivity

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.payback.data.Constants
import com.payback.data.local.Image
import com.payback.data.local.Search
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(

    private val search: Search
) : ViewModel() {


    var image : Image? = null
    private  val defaultWord = Constants.DEFAULT_WORD
    var currentSearch = defaultWord


     fun searchForImage(searchString: String) : Flow<PagingData<Image>> {

        currentSearch = searchString
        return search.invoke(searchString)
    }

}