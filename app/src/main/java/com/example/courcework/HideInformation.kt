package com.example.courcework

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.courcework.databinding.FragmentHideInformationBinding
import com.example.courcework.databinding.FragmentSupportBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HideInformation(val phone: String, val address: String): BottomSheetDialogFragment() {
    private var _binding: FragmentHideInformationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHideInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (phone != "")
            binding.textPhone.text = phone
        if (address != "")
            binding.textAddress.text = address


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}