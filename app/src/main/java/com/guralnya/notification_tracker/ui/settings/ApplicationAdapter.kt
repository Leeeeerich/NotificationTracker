package com.guralnya.notification_tracker.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guralnya.notification_tracker.databinding.ItemApplicationBinding
import com.guralnya.notification_tracker.model.models.vo.IgnoreAppVo

class ApplicationAdapter : RecyclerView.Adapter<ApplicationAdapter.ApplicationItemHolder>() {

    var list = mutableListOf<IgnoreAppVo>()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ApplicationItemHolder(
        ItemApplicationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: ApplicationItemHolder, position: Int) {
        holder.bind(list[position])
    }

    fun getIgnorePackages() = list.filter { it.isChecked.get() }

    inner class ApplicationItemHolder(private val binding: ItemApplicationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: IgnoreAppVo) {
            binding.ignoreAppVo = data
        }
    }
}