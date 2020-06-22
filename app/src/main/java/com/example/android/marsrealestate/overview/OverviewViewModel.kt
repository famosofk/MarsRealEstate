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
class OverviewViewModel : ViewModel() {

    private var viewmodelJob = Job()
    private val uiScope = CoroutineScope(viewmodelJob + Dispatchers.Main)

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<String>()

    // The external immutable LiveData for the request status String
    val status: LiveData<String>
        get() = _status

    private val _property = MutableLiveData<MarsProperty>()
    val property: LiveData<MarsProperty>
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
                var listResult = getReferencesDeferred.await()
                if (listResult.size > 0) {
                    _property.value = listResult[0]
                }
                _status.value = "Success: ${listResult.size} mars properties are available"
            }
            catch (t:Throwable){
                _status.value = "Failure: " + t.message
            }}

        _status.value = "Set the Mars API Response here!"

        }

    override fun onCleared() {
        super.onCleared()
        viewmodelJob.cancel()
    }
}



