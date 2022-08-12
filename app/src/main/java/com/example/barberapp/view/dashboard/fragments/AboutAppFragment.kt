package com.example.barberapp.view.dashboard.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.barberapp.R
import com.example.barberapp.databinding.FragmentAboutAppBinding

class AboutAppFragment : Fragment() {
    private lateinit var binding: FragmentAboutAppBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAboutAppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpEvents()
    }

    private fun setUpEvents() {
        binding.phone.setOnClickListener {
            val uri: Uri = Uri.parse("tel:3322999766")
            val intent = Intent(Intent.ACTION_DIAL, uri)
            requireActivity().startActivity(intent)
        }

        binding.email.setOnClickListener {
            val intent = Intent()
            intent.action = "android.intent.action.SEND"
            intent.putExtra(Intent.EXTRA_EMAIL, "jihee.son914@gmail.com")
            intent.type = "text/plain"
            requireActivity().startActivity(intent)
        }

        binding.link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/JiheeSon"))
            requireActivity().startActivity(intent)
        }
    }

}