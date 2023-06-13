package capstone.project.trasholution.ui.mainmenu.article

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import capstone.project.trasholution.databinding.ItemArtikelBinding
import capstone.project.trasholution.logic.repository.responses.ArticleAddItem
import com.bumptech.glide.Glide
import setLocalDateFormat

class ArticleAdapter: PagingDataAdapter<ArticleAddItem, ArticleAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: ItemArtikelBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(articleUser: ArticleAddItem) {
            with(binding) {
                tvItemName.text = articleUser.title
                createdAt.setLocalDateFormat(articleUser.createDate)
                tvItemDescription.text = articleUser.content
                Glide.with(itemView.context)
                    .load(articleUser.imgUrl)
                    .into(imgWaste)
            }
            itemView.animation =
                AnimationUtils.loadAnimation(itemView.context, R.anim.slide_in_left)
            itemView.startAnimation(itemView.animation)
        }
    }
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemArtikelBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = getItem(position)

        if (article != null) {
            holder.bind(article)
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(article)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ArticleAddItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticleAddItem>() {
            override fun areItemsTheSame(oldItem: ArticleAddItem, newItem: ArticleAddItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticleAddItem, newItem: ArticleAddItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}
