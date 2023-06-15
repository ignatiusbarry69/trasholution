package capstone.project.trasholution.ui.mainmenu.collector

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import capstone.project.trasholution.databinding.FragmentCollectorBinding
import capstone.project.trasholution.ui.ViewModelFactory
import capstone.project.trasholution.ui.mainmenu.LoadingStateAdapter
import capstone.project.trasholution.ui.mainmenu.MainViewModel

class CollectorFragment : Fragment() {
    private lateinit var binding : FragmentCollectorBinding
    private lateinit var mainViewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCollectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupView()
        refreshApp()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity().applicationContext)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private fun setupView() {
        binding.rvPengepul.layoutManager = LinearLayoutManager(requireActivity())
        val pengepulAdapter = PengepulAdapter()

        binding.rvPengepul.adapter = pengepulAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                pengepulAdapter.retry()
            }
        )
        mainViewModel.getListPengepul.observe(requireActivity()) {
            pengepulAdapter.submitData(lifecycle, it)
        }

    }

    private fun refreshApp() {
        binding.swipeToRefresh.setOnRefreshListener {
            setupView()
            binding.swipeToRefresh.isRefreshing = false
        }
    }

}