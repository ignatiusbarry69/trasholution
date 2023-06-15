package capstone.project.trasholution.ui.mainmenu.collector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import capstone.project.trasholution.databinding.ItemPengepulBinding
import capstone.project.trasholution.logic.repository.responses.DataItem

class PengepulAdapter: PagingDataAdapter<DataItem, PengepulAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: ItemPengepulBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pengepulUser: DataItem) {
            with(binding) {
                userName.text = pengepulUser.username
                ads.text = pengepulUser.description
                contact.text = pengepulUser.contact
                alamat.text = pengepulUser.location
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPengepulBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pengepul = getItem(position)

        if (pengepul != null) {
            holder.bind(pengepul)
//            holder.itemView.setOnClickListener {
//                onItemClickCallback.onItemClicked(pengepul)
//            }
        }
    }



    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}
