package com.example.mycontactapp.db

import com.example.mycontactapp.models.Contact

interface MyDbInterface {
    fun addContacts(contact: Contact)
    fun getAllContacts(): ArrayList<Contact>

//    fun readContacts():ArrayList<Contact>
}