package com.hanmajid.android.seed.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.hanmajid.android.seed.databinding.FragmentRegisterBinding
import com.hanmajid.android.seed.ui.AuthViewModel

class RegisterFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()
    private val viewModel: RegisterViewModel by viewModels()

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()

        authViewModel.authenticationState.observe(viewLifecycleOwner, Observer {
            when (it) {
                AuthViewModel.AuthenticationState.AUTHENTICATED -> {
                    Toast.makeText(context, "Welcome, user", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(
                        RegisterFragmentDirections.actionRegisterFragmentToNavGraph()
                    )
                }
                AuthViewModel.AuthenticationState.INVALID_AUTHENTICATION -> {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
        })
        viewModel.registerState.observe(viewLifecycleOwner, Observer {
            when (it) {
                RegisterViewModel.RegisterState.FORM_VALID -> {
                    authViewModel.authRegister(viewModel.registerForm)
                }
                else -> {
                }
            }
        })
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.refuseRegister()
            authViewModel.refuseAuthentication()
            findNavController().popBackStack()
        }
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    companion object {
        val TAG = "RegisterFragment"
    }
}