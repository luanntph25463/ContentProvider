package com.example.contentprovider

import android.content.ContentProviderOperation
import android.content.ContentProviderResult
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.Manifest
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment1 : Fragment() {
    private lateinit var contactsAdapter: ContactsAdapter

    private lateinit var contactId: String
    private lateinit var contactName: String
    private lateinit var contactPhoneNumber: String
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }
    companion object {
        private const val REQUEST_WRITE_CONTACTS = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_1, container, false)

        val bundle = arguments
        // Truy xuất các giá trị từ Bundle
        contactId = bundle?.getString("contactId", "").toString()
        contactName = bundle?.getString("contactName", "").toString()
        contactPhoneNumber = bundle?.getString("contactPhoneNumber", "").toString()

        val name: TextView? = view.findViewById(R.id.name)
        val phone: TextView? = view.findViewById(R.id.phone)
        val id: TextView? = view.findViewById(R.id.id_eidt)
        val add: Button? = view.findViewById(R.id.add)


        val contactsAdapter = ContactsAdapter(requireContext())

        name?.text = contactName
        id?.text = contactId
        phone?.text = contactPhoneNumber

        val editTextName: TextView? = view.findViewById(R.id.name)
        val editTextPhoneNumber: TextView? = view.findViewById(R.id.phone)


        if (add != null) {
            add.setOnClickListener {
                val name = editTextName?.text.toString().trim()
                val phone = editTextPhoneNumber?.text.toString().trim()

                if (name.isNotEmpty() && phone.isNotEmpty()) {
                    if (hasWriteContactsPermission()) {
                        addContactToPhoneBook(name, phone)
                    } else {
                        requestWriteContactsPermission()
                    }
                } else {
                    Toast.makeText(requireContext(), "Please enter name and phone number", Toast.LENGTH_SHORT).show()
                }
            }
        }


        // Thực hiện các hành động tiếp theo của bạn tại đây

        return view
    }
    private fun hasWriteContactsPermission(): Boolean {
        val permission = Manifest.permission.WRITE_CONTACTS
        val result = ContextCompat.checkSelfPermission(requireContext(), permission)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestWriteContactsPermission() {
        val permission = Manifest.permission.WRITE_CONTACTS
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), REQUEST_WRITE_CONTACTS)
    }

    private fun addContactToPhoneBook(name: String, phoneNumber: String) {
        try {
            val resolver: ContentResolver = requireContext().contentResolver
            val ops: ArrayList<ContentProviderOperation> = ArrayList()

            ops.add(
                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build())

            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build())

            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build())

            val results: Array<ContentProviderResult> = resolver.applyBatch(ContactsContract.AUTHORITY, ops)

            for (result in results) {
                if (result.uri == null) {
                    Toast.makeText(requireContext(), "Failed to add contact", Toast.LENGTH_SHORT).show()
                    return
                }
            }

            Toast.makeText(requireContext(), "Contact added successfully", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Failed to add contact", Toast.LENGTH_SHORT).show()
        }
    }
}
