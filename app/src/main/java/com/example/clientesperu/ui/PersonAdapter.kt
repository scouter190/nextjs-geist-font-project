package com.example.clientesperu.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.clientesperu.R
import com.example.clientesperu.data.Person

class PersonAdapter(
    private val onItemClick: (Person) -> Unit
) : ListAdapter<Person, PersonAdapter.PersonViewHolder>(PersonDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_person, parent, false)
        return PersonViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PersonViewHolder(
        itemView: View,
        private val onItemClick: (Person) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvDocument: TextView = itemView.findViewById(R.id.tvDocument)
        private val tvPhone: TextView = itemView.findViewById(R.id.tvPhone)
        private val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        private val tvDistrict: TextView = itemView.findViewById(R.id.tvDistrict)

        fun bind(person: Person) {
            tvName.text = person.fullName
            tvDocument.text = person.documentId
            tvPhone.text = person.phone
            tvAddress.text = person.address
            tvDistrict.text = person.district

            itemView.setOnClickListener { onItemClick(person) }
        }
    }

    private class PersonDiffCallback : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem.documentId == newItem.documentId
        }

        override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem == newItem
        }
    }
}
