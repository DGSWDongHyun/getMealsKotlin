package com.project.getmeals.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class ViewModelCheck : ViewModel() {
    var currentCheck: MutableLiveData<Boolean>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
            }
            return field
        }
        private set
}