package com.pierreduchemin.smsforward.presentation.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pierreduchemin.smsforward.data.source.database.ReplacementRule
import com.pierreduchemin.smsforward.databinding.ReplacementRuleItemBinding

class ReplacementRuleAdapter(
    private val onRuleChanged: (ReplacementRule) -> Unit,
    private val onRuleDeleted: (ReplacementRule) -> Unit
) : RecyclerView.Adapter<ReplacementRuleAdapter.ViewHolder>() {

    private val items = mutableListOf<ReplacementRule>()

    fun setItems(newItems: List<ReplacementRule>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = items.size
            override fun getNewListSize(): Int = newItems.size
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean = items[oldPos].id == newItems[newPos].id
            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean = items[oldPos] == newItems[newPos]
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ReplacementRuleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ReplacementRuleItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private var currentRule: ReplacementRule? = null

        init {
            binding.etPattern.doAfterTextChanged {
                val rule = currentRule ?: return@doAfterTextChanged
                if (rule.pattern != it.toString()) {
                    currentRule = rule.copy(pattern = it.toString())
                    onRuleChanged(currentRule!!)
                }
            }
            binding.etReplacement.doAfterTextChanged {
                val rule = currentRule ?: return@doAfterTextChanged
                if (rule.replacement != it.toString()) {
                    currentRule = rule.copy(replacement = it.toString())
                    onRuleChanged(currentRule!!)
                }
            }
            binding.btnDelete.setOnClickListener {
                currentRule?.let { onRuleDeleted(it) }
            }
        }

        fun bind(rule: ReplacementRule) {
            currentRule = rule
            if (binding.etPattern.text.toString() != rule.pattern) {
                binding.etPattern.setText(rule.pattern)
            }
            if (binding.etReplacement.text.toString() != rule.replacement) {
                binding.etReplacement.setText(rule.replacement)
            }
        }
    }
}
