package capstone.project.trasholution.ui.mainmenu.collector

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.FragmentCollectorBinding
import capstone.project.trasholution.logic.repository.responses.ArticleAddItem
import capstone.project.trasholution.logic.repository.responses.DataItem
import capstone.project.trasholution.ui.ViewModelFactory
import capstone.project.trasholution.ui.mainmenu.LoadingStateAdapter
import capstone.project.trasholution.ui.mainmenu.MainViewModel
import capstone.project.trasholution.ui.mainmenu.article.ArticleAdapter
import capstone.project.trasholution.ui.mainmenu.article.DetailArticleActivity
import capstone.project.trasholution.ui.map.MapsActivity
import createToast

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

        pengepulAdapter.setOnItemClickCallback(object : PengepulAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataItem) {
                copyText(requireContext(), data.contact)
            }
        })

    }

    fun copyText(context: Context, string: String) {
        val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("label", string)
        clipboardManager.setPrimaryClip(clipData)
        createToast(requireActivity(), getString(R.string.no_copied))
    }

    private fun refreshApp() {
        binding.swipeToRefresh.setOnRefreshListener {
            setupView()
            binding.swipeToRefresh.isRefreshing = false
        }
    }

}