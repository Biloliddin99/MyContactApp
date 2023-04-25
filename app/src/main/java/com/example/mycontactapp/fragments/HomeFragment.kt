package com.example.mycontactapp.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mycontactapp.R
import com.example.mycontactapp.adapters.MyRvAdapter
import com.example.mycontactapp.databinding.FragmentHomeBinding
import com.example.mycontactapp.db.MyDbHelper
import com.example.mycontactapp.models.Contact
import com.github.florent37.runtimepermission.kotlin.askPermission


class HomeFragment : Fragment() {

    private lateinit var myRvAdapter: MyRvAdapter
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        myMethod()

        return binding.root
    }


    private fun myMethod(){
        askPermission(Manifest.permission.READ_CONTACTS){
            //all permissions already granted or just granted
            myRvAdapter = MyRvAdapter(binding.root.context,readContacts())
            binding.itemRv.adapter = myRvAdapter
        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(context)
                    .setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if(e.hasForeverDenied()) {
                //the list of forever denied permissions, user has check 'never ask again'

                // you need to open setting manually if you really need it
                e.goToSettings();
            }
        }

    }

    @SuppressLint("Range")
    fun readContacts():ArrayList<Contact> {
        val list = ArrayList<Contact>()

        val contacts = context?.contentResolver?.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null)
        while (contacts!!.moveToNext()){
            val contact = Contact(
                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
            )
            list.add(contact)
        }

        return list
    }

}
