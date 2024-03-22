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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.loginapp.databinding.FragmentBookBinding
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import com.example.loginapp.model.*

class FragmentBook : Fragment() {
    private var _binding: FragmentBookBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "null"
        }

    private val args by navArgs<FragmentBookArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentBookBinding.inflate(inflater, container, false)
            .also {
                _binding = it
            }.root
    }

    private val dataBase: PersonBookDao by lazy {
        requireContext()
            .appDatabase
            .personBookDao()
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

    private fun requestPermission(permission: String) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {

            }

            shouldShowRequestPermissionRationale(permission) -> {
                showCustomDialog()
            }


            else -> {

                permissionLauncher.launch(permission)

            }

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val counter = args.keyCounter
        binding.run {

            share.setOnClickListener{
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Hello!! I think you will like ${title.text} book :)")
                    putExtra(Intent.EXTRA_SUBJECT, "subject")
                }.also {
                    val chooserIntent =
                        Intent.createChooser(it, "message for my friend")
                    startActivity(chooserIntent)
                }
            }

            smsButton.setOnClickListener{
                //permissionLauncher.launch(PERMISSION)
                requestPermission(PERMISSION)
            }


            title.text = counter.title
            image.setImageResource(counter.image)

            val name = title.text
            val id = FragmentLogin.ID
            if(dataBase.getUserBooks(id, name.toString()).size == 1) {
                save.setColorFilter(resources.getColor(R.color.purple_200))
            }
            else  {
                save.setColorFilter(resources.getColor(R.color.black))
                Log.d("зашли в else", dataBase.getUserBooks(id, name.toString()).toString())
            }

            save.setOnClickListener {

                try {

                    val book = PersonBook(0, id, name.toString())
                    var books = arrayOf(book)
                    Log.d("USER_ID", dataBase.getUserBooks(id, name.toString()).toString())



                    /*if(dataBase.getUserBooks(id, name.toString()).size == 1) {
                        save.setColorFilter(resources.getColor(R.color.purple_200))
                    }
                    else  {
                        save.setColorFilter(resources.getColor(R.color.black))
                        Log.d("зашли в else1", dataBase.getUserBooks(id, name.toString()).toString())

                    }*/

                    if(dataBase.getUserBooks(id, name.toString()).size == 1) {
                        save.setColorFilter(resources.getColor(R.color.black))
                        dataBase.deleteBookIDuserName(id, name.toString())
                    } else {

                        save.setColorFilter(resources.getColor(R.color.purple_200))
                        dataBase.insertBooks(*books)
                    }



                } catch (i: NullPointerException) {
                    findNavController().navigate(
                        FragmentLoginDirections.toClick6()
                    )
                }

            }


        }

    }

    companion object {
        val PERMISSION = Manifest.permission.READ_CONTACTS
    }


    private fun showCustomDialog() {
        CustomDialogContact(permissionLauncher).show(childFragmentManager, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }



}
