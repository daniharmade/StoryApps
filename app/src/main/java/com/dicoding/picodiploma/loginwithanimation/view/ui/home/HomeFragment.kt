package com.dicoding.picodiploma.loginwithanimation.view.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.adapter.StoryListAdapter
import com.dicoding.picodiploma.loginwithanimation.databinding.FragmentHomeBinding
import com.dicoding.picodiploma.loginwithanimation.view.factory.StoryViewModelFactory

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            StoryViewModelFactory.getInstance(requireContext())
        )[HomeViewModel::class.java]

        val adapter = StoryListAdapter()
        binding.rvStory.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvStory.layoutManager = layoutManager

        viewModel.stories.observe(viewLifecycleOwner) { stories ->
            adapter.submitList(stories)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                showToast(it)
            }
        }
        viewModel.loadStories()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}