package com.example.contentprovider

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class ContactsAdapter(private val context: Context) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {
    private val contacts: MutableList<Contact> = mutableListOf()
    private var editButtonClickListener: OnEditButtonClickListener? = null
    private var deleteButtonClickListener: OnEditButtonClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    fun setData(data: List<Contact>) {
        contacts.clear()
        contacts.addAll(data)
        notifyDataSetChanged()
    }

    fun setOnEditButtonClickListener(listener: OnEditButtonClickListener) {
        editButtonClickListener = listener
        deleteButtonClickListener = listener
    }

    fun addContact(contact: Contact) {
        contacts.add(contact)
        contacts.forEach {
            Log.d("ContactsAdapter", "Name: ${it.name}, Phone: ${it.phoneNumber}")
        }
        notifyDataSetChanged()
    }


    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textName: TextView = itemView.findViewById(R.id.textName)
        private val textPhoneNumber: TextView = itemView.findViewById(R.id.textPhoneNumber)
        private val editButton: Button = itemView.findViewById(R.id.edt)
        private val delete: Button = itemView.findViewById(R.id.delete)

        fun bind(contact: Contact) {
            textName.text = contact.id.toString()
            textPhoneNumber.text = contact.phoneNumber

            editButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedContact = contacts[position]
                    Log.d("contact","$selectedContact")
                    editButtonClickListener?.onEditButtonClick(selectedContact)
                }
            }
            delete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedContact = contacts[position]
                    Log.d("id khi xoa", selectedContact.id.toString())
                    deleteButtonClickListener?.deleteContactFromFragment(selectedContact.id)
                }
            }
        }
    }

    interface OnEditButtonClickListener {
        fun onEditButtonClick(contact: Contact)
        fun deleteContactFromFragment(id: Int)
    }
}

