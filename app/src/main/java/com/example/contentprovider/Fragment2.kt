package com.example.contentprovider

import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.Manifest;
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.android.synthetic.main.fragment_2.addContact

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Fragment2 : Fragment(), ContactsAdapter.OnEditButtonClickListener {
    //
    private lateinit var spinner: Spinner
    //
    private lateinit var contactsRecyclerView: RecyclerView
    private lateinit var contactsAdapter: ContactsAdapter
    val fragment1 = Fragment1()

    private val REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_2, container, false)
        // spinner
        spinner = rootView.findViewById(R.id.spinnerId)




        contactsAdapter = ContactsAdapter(requireContext())
        val s = contactsAdapter.setOnEditButtonClickListener(this)
        Log.d("TAG", "$s")

        val spinnerData = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerData)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = spinnerData[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        contactsRecyclerView = rootView.findViewById(R.id.contactList)
        contactsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        contactsRecyclerView.adapter = contactsAdapter



        // Kiểm tra quyền truy cập danh bạ
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Nếu quyền đã được cấp, truy vấn danh bạ
                listContacts()
            } else {
                // Nếu quyền không được cấp, xử lý theo ý của bạn
                // ví dụ: hiển thị thông báo hoặc tiến hành các hành động thay thế
            }
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Nếu quyền chưa được cấp, yêu cầu người dùng cấp quyền
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        } else {
            // Nếu quyền đã được cấp, truy vấn danh bạ
            listContacts()
        }

        return rootView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addContact.setOnClickListener {
            moveToFragment(fragment1)
        }

    }

    fun moveToFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Nếu quyền đã được cấp, truy vấn danh bạ
                listContacts()
            } else {
                // Nếu quyền bị từ chối, xử lý tương ứng (hiển thị thông báo hoặc vô hiệu hóa chức năng)
            }
        }
    }

    private fun listContacts() {
        val contacts: MutableList<Contact> = mutableListOf()

        // Truy vấn danh bạ thông qua ContentProvider
        val cursor: Cursor? = requireContext().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            while (cursor.moveToNext()) {
                val id: String = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID) + 0)
                val name: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME) + 1 )
                val phoneNumber: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)  + 0)
                val contact = Contact(id,name, phoneNumber)
                contacts.add(contact)
            }
        }

        contactsAdapter.setData(contacts)
    }

    // nhân giá trị các item in recycleView
    override fun onEditButtonClick(contact: Contact) {
        val fragment1 = Fragment1()

        // Truyền dữ liệu liên hệ sang Fragment1
        val bundle = Bundle()
        bundle.putString("contactId", contact.id)
        bundle.putString("contactName", contact.name)
        bundle.putString("contactPhoneNumber", contact.phoneNumber)
        fragment1.arguments = bundle

        // Chuyển đổi Fragment1
        moveToFragment(fragment1)

    }

}




