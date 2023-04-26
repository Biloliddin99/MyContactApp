package com.example.mycontactapp.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mycontactapp.Manifest
import com.example.mycontactapp.R
import com.example.mycontactapp.databinding.FragmentSmsBinding
import com.example.mycontactapp.models.Contact
import com.github.florent37.runtimepermission.kotlin.askPermission

class SmsFragment : Fragment() {


    private lateinit var binding: FragmentSmsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSmsBinding.inflate(layoutInflater)

        val contact = arguments?.getSerializable("key") as Contact
        binding.txtNameSms.text = contact.name
        binding.txtNumberSms.text=contact.number

       binding.btnSend.setOnClickListener {
            askPermission(android.Manifest.permission.SEND_SMS){
                //all permissions already granted or just granted

                val matn = binding.edtMatn.text.toString()
                var obj = SmsManager.getDefault()
                obj.sendTextMessage(contact.number,
                    null,  matn,
                    null, null)
                Toast.makeText(requireContext(), "Send message", Toast.LENGTH_SHORT).show()

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

                if(e.hasForeverDenied()) {
                    //the list of forever denied permissions, user has check 'never ask again'

                    // you need to open setting manually if you really need it
                    e.goToSettings();
                }
            }
        }



        return binding.root
    }


}