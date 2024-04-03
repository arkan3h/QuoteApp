package com.arkan.quoteapp.presentation.main

import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.arkan.quoteapp.R
import com.arkan.quoteapp.data.datasource.QuoteApiDataSource
import com.arkan.quoteapp.data.datasource.QuoteDataSource
import com.arkan.quoteapp.data.repository.QuoteRepository
import com.arkan.quoteapp.data.repository.QuoteRepositoryImpl
import com.arkan.quoteapp.data.source.network.services.QuoteApiServices
import com.arkan.quoteapp.databinding.ActivityMainBinding
import com.arkan.quoteapp.utils.GenericViewModelFactory
import com.arkan.quoteapp.utils.proceedWhen

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val quoteAdapter = QuoteAdapter()
    private val viewModel: MainViewModel by viewModels {
        val s = QuoteApiServices.invoke()
        val ds: QuoteDataSource = QuoteApiDataSource(s)
        val rp: QuoteRepository = QuoteRepositoryImpl(ds)
        GenericViewModelFactory.create(MainViewModel(rp))
    }
    private lateinit var swipeRefreshLayout : SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        observeQuote()
        bindQuoteList()
        refreshLayout()
    }

    private fun refreshLayout() {
        swipeRefreshLayout = binding.swipe
        swipeRefreshLayout.setOnRefreshListener {
            observeQuote()
            bindQuoteList()
            Handler().postDelayed(
                { swipeRefreshLayout.isRefreshing = false },
                1000
            )
        }
    }

    private fun observeQuote() {
        viewModel.getQuotes().observe(this) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.rvQuote.isVisible = false
                },
                doOnError = {
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = result.exception?.message.orEmpty()
                    binding.rvQuote.isVisible = false
                },
                doOnSuccess = {
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    binding.rvQuote.isVisible = true
                    result.payload?.let { quote ->
                        quoteAdapter.submitData(quote)
                    }
                },
                doOnEmpty = {
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = getString(R.string.text_cart_is_empty)
                    binding.rvQuote.isVisible = false
                }
            )
        }
    }

    private fun bindQuoteList() {
        binding.rvQuote.adapter = this@MainActivity.quoteAdapter
    }
}