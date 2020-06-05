package com.arkapp.partyplanner.ui.finalChecklist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.data.models.Caterer;
import com.arkapp.partyplanner.data.models.CheckedItem;
import com.arkapp.partyplanner.data.models.PartyDetails;
import com.arkapp.partyplanner.data.models.SummaryDetails;
import com.arkapp.partyplanner.data.models.Venue;
import com.arkapp.partyplanner.data.repository.PrefRepository;
import com.arkapp.partyplanner.data.room.AppDatabase.Companion;
import com.arkapp.partyplanner.data.room.HistorySummaryDao;
import com.arkapp.partyplanner.data.room.SummaryDetailsDao;
import com.arkapp.partyplanner.data.room.UnfinishedDetailsDao;
import com.arkapp.partyplanner.databinding.FragmentFinalChecklistBinding;
import com.arkapp.partyplanner.ui.finalChecklist.utils.DialogChangeBudget;
import com.arkapp.partyplanner.ui.finalChecklist.utils.DialogChangeGuest;
import com.arkapp.partyplanner.ui.finalChecklist.utils.DialogChangePartyType;
import com.arkapp.partyplanner.ui.finalChecklist.utils.DialogDestinationSelection;
import com.arkapp.partyplanner.ui.finalChecklist.utils.GetSummaryListener;
import com.arkapp.partyplanner.utils.AppDataKt;
import com.arkapp.partyplanner.utils.ViewUtilsKt;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static androidx.navigation.fragment.NavHostFragment.findNavController;
import static com.arkapp.partyplanner.utils.AppDataKt.CB_ALCOHOL;
import static com.arkapp.partyplanner.utils.AppDataKt.CB_BUDGET;
import static com.arkapp.partyplanner.utils.AppDataKt.CB_CATERER;
import static com.arkapp.partyplanner.utils.AppDataKt.CB_DECORATOR;
import static com.arkapp.partyplanner.utils.AppDataKt.CB_MAGIC_SHOW;
import static com.arkapp.partyplanner.utils.AppDataKt.CB_PARTY_TYPE;
import static com.arkapp.partyplanner.utils.AppDataKt.CB_VENUE;
import static com.arkapp.partyplanner.utils.AppDataKt.OPTION_CHECKLIST;
import static com.arkapp.partyplanner.utils.AppDataKt.OPTION_CREATE;
import static com.arkapp.partyplanner.utils.AppDataKt.OPTION_PAST;
import static com.arkapp.partyplanner.utils.AppDataKt.OPTION_UNFINISHED;
import static com.arkapp.partyplanner.utils.AppDataKt.PARTY_TYPE_ALCOHOL;
import static com.arkapp.partyplanner.utils.AppDataKt.PARTY_TYPE_DECORATION;
import static com.arkapp.partyplanner.utils.AppDataKt.PARTY_TYPE_MAGIC_SHOW;
import static com.arkapp.partyplanner.utils.AppDataKt.addEmptyGuest;
import static com.arkapp.partyplanner.utils.AppDataKt.getPartyTypeFromStringArray;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.getDrawableRes;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.getFormattedDate;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.hide;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.initGridAdapter;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.isDoubleClicked;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.show;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.toastShort;

public class FinalChecklistFragment extends Fragment {
    private final Gson gson = new Gson();
    private final Type arrayListType = new TypeToken<ArrayList<String>>() {
    }.getType();
    private PrefRepository prefRepository;
    private PartyDetails details;
    private FragmentFinalChecklistBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentFinalChecklistBinding.inflate(inflater);
        prefRepository = new PrefRepository(requireContext());

        return binding.getRoot();
    }

    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewUtilsKt.hideKeyboard(requireActivity());

        //Checking from where the checklist screen is opened and setting appropriate data
        switch (AppDataKt.getCURRENT_SELECTED_OPTION()) {
            case OPTION_CHECKLIST:
                //Fetching the summary table in background
                GetSummaryListener taskListener = (it -> {
                    details = AppDataKt.convertPartyFromSummary(it.get(0));
                    prefRepository.setCurrentPartyDetails(details);
                    setAllPartyData();
                });
                new FinalChecklistFragment.GetSummaryAsyncTask(requireActivity(), prefRepository, taskListener).execute();
                break;
            case OPTION_UNFINISHED:
            default:
                //Deleting the unfinished data and setting all the party data
                new DeleteUnfinishedSummaryAsyncTask(requireActivity(), prefRepository).execute();
                details = prefRepository.getCurrentPartyDetails();
                setAllPartyData();

                new UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute();

                //Not adding in history if screen is opened from the guest checklist
                if (!AppDataKt.getOPENED_GUEST_LIST()) {
                    new UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute();
                }
                break;
            case OPTION_PAST:
                //Deleting the unfinished data and setting all the party data
                new DeleteUnfinishedSummaryAsyncTask(requireActivity(), prefRepository).execute();
                details = prefRepository.getCurrentPartyDetails();
                setAllPartyData();
        }

        //Handling back press in checklist screen.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Resetting the shared preferences party data
                prefRepository.setCurrentPartyDetails(
                        new PartyDetails(
                                null,
                                null,
                                null,
                                null,
                                null,
                                new ArrayList<String>(),
                                null,
                                null,
                                null,
                                null,
                                null,
                                new ArrayList<String>())
                );
                this.remove();
                if (AppDataKt.getCURRENT_SELECTED_OPTION() == OPTION_PAST)
                    findNavController(FinalChecklistFragment.this).navigate(R.id.action_finalChecklistFragment_to_historySummaryFragment);
                else
                    findNavController(FinalChecklistFragment.this).navigate(R.id.action_finalChecklistFragment_to_optionsFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        binding.btFinish.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    @SuppressLint({"SetTextI18n"})
    //Used to set all the party data displayed on the checklist data
    private void setAllPartyData() {
        binding.partyDate.setText(getFormattedDate(details.getPartyDate()));
        binding.destinationType.setText(details.getPartyDestination());
        binding.totalGuest.setText(details.getPartyGuest() + " Guests");
        binding.notesEt.setText(details.getExtraNote());

        setCheckBox();
        setLocations();
        setBudget();
        setSelectedPartyTypes();
        setCatererDetails();
        setVenueDetails();
        setSpecialDetails();

        setViewListeners();
    }

    //Used to set the checkbox data of all items.
    private void setCheckBox() {
        //Checking the checkbox data in the party data and setting accordingly
        ArrayList<CheckedItem> item = details.getCheckedItemList();
        if (item != null && !item.isEmpty()) {
            for (CheckedItem x : item) {
                switch (x.getItemName()) {
                    case CB_PARTY_TYPE:
                        binding.selectedPartyTypeCb.setChecked(x.getSelected());
                        break;
                    case CB_CATERER:
                        binding.catererCb.setChecked(x.getSelected());
                        break;
                    case CB_VENUE:
                        binding.venueCb.setChecked(x.getSelected());
                        break;
                    case CB_DECORATOR:
                        binding.decorationCb.setChecked(x.getSelected());
                        break;
                    case CB_MAGIC_SHOW:
                        binding.magicCb.setChecked(x.getSelected());
                        break;
                    case CB_ALCOHOL:
                        binding.alcoholCb.setChecked(x.getSelected());
                        break;
                    case CB_BUDGET:
                        binding.budgetCb.setChecked(x.getSelected());
                }
            }
        } else {
            //adding blank values in the checked item list
            details.setCheckedItemList(new ArrayList<>());
            details.getCheckedItemList().add(new CheckedItem(CB_PARTY_TYPE, false));
            details.getCheckedItemList().add(new CheckedItem(CB_CATERER, false));
            details.getCheckedItemList().add(new CheckedItem(CB_VENUE, false));
            details.getCheckedItemList().add(new CheckedItem(CB_DECORATOR, false));
            details.getCheckedItemList().add(new CheckedItem(CB_MAGIC_SHOW, false));
            details.getCheckedItemList().add(new CheckedItem(CB_ALCOHOL, false));
            details.getCheckedItemList().add(new CheckedItem(CB_BUDGET, false));
            prefRepository.setCurrentPartyDetails(details);
            new UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute();
        }
    }


    //Used to set the listener of button and edit text etc
    private void setViewListeners() {

        //Opening the guest checklist on clicking update guest button
        binding.updateGuestBtn.setOnClickListener(v -> {
            ArrayList<CheckedItem> guestList = details.getGuestNameList();

            if (guestList == null || guestList.isEmpty())
                AppDataKt.setGUEST_LIST_NAMES(new ArrayList<>());
            else
                AppDataKt.setGUEST_LIST_NAMES(guestList);

            addEmptyGuest(details.getPartyGuest());
            findNavController(this).navigate(R.id.action_finalChecklistFragment_to_guestListFragment);
        });

        //Storing the notes on updating

        binding.notesEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                PartyDetails details = prefRepository.getCurrentPartyDetails();
                details.setExtraNote(s.toString());
                prefRepository.setCurrentPartyDetails(details);

                if (AppDataKt.getCURRENT_SELECTED_OPTION() == OPTION_CHECKLIST || AppDataKt.getCURRENT_SELECTED_OPTION() == OPTION_CREATE)
                    new UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute();
                else
                    new UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute();
            }
        });

        setCbListener();
        setEditBtnListener();

    }

    //Setting the edit button listeners.
    @SuppressLint("SetTextI18n")
    private void setEditBtnListener() {

        binding.editDateBtn.setOnClickListener(v -> {

            //Checking if the button is not double clicked
            if (isDoubleClicked(1000)) return;
            Calendar currentDate = Calendar.getInstance();

            //Showing the date picker dialog
            DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, month);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    details.setPartyDate(selectedDate.getTime());

                    binding.partyDate.setText(getFormattedDate(selectedDate.getTime()));

                    prefRepository.setCurrentPartyDetails(details);
                    new UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute();
                }
            };

            DatePickerDialog datePicker = new DatePickerDialog(requireContext(),
                    listener,
                    currentDate.get(Calendar.YEAR),
                    currentDate.get(Calendar.MONTH),
                    currentDate.get(Calendar.DAY_OF_MONTH));

            //Setting minimim date as current date.
            datePicker.getDatePicker().setMinDate(currentDate.getTimeInMillis());
            datePicker.show();
        });

        binding.editDestinationBtn.setOnClickListener(v -> {
            if (isDoubleClicked(1000)) return;
            Dialog dialog = new DialogDestinationSelection(requireContext(), prefRepository);
            dialog.show();

            //Checking and updating the value selected after the dialog is closed.
            dialog.setOnDismissListener(dialog1 -> {
                binding.destinationType.setText(prefRepository.getCurrentPartyDetails().getPartyDestination());
                new UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute();
                setBudget();

                details = prefRepository.getCurrentPartyDetails();

                //On changing the party type, updating the option in the checklist screen
                if (!prefRepository.getCurrentPartyDetails().getPartyDestination().equals(getString(R.string.home))) {

                    show(binding.include.parent);
                    show(binding.venueTitle);
                    show(binding.venueCb);
                    show(binding.editVenueBtn);

                    show(binding.locationSelected);
                    show(binding.textView22);
                    show(binding.editLocationBtn);

                    Venue venue = prefRepository.getCurrentPartyDetails().getSelectedDestination();


                    if (venue == null || venue.getName().isEmpty()) {
                        binding.include.venueName.setText("Click edit icon to select venue!");
                        binding.locationSelected.setText("Select location!");
                    } else {
                        setLocations();
                        setVenueDetails();
                    }
                } else {
                    setLocations();
                    setVenueDetails();
                }
            });
        });

        //Used to update the guest count of party
        binding.editGuestBtn.setOnClickListener(v -> {
            if (isDoubleClicked(1000)) return;

            Dialog dialog = new DialogChangeGuest(requireContext(), prefRepository);
            dialog.show();
            dialog.setOnDismissListener(d -> {
                binding.totalGuest.setText(prefRepository.getCurrentPartyDetails().getPartyGuest() + " Guests");
                details = prefRepository.getCurrentPartyDetails();
                setBudget();
                new UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute();
            });
        });

        //Used to edit the budget of the party
        binding.editBudgetBtn.setOnClickListener(v -> {
            if (isDoubleClicked(1000)) return;

            Dialog dialog = new DialogChangeBudget(requireContext(), prefRepository);
            dialog.show();
            dialog.setOnDismissListener(d -> {
                details = prefRepository.getCurrentPartyDetails();
                setBudget();
                new UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute();
            });
        });

        //Open the location screen on clicking the edit location button
        binding.editLocationBtn.setOnClickListener(v -> {
            if (isDoubleClicked(1000)) return;
            findNavController(this).navigate(R.id.action_finalChecklistFragment_to_venueLocationFragment);
        });

        //Used to update the selected party options
        binding.editPartyTypeBtn.setOnClickListener(v -> {
            if (isDoubleClicked(1000)) return;

            Dialog dialog = new DialogChangePartyType(requireContext(), prefRepository);
            dialog.show();
            dialog.setOnDismissListener(d -> {
                details = prefRepository.getCurrentPartyDetails();
                setSelectedPartyTypes();
                new UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute();
                setBudget();
            });
        });

        //This will open the caterer screen to change the caterers
        binding.editCatererBtn.setOnClickListener(v -> {
            if (isDoubleClicked(1000)) return;
            findNavController(this).navigate(R.id.action_finalChecklistFragment_to_caterersListFragment);
        });

        //This will open the venue screen to change the venue
        binding.editVenueBtn.setOnClickListener(v -> {
            if (isDoubleClicked(1000)) return;
            if (prefRepository.getCurrentPartyDetails().getLocations() == null || prefRepository.getCurrentPartyDetails().getLocations().isEmpty()) {
                toastShort(requireContext(), "First Select location!");
                return;
            }
            findNavController(this).navigate(R.id.action_finalChecklistFragment_to_venueListFragment);
        });

    }

    //Used to set the checkbox listeners
    private void setCbListener() {
        //Used to store the changed status of checkbox of party type
        binding.selectedPartyTypeCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ArrayList<CheckedItem> checkList = details.getCheckedItemList();

            CheckedItem checkedItem = null;
            for (CheckedItem x : checkList) {
                if (x.getItemName().equals(CB_PARTY_TYPE)) {
                    checkedItem = x;
                }
            }

            if (checkedItem != null) {
                details.getCheckedItemList().remove(checkedItem);
                details.getCheckedItemList().add(new CheckedItem(CB_PARTY_TYPE, isChecked));
            }

            prefRepository.setCurrentPartyDetails(details);
            if (AppDataKt.getCURRENT_SELECTED_OPTION() == OPTION_PAST)
                new UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute();
            else
                new UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute();
        });

        //Used to store the changed status of checkbox of caterer
        binding.catererCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ArrayList<CheckedItem> checkList = details.getCheckedItemList();

            CheckedItem checkedItem = null;
            for (CheckedItem x : checkList) {
                if (x.getItemName().equals(CB_CATERER)) {
                    checkedItem = x;
                }
            }

            if (checkedItem != null) {
                details.getCheckedItemList().remove(checkedItem);
                details.getCheckedItemList().add(new CheckedItem(CB_CATERER, isChecked));
            }

            prefRepository.setCurrentPartyDetails(details);
            if (AppDataKt.getCURRENT_SELECTED_OPTION() == OPTION_PAST)
                new UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute();
            else
                new UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute();
        });


        //Used to store the changed status of checkbox of venue
        binding.venueCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ArrayList<CheckedItem> checkList = details.getCheckedItemList();

            CheckedItem checkedItem = null;
            for (CheckedItem x : checkList) {
                if (x.getItemName().equals(CB_VENUE)) {
                    checkedItem = x;
                }
            }

            if (checkedItem != null) {
                details.getCheckedItemList().remove(checkedItem);
                details.getCheckedItemList().add(new CheckedItem(CB_VENUE, isChecked));
            }

            prefRepository.setCurrentPartyDetails(details);
            if (AppDataKt.getCURRENT_SELECTED_OPTION() == OPTION_PAST)
                new UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute();
            else
                new UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute();
        });


        //Used to store the changed status of checkbox of magic show
        binding.magicCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ArrayList<CheckedItem> checkList = details.getCheckedItemList();

            CheckedItem checkedItem = null;
            for (CheckedItem x : checkList) {
                if (x.getItemName().equals(CB_MAGIC_SHOW)) {
                    checkedItem = x;
                }
            }

            if (checkedItem != null) {
                details.getCheckedItemList().remove(checkedItem);
                details.getCheckedItemList().add(new CheckedItem(CB_MAGIC_SHOW, isChecked));
            }

            prefRepository.setCurrentPartyDetails(details);
            if (AppDataKt.getCURRENT_SELECTED_OPTION() == OPTION_PAST)
                new UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute();
            else
                new UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute();
        });


        //Used to store the changed status of checkbox of decoration
        binding.decorationCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ArrayList<CheckedItem> checkList = details.getCheckedItemList();

            CheckedItem checkedItem = null;
            for (CheckedItem x : checkList) {
                if (x.getItemName().equals(CB_DECORATOR)) {
                    checkedItem = x;
                }
            }

            if (checkedItem != null) {
                details.getCheckedItemList().remove(checkedItem);
                details.getCheckedItemList().add(new CheckedItem(CB_DECORATOR, isChecked));
            }

            prefRepository.setCurrentPartyDetails(details);
            if (AppDataKt.getCURRENT_SELECTED_OPTION() == OPTION_PAST)
                new UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute();
            else
                new UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute();
        });

        binding.alcoholCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ArrayList<CheckedItem> checkList = details.getCheckedItemList();

            CheckedItem checkedItem = null;
            for (CheckedItem x : checkList) {
                if (x.getItemName().equals(CB_ALCOHOL)) {
                    checkedItem = x;
                }
            }

            if (checkedItem != null) {
                details.getCheckedItemList().remove(checkedItem);
                details.getCheckedItemList().add(new CheckedItem(CB_ALCOHOL, isChecked));
            }

            prefRepository.setCurrentPartyDetails(details);
            if (AppDataKt.getCURRENT_SELECTED_OPTION() == OPTION_PAST)
                new UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute();
            else
                new UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute();
        });

        //Used to store the changed status of checkbox of budget
        binding.budgetCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ArrayList<CheckedItem> checkList = details.getCheckedItemList();

            CheckedItem checkedItem = null;
            for (CheckedItem x : checkList) {
                if (x.getItemName().equals(CB_BUDGET)) {
                    checkedItem = x;
                }
            }

            if (checkedItem != null) {
                details.getCheckedItemList().remove(checkedItem);
                details.getCheckedItemList().add(new CheckedItem(CB_BUDGET, isChecked));
            }

            prefRepository.setCurrentPartyDetails(details);
            if (AppDataKt.getCURRENT_SELECTED_OPTION() == OPTION_PAST)
                new UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute();
            else
                new UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute();
        });
    }


    //Used to set the location of venue and updating the UI accordingly.
    private void setLocations() {
        if (details.getPartyDestination().equals(getString(R.string.home))) {
            hide(binding.locationSelected);
            hide(binding.textView22);
            hide(binding.editLocationBtn);
        } else {
            if (details.getLocations() == null || details.getLocations().isEmpty()) {
                binding.locationSelected.setText("Select location!");
            } else {
                StringBuilder locationString = new StringBuilder();
                for (String x : details.getLocations()) {
                    locationString.append(x + ", ");
                }
                binding.locationSelected.setText(locationString.toString().substring(0, locationString.toString().length() - 2));
            }
        }
    }

    //Used to set the budget of the party after calculating
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void setBudget() {
        String budgetLimit;
        if (getString(R.string.low).equals(details.getPartyBudget())) {
            budgetLimit = "$200 - $400";
        } else if (getString(R.string.medium).equals(details.getPartyBudget())) {
            budgetLimit = "$400 - $600";
        } else if (getString(R.string.high).equals(details.getPartyBudget())) {
            budgetLimit = "$600 - $800";
        } else if (getString(R.string.very_high).equals(details.getPartyBudget())) {
            budgetLimit = "More than $800";
        } else {
            budgetLimit = "$200 - $400";
        }
        binding.budgetSelected.setText(String.format("%s (%s)", budgetLimit, details.getPartyBudget()));

        StringBuilder budgetDistributionText = new StringBuilder();

        double catererPrice = prefRepository.getCurrentPartyDetails().getSelectedCaterer().getPricePerPax() * prefRepository.getCurrentPartyDetails().getPartyGuest();
        budgetDistributionText.append(String.format("Caterer($%s), ", catererPrice));

        int venuePrice;
        if (!prefRepository.getCurrentPartyDetails().getPartyDestination().equals(getString(R.string.home))) {
            if (prefRepository.getCurrentPartyDetails().getSelectedDestination() != null) {
                budgetDistributionText.append(String.format("Venue($%d), ", Integer.parseInt(prefRepository.getCurrentPartyDetails().getSelectedDestination().getPrice())));
                venuePrice = Integer.parseInt(prefRepository.getCurrentPartyDetails().getSelectedDestination().getPrice());
            } else venuePrice = 0;
        } else
            venuePrice = 0;

        int alcoholPrice = 0;

        for (String x : prefRepository.getCurrentPartyDetails().getPartyType()) {
            if (x.equals(PARTY_TYPE_ALCOHOL)) {
                budgetDistributionText.append(String.format("Alcohol($%d), ", 10 * prefRepository.getCurrentPartyDetails().getPartyGuest()));
                alcoholPrice = 10 * prefRepository.getCurrentPartyDetails().getPartyGuest();
                break;
            }
        }

        int magicPrice = 0;
        for (String x : prefRepository.getCurrentPartyDetails().getPartyType()) {
            if (x.equals(PARTY_TYPE_MAGIC_SHOW)) {
                budgetDistributionText.append("Magic Show($200), ");
                magicPrice = 200;
                break;
            }
        }
        int decoration = 0;
        for (String x : prefRepository.getCurrentPartyDetails().getPartyType()) {
            if (x.equals(PARTY_TYPE_DECORATION)) {
                budgetDistributionText.append("Decoration($100), ");
                decoration = 100;
                break;
            }
        }

        double totalBudget = venuePrice + catererPrice + alcoholPrice + magicPrice + decoration;
        binding.partyBudget.setText(String.format("$%s", totalBudget));
        binding.budgetDistribution.setText(budgetDistributionText.toString().substring(0, budgetDistributionText.toString().length() - 2));
    }

    //Used to set the selected party type in the recycler view
    private void setSelectedPartyTypes() {
        initGridAdapter(
                binding.selectedPartyTypeRv,
                new SelectedPartyTypeAdapter(getPartyTypeFromStringArray(details.getPartyType())),
                true,
                3);
    }

    //Used to set the selected caterer detail
    @SuppressLint("SetTextI18n")
    private void setCatererDetails() {
        Caterer caterersData = details.getSelectedCaterer();
        binding.include2.name.setText(caterersData.getName().trim());
        binding.include2.price.setText("$" + caterersData.getPricePerPax());
        binding.include2.totalGuestPriceTv.setText(prefRepository.getCurrentPartyDetails().getPartyGuest() + " Pax total");
        binding.include2.totalGuestPrice.setText("$" + (caterersData.getPricePerPax() * prefRepository.getCurrentPartyDetails().getPartyGuest()));

        //This will show all the supported party type of the caterer
        ArrayList<String> partyTypes = gson.fromJson(caterersData.getPartyType(), arrayListType);
        StringBuilder partyTypeString = new StringBuilder();

        for (String x : partyTypes) {
            partyTypeString.append(x + ", ");
        }
        binding.include2.partyTypeValue.setText(partyTypeString.toString().substring(0, partyTypeString.toString().length() - 2));
        binding.include2.parent.setEnabled(false);
    }

    //Used to set the selected venue data after checking selected destination type
    @SuppressLint("SetTextI18n")
    private void setVenueDetails() {
        if (!details.getPartyDestination().equals(getString(R.string.home))) {
            binding.include.parent.setEnabled(false);
            if (details.getSelectedDestination() != null) {

                Venue venueData = details.getSelectedDestination();
                binding.include.venueName.setText(venueData.getName());
                binding.include.venueAdd.setText(venueData.getAddress());
                binding.include.capacity.setText(venueData.getCapacity() + " Guest");
                binding.include.contact.setText(venueData.getContact());
                binding.include.price.setText("$" + venueData.getPrice());
                binding.include.location.setText(venueData.getLocation());

                ArrayList<String> partyTypes = gson.fromJson(venueData.getPartyType(), arrayListType);
                StringBuilder partyTypeString = new StringBuilder();

                for (String x : partyTypes) {
                    partyTypeString.append(x + ", ");
                }
                binding.include.suitable.setText(partyTypeString.toString().substring(0, partyTypeString.toString().length() - 2));
            }
        } else {
            hide(binding.include.parent);
            hide(binding.venueTitle);
            hide(binding.venueCb);
            hide(binding.editVenueBtn);
        }
    }

    //Used to set the phone no. and other details
    //of the magic show, decoration and alcohol if they are selected
    private void setSpecialDetails() {
        for (String x : details.getPartyType()) {
            switch (x) {
                case PARTY_TYPE_ALCOHOL:
                    show(binding.alcoholDetails);
                    break;
                case PARTY_TYPE_MAGIC_SHOW:
                    show(binding.magicianDetails);
                    break;
                case PARTY_TYPE_DECORATION:
                    show(binding.decoratorDetails);
                    break;
            }
        }

    }

    //Used for showing the edit icon in the toolbar
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (AppDataKt.getCURRENT_SELECTED_OPTION() != OPTION_PAST) {
            requireActivity().getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        }
    }

    //Toolbar edit icon click listener
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        //Changing the edit and save icon on clicking
        if (item.getItemId() == R.id.edit) {
            if (binding.groupEditBtn.getVisibility() == View.VISIBLE) {
                item.setIcon(getDrawableRes(requireContext(), R.drawable.ic_edit));
                hide(binding.groupEditBtn);
                if (!details.getPartyDestination().equals(getString(R.string.home))) {
                    hide(binding.editLocationBtn);
                    hide(binding.editVenueBtn);
                }
            } else {
                item.setIcon(getDrawableRes(requireContext(), R.drawable.ic_save));
                show(binding.groupEditBtn);
                if (!details.getPartyDestination().equals(getString(R.string.home))) {
                    show(binding.editLocationBtn);
                    show(binding.editVenueBtn);
                }
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    //Used to store the summary data in the SQL in background thread
    private class UpdateSummaryAsyncTask extends AsyncTask<Void, Void, Void> {
        private final Activity context;
        private final PrefRepository prefRepository;

        public UpdateSummaryAsyncTask(@NotNull Activity context, @NotNull PrefRepository prefRepository) {
            super();
            this.context = context;
            this.prefRepository = prefRepository;
        }

        @Nullable
        @Override
        protected Void doInBackground(@NotNull Void... params) {
            SummaryDetailsDao summaryDao = (new Companion()).getDatabase(context).summaryDao();
            summaryDao.delete(prefRepository.getCurrentUser().getUid());
            summaryDao.insert(AppDataKt.convertSummary(prefRepository.getCurrentPartyDetails(), prefRepository.getCurrentUser().getUid()));
            return null;
        }
    }

    //Used to store the summary data in the SQL in background thread
    private class GetSummaryAsyncTask extends AsyncTask<Void, Void, List<SummaryDetails>> {
        private final Activity context;
        private final PrefRepository prefRepository;
        @NotNull
        private final GetSummaryListener taskListener;

        public GetSummaryAsyncTask(@NotNull Activity context, @NotNull PrefRepository prefRepository, @NotNull GetSummaryListener taskListener) {
            super();
            this.context = context;
            this.prefRepository = prefRepository;
            this.taskListener = taskListener;
        }

        @Nullable
        @Override
        protected List<SummaryDetails> doInBackground(@NotNull Void... params) {
            SummaryDetailsDao summaryDao = (new Companion()).getDatabase(context).summaryDao();
            return summaryDao.getUserSummary(prefRepository.getCurrentUser().getUid());
        }

        @Override
        protected void onPostExecute(@Nullable List<SummaryDetails> result) {
            super.onPostExecute(result);
            taskListener.onTaskEnded(result);
        }
    }

    //Used to delete the unfinished data from the SQL in background thread
    private class DeleteUnfinishedSummaryAsyncTask extends AsyncTask<Void, Void, Void> {
        private final Activity context;
        private final PrefRepository prefRepository;

        public DeleteUnfinishedSummaryAsyncTask(@NotNull Activity context, @NotNull PrefRepository prefRepository) {
            super();
            this.context = context;
            this.prefRepository = prefRepository;
        }

        @Nullable
        @Override
        protected Void doInBackground(@NotNull Void... params) {
            UnfinishedDetailsDao unfinishedDao = (new Companion()).getDatabase(context).unfinishedDao();
            unfinishedDao.delete(prefRepository.getCurrentUser().getUid());
            return null;
        }
    }

    //Used to store the history summary data in the SQL in background thread
    private class UpdateHistorySummaryAsyncTask extends AsyncTask<Void, Void, Void> {
        private final Activity context;
        private final PrefRepository prefRepository;

        public UpdateHistorySummaryAsyncTask(@NotNull Activity context, @NotNull PrefRepository prefRepository) {
            super();
            this.context = context;
            this.prefRepository = prefRepository;
        }

        @Nullable
        @Override
        protected Void doInBackground(@NotNull Void... params) {
            HistorySummaryDao summaryDao = (new Companion()).getDatabase(this.context).historySummaryDao();
            if (prefRepository.getCurrentPartyDetails().getId() == null) {
                PartyDetails details = prefRepository.getCurrentPartyDetails();
                details.setId(AppDataKt.getRandom());
                summaryDao.insert(AppDataKt.convertHistorySummary(details, prefRepository.getCurrentUser().getUid()));
            } else {
                summaryDao.delete(prefRepository.getCurrentPartyDetails().getId());
                summaryDao.insert(AppDataKt.convertHistorySummary(prefRepository.getCurrentPartyDetails(), prefRepository.getCurrentUser().getUid()));
            }
            return null;
        }
    }
}
