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

        when (CURRENT_SELECTED_OPTION) {
            OPTION_CHECKLIST -> {
                lifecycleScope.launch(Dispatchers.Main) {
                    val summaryDao = AppDatabase.getDatabase(requireContext()).summaryDao()
                    val summaryData = summaryDao.getUserSummary(prefRepository.getCurrentUser()?.uid!!)

                    details = convertPartyFromSummary(summaryData[0])
                    prefRepository.setCurrentPartyDetails(details)

                    setAllPartyData()
                }
            }
            OPTION_PAST -> {
                deleteUnfinishedData()
                details = prefRepository.getCurrentPartyDetails()
                setAllPartyData()
            }
            else -> {
                deleteUnfinishedData()
                details = prefRepository.getCurrentPartyDetails()
                setAllPartyData()
                updateSummaryData()
                if (!OPENED_GUEST_LIST)
                    updateHistorySummaryData()
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) {
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

    private fun setCheckBox() {
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

    private fun setViewListeners() {
        binding.updateGuestBtn.setOnClickListener {
            details.guestNameList.also {
                GUEST_LIST_NAMES = it ?: ArrayList()
            }
            addEmptyGuest(details.partyGuest!!)
            findNavController().navigate(R.id.action_finalChecklistFragment_to_guestListFragment)
        }

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

    @SuppressLint("SetTextI18n")
    private fun setEditBtnListener() {
        binding.editDateBtn.setOnClickListener {
            if (isDoubleClicked(1000)) return@setOnClickListener
            val currentDate = Calendar.getInstance()

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

            datePicker.datePicker.minDate = currentDate.timeInMillis
            datePicker.show()
        }

        binding.editDestinationBtn.setOnClickListener {
            if (isDoubleClicked(1000)) return@setOnClickListener
            val dialog = DialogDestinationSelection(requireContext(), prefRepository)
            dialog.show()
            dialog.setOnDismissListener {
                binding.destinationType.text = prefRepository.getCurrentPartyDetails().partyDestination
                updateSummaryData()
            }
        }

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

        binding.editLocationBtn.setOnClickListener {
            if (isDoubleClicked(1000)) return@setOnClickListener
            findNavController().navigate(R.id.action_finalChecklistFragment_to_venueLocationFragment)
        }

        binding.editPartyTypeBtn.setOnClickListener {
            if (isDoubleClicked(1000)) return@setOnClickListener

            val dialog = DialogChangePartyType(requireContext(), prefRepository)
            dialog.show()
            dialog.setOnDismissListener {
                details = prefRepository.getCurrentPartyDetails()
                setSelectedPartyTypes()
                updateSummaryData()
            }
        }

        binding.editCatererBtn.setOnClickListener {
            if (isDoubleClicked(1000)) return@setOnClickListener
            findNavController().navigate(R.id.action_finalChecklistFragment_to_caterersListFragment)
        }

        binding.editVenueBtn.setOnClickListener {
            if (isDoubleClicked(1000)) return@setOnClickListener
            findNavController().navigate(R.id.action_finalChecklistFragment_to_venueListFragment)
        }

    }

    private fun setCbListener() {
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


    private fun setLocations() {
        if (details.locations.isNullOrEmpty()) {
            binding.locationSelected.hide()
            binding.textView22.hide()
        } else {
            var locationString = ""
            for (x in details.locations!!) {
                locationString += "$x, "
            }
            binding.locationSelected.text = locationString.substring(0, locationString.length - 2)
        }
    }

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
        binding.partyBudget.text = "$${details.selectedCaterer!!.pricePerPax * details.partyGuest!!}"
    }

    private fun setSelectedPartyTypes() {
        binding.selectedPartyTypeRv.initGridAdapter(
            SelectedPartyTypeAdapter(getPartyTypeFromStringArray(details.partyType)), true, 3)
    }

    @SuppressLint("SetTextI18n")
    private fun setCatererDetails() {
        binding.include2.name.text = details.selectedCaterer!!.name.trim()
        binding.include2.price.text = "$${details.selectedCaterer!!.pricePerPax}"
        val partyTypes = gson.fromJson<ArrayList<String>>(details.selectedCaterer!!.partyType,
                                                          arrayListType)
        var partyTypeString = ""
        for (x in partyTypes) {
            partyTypeString += "$x, "
        }
        binding.include2.partyTypeValue.text = partyTypeString.substring(0,
                                                                         partyTypeString.length - 2)
        binding.include2.parent.isEnabled = false
    }

    @SuppressLint("SetTextI18n")
    private fun setVenueDetails() {
        if (details.partyDestination != getString(R.string.home)) {
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
            binding.include.suitable.text = partyTypeStringVenue.substring(0,
                                                                           partyTypeStringVenue.length - 2)
            binding.include.parent.isEnabled = false
        } else {
            binding.include.parent.hide()
            binding.venueTitle.hide()
            binding.venueCb.hide()
            binding.editVenueBtn.hide()
        }
    }

    private fun setSpecialDetails() {
        if (details.partyType.contains(PARTY_TYPE_MAGIC_SHOW))
            binding.magicianDetails.show()

        if (details.partyType.contains(PARTY_TYPE_DECORATION))
            binding.decoratorDetails.show()

        if (details.partyType.contains(PARTY_TYPE_ALCOHOL))
            binding.alcoholDetails.show()
    }


    private fun updateSummaryData() {
        println("updateSummaryData called")
        lifecycleScope.launch(Dispatchers.Main) {
            val summaryDao = AppDatabase.getDatabase(requireContext()).summaryDao()
            summaryDao.delete(prefRepository.getCurrentUser()?.uid!!)
            summaryDao.insert(convertSummary(prefRepository.getCurrentPartyDetails(),
                                             prefRepository.getCurrentUser()?.uid!!))
        }
    }

    private fun deleteUnfinishedData() {
        lifecycleScope.launch(Dispatchers.Main) {
            val unfinishedDao = AppDatabase.getDatabase(requireContext()).unfinishedDao()
            unfinishedDao.delete(prefRepository.getCurrentUser()?.uid!!)
        }
    }

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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (CURRENT_SELECTED_OPTION != OPTION_PAST)
            requireActivity().menuInflater.inflate(R.menu.menu_toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
