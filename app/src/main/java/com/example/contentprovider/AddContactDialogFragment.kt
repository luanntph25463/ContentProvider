package com.example.contentprovider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class AddContactDialogFragment : DialogFragment() {
    private lateinit var editTextName: EditText
    private lateinit var editTextPhoneNumber: EditText
    private lateinit var buttonAdd: Button

    private var onContactAddedListener: OnContactAddedListener? = null

    interface OnContactAddedListener {
        fun onContactAdded(contact: Contact)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_1, container, false)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextName = view.findViewById(R.id.name)
        editTextPhoneNumber = view.findViewById(R.id.phone)
        buttonAdd = view.findViewById(R.id.add)


    }

    fun setOnContactAddedListener(listener: OnContactAddedListener) {
        onContactAddedListener = listener
    }

    companion object {
        fun newInstance(): AddContactDialogFragment {
            return AddContactDialogFragment()
        }
    }
}