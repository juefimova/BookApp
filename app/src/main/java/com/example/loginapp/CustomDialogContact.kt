package com.example.loginapp

import android.Manifest
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.fragment.app.DialogFragment
import com.example.loginapp.databinding.ContactDialogBinding


class CustomDialogContact(private val permissionLauncher: ActivityResultLauncher<String>) : DialogFragment()  {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val textStyle = buildSpannedString {
            inSpans(
                ForegroundColorSpan(Color.rgb(246, 243, 12)),
                BackgroundColorSpan(Color.rgb(176, 23, 175))
            ) {

            }
        }

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return ContactDialogBinding.inflate(inflater, container, false)
            .also {
                it.title.run{
                    append(textStyle)
                }
                it.title.append("Access to contacts is required")
                it.message.run {
                    append(textStyle)
                }
                it.message.append("Please allow access to your contacts")
                it.NO.run{
                    append(textStyle)
                }
                it.NO.setOnClickListener{
                    dismiss()
                }
                it.OK.run {
                    append(textStyle)
                }
                it.OK.setOnClickListener{
                    dismiss()
                    permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                }
            }
            .root
    }


}