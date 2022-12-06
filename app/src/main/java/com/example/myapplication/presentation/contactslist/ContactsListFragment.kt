package com.example.myapplication.presentation.contactslist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentContactsListBinding
import com.example.myapplication.utils.*
import kotlin.properties.Delegates

class ContactsListFragment: Fragment() {
    private var binding by Delegates.notNull<FragmentContactsListBinding>()
    private var adapter by Delegates.notNull<ContactsListAdapter>()

    private val viewModel: ContactsListViewModel by viewModels { ContactsListViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentContactsListBinding.inflate(inflater, container, false)
        adapter = ContactsListAdapter(viewModel)

        viewModel.contacts.observe(viewLifecycleOwner){
            hideAll()
            when(it){
                is SuccessResult -> {
                    adapter.contacts = it.data
                    binding.contactsListView.visibility = View.VISIBLE
                }
                is ErrorResult -> {
                }
                is PendingResult -> {
                    binding.progressBarView.visibility = View.VISIBLE
                }
                is EmptyResult -> {

                }
            }
        }

        viewModel.actionShowToast.observe(viewLifecycleOwner){
            it.getValue()?.let{ message ->
                navigator().showToast(message)
            }
        }

        viewModel.actionContactClicked.observe(viewLifecycleOwner){
            it.getValue()?.let{number ->
                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
                startActivity(callIntent)
            }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.contactsListView.layoutManager = layoutManager
        binding.contactsListView.adapter = adapter

        return binding.root
    }

    private fun hideAll(){
        binding.contactsListView.visibility = View.GONE
        binding.progressBarView.visibility = View.GONE
    }
}