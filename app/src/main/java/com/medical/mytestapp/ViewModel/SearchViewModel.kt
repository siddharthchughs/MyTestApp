package com.medical.mytestapp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.medical.mytestapp.ConnectionInterface.ServicesUtility
import com.medical.mytestapp.model.Data
import com.medical.mytestapp.repository.SearchRepository

/**
 * Created on : Feb 26, 2019
 * Author     : AndroidWave
 */
class SearchViewModel(application: Application):AndroidViewModel(application){

    private val movieRepository: SearchRepository

    val allBlog: LiveData<List<Data>?> get() = movieRepository.getMutableLiveData()

    init {
        movieRepository = SearchRepository(application)
    }

    fun refreshData() {
        movieRepository.refreshData()
    }
}