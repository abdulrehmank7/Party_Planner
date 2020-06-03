package com.arkapp.partyplanner.ui.signup

import android.annotation.SuppressLint
import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.UserLogin
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.databinding.FragmentSignupBinding
import com.arkapp.partyplanner.utils.*

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

        //Initializing the button listeners for sign up and sign in
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

    //Validating all data before signup
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

    //Validating all data before login
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

    //Used to check the credential of user in the SQL table
    private fun checkCredentials() {
        insertCaterersData()
        insertVenueData()

        val taskListener = object : AddUserListener {
            override fun onTaskEnded() {}

            override fun onTaskEnded(userData: MutableList<UserLogin>?) {
                ENTERED_USER_NAME = binding.userNameEt.text.toString()

                if (userData.isNullOrEmpty()) {
                    requireContext().toast("Login failed!")
                    binding.userName.error = "Check username and password!"
                    binding.loginProgress.hide()
                    requireActivity().window.enableTouch()
                } else {
                    binding.loginProgress.hide()
                    requireActivity().window.enableTouch()
                    onLoginSuccess()
                }
            }
        }
        GetLoggedInUserAsyncTask(requireActivity(),
                                 binding.userNameEt.text.toString(),
                                 binding.passwordEt.text.toString(),
                                 taskListener)
            .execute()

    }

    //Check if the user is exist before the signup in SQL tables
    private fun checkIfUserNameExist() {

        val taskListener = object : AddUserListener {
            override fun onTaskEnded() {}
            override fun onTaskEnded(data: MutableList<UserLogin>?) {
                if (!data.isNullOrEmpty()) {
                    requireContext().toast("Signup failed!")
                    binding.signUpUserName.error = "Username already exits!"

                    binding.signupProgress.hide()
                    requireActivity().window.enableTouch()
                } else
                    storeCredentials()
            }
        }

        CheckLoggedInUserAsyncTask(
            requireActivity(),
            binding.signUpUserNameEt.text.toString(),
            taskListener)
            .execute()
    }

    //Opening the app on successfull signup or login
    private fun onLoginSuccess() {
        requireContext().toast("Login success")
        prefRepository.setLoggedIn(true)
        findNavController().navigate(R.id.action_signupFragment_to_splashFragment)
    }

    //Store the signup credential in the SQL table
    private fun storeCredentials() {
        insertCaterersData()
        insertVenueData()

        val taskListener = object : AddUserListener {
            override fun onTaskEnded() {
                ENTERED_USER_NAME = binding.signUpUserNameEt.text.toString()
                binding.signupProgress.hide()
                requireActivity().window.enableTouch()

                onLoginSuccess()
            }

            override fun onTaskEnded(data: MutableList<UserLogin>?) {}
        }

        AddUserAsyncTask(requireActivity(),
                         binding.signUpUserNameEt.text.toString(),
                         binding.signUpPasswordEt.text.toString(),
                         taskListener).execute()
    }

    private class AddUserAsyncTask(private val context: Activity,
                                   private val username: String,
                                   private val password: String,
                                   private val taskListener: AddUserListener) : AsyncTask<Void, Void, Void?>() {

        override fun doInBackground(vararg params: Void?): Void? {
            val userLoginDao = AppDatabase.Companion().getDatabase(context).userLoginDao()
            userLoginDao.insert(UserLogin(null, username, password))
            Thread.sleep(1000)
            return null
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(unfinisedSummary: Void?) {
            taskListener.onTaskEnded()
        }
    }

    private class GetLoggedInUserAsyncTask(private val context: Activity,
                                           private val username: String,
                                           private val password: String,
                                           private val taskListener: AddUserListener) : AsyncTask<Void, Void, MutableList<UserLogin>?>() {

        override fun doInBackground(vararg params: Void?): MutableList<UserLogin>? {
            val userLoginDao = AppDatabase.Companion().getDatabase(context).userLoginDao()
            val data = userLoginDao.getLoggedInUser(username, password)
            Thread.sleep(1000)
            return data
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(data: MutableList<UserLogin>?) {
            taskListener.onTaskEnded(data)
        }
    }

    private class CheckLoggedInUserAsyncTask(private val context: Activity,
                                             private val username: String,
                                             private val taskListener: AddUserListener) : AsyncTask<Void, Void, MutableList<UserLogin>?>() {

        override fun doInBackground(vararg params: Void?): MutableList<UserLogin>? {
            val userLoginDao = AppDatabase.Companion().getDatabase(context).userLoginDao()
            return userLoginDao.checkLoggedInUser(username)
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(data: MutableList<UserLogin>?) {
            taskListener.onTaskEnded(data)
        }
    }

    //Insert the all the venue in venue table
    private fun insertVenueData() {
        val runnable = Runnable {
            val venueDao = AppDatabase.Companion().getDatabase(requireContext()).venueDao()

            for (venue in getVenueList()) {
                venueDao.insert(venue)
            }
        }
        Thread(runnable).start()
    }

    //Insert the all the caterer in caterer table
    private fun insertCaterersData() {
        val runnable = Runnable {
            val catererDao = AppDatabase.Companion().getDatabase(requireContext()).catererDao()

            for (caterer in getCatererList()) {
                catererDao.insert(caterer)
            }
        }
        Thread(runnable).start()
    }

    //Remove error on changing the values.
    private fun removeErrorOnChange() {
        binding.userNameEt.doAfterTextChanged { binding.userName.error = null }
        binding.passwordEt.doAfterTextChanged { binding.password.error = null }
        binding.signUpUserNameEt.doAfterTextChanged { binding.signUpUserName.error = null }
        binding.signUpPasswordEt.doAfterTextChanged { binding.signUpPassword.error = null }
        binding.signUpConfirmPasswordEt.doAfterTextChanged { binding.signUpConfirmPassword.error = null }
    }

}
