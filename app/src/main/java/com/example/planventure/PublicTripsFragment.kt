package com.example.planventure

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.planventure.databinding.FragmentMyTripsBinding
import com.example.planventure.databinding.FragmentPublicTripsBinding

class PublicTripsFragment : Fragment() {
    private lateinit var binding: FragmentPublicTripsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPublicTripsBinding.inflate(inflater, container, false)
        return binding.root
    }
}