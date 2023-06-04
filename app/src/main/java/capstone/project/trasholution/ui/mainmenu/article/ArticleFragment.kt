package capstone.project.trasholution.ui.mainmenu.article

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import capstone.project.trasholution.databinding.FragmentArticleBinding
import capstone.project.trasholution.logic.repository.responses.ArticleItem
import capstone.project.trasholution.ui.ViewModelFactory
import capstone.project.trasholution.ui.mainmenu.MainViewModel
import capstone.project.trasholution.ui.mainmenu.LoadingStateAdapter
import capstone.project.trasholution.ui.mainmenu.collector.PengepulAdapter

class ArticleFragment : Fragment() {
    private lateinit var binding : FragmentArticleBinding
    private lateinit var mainViewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentArticleBinding.inflate(inflater, container, false)
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
        binding.rvArticle.layoutManager = LinearLayoutManager(requireActivity())
        val artikelAdapter = ArticleAdapter()

        binding.rvArticle.adapter = artikelAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                artikelAdapter.retry()
            }
        )
        mainViewModel.getListArtikel.observe(requireActivity()) {
            Log.d("storyilang", (it == null).toString())
            artikelAdapter.submitData(lifecycle, it)
        }

        artikelAdapter.setOnItemClickCallback(object : ArticleAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArticleItem) {
                val detailIntent = Intent(requireActivity(), DetailArticleActivity::class.java)
                detailIntent.putExtra(DetailArticleActivity.article, data)
                startActivity(detailIntent)
            }
        })

    }

    private fun refreshApp() {
        binding.swipeToRefresh.setOnRefreshListener {
            setupView()
            binding.swipeToRefresh.isRefreshing = false
        }
    }

}