package capstone.project.trasholution.ui.mainmenu.article

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.FragmentArticleBinding
import capstone.project.trasholution.logic.repository.responses.ArticleAddItem
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
        setupRV("")
        setupSearch()
        refreshApp()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity().applicationContext)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private fun setupSearch(){
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.etSearch
        searchView.setIconifiedByDefault(false)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                if (searchView.query.isEmpty()) {
                    setupRV("")
                } else {
                    setupRV(query)
                    searchView.clearFocus()
                }
                return true

            }
            override fun onQueryTextChange(query: String): Boolean {
                if (searchView.query.isEmpty()) {
                    setupRV("")
                } else {
                    setupRV(query)
                }
//                searchView.clearFocus()
                return false
            }
        })

    }

    private fun setupRV(query: String) {
        binding.rvArticle.layoutManager = LinearLayoutManager(requireActivity())
        val artikelAdapter = ArticleAdapter()

        binding.btnAddArtikel.setOnClickListener {
            val intent = Intent(activity, AddArticleActivity::class.java)
            startActivity(intent)
        }

        binding.rvArticle.adapter = artikelAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                artikelAdapter.retry()
            }
        )
        mainViewModel.getListArtikel(query).observe(requireActivity()) {
            artikelAdapter.submitData(lifecycle, it)
        }

        artikelAdapter.setOnItemClickCallback(object : ArticleAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArticleAddItem) {
                val detailIntent = Intent(requireActivity(), DetailArticleActivity::class.java)
                detailIntent.putExtra(DetailArticleActivity.article, data)
                startActivity(detailIntent)
            }
        })

    }

    private fun refreshApp() {
        binding.swipeToRefresh.setOnRefreshListener {
            setupSearch()
            binding.swipeToRefresh.isRefreshing = false
        }
    }

}