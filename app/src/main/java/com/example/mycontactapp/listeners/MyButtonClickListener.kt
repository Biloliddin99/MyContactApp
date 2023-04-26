package com.example.mycontactapp.listeners

import com.example.mycontactapp.models.Contact


interface MyButtonClickListener {
    fun onClick(contact: Contact, position: Int)
}