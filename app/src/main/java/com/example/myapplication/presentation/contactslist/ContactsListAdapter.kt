package com.example.myapplication.presentation.contactslist

import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.repository.ContactsListener
import com.example.myapplication.databinding.ContactItemBinding
import com.example.myapplication.domain.entinties.Contact

class ContactsListAdapter(
    private val contactListener: ContactClickListener
): RecyclerView.Adapter<ContactsListAdapter.ContactViewHolder>(), View.OnClickListener {

    private val TAG = this::class.java.simpleName

    var contacts: List<ContactListItem> = emptyList()
        set(value){
            val diffUtilCallback = ContactsListDiffUtilCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
            field = value
            diffResult.dispatchUpdatesTo(this)

            /**
             * simplest: notifyDataSetChanged()
             * notifyitemchanged()
             * notifyitemmoved()
             * notifyrangeinserted()
             * notifyitemrangechanged()
             */
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ContactItemBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.moreBtn.setOnClickListener(this)

        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contactListItem = contacts[position]

        with(holder.binding){
            holder.itemView.tag = contactListItem.contact
            moreBtn.tag = contactListItem.contact

            contactNameView.text = contactListItem.contact.name
            contactPhoneNumberView.text = contactListItem.contact.phoneNumber

            if (contactListItem.inProgress){
                moreBtn.visibility = View.GONE
                moreProgressBar.visibility = View.VISIBLE
                moreBtn.setOnClickListener(null)
            }
            else {
                moreBtn.visibility = View.VISIBLE
                moreProgressBar.visibility = View.GONE
                moreBtn.setOnClickListener(this@ContactsListAdapter)
            }

            if(contactListItem.contact.avatar.isNotBlank()){
                Glide.with(avatarView.context)
                    .load(contactListItem.contact.avatar)
                    .circleCrop()
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .error(R.drawable.ic_baseline_account_circle_24)
                    .into(avatarView)
            }
            else {
                Glide.with(avatarView.context)
                    .load(R.drawable.ic_baseline_account_circle_24)
                    .circleCrop()
                    .into(avatarView)
            }
        }
    }

    override fun getItemCount(): Int = contacts.size

    override fun onClick(v: View) {
        val contact = v.tag as Contact

        when(v.id){
            R.id.moreBtn -> {
                createPopupMenu(v)
            }
            else -> {
                Log.d(TAG, "On contact clicked")
                contactListener.contactClicked(contact)
            }
        }
    }

    private fun createPopupMenu(v: View){
        val contact = v.tag as Contact
        val context = v.context
        val popupMenu = PopupMenu(context, v)

        popupMenu.menu.add(0, 1, Menu.NONE, "Delete")

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                1 -> {
                    contactListener.deleteContact(contact)
                }
                else -> {

                }
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    class ContactViewHolder(
        val binding: ContactItemBinding
    ): RecyclerView.ViewHolder(binding.root)

}