package com.dicoding.picodiploma.loginwithanimation.view.ui.upload

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.repository.AppRepository
import java.io.File

class UploadViewModel(private val repository: AppRepository) : ViewModel() {
    fun uploadImage(file: File, description: String) = repository.uploadImage(file, description)
}