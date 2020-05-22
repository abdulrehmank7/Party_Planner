package com.arkapp.partyplanner.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            onLoginClicked()
        }

        binding.insideSignUpBtn.setOnClickListener {
            onSignUpClicked()
        }

        removeErrorOnChange()
    }

    private fun onSignUpClicked() {
        if (binding.signUpUserNameEt.text!!.isEmpty()) {
            binding.signUpUserName.error = "Username required!"
            return
        }

        if (binding.signUpUserNameEt.text!!.length < 3) {
            binding.signUpUserName.error = "Invalid Username!"
            return
        }

        if (binding.signUpPasswordEt.text!!.isEmpty()) {
            binding.signUpPassword.error = "Password required!"
            return
        }

        if (binding.signUpPasswordEt.text!!.length < 3) {
            binding.signUpPassword.error = "Invalid Password! Length should be more than 4"
            return
        }

        if (binding.signUpConfirmPasswordEt.text!!.isEmpty()) {
            binding.signUpConfirmPassword.error = "Password required!"
            return
        }

        if (binding.signUpConfirmPasswordEt.text.toString() != binding.signUpPasswordEt.text.toString()) {
            binding.signUpConfirmPassword.error = "Password incorrect!"
            return
        }

        checkIfUserNameExist()
    }

    private fun onLoginClicked() {
        if (binding.userNameEt.text!!.isEmpty()) {
            binding.userName.error = "Username required!"
            return
        }

        if (binding.passwordEt.text!!.isEmpty()) {
            binding.password.error = "Password required!"
            return
        }

        checkCredentials()
    }

    private fun checkCredentials() {

        lifecycleScope.launch(Dispatchers.Main) {
            insertFoodData()
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
            } else
                onLoginSuccess()
        }

    }

    private fun checkIfUserNameExist() {

        lifecycleScope.launch(Dispatchers.Main) {

            val userLoginDao = AppDatabase.getDatabase(requireContext()).userLoginDao()

            val userData = userLoginDao.checkLoggedInUser(
                binding.userNameEt.text.toString()
            )

            if (userData.isNotEmpty()) {
                requireContext().toast("Signup failed!")
                binding.userName.error = "Username already exits!"
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
            insertFoodData()
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

            onLoginSuccess()
        }

    }

    private suspend fun insertFoodData() {
        val foodDao = AppDatabase.getDatabase(requireContext()).foodDao()

        for (food in getFoodList()) {
            foodDao.insert(food)
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
        binding.userNameEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.userName.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.passwordEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.password.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.signUpUserNameEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.signUpUserName.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.signUpPasswordEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.signUpPassword.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.signUpConfirmPasswordEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.signUpConfirmPassword.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

}
