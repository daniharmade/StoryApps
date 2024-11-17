package com.dicoding.picodiploma.loginwithanimation.view.ui.upload

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.pref.Result
import com.dicoding.picodiploma.loginwithanimation.data.pref.getImageUri
import com.dicoding.picodiploma.loginwithanimation.data.pref.reduceFileImage
import com.dicoding.picodiploma.loginwithanimation.data.pref.uriToFile
import com.dicoding.picodiploma.loginwithanimation.databinding.FragmentUploadBinding
import com.dicoding.picodiploma.loginwithanimation.view.factory.StoryViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.ui.home.HomeFragment
import com.dicoding.picodiploma.loginwithanimation.view.ui.main.MainActivity

class UploadFragment : Fragment() {
    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null


    private val viewModel by viewModels<UploadViewModel> {
        StoryViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnCamera.setOnClickListener {
            startCamera()
        }

        binding.btnUpload.setOnClickListener {
            uploadImage()
        }

    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun uploadImage() {
        if (currentImageUri != null && binding.description.text.toString().isNotEmpty()) {
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
                val description = binding.description.text.toString()

                viewModel.uploadImage(imageFile, description)
                    .observe(viewLifecycleOwner) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.DataLoading -> {
                                    showLoading(true)
                                }

                                is Result.DataSuccess -> {
                                    AlertDialog.Builder(requireContext()).apply {
                                        setTitle("Yeah!")
                                        setMessage(getString(R.string.success_upload))
                                        setNeutralButton("Oke") { _, _ ->

                                        }
                                        create()
                                        show()
                                    }
                                    showLoading(false)
                                    navigateToHomeFragment()
                                }

                                is Result.DataError -> {
                                    showToast(result.error)
                                    showLoading(false)
                                }
                            }
                        }
                    }
            }
        } else {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Ups!")
                setMessage(getString(R.string.failed_upload))
                setNeutralButton("Oke") { _, _ ->

                }
                create()
                show()
            }
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imagePreview.setImageURI(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToHomeFragment() {
        val homeFragment = HomeFragment()
        (activity as? MainActivity)?.switchFragment(homeFragment)
    }
}