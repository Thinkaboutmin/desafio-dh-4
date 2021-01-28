package com.example.dhdesafio4.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dhdesafio4.R
import com.example.dhdesafio4.data.GameItem
import com.example.dhdesafio4.databinding.GameItemBinding
import com.squareup.picasso.Picasso
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*
import kotlin.collections.ArrayList

class GamesAdapter(val gameItems: ArrayList<GameItem>, val holderClick: ViewHolderClick)
    : RecyclerView.Adapter<GamesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = GameItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gameItem = gameItems[position]
        Picasso.get().load(gameItem.imageURI).into(holder.binding.imgCard)
        val formatter = SimpleDateFormat("yyyy", Locale.UK)
        holder.binding.tvCardContent.text = formatter.format(gameItem.createdAt)
        holder.binding.tvCardTitulo.text = gameItem.name
    }

    override fun getItemCount(): Int = gameItems.size

    interface ViewHolderClick {
        fun onClick(gameItem: GameItem)
    }

    inner class ViewHolder(val binding: GameItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
            init {
                binding.content.setOnClickListener(this)
            }

        override fun onClick(p0: View?) {
            holderClick.onClick(gameItems[adapterPosition])
        }
    }
}