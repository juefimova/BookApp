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
import androidx.core.view.isVisible
import com.example.loginapp.model.*
import kotlin.math.roundToInt

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

    private val dataBase2: RoomUserFriendsDao by lazy {
        requireContext()
            .appDatabase
            .userFriendsDao()
    }

    private val dataBase3: RoomBookDao by lazy {
        requireContext()
            .appDatabase
            .bookDao()
    }

    private val database4: RoomUserRatingBookDao by lazy {
        requireContext()
            .appDatabase
            .roomUserRatingBookDao()
    }



    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissionGranted ->
        if (permissionGranted)
            resultLauncherGetContacts.launch(intent)
    }


    val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)

    @SuppressLint("Range")
    val resultLauncherGetContacts = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            var uri: Uri = it.data?.data ?: return@registerForActivityResult
            Log.d("uri->", uri.toString() + "\n" + it.data?.toString())
            val cursor: Cursor = activity?.contentResolver?.query(
                uri, null, null, null, null
            ) ?: return@registerForActivityResult
            if (cursor.moveToFirst()) {
                val id: String =
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val hasPhone: String =
                    cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                if (hasPhone.equals("1")) {
                    Log.d(
                        "Phone.CONTENT_URI -> ",
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI.toString()
                    )
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


                    val friend = RoomUserFriends(0, FragmentLogin.ID.toString(), name, number)
                    if(friend != dataBase2.findByName(name, number)) {
                        dataBase2.insertFriend(friend)
                    }

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
                permissionLauncher.launch(permission)

            }

            shouldShowRequestPermissionRationale(permission) -> {
                showCustomDialog()
            }


            else -> {

                permissionLauncher.launch(permission)

            }

        }

    }
    var numClicks = 0;
    var newRating = 0f;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val counter = args.keyCounter
        binding.run {

            share.setOnClickListener {
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Hello!! I think you will like ${title.text} book :)"
                    )
                    putExtra(Intent.EXTRA_SUBJECT, "subject")
                }.also {
                    val chooserIntent =
                        Intent.createChooser(it, "message for my friend")
                    startActivity(chooserIntent)
                }
            }

            smsButton.setOnClickListener {
                //permissionLauncher.launch(PERMISSION)
                requestPermission(PERMISSION)
            }

            buy.setOnClickListener{
                findNavController().navigate(
                    FragmentBookDirections.toShops()
                )
            }


            rating2.setOnRatingBarChangeListener{ ratingBar, rating, bool ->
                Log.d("ratingBar", ratingBar.toString())
                Log.d("rating", rating.toString())
                Log.d("bool", bool.toString())



                val book = RoomUserRatingBook(FragmentLogin.ID, title.text.toString(), rating)
                database4.insertBook(book)
                numClicks = dataBase3.selectUsersClicked(FragmentLogin.ID, title.text.toString()) + 1;
                val ratingFromDB = dataBase3.selectRating(FragmentLogin.ID, title.text.toString())
                val newRaiting = (ratingFromDB * (numClicks-1) + rating) / numClicks

                dataBase3.update(newRaiting, numClicks, FragmentLogin.ID)


                //parentFragmentManager.setFragmentResultListener()

                rating1.isVisible = false
                newRating = String.format("%.1f", ((newRaiting*10.0)/ 5f)).toInt() * 5 / 10f
                rate.text = newRating.toString()
                rating2.rating = newRaiting
                /*3,3*10=33

                33/5f=6.6

                6.6 округли до целого ближайшего - станет 7

                7*5=35

                35/10f=3,5*/



                clicks.text = numClicks.toString()

                rating2.isVisible = false

                findNavController().navigate(
                    FragmentBookDirections.toVote()
                )


                title.text = counter.title
                image.setImageResource(counter.image)
                rating1.rating = counter.rating


            }


            title.text = counter.title
            image.setImageResource(counter.image)
            rating1.rating = counter.rating
            rate.text = String.format("%.1f", dataBase3.selectRating(FragmentLogin.ID, title.text.toString()))
            numClicks = dataBase3.selectUsersClicked(FragmentLogin.ID, title.text.toString())

            val name = title.text
            val id = FragmentLogin.ID
            if (dataBase.getUserBooks(id, name.toString()).size == 1) {
                save.setColorFilter(resources.getColor(R.color.purple_200))
            } else {
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

                    if (dataBase.getUserBooks(id, name.toString()).size == 1) {
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
