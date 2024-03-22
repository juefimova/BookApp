package com.example.loginapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.loginapp.databinding.FirstFragmentBinding

class FragmentFirst : Fragment() {
    private var _binding: FirstFragmentBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "null"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FirstFragmentBinding.inflate(inflater, container, false)
            .also {
                _binding = it
            }.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {

            create.setOnClickListener{
                findNavController().navigate(
                    FragmentFirstDirections.toClick()
                )
            }

            sign.setOnClickListener{
                findNavController().navigate(
                    FragmentFirstDirections.toClick2()
                )
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}