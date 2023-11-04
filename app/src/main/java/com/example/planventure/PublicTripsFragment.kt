package com.example.planventure

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.planventure.databinding.FragmentMyTripsBinding
import com.example.planventure.databinding.FragmentPublicTripsBinding

/**
 * A fragment for displaying a list of public trips. Not yet implemented as it is an application running locally
 *
 * This fragment is responsible for presenting a list of public trips. It inflates the layout
 * and provides the necessary UI components to display the list of public trips.
 *
 * @property binding The binding object for the fragment's layout.
 */
class PublicTripsFragment : Fragment() {
    private lateinit var binding: FragmentPublicTripsBinding

    /**
     * Initializes the fragment's view and sets up UI components.
     *
     * @param inflater The layout inflater to inflate the fragment's layout.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState The saved instance state, if any.
     * @return The root view of the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPublicTripsBinding.inflate(inflater, container, false)
        return binding.root
    }
}