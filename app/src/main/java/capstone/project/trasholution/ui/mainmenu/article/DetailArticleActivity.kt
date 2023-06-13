package capstone.project.trasholution.ui.mainmenu.article

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.ActivityDetailArticleBinding
import capstone.project.trasholution.logic.repository.responses.ArticleAddItem
import com.bumptech.glide.Glide

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView(){
        val article = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(article, ArticleAddItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(article)
        }

        getDetaiStory(article)
    }
    private fun getDetaiStory(data: ArticleAddItem?) {
        Glide.with(this)
            .load(data?.imgUrl)
            .into(binding.ivArticle)
        with(binding){
            tvName.text = data?.title
            val date = data?.createDate.toString().split("-")
            val formattedDate = "${date[0]}-${date[1]}-${date[2].take(2)}"
            tvDate.text = resources.getString(R.string.uploadedAt, formattedDate)
            tvDesc.text = resources.getString(R.string.desc, data?.content)
        }
    }

    companion object{
        const val article = "article"
    }
}