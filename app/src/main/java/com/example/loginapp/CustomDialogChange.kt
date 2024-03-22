package com.example.loginapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.example.loginapp.databinding.DialogChangeBinding
import com.example.loginapp.databinding.FragmentDialogCreatedBinding
import com.example.loginapp.model.PersonBookDao
import com.example.loginapp.model.RoomBookDao
import com.example.loginapp.model.RoomUserDao

class CustomDialogChange : DialogFragment()  {
    private val args by navArgs<CustomDialogChangeArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val key = requireArguments().getString("key")
        Log.d(key, "mes")
        val position = requireArguments().getInt(POSITION)
        Log.d(position.toString(), "pos")
        val textStyle = buildSpannedString {
            inSpans(
                ForegroundColorSpan(Color.rgb(246, 243, 12)),
                BackgroundColorSpan(Color.rgb(176, 23, 175))
            ) {

            }
        }




        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return DialogChangeBinding.inflate(inflater, container, false)

            .also {
                val counter = args.user

                it.email.setText(counter.email)
                it.password.setText(counter.password)
                it.change.run {
                    append(textStyle)
                }

                it.OK.setOnClickListener{ view ->

                    parentFragmentManager.setFragmentResult(
                        "result",
                        bundleOf("email" to it.email.text.toString(), "password" to it.password.text.toString())
                    )
                    Log.d("OK", "попали на кнопку ОК")

                    //parentfragmentmanager setfragmentresult(email, password)
                    dismiss()
                }
            }
            .root
    }



    companion object{
        val KEY = "key"
        val POSITION = "position"
        fun getIstance(key: String, position: Int) = CustomDialogCreated().apply {
            Log.d(key, "key")
            arguments = bundleOf(KEY to key, POSITION to position)
        }
    }

}