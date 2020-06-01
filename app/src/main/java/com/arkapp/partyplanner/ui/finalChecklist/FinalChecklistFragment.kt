package com.arkapp.partyplanner.ui.finalChecklist

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.CheckedItem
import com.arkapp.partyplanner.data.models.PartyDetails
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.arkapp.partyplanner.databinding.FragmentFinalChecklistBinding
import com.arkapp.partyplanner.ui.finalChecklist.utils.DialogChangeBudget
import com.arkapp.partyplanner.ui.finalChecklist.utils.DialogChangeGuest
import com.arkapp.partyplanner.ui.finalChecklist.utils.DialogChangePartyType
import com.arkapp.partyplanner.ui.finalChecklist.utils.DialogDestinationSelection
import com.arkapp.partyplanner.utils.*
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
                lifecycleScope.launch(Dispatchers.Main) {
                    val summaryDao = AppDatabase.getDatabase(requireContext()).summaryDao()
                    val summaryData = summaryDao.getUserSummary(prefRepository.getCurrentUser()?.uid!!)

                    details = convertPartyFromSummary(summaryData[0])
                    prefRepository.setCurrentPartyDetails(details)

                    setAllPartyData()
                }
            }
            OPTION_PAST -> {
                //Deleting the unfinished data and setting all the party data
                deleteUnfinishedData()
                details = prefRepository.getCurrentPartyDetails()
                setAllPartyData()
            }
            else -> {
                //Deleting the unfinished data and setting all the party data
                deleteUnfinishedData()
                details = prefRepository.getCurrentPartyDetails()
                setAllPartyData()
                updateSummaryData()

                //Not adding in history if screen is opened from the guest checklist
                if (!OPENED_GUEST_LIST)
                    updateHistorySummaryData()
            }
        }

        //Handling back press in checklist screen.
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) {
                //Resetting the shared preferences party data
                prefRepository
                    .setCurrentPartyDetails(
                        PartyDetails(
                            null,
                            null,
                            null,
                            null,
                            null,
                            ArrayList(),
                            null,
                            null,
                            null,
                            null,
                            null,
                            ArrayList())
                    )
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
                prefRepository.setCurrentPartyDetails(details)
                updateSummaryData()
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

            val details = prefRepository.getCurrentPartyDetails()
            details.extraNote = it.toString()
            prefRepository.setCurrentPartyDetails(details)

            if (CURRENT_SELECTED_OPTION == OPTION_CHECKLIST || CURRENT_SELECTED_OPTION == OPTION_CREATE)
                updateSummaryData()
            else
                updateHistorySummaryData()
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

                prefRepository.setCurrentPartyDetails(details)
                updateSummaryData()
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
                binding.destinationType.text = prefRepository.getCurrentPartyDetails().partyDestination
                updateSummaryData()
                setBudget()

                details = prefRepository.getCurrentPartyDetails()

                //On changing the party type, updating the option in the checklist screen
                if (prefRepository.getCurrentPartyDetails().partyDestination != getString(R.string.home)) {

                    binding.include.parent.show()
                    binding.venueTitle.show()
                    binding.venueCb.show()
                    binding.editVenueBtn.show()

                    binding.locationSelected.show()
                    binding.textView22.show()
                    binding.editLocationBtn.show()

                    prefRepository.getCurrentPartyDetails().selectedDestination.also {
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
                binding.totalGuest.text = "${prefRepository.getCurrentPartyDetails().partyGuest} Guests"
                details = prefRepository.getCurrentPartyDetails()
                setBudget()
                updateSummaryData()
            }
        }

        //Used to edit the budget of the party
        binding.editBudgetBtn.setOnClickListener {
            if (isDoubleClicked(1000)) return@setOnClickListener

            val dialog = DialogChangeBudget(requireContext(), prefRepository)
            dialog.show()
            dialog.setOnDismissListener {
                details = prefRepository.getCurrentPartyDetails()
                setBudget()
                updateSummaryData()
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
                details = prefRepository.getCurrentPartyDetails()
                setSelectedPartyTypes()
                updateSummaryData()
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
            if (prefRepository.getCurrentPartyDetails().locations.isNullOrEmpty()) {
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

            prefRepository.setCurrentPartyDetails(details)
            if (CURRENT_SELECTED_OPTION == OPTION_PAST)
                updateHistorySummaryData()
            else
                updateSummaryData()
        }

        //Used to store the changed status of checkbox of caterer
        binding.catererCb.setOnCheckedChangeListener { _, isChecked ->
            val lastValue = details.checkedItemList!!.find { it.itemName == CB_CATERER }
            details.checkedItemList?.remove(lastValue!!)
            details.checkedItemList?.add(CheckedItem(CB_CATERER, isChecked))

            prefRepository.setCurrentPartyDetails(details)
            if (CURRENT_SELECTED_OPTION == OPTION_PAST)
                updateHistorySummaryData()
            else
                updateSummaryData()
        }

        //Used to store the changed status of checkbox of venue
        binding.venueCb.setOnCheckedChangeListener { _, isChecked ->
            val lastValue = details.checkedItemList!!.find { it.itemName == CB_VENUE }
            details.checkedItemList?.remove(lastValue!!)
            details.checkedItemList?.add(CheckedItem(CB_VENUE, isChecked))

            prefRepository.setCurrentPartyDetails(details)
            if (CURRENT_SELECTED_OPTION == OPTION_PAST)
                updateHistorySummaryData()
            else
                updateSummaryData()
        }

        //Used to store the changed status of checkbox of magic show
        binding.magicCb.setOnCheckedChangeListener { _, isChecked ->
            val lastValue = details.checkedItemList!!.find { it.itemName == CB_MAGIC_SHOW }
            details.checkedItemList?.remove(lastValue!!)
            details.checkedItemList?.add(CheckedItem(CB_MAGIC_SHOW, isChecked))

            prefRepository.setCurrentPartyDetails(details)
            if (CURRENT_SELECTED_OPTION == OPTION_PAST)
                updateHistorySummaryData()
            else
                updateSummaryData()
        }

        //Used to store the changed status of checkbox of decoration
        binding.decorationCb.setOnCheckedChangeListener { _, isChecked ->
            val lastValue = details.checkedItemList!!.find { it.itemName == CB_DECORATOR }
            details.checkedItemList?.remove(lastValue!!)
            details.checkedItemList?.add(CheckedItem(CB_DECORATOR, isChecked))

            prefRepository.setCurrentPartyDetails(details)
            if (CURRENT_SELECTED_OPTION == OPTION_PAST)
                updateHistorySummaryData()
            else
                updateSummaryData()
        }

        //Used to store the changed status of checkbox of alcohol
        binding.alcoholCb.setOnCheckedChangeListener { _, isChecked ->
            val lastValue = details.checkedItemList!!.find { it.itemName == CB_ALCOHOL }
            details.checkedItemList?.remove(lastValue!!)
            details.checkedItemList?.add(CheckedItem(CB_ALCOHOL, isChecked))

            prefRepository.setCurrentPartyDetails(details)
            if (CURRENT_SELECTED_OPTION == OPTION_PAST)
                updateHistorySummaryData()
            else
                updateSummaryData()
        }

        //Used to store the changed status of checkbox of budget
        binding.budgetCb.setOnCheckedChangeListener { _, isChecked ->
            val lastValue = details.checkedItemList!!.find { it.itemName == CB_BUDGET }
            details.checkedItemList?.remove(lastValue!!)
            details.checkedItemList?.add(CheckedItem(CB_BUDGET, isChecked))

            prefRepository.setCurrentPartyDetails(details)
            if (CURRENT_SELECTED_OPTION == OPTION_PAST)
                updateHistorySummaryData()
            else
                updateSummaryData()
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

        val catererPrice = prefRepository.getCurrentPartyDetails().selectedCaterer!!.pricePerPax * prefRepository.getCurrentPartyDetails().partyGuest!!
        budgetDistributionText += "Caterer($${catererPrice}), "

        val venuePrice =
            if (prefRepository.getCurrentPartyDetails().partyDestination != getString(R.string.home)) {
                budgetDistributionText += "Venue($${prefRepository.getCurrentPartyDetails().selectedDestination!!.price.toInt()}), "
                prefRepository.getCurrentPartyDetails().selectedDestination!!.price.toInt()
            } else
                0
        val alcoholPrice =
            if (prefRepository.getCurrentPartyDetails().partyType.contains(PARTY_TYPE_ALCOHOL)) {
                budgetDistributionText += "Alcohol($${10 * prefRepository.getCurrentPartyDetails().partyGuest!!}), "
                10 * prefRepository.getCurrentPartyDetails().partyGuest!!
            } else
                0
        val magicPrice =
            if (prefRepository.getCurrentPartyDetails().partyType.contains(PARTY_TYPE_MAGIC_SHOW)) {
                budgetDistributionText += "Magic Show($200), "
                200
            } else
                0
        val decoration =
            if (prefRepository.getCurrentPartyDetails().partyType.contains(PARTY_TYPE_DECORATION)) {
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
        binding.include2.totalGuestPriceTv.text = "${prefRepository.getCurrentPartyDetails().partyGuest} Pax total"
        binding.include2.totalGuestPrice.text = "$${prefRepository.getCurrentPartyDetails().selectedCaterer!!.pricePerPax * prefRepository.getCurrentPartyDetails().partyGuest!!}"

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
    private fun updateSummaryData() {
        println("updateSummaryData called")
        lifecycleScope.launch(Dispatchers.Main) {
            val summaryDao = AppDatabase.getDatabase(requireContext()).summaryDao()
            summaryDao.delete(prefRepository.getCurrentUser()?.uid!!)
            summaryDao.insert(convertSummary(prefRepository.getCurrentPartyDetails(),
                                             prefRepository.getCurrentUser()?.uid!!))
        }
    }

    //Used to delete the unfinished data from the SQL in background thread
    private fun deleteUnfinishedData() {
        lifecycleScope.launch(Dispatchers.Main) {
            val unfinishedDao = AppDatabase.getDatabase(requireContext()).unfinishedDao()
            unfinishedDao.delete(prefRepository.getCurrentUser()?.uid!!)
        }
    }

    //Used to store the history summary data in the SQL in background thread
    private fun updateHistorySummaryData() {
        lifecycleScope.launch(Dispatchers.Main) {
            val summaryDao = AppDatabase.getDatabase(requireContext()).historySummaryDao()
            if (prefRepository.getCurrentPartyDetails().id == null) {
                val details = prefRepository.getCurrentPartyDetails()
                details.id = getRandom()
                summaryDao.insert(convertHistorySummary(details,
                                                        prefRepository.getCurrentUser()?.uid!!))
            } else {
                summaryDao.delete(prefRepository.getCurrentPartyDetails().id!!)
                summaryDao.insert(convertHistorySummary(prefRepository.getCurrentPartyDetails(),
                                                        prefRepository.getCurrentUser()?.uid!!))
            }
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
