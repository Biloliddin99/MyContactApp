package com.example.mycontactapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mycontactapp.models.Contact
import com.example.mycontactapp.utils.Constants
import com.example.mycontactapp.utils.Constants.CONTACT_ID
import com.example.mycontactapp.utils.Constants.CONTACT_NAME
import com.example.mycontactapp.utils.Constants.CONTACT_NUMBER
import com.example.mycontactapp.utils.Constants.CONTACT_TABLE
import com.example.mycontactapp.utils.Constants.DB_NAME


class MyDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, Constants.DB_VERSION),
    MyDbInterface {

    override fun onCreate(p0: SQLiteDatabase?) {
        val contactQuery =
            "create table $CONTACT_TABLE ($CONTACT_ID integer not null primary key autoincrement unique, $CONTACT_NAME text not null, $CONTACT_NUMBER text not null)"

        p0?.execSQL(contactQuery)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    override fun addContacts(contact: Contact) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(CONTACT_NAME, contact.name)
        contentValues.put(CONTACT_NUMBER, contact.number)
        database.insert(CONTACT_TABLE, null, contentValues)
        database.close()
    }

    override fun getAllContacts(): ArrayList<Contact> {
        val database = this.readableDatabase
        val contactList = ArrayList<Contact>()
        val cursor = database.rawQuery("select * from $CONTACT_TABLE", null)
        if (cursor.moveToFirst()) {
            do {
                contactList.add(
                    Contact(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                    )
                )
            } while (cursor.moveToNext())
        }
        return contactList
    }




}