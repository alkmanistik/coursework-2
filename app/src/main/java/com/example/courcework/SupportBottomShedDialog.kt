package com.example.courcework

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.courcework.databinding.FragmentDefaultProductSettingBinding
import com.example.courcework.databinding.FragmentSupportBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SupportBottomShedDialog : BottomSheetDialogFragment() {
    private var _binding: FragmentSupportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSupportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonCall.setOnClickListener{
            val uri = "tel:" + posted_by.trim()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse(uri))
            startActivity(intent)
        }
        binding.buttonWriteEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.setData(Uri.parse("mailto:"))
            intent.putExtra(Intent.EXTRA_EMAIL, addresses);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            startActivity(Intent.createChooser(intent, "Send Email"));
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}