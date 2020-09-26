package com.project.getmeals.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ViewModelCheckFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return try {
            modelClass.newInstance()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            throw RuntimeException("팩토리 런타임 에러")
        } catch (e: InstantiationException) {
            e.printStackTrace()
            throw RuntimeException("팩토리 런타임 에러2")
        }
    }
}