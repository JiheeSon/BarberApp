package com.example.barberapp.view.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.barberapp.R
import com.example.barberapp.databinding.FragmentReachBinding
import com.example.barberapp.viewmodel.DashboardViewModel

class ReachFragment : Fragment() {
    private lateinit var binding: FragmentReachBinding
    private lateinit var viewModel: DashboardViewModel
    private lateinit var arr: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReachBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[DashboardViewModel::class.java]
        viewModel.getContacts()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        setUpEvents()
    }

    private fun setUpView() {
        viewModel.contactsLiveData.observe(requireActivity()) {
            arr = it[3].contactData.split("###")
            binding.apply {
                textPerson.text = it[0].contactData
                textTime.text = it[1].contactData
                textNumber.text = it[2].contactData
                textAddress.text = arr[2]
                textFacebook.text = it[4].contactData
                textEmail.text = it[5].contactData
            }
        }
    }

    private fun setUpEvents() {
        binding.apply {
            btnCall.setOnClickListener {
                val uri: Uri = Uri.parse("tel:" + textNumber.text)
                val intent = Intent(Intent.ACTION_DIAL, uri)
                requireActivity().startActivity(intent)
            }

            btnMessage.setOnClickListener {
                val uri: Uri = Uri.parse("smsto:" + textNumber.text)
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                requireActivity().startActivity(intent)
            }

            btnLocation.setOnClickListener {
                val stringBuilder = StringBuilder()
                stringBuilder.append("geo:")
                stringBuilder.append(arr[0].toDouble())
                stringBuilder.append(",")
                stringBuilder.append(arr[1].toDouble())
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(stringBuilder.toString()))
                requireActivity().startActivity(intent)
            }

            btnEmail.setOnClickListener {
                val intent = Intent()
                intent.action = "android.intent.action.SEND"
                intent.putExtra(Intent.EXTRA_EMAIL, textEmail.text)
                intent.type = "text/plain"
                requireActivity().startActivity(intent)
            }

            btnLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(textFacebook.text as String?))
                requireActivity().startActivity(intent)
            }
        }
    }
}