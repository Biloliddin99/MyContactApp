package com.example.mycontactapp.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mycontactapp.R
import com.example.mycontactapp.adapters.MyRvAdapter
import com.example.mycontactapp.databinding.FragmentHomeBinding
import com.example.mycontactapp.helper.MySwipeHelper
import com.example.mycontactapp.listeners.MyButtonClickListener
import com.example.mycontactapp.models.Contact
import com.example.mycontactapp.models.MyButton
import com.github.florent37.runtimepermission.kotlin.askPermission


class HomeFragment : Fragment() {

    private lateinit var myRvAdapter: MyRvAdapter
    private lateinit var contactList: ArrayList<Contact>
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        myMethod()

        val swipe = object : MySwipeHelper(binding.root.context, binding.itemRv, 120) {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                //add button
                buffer.add(
                    MyButton(binding.root.context,
                        "Sms",
                        30,
                        R.drawable.ic_sms,
                        Color.parseColor("#FFDD2371"),
                        object : MyButtonClickListener {
                            override fun onClick(contact: Contact, position: Int) {
                                Toast.makeText(
                                    binding.root.context,
                                    "Delete id $position",
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigate(
                                    R.id.smsFragment,
                                    bundleOf("key" to contact)
                                )
                            }


                        })
                )
                buffer.add(
                    MyButton(requireContext(),
                        "Call",
                        30,
                        R.drawable.ic_call,
                        Color.parseColor("#FFF8CA2A"),
                        object : MyButtonClickListener {
                            override fun onClick(contact: Contact, position: Int) {
                                Toast.makeText(
                                    requireContext(),
                                    "Update id $position",
                                    Toast.LENGTH_SHORT
                                ).show()
                                calling(position)
                            }
                        })
                )
            }

        }


        return binding.root
    }


    private fun myMethod() {
        askPermission(Manifest.permission.READ_CONTACTS) {
            //all permissions already granted or just granted
            myRvAdapter = MyRvAdapter(binding.root.context, readContacts())
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

            if (e.hasForeverDenied()) {
                //the list of forever denied permissions, user has check 'never ask again'

                // you need to open setting manually if you really need it
                e.goToSettings();
            }
        }

    }

    @SuppressLint("Range")
    fun readContacts(): ArrayList<Contact> {
        val list = ArrayList<Contact>()

        val contacts = context?.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null
        )
        while (contacts!!.moveToNext()) {
            val contact = Contact(
                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
            )
            list.add(contact)
        }

        return list
    }

    private fun calling(position: Int) {

        askPermission(Manifest.permission.CALL_PHONE) {
            //all permissions already granted or just granted
            val phonNumber = contactList[position].number
            val intent = Intent(Intent(Intent.ACTION_CALL))
            intent.data = Uri.parse("tel:$phonNumber")
            startActivity(intent)
        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(requireContext())
                    .setMessage("Ruxsat bermasangiz ilova ishlay olmaydi ruxsat bering...")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if (e.hasForeverDenied()) {
                //the list of forever denied permissions, user has check 'never ask again'

                // you need to open setting manually if you really need it
                e.goToSettings();
            }
        }
    }

}
