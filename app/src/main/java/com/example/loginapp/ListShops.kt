package com.example.loginapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginapp.databinding.ListShopsBinding
import com.example.loginapp.model.*

class ListShops : Fragment() {

    private var _binding: ListShopsBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "null"
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ListShopsBinding.inflate(inflater, container, false)
            .also {
                _binding = it
            }.root
    }

    private var adapter: NotesAdapter2? = null

    private val dataBase: RoomPhoneDao by lazy {
        requireContext()
            .appDatabase
            .roomPhoneDao()
    }



    val phone1 = RoomPhone(1, "Ленина 8", "+375256589834")
    val phone2 = RoomPhone(2, "Держинского 23", "+375291212344")
    val phone3 = RoomPhone(3, "Независимости 4", "+37544567890")
    val phone4 = RoomPhone(4, "Октябрьская 12", "+375298080800")
    val phone5 = RoomPhone(5, "Скрипникова 44", "+375446767670")

    val phones = arrayOf(phone1, phone2, phone3, phone4, phone5)

    val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.setFragmentResultListener(
            "result",
            viewLifecycleOwner
        ) { _, bundle ->



        }



        binding.run {


            recyclerView.layoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false
            )




            adapter = NotesAdapter2(phones.toMutableList()) {
                val uri = Uri.parse("tel:${Uri.encode(it.phone)}")
                val intent = Intent(Intent.ACTION_DIAL, uri)
                resultLauncher.launch(intent)
            }



            recyclerView.adapter = adapter

            val book = dataBase.getAll()
            if(book.size == 0) {
                dataBase.insertPhones(phones)
            }

        }

    }


    companion object {
        var name = "name"
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}