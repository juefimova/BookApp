package com.example.loginapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.loginapp.databinding.LoginFragmentBinding
import com.example.loginapp.model.RoomBookDao
import com.example.loginapp.model.RoomUserDao


class FragmentLogin : Fragment() {
    private var _binding: LoginFragmentBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "null"
        }

    private val dataBase2: RoomBookDao by lazy {
        requireContext()
            .appDatabase
            .bookDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LoginFragmentBinding.inflate(inflater, container, false)
            .also {
                _binding = it
            }.root
    }

    private val dataBase: RoomUserDao by lazy {
        requireContext()
            .appDatabase
            .userDao()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



            binding.run {
                signup.setOnClickListener {
                    findNavController().navigate(
                        FragmentLoginDirections.toClick4()
                    )
                }


                log.setOnClickListener {
                    try {
                        val email = email.text.toString()
                        val password = password.text.toString()
                        val user = dataBase.findByName(email, password)

                        ID = user.id

                        if (email != user.email) {
                            findNavController().navigate(
                                FragmentLoginDirections.toClick6()
                            )
                        } else if (email == user.email && password == user.password) {
                            findNavController().navigate(
                                FragmentLoginDirections.toClick8()
                            )

                        }
                    } catch (i: NullPointerException) {
                        findNavController().navigate(
                            FragmentLoginDirections.toClick6()
                        )
                    }

                }



                forgot.setOnClickListener {
                    findNavController().navigate(
                        FragmentLoginDirections.toClick5()

                    )
                }
            }

    }

    companion object {
        var ID: Long = 0
    }


        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null

        }
    }
