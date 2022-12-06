package com.example.myapplication.presentation.contactslist

import androidx.recyclerview.widget.DiffUtil


class ContactsListDiffUtilCallback(
    val oldList: List<ContactListItem>,
    val newList: List<ContactListItem>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContact = oldList[oldItemPosition]
        val newContact = newList[newItemPosition]

        return oldContact.contact.id == newContact.contact.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContact = oldList[oldItemPosition]
        val newContact = newList[newItemPosition]

        return oldContact == newContact
    }
}