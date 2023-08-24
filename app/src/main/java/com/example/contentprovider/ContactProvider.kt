package com.example.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class ContactProvider : ContentProvider() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(): Boolean {
        dbHelper = DatabaseHelper(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val db = dbHelper.readableDatabase
        return db.query(
            TABLE_CONTACTS,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.dir/$AUTHORITY.$TABLE_CONTACTS"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper.writableDatabase
        val id = db.insert(TABLE_CONTACTS, null, values)
        return Uri.withAppendedPath(CONTENT_URI, id.toString())
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = dbHelper.writableDatabase
        return db.delete(TABLE_CONTACTS, selection, selectionArgs)
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val db = dbHelper.writableDatabase
        return db.update(TABLE_CONTACTS, values, selection, selectionArgs)
    }

    companion object {
        private const val AUTHORITY = "com.example.contactsbook"
        private const val TABLE_CONTACTS = "contacts"
        private const val CONTENT_URI_PATH = "contacts"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$CONTENT_URI_PATH")
    }
}