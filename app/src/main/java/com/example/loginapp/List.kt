package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginapp.databinding.FragmentListBinding
import com.example.loginapp.model.RoomBook
import com.example.loginapp.model.RoomBookDao
import com.example.loginapp.model.RoomUserDao
import java.util.Locale.filter

class List : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "null"
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentListBinding.inflate(inflater, container, false)
            .also {
                _binding = it
            }.root
    }

    private var adapter: NotesAdapter? = null

    private val dataBase: RoomBookDao by lazy {
        requireContext()
            .appDatabase
            .bookDao()
    }

    private val dataBase2: RoomUserDao by lazy {
        requireContext()
            .appDatabase
            .userDao()
    }


    val book1 = RoomBook(1, "title1", "", R.drawable.book1)
    val book2 = RoomBook(2, "title2", "", R.drawable.book2)
    val book3 = RoomBook(3, "title3", "", R.drawable.book3)
    val book4 = RoomBook(4, "title4", "", R.drawable.book4)
    val book5 = RoomBook(5, "title5", "", R.drawable.book5)

    val books = arrayOf(book1, book2, book3, book4, book5)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //parentfragmnetmanager listener(email, password) обратиться к базе данных, вызвать метод update

        parentFragmentManager.setFragmentResultListener(
            "result",
            viewLifecycleOwner
        ) { _, bundle ->
            Log.d(bundle.getString("email"),"email")
            Log.d(bundle.getString("password"),"password")

            val email = bundle.getString("email")
            val password = bundle.getString("password")

            dataBase2.update(email, password, FragmentLogin.ID)

        }

        Log.d("FragmentLogin.ID", FragmentLogin.ID.toString())
        binding.run {

            toolbar.setOnMenuItemClickListener{
                when(it.itemId) {
                    R.id.action_search -> {
                        (it.actionView as SearchView)
                            .apply {
                                setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                                    override fun onQueryTextSubmit(query: String?): Boolean {
                                        Log.d("query", "QueryTextSubmit: $query")
                                        return true
                                    }

                                    override fun onQueryTextChange(newText: String?): Boolean {
                                        newText?.let { filter(newText)}?: "error"
                                        Log.d("newText", "QueryTextChange: $newText")
                                        return false
                                    }

                                })
                            }
                        true
                    }
                    R.id.change -> {
                        Log.d("change", "нажали на change")
                        findNavController().navigate(
                            ListDirections.toChange(dataBase2.loadAllByID(FragmentLogin.ID))
                        )
                        true
                    }

                    R.id.send -> {
                        Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "Hello, my friend")
                            putExtra(Intent.EXTRA_SUBJECT, "subject")
                        }.also{
                            val chooserIntent =
                                Intent.createChooser(it, "message to my friend")
                            startActivity(chooserIntent)
                        }
                        true
                    }


                    else -> false
                }
            }

            /*val book1 = RoomBook(1, "title1", "", R.drawable.book1)
            val book2 = RoomBook(2, "title2", "", R.drawable.book2)
            val book3 = RoomBook(3, "title3", "", R.drawable.book3)
            val book4 = RoomBook(4, "title4", "", R.drawable.book4)
            val book5 = RoomBook(5, "title5", "", R.drawable.book5)

            val books = arrayOf(book1, book2, book3, book4, book5)*/


            recyclerView.layoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.VERTICAL, false
            )


            adapter = NotesAdapter(books.toMutableList()) {
                findNavController().navigate(
                    ListDirections.toFragment(it)
                )
            }

            recyclerView.adapter = adapter

            val book = dataBase.getAll()
            if(book.size == 0) {
                dataBase.insertBooks(*books)
            }

        }

    }


    companion object {
        var name = "name"
    }


    private fun filter(text: String) {
        val filteredlist: ArrayList<RoomBook> = ArrayList()
        for(item in books) {
            if(item.title.lowercase().contains(text.lowercase())) {
                filteredlist.add(item)
            }
        }
        if(filteredlist.isEmpty()) {
            Toast.makeText(context, "No Date found..", Toast.LENGTH_SHORT).show()
            adapter?.filterList(filteredlist)
        } else {
            adapter?.filterList(filteredlist)
        }
    }



override fun onDestroyView() {
    super.onDestroyView()
    _binding = null

}

}