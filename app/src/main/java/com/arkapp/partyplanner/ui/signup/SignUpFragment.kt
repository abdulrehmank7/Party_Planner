package com.arkapp.partyplanner.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.UserLogin
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.databinding.FragmentSignupBinding
import com.arkapp.partyplanner.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding

    private val prefRepository by lazy { PrefRepository(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSignupBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpBtn.setOnClickListener {
            if (isDoubleClicked(1000)) return@setOnClickListener
            if (binding.signUpBtn.text == getString(R.string.sign_up)) {
                binding.signUpBtn.text = getString(R.string.login)
                binding.signUpDesc.text = getString(R.string.already_have_account)

                binding.loginCard.hide()
                binding.signUpCard.show()

                binding.userNameEt.text?.clear()
                binding.passwordEt.text?.clear()

                binding.userName.error = null
                binding.password.error = null

            } else {
                binding.signUpBtn.text = getString(R.string.sign_up)
                binding.signUpDesc.text = getString(R.string.not_have_an_account)

                binding.signUpCard.hide()
                binding.loginCard.show()

                binding.signUpUserNameEt.text?.clear()
                binding.signUpPasswordEt.text?.clear()
                binding.signUpConfirmPasswordEt.text?.clear()

                binding.signUpUserName.error = null
                binding.signUpPassword.error = null
                binding.signUpConfirmPassword.error = null
            }
        }

        binding.loginBtn.setOnClickListener {
            if (isDoubleClicked(1000)) return@setOnClickListener
            binding.loginProgress.show()
            requireActivity().window.disableTouch()
            onLoginClicked()
        }

        binding.insideSignUpBtn.setOnClickListener {
            if (isDoubleClicked(1000)) return@setOnClickListener
            binding.signupProgress.show()
            requireActivity().window.disableTouch()
            onSignUpClicked()
        }

        removeErrorOnChange()
    }

    private fun onSignUpClicked() {
        if (binding.signUpUserNameEt.text!!.isEmpty()) {
            binding.signUpUserName.error = "Username required!"

            binding.signupProgress.hide()
            requireActivity().window.enableTouch()
            return
        }

        if (binding.signUpUserNameEt.text!!.length < 3) {
            binding.signUpUserName.error = "Invalid Username!"

            binding.signupProgress.hide()
            requireActivity().window.enableTouch()
            return
        }

        if (binding.signUpPasswordEt.text!!.isEmpty()) {
            binding.signUpPassword.error = "Password required!"

            binding.signupProgress.hide()
            requireActivity().window.enableTouch()
            return
        }

        if (binding.signUpPasswordEt.text!!.length < 3) {
            binding.signUpPassword.error = "Invalid Password! Length should be more than 4"

            binding.signupProgress.hide()
            requireActivity().window.enableTouch()
            return
        }

        if (binding.signUpConfirmPasswordEt.text!!.isEmpty()) {
            binding.signUpConfirmPassword.error = "Password required!"

            binding.signupProgress.hide()
            requireActivity().window.enableTouch()
            return
        }

        if (binding.signUpConfirmPasswordEt.text.toString() != binding.signUpPasswordEt.text.toString()) {
            binding.signUpConfirmPassword.error = "Password incorrect!"

            binding.signupProgress.hide()
            requireActivity().window.enableTouch()
            return
        }

        checkIfUserNameExist()
    }

    private fun onLoginClicked() {
        if (binding.userNameEt.text!!.isEmpty()) {
            binding.userName.error = "Username required!"

            binding.loginProgress.hide()
            requireActivity().window.enableTouch()
            return
        }

        if (binding.passwordEt.text!!.isEmpty()) {
            binding.password.error = "Password required!"

            binding.loginProgress.hide()
            requireActivity().window.enableTouch()
            return
        }

        checkCredentials()
    }

    private fun checkCredentials() {

        lifecycleScope.launch(Dispatchers.Main) {
            insertCaterersData()
            insertVenueData()

            val userLoginDao = AppDatabase.getDatabase(requireContext()).userLoginDao()

            val userData = userLoginDao.getLoggedInUser(
                binding.userNameEt.text.toString(),
                binding.passwordEt.text.toString()
            )

            ENTERED_USER_NAME = binding.userNameEt.text.toString()

            if (userData.isEmpty()) {
                requireContext().toast("Login failed!")
                binding.userName.error = "Check username and password!"
                binding.loginProgress.hide()
                requireActivity().window.enableTouch()
            } else {
                delay(1000)
                binding.loginProgress.hide()
                requireActivity().window.enableTouch()
                onLoginSuccess()
            }
        }

    }

    private fun checkIfUserNameExist() {

        lifecycleScope.launch(Dispatchers.Main) {

            val userLoginDao = AppDatabase.getDatabase(requireContext()).userLoginDao()

            val userData = userLoginDao.checkLoggedInUser(
                binding.signUpUserNameEt.text.toString()
            )

            if (userData.isNotEmpty()) {
                requireContext().toast("Signup failed!")
                binding.signUpUserName.error = "Username already exits!"

                binding.signupProgress.hide()
                requireActivity().window.enableTouch()
            } else
                storeCredentials()
        }
    }

    private fun onLoginSuccess() {
        requireContext().toast("Login success")
        prefRepository.setLoggedIn(true)
        findNavController().navigate(R.id.action_signupFragment_to_splashFragment)
    }

    private fun storeCredentials() {

        lifecycleScope.launch(Dispatchers.Main) {
            insertCaterersData()
            insertVenueData()

            val userLoginDao = AppDatabase.getDatabase(requireContext()).userLoginDao()

            userLoginDao.insert(
                UserLogin(
                    null,
                    binding.signUpUserNameEt.text.toString(),
                    binding.signUpPasswordEt.text.toString()
                )
            )

            ENTERED_USER_NAME = binding.signUpUserNameEt.text.toString()

            delay(1000)
            binding.signupProgress.hide()
            requireActivity().window.enableTouch()

            onLoginSuccess()
        }

    }

    private suspend fun insertVenueData() {
        val venueDao = AppDatabase.getDatabase(requireContext()).venueDao()

        for (venue in getVenueList()) {
            venueDao.insert(venue)
        }
    }

    private suspend fun insertCaterersData() {
        val catererDao = AppDatabase.getDatabase(requireContext()).catererDao()

        for (venue in getCatererList()) {
            catererDao.insert(venue)
        }
    }

    private fun removeErrorOnChange() {
        binding.userNameEt.doAfterTextChanged { binding.userName.error = null }
        binding.passwordEt.doAfterTextChanged { binding.password.error = null }
        binding.signUpUserNameEt.doAfterTextChanged { binding.signUpUserName.error = null }
        binding.signUpPasswordEt.doAfterTextChanged { binding.signUpPassword.error = null }
        binding.signUpConfirmPasswordEt.doAfterTextChanged { binding.signUpConfirmPassword.error = null }
    }

}
