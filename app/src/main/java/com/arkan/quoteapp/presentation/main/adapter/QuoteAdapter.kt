package com.arkan.quoteapp.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arkan.quoteapp.data.model.Quote
import com.arkan.quoteapp.databinding.ItemQuoteBinding

class QuoteAdapter : RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>() {
    private val asyncDataDiffer = AsyncListDiffer(
        this, object : DiffUtil.ItemCallback<Quote>() {
            override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

        }
    )

    fun submitData(items: List<Quote>) {
        asyncDataDiffer.submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        return QuoteViewHolder(
            ItemQuoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int  = asyncDataDiffer.currentList.size

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(asyncDataDiffer.currentList[position])
    }

    class QuoteViewHolder(private val binding: ItemQuoteBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(item: Quote) {
            binding.tvAnime.text = item.anime
            binding.tvCharacter.text = item.character
            binding.tvQuote.text = item.quote
        }
    }
}