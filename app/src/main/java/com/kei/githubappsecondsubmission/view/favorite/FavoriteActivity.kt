package com.kei.githubappsecondsubmission.view.favorite


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kei.githubappsecondsubmission.databinding.ActivityFavoriteBinding
import com.kei.githubappsecondsubmission.domain.data.model.UsersItem
import com.kei.githubappsecondsubmission.view.detail.dashboard.DetailActivity
import com.kei.githubappsecondsubmission.view.home.MainAdapter
import com.kei.githubappsecondsubmission.view.home.OnItemClickCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {
    private lateinit var favoriteViewBinding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteViewBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteViewBinding.root)
        customAppBar()
        setViewModelProvider()
        observeData()
        showRecyclerView()
    }


    private fun showRecyclerView() {
        favoriteViewBinding.rvFavorite.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            adapter = MainAdapter(mutableListOf())
        }
    }

    private fun observeData() {
        favoriteViewModel.favoriteLiveData.observe(this, {
            if (it?.isEmpty() == true) {
                favoriteViewBinding.apply {
                    ivFavoriteError.visibility = View.VISIBLE
                    rvFavorite.visibility = View.GONE
                }
            } else {
                favoriteViewBinding.apply {
                    ivFavoriteError.visibility = View.GONE
                    rvFavorite.visibility = View.VISIBLE
                }
                val mainAdapter = MainAdapter(it?.map { detailUserResponse ->
                    UsersItem(
                        detailUserResponse.login,
                        type = "",
                        detailUserResponse.avatar_url ?: ""
                    )
                })
                mainAdapter.setItemClickCallback(object : OnItemClickCallback {
                    override fun onItemClicked(user: UsersItem?) {
                        val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.EXTRA_DATA, user)
                        startActivity(intent)
                    }

                })
                favoriteViewBinding.rvFavorite.adapter = mainAdapter
            }
        })
    }

    private fun setViewModelProvider() {
        favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
    }

    private fun customAppBar() {
        title = "Favorite"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.getDataListFavorite()
    }

}