/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsAPI
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.*

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */

enum class MarsAPIStatus{LOADING, ERROR, DONE}
class OverviewViewModel : ViewModel() {

    private var viewmodelJob = Job()
    private val uiScope = CoroutineScope(viewmodelJob + Dispatchers.Main)

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<MarsAPIStatus>()

    // The external immutable LiveData for the request status String
    val status: LiveData<MarsAPIStatus>
        get() = _status

    private val _property = MutableLiveData<List<MarsProperty>>()
    val property: LiveData<List<MarsProperty>>
    get() = _property

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties()
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getMarsRealEstateProperties() {

        uiScope.launch {
            var getReferencesDeferred = MarsAPI.retrofitService.getProperties()
            try {
                _status.value =MarsAPIStatus.LOADING
                var listResult = getReferencesDeferred.await()
                _status.value = MarsAPIStatus.DONE
            }
            catch (t:Throwable){
                _status.value = MarsAPIStatus.ERROR
                _property.value = ArrayList()
            }}

        }

    override fun onCleared() {
        super.onCleared()
        viewmodelJob.cancel()
    }
}



