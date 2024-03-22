package com.example.loginapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.loginapp.databinding.FragmentPermissionBinding


class Permission : Fragment() {
    private var _binding: FragmentPermissionBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "null"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentPermissionBinding.inflate(inflater, container, false)
            .also {
                _binding = it
            }.root
    }


    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {permissionGranted ->
        if(permissionGranted)
            resultLauncherGetContacts.launch(intent)
    }


    val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)

    @SuppressLint("Range")
    val resultLauncherGetContacts = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if(it.resultCode == Activity.RESULT_OK) {
            var uri: Uri = it.data?.data ?: return@registerForActivityResult
            Log.d("uri->", uri.toString() + "\n" + it.data?.toString())
            val cursor: Cursor = activity?.contentResolver?.query(
                uri, null, null, null, null
            ) ?: return@registerForActivityResult
            if(cursor.moveToFirst()) {
                val id: String =
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val hasPhone: String =
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                if(hasPhone.equals("1")) {
                   Log.d("Phone.CONTENT_URI -> ", ContactsContract.CommonDataKinds.Phone.CONTENT_URI.toString())
                    val cursorPhones: Cursor = activity?.contentResolver?.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                        null,
                        null
                    ) ?: return@registerForActivityResult
                    cursorPhones.moveToFirst()
                    val number =
                        cursorPhones.getString(cursorPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val name =
                        cursorPhones.getString(cursorPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    Log.d("DATA:", "number: $number ; name:$name")
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            /*shareButton.setOnClickListener{
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Hello, my friend")
                    putExtra(Intent.EXTRA_SUBJECT, "subject")
                }.also {
                    val chooserIntent =
                        Intent.createChooser(it, "message to my friend")
                    startActivity(chooserIntent)
                }
            }

            smsButton.setOnClickListener{
                permissionLauncher.launch(PERMISSION)
            }*/



        }
    }

    companion object {
        val PERMISSION = android.Manifest.permission.READ_CONTACTS
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}

