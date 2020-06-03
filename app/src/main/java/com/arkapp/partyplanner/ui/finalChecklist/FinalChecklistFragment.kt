package com.arkapp.partyplanner.ui.finalChecklist

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.CheckedItem
import com.arkapp.partyplanner.data.models.PartyDetails
import com.arkapp.partyplanner.data.models.SummaryDetails
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.databinding.FragmentFinalChecklistBinding
import com.arkapp.partyplanner.ui.finalChecklist.utils.*
import com.arkapp.partyplanner.utils.*
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList

class FinalChecklistFragment : Fragment() {

    private val prefRepository by lazy { PrefRepository(requireContext()) }

    private lateinit var details: PartyDetails
    private lateinit var binding: FragmentFinalChecklistBinding
    private val arrayListType = object : TypeToken<ArrayList<String>>() {}.type!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentFinalChecklistBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().hideKeyboard()

        //Checking from where the checklist screen is opened and setting appropriate data
        when (CURRENT_SELECTED_OPTION) {
            OPTION_CHECKLIST -> {
                //Fetching the summary table in background
                val taskListener = GetSummaryListener {
                    details = convertPartyFromSummary(it[0])
                    prefRepository.currentPartyDetails = details
                    setAllPartyData()
                }
                GetSummaryAsyncTask(requireActivity(), prefRepository, taskListener).execute()
            }
            OPTION_PAST -> {
                //Deleting the unfinished data and setting all the party data
                DeleteUnfinishedSummaryAsyncTask(requireActivity(), prefRepository).execute()
                details = prefRepository.currentPartyDetails
                setAllPartyData()
            }
            else -> {
                //Deleting the unfinished data and setting all the party data
                DeleteUnfinishedSummaryAsyncTask(requireActivity(), prefRepository).execute()
                details = prefRepository.currentPartyDetails
                setAllPartyData()
                UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute()

                //Not adding in history if screen is opened from the guest checklist
                if (!OPENED_GUEST_LIST)
                    UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute()
            }
        }

        //Handling back press in checklist screen.
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) {
                //Resetting the shared preferences party data
                prefRepository.currentPartyDetails = PartyDetails(
                    null,
                    null,
                    null,
                    null,
                    null,
                    ArrayList<String>(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    ArrayList<String>())
                this.remove()
                if (CURRENT_SELECTED_OPTION == OPTION_PAST)
                    findNavController().navigate(R.id.action_finalChecklistFragment_to_historySummaryFragment)
                else
                    findNavController().navigate(R.id.action_finalChecklistFragment_to_optionsFragment)
                true
            }
    }


    //Used to set all the party data displayed on the checklist data
    @SuppressLint("SetTextI18n")
    private fun setAllPartyData() {
        binding.partyDate.text = details.partyDate?.getFormattedDate()
        binding.destinationType.text = details.partyDestination
        binding.totalGuest.text = "${details.partyGuest} Guests"
        binding.notesEt.setText(details.extraNote)

        setCheckBox()
        setLocations()
        setBudget()
        setSelectedPartyTypes()
        setCatererDetails()
        setVenueDetails()
        setSpecialDetails()

        setViewListeners()
    }

    //Used to set the checkbox data of all items.
    private fun setCheckBox() {
        //Checking the checkbox data in the party data and setting accordingly
        details.checkedItemList.also {
            if (!it.isNullOrEmpty()) {
                for (item in it) {
                    when (item.itemName) {
                        CB_PARTY_TYPE -> binding.selectedPartyTypeCb.isChecked = item.selected
                        CB_CATERER -> binding.catererCb.isChecked = item.selected
                        CB_VENUE -> binding.venueCb.isChecked = item.selected
                        CB_DECORATOR -> binding.decorationCb.isChecked = item.selected
                        CB_MAGIC_SHOW -> binding.magicCb.isChecked = item.selected
                        CB_ALCOHOL -> binding.alcoholCb.isChecked = item.selected
                        CB_BUDGET -> binding.budgetCb.isChecked = item.selected
                    }
                }
            } else {
                //adding blank values in the checked item list
                details.checkedItemList = ArrayList()
                details.checkedItemList!!.add(CheckedItem(CB_PARTY_TYPE, false))
                details.checkedItemList!!.add(CheckedItem(CB_CATERER, false))
                details.checkedItemList!!.add(CheckedItem(CB_VENUE, false))
                details.checkedItemList!!.add(CheckedItem(CB_DECORATOR, false))
                details.checkedItemList!!.add(CheckedItem(CB_MAGIC_SHOW, false))
                details.checkedItemList!!.add(CheckedItem(CB_ALCOHOL, false))
                details.checkedItemList!!.add(CheckedItem(CB_BUDGET, false))
                prefRepository.currentPartyDetails = details
                UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute()
            }
        }
    }

    //Used to set the listener of button and edit text etc
    private fun setViewListeners() {

        //Opening the guest checklist on clicking update guest button
        binding.updateGuestBtn.setOnClickListener {
            details.guestNameList.also {
                GUEST_LIST_NAMES = it ?: ArrayList()
            }
            addEmptyGuest(details.partyGuest!!)
            findNavController().navigate(R.id.action_finalChecklistFragment_to_guestListFragment)
        }

        //Storing the notes on updating
        binding.notesEt.doAfterTextChanged {

            val details = prefRepository.currentPartyDetails
            details.extraNote = it.toString()
            prefRepository.currentPartyDetails = details

            if (CURRENT_SELECTED_OPTION == OPTION_CHECKLIST || CURRENT_SELECTED_OPTION == OPTION_CREATE)
                UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute()
            else
                UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute()
        }

        setCbListener()
        setEditBtnListener()

    }

    //Setting the edit button listeners.
    @SuppressLint("SetTextI18n")
    private fun setEditBtnListener() {

        binding.editDateBtn.setOnClickListener {

            //Checking if the button is not double clicked
            if (isDoubleClicked(1000)) return@setOnClickListener
            val currentDate = Calendar.getInstance()

            //Showing the date picker dialog
            val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                details.partyDate = selectedDate.time

                binding.partyDate.text = selectedDate.time.getFormattedDate()

                prefRepository.currentPartyDetails = details
                UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute()
            }

            val datePicker = DatePickerDialog(requireContext(),
                                              listener,
                                              currentDate.get(Calendar.YEAR),
                                              currentDate.get(Calendar.MONTH),
                                              currentDate.get(Calendar.DAY_OF_MONTH))

            //Setting minimim date as current date.
            datePicker.datePicker.minDate = currentDate.timeInMillis
            datePicker.show()
        }

        binding.editDestinationBtn.setOnClickListener {
            if (isDoubleClicked(1000)) return@setOnClickListener
            val dialog = DialogDestinationSelection(requireContext(), prefRepository)
            dialog.show()

            //Checking and updating the value selected after the dialog is closed.
            dialog.setOnDismissListener {
                binding.destinationType.text = prefRepository.currentPartyDetails.partyDestination
                UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute()
                setBudget()

                details = prefRepository.currentPartyDetails

                //On changing the party type, updating the option in the checklist screen
                if (prefRepository.currentPartyDetails.partyDestination != getString(R.string.home)) {

                    binding.include.parent.show()
                    binding.venueTitle.show()
                    binding.venueCb.show()
                    binding.editVenueBtn.show()

                    binding.locationSelected.show()
                    binding.textView22.show()
                    binding.editLocationBtn.show()

                    prefRepository.currentPartyDetails.selectedDestination.also {
                        if (it == null || it.name.isEmpty()) {
                            binding.include.venueName.text = "Click edit icon to select venue!"
                            binding.locationSelected.text = "Select location!"
                        } else {
                            setLocations()
                            setVenueDetails()
                        }

                    }
                } else {
                    setLocations()
                    setVenueDetails()
                }
            }
        }

        //Used to update the guest count of party
        binding.editGuestBtn.setOnClickListener {
            if (isDoubleClicked(1000)) return@setOnClickListener

            val dialog = DialogChangeGuest(requireContext(), prefRepository)
            dialog.show()
            dialog.setOnDismissListener {
                binding.totalGuest.text = "${prefRepository.currentPartyDetails.partyGuest} Guests"
                details = prefRepository.currentPartyDetails
                setBudget()
                UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute()
            }
        }

        //Used to edit the budget of the party
        binding.editBudgetBtn.setOnClickListener {
            if (isDoubleClicked(1000)) return@setOnClickListener

            val dialog = DialogChangeBudget(requireContext(), prefRepository)
            dialog.show()
            dialog.setOnDismissListener {
                details = prefRepository.currentPartyDetails
                setBudget()
                UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute()
            }
        }

        //Open the location screen on clicking the edit location button
        binding.editLocationBtn.setOnClickListener {
            if (isDoubleClicked(1000)) return@setOnClickListener
            findNavController().navigate(R.id.action_finalChecklistFragment_to_venueLocationFragment)
        }

        //Used to update the selected party options
        binding.editPartyTypeBtn.setOnClickListener {
            if (isDoubleClicked(1000)) return@setOnClickListener

            val dialog = DialogChangePartyType(requireContext(), prefRepository)
            dialog.show()
            dialog.setOnDismissListener {
                details = prefRepository.currentPartyDetails
                setSelectedPartyTypes()
                UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute()
                setBudget()
            }
        }

        //This will open the caterer screen to change the caterers
        binding.editCatererBtn.setOnClickListener {
            if (isDoubleClicked(1000)) return@setOnClickListener
            findNavController().navigate(R.id.action_finalChecklistFragment_to_caterersListFragment)
        }

        //This will open the venue screen to change the venue
        binding.editVenueBtn.setOnClickListener {
            if (isDoubleClicked(1000)) return@setOnClickListener
            if (prefRepository.currentPartyDetails.locations.isNullOrEmpty()) {
                requireContext().toastShort("First Select location!")
                return@setOnClickListener
            }
            findNavController().navigate(R.id.action_finalChecklistFragment_to_venueListFragment)
        }

    }

    //Used to set the checkbox listeners
    private fun setCbListener() {
        //Used to store the changed status of checkbox of party type
        binding.selectedPartyTypeCb.setOnCheckedChangeListener { _, isChecked ->
            val lastValue = details.checkedItemList!!.find { it.itemName == CB_PARTY_TYPE }
            details.checkedItemList?.remove(lastValue!!)
            details.checkedItemList?.add(CheckedItem(CB_PARTY_TYPE, isChecked))

            prefRepository.currentPartyDetails = details
            if (CURRENT_SELECTED_OPTION == OPTION_PAST)
                UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute()
            else
                UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute()
        }

        //Used to store the changed status of checkbox of caterer
        binding.catererCb.setOnCheckedChangeListener { _, isChecked ->
            val lastValue = details.checkedItemList!!.find { it.itemName == CB_CATERER }
            details.checkedItemList?.remove(lastValue!!)
            details.checkedItemList?.add(CheckedItem(CB_CATERER, isChecked))

            prefRepository.currentPartyDetails = details
            if (CURRENT_SELECTED_OPTION == OPTION_PAST)
                UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute()
            else
                UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute()
        }

        //Used to store the changed status of checkbox of venue
        binding.venueCb.setOnCheckedChangeListener { _, isChecked ->
            val lastValue = details.checkedItemList!!.find { it.itemName == CB_VENUE }
            details.checkedItemList?.remove(lastValue!!)
            details.checkedItemList?.add(CheckedItem(CB_VENUE, isChecked))

            prefRepository.currentPartyDetails = details
            if (CURRENT_SELECTED_OPTION == OPTION_PAST)
                UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute()
            else
                UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute()
        }

        //Used to store the changed status of checkbox of magic show
        binding.magicCb.setOnCheckedChangeListener { _, isChecked ->
            val lastValue = details.checkedItemList!!.find { it.itemName == CB_MAGIC_SHOW }
            details.checkedItemList?.remove(lastValue!!)
            details.checkedItemList?.add(CheckedItem(CB_MAGIC_SHOW, isChecked))

            prefRepository.currentPartyDetails = details
            if (CURRENT_SELECTED_OPTION == OPTION_PAST)
                UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute()
            else
                UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute()
        }

        //Used to store the changed status of checkbox of decoration
        binding.decorationCb.setOnCheckedChangeListener { _, isChecked ->
            val lastValue = details.checkedItemList!!.find { it.itemName == CB_DECORATOR }
            details.checkedItemList?.remove(lastValue!!)
            details.checkedItemList?.add(CheckedItem(CB_DECORATOR, isChecked))

            prefRepository.currentPartyDetails = details
            if (CURRENT_SELECTED_OPTION == OPTION_PAST)
                UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute()
            else
                UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute()
        }

        //Used to store the changed status of checkbox of alcohol
        binding.alcoholCb.setOnCheckedChangeListener { _, isChecked ->
            val lastValue = details.checkedItemList!!.find { it.itemName == CB_ALCOHOL }
            details.checkedItemList?.remove(lastValue!!)
            details.checkedItemList?.add(CheckedItem(CB_ALCOHOL, isChecked))

            prefRepository.currentPartyDetails = details
            if (CURRENT_SELECTED_OPTION == OPTION_PAST)
                UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute()
            else
                UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute()
        }

        //Used to store the changed status of checkbox of budget
        binding.budgetCb.setOnCheckedChangeListener { _, isChecked ->
            val lastValue = details.checkedItemList!!.find { it.itemName == CB_BUDGET }
            details.checkedItemList?.remove(lastValue!!)
            details.checkedItemList?.add(CheckedItem(CB_BUDGET, isChecked))

            prefRepository.currentPartyDetails = details
            if (CURRENT_SELECTED_OPTION == OPTION_PAST)
                UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute()
            else
                UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute()
        }
    }


    //Used to set the location of venue and updating the UI accordingly.
    private fun setLocations() {
        if (details.partyDestination == getString(R.string.home)) {
            binding.locationSelected.hide()
            binding.textView22.hide()
            binding.editLocationBtn.hide()
        } else {
            if (details.locations.isNullOrEmpty()) {
                binding.locationSelected.text = "Select location!"
            } else {
                var locationString = ""
                for (x in details.locations!!) {
                    locationString += "$x, "
                }
                binding.locationSelected.text =
                    locationString.substring(0, locationString.length - 2)
            }
        }
    }

    //Used to set the budget of the party after calculating
    @SuppressLint("SetTextI18n")
    private fun setBudget() {
        val budgetLimit = when (details.partyBudget) {
            getString(R.string.low) -> "$200 - $400"
            getString(R.string.medium) -> "$400 - $600"
            getString(R.string.high) -> "$600 - $800"
            getString(R.string.very_high) -> "More than $800"
            else -> "$200 - $400"
        }
        binding.budgetSelected.text = "$budgetLimit (${details.partyBudget})"

        var budgetDistributionText = ""

        val catererPrice = prefRepository.currentPartyDetails.selectedCaterer!!.pricePerPax * prefRepository.currentPartyDetails.partyGuest!!
        budgetDistributionText += "Caterer($${catererPrice}), "

        val venuePrice =
            if (prefRepository.currentPartyDetails.partyDestination != getString(R.string.home)) {
                if (prefRepository.currentPartyDetails.selectedDestination != null) {
                    budgetDistributionText += "Venue($${prefRepository.currentPartyDetails.selectedDestination!!.price.toInt()}), "
                    prefRepository.currentPartyDetails.selectedDestination!!.price.toInt()
                } else 0
            } else
                0
        val alcoholPrice =
            if (prefRepository.currentPartyDetails.partyType.contains(PARTY_TYPE_ALCOHOL)) {
                budgetDistributionText += "Alcohol($${10 * prefRepository.currentPartyDetails.partyGuest!!}), "
                10 * prefRepository.currentPartyDetails.partyGuest!!
            } else
                0
        val magicPrice =
            if (prefRepository.currentPartyDetails.partyType.contains(PARTY_TYPE_MAGIC_SHOW)) {
                budgetDistributionText += "Magic Show($200), "
                200
            } else
                0
        val decoration =
            if (prefRepository.currentPartyDetails.partyType.contains(PARTY_TYPE_DECORATION)) {
                budgetDistributionText += "Decoration($100), "
                100
            } else
                0

        val totalBudget = venuePrice + catererPrice + alcoholPrice + magicPrice + decoration
        binding.partyBudget.text = "$${totalBudget}"
        binding.budgetDistribution.text =
            budgetDistributionText.substring(0, budgetDistributionText.length - 2)
    }

    //Used to set the selected party type in the recycler view
    private fun setSelectedPartyTypes() {
        binding.selectedPartyTypeRv.initGridAdapter(
            SelectedPartyTypeAdapter(getPartyTypeFromStringArray(details.partyType)), true, 3)
    }

    //Used to set the selected caterer detail
    @SuppressLint("SetTextI18n")
    private fun setCatererDetails() {
        binding.include2.name.text = details.selectedCaterer!!.name.trim()
        binding.include2.price.text = "$${details.selectedCaterer!!.pricePerPax}"
        binding.include2.totalGuestPriceTv.text = "${prefRepository.currentPartyDetails.partyGuest} Pax total"
        binding.include2.totalGuestPrice.text = "$${prefRepository.currentPartyDetails.selectedCaterer!!.pricePerPax * prefRepository.currentPartyDetails.partyGuest!!}"

        val partyTypes =
            gson.fromJson<ArrayList<String>>(details.selectedCaterer!!.partyType, arrayListType)
        var partyTypeString = ""
        for (x in partyTypes) {
            partyTypeString += "$x, "
        }
        binding.include2.partyTypeValue.text =
            partyTypeString.substring(0, partyTypeString.length - 2)
        binding.include2.parent.isEnabled = false
    }

    //Used to set the selected venue data after checking selected destination type
    @SuppressLint("SetTextI18n")
    private fun setVenueDetails() {
        if (details.partyDestination != getString(R.string.home)) {
            binding.include.parent.isEnabled = false
            if (details.selectedDestination != null) {
                binding.include.venueName.text = details.selectedDestination!!.name
                binding.include.venueAdd.text = details.selectedDestination!!.address
                binding.include.capacity.text = "${details.selectedDestination!!.capacity} Guest"
                binding.include.contact.text = details.selectedDestination!!.contact
                binding.include.price.text = "$${details.selectedDestination!!.price}"
                binding.include.location.text = details.selectedDestination!!.location
                val partyTypes = gson.fromJson<ArrayList<String>>(details.selectedDestination!!.partyType,
                                                                  arrayListType)
                var partyTypeStringVenue = ""

                for (x in partyTypes) {
                    partyTypeStringVenue += "$x, "
                }
                binding.include.suitable.text =
                    partyTypeStringVenue.substring(0, partyTypeStringVenue.length - 2)
            }
        } else {
            binding.include.parent.hide()
            binding.venueTitle.hide()
            binding.venueCb.hide()
            binding.editVenueBtn.hide()
        }
    }

    //Used to set the phone no. and other details
    //of the magic show, decoration and alcohol if they are selected
    private fun setSpecialDetails() {
        if (details.partyType.contains(PARTY_TYPE_MAGIC_SHOW))
            binding.magicianDetails.show()

        if (details.partyType.contains(PARTY_TYPE_DECORATION))
            binding.decoratorDetails.show()

        if (details.partyType.contains(PARTY_TYPE_ALCOHOL))
            binding.alcoholDetails.show()
    }


    //Used to store the summary data in the SQL in background thread
    private class UpdateSummaryAsyncTask(private val context: Activity,
                                         private val prefRepository: PrefRepository) : AsyncTask<Void, Void, Void?>() {

        override fun doInBackground(vararg params: Void?): Void? {
            val summaryDao = AppDatabase.Companion().getDatabase(context).summaryDao()
            summaryDao.delete(prefRepository.currentUser?.uid!!)
            summaryDao.insert(convertSummary(prefRepository.currentPartyDetails,
                                             prefRepository.currentUser?.uid!!))
            return null
        }
    }

    //Used to store the summary data in the SQL in background thread
    private class GetSummaryAsyncTask(private val context: Activity,
                                      private val prefRepository: PrefRepository,
                                      val taskListener: GetSummaryListener) : AsyncTask<Void, Void, MutableList<SummaryDetails>?>() {

        override fun doInBackground(vararg params: Void?): MutableList<SummaryDetails>? {
            val summaryDao = AppDatabase.Companion().getDatabase(context).summaryDao()
            return summaryDao.getUserSummary(prefRepository.currentUser?.uid!!)
        }

        override fun onPostExecute(result: MutableList<SummaryDetails>?) {
            super.onPostExecute(result)
            taskListener.onTaskEnded(result)
        }
    }

    //Used to delete the unfinished data from the SQL in background thread
    private class DeleteUnfinishedSummaryAsyncTask(private val context: Activity,
                                                   private val prefRepository: PrefRepository) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            val unfinishedDao = AppDatabase.Companion().getDatabase(context)
                .unfinishedDao()
            unfinishedDao.delete(prefRepository.currentUser?.uid!!)
            return null
        }
    }


    //Used to store the history summary data in the SQL in background thread
    private class UpdateHistorySummaryAsyncTask(private val context: Activity,
                                                private val prefRepository: PrefRepository) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            val summaryDao = AppDatabase.Companion().getDatabase(context).historySummaryDao()
            if (prefRepository.currentPartyDetails.id == null) {
                val details = prefRepository.currentPartyDetails
                details.id = getRandom()
                summaryDao.insert(convertHistorySummary(details,
                                                        prefRepository.currentUser?.uid!!))
            } else {
                summaryDao.delete(prefRepository.currentPartyDetails.id!!)
                summaryDao.insert(convertHistorySummary(prefRepository.currentPartyDetails,
                                                        prefRepository.currentUser?.uid!!))
            }

            return null
        }
    }


    //Used for showing the edit icon in the toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (CURRENT_SELECTED_OPTION != OPTION_PAST)
            requireActivity().menuInflater.inflate(R.menu.menu_toolbar, menu)
    }

    //Toolbar edit icon click listener
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Changing the edit and save icon on clicking
        if (item.itemId == R.id.edit) {
            if (binding.groupEditBtn.isVisible) {
                item.icon = requireContext().getDrawableRes(R.drawable.ic_edit)
                binding.groupEditBtn.hide()
                if (details.partyDestination != getString(R.string.home)) {
                    binding.editLocationBtn.hide()
                    binding.editVenueBtn.hide()
                }
            } else {
                item.icon = requireContext().getDrawableRes(R.drawable.ic_save)
                binding.groupEditBtn.show()
                if (details.partyDestination != getString(R.string.home)) {
                    binding.editLocationBtn.show()
                    binding.editVenueBtn.show()
                }
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
