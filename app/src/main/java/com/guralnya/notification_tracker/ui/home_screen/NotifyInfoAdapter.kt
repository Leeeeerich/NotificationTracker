package com.guralnya.notification_tracker.ui.home_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guralnya.notification_tracker.databinding.ItemNotifyInfoBinding
import com.guralnya.notification_tracker.model.models.NotifyInfo

class NotifyInfoAdapter(private val selectableModeListener: (Boolean) -> Unit) :
    RecyclerView.Adapter<NotifyInfoAdapter.NotifyInfoItem>() {

    var isSelectableMode: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var list: MutableList<NotifyInfo> = mutableListOf()
        set(value) {
            if (field.size > 0) list.clear()
            field.addAll(value)
            notifyDataSetChanged() //TODO upgrade with pagination and DiffUtil
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NotifyInfoItem(
        ItemNotifyInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: NotifyInfoItem, position: Int) {
        holder.bind(list[position])
    }

    inner class NotifyInfoItem(private val binding: ItemNotifyInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: NotifyInfo) {
            binding.notifyInfo = data
            binding.selectableMode = isSelectableMode
            binding.root.setOnLongClickListener {
                selectableModeListener.invoke(true)
                isSelectableMode = true
                true
            }
            binding.root.setOnClickListener {
                if (isSelectableMode) {
                    data.isChecked.set(!data.isChecked.get())
                }
            }
        }
    }
}
