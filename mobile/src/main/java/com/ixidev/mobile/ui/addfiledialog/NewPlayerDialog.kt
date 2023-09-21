package com.ixidev.mobile.ui.addfiledialog

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fxn.stash.Stash
import androidx.core.net.toUri
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ixidev.mobile.databinding.NewVersionDialogBinding

class NewPlayerDialog  : BottomSheetDialogFragment(){
    private lateinit var binding: NewVersionDialogBinding
    var isClicked:String = "isClicked"
    var app_link:String = "com.google.android.apps.messaging"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NewVersionDialogBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.downloadNow.setOnClickListener{
            openPlayStore(requireContext(), app_link)
        }
    }

    fun openPlayStore(context: Context, packageName: String) {
        Stash.put(isClicked, true)
        dismiss()
        /*val intent = Intent(Intent.ACTION_VIEW)
        intent.data = packageName.toUri()
        context.startActivity(intent)*/
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
        }
    }

}