package com.hanmajid.android.seed.ui.auth.login

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
import com.hanmajid.android.seed.databinding.FragmentLoginBinding
import com.hanmajid.android.seed.ui.AuthViewModel

//@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()
    private val viewModel: LoginViewModel by viewModels()

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

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
                        LoginFragmentDirections.actionLoginFragmentToNavGraph()
                    )
                }
                AuthViewModel.AuthenticationState.INVALID_AUTHENTICATION -> {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
        })
        viewModel.loginState.observe(viewLifecycleOwner, Observer {
            when (it) {
                LoginViewModel.LoginState.FORM_VALID -> {
                    authViewModel.authLogin(viewModel.loginForm)
                }
                else -> {
                }
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.refuseLogin()
            authViewModel.refuseAuthentication()
            requireActivity().finish()
        }
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.authViewModel = authViewModel
        binding.viewModel = viewModel
        binding.btnRegister.setOnClickListener {
            viewModel.refuseLogin()
            authViewModel.refuseAuthentication()
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            )
        }
    }

    companion object {
        val TAG = "LoginFragment"
    }
}