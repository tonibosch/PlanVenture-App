package com.example.planventure

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.planventure.databinding.FragmentConfigurationBinding

/**
* A fragment for configuring application settings and preferences. Not yet implemented as it is an application running locally.
*
* This fragment serves as the user interface for configuring various application settings
* and preferences. It inflates the layout for the configuration screen and provides
* UI components for users to adjust settings.
*
* @property binding The binding object for the fragment's layout.
*/
class ConfigurationFragment : Fragment() {
    private lateinit var binding: FragmentConfigurationBinding

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
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        return binding.root
    }
}