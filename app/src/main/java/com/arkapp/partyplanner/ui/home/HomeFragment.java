// HomeFragment.java
package com.arkapp.partyplanner.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.data.models.PartyDetails;
import com.arkapp.partyplanner.data.repository.PrefRepository;
import com.arkapp.partyplanner.data.room.AppDatabase.Companion;
import com.arkapp.partyplanner.data.room.UnfinishedDetailsDao;
import com.arkapp.partyplanner.databinding.FragmentHomeBinding;
import com.arkapp.partyplanner.utils.AddUnfinishedAsyncTask;
import com.arkapp.partyplanner.utils.AppDataKt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kotlin.jvm.internal.Intrinsics;

import static androidx.navigation.fragment.NavHostFragment.findNavController;
import static com.arkapp.partyplanner.utils.AppDataKt.OPTION_UNFINISHED;
import static com.arkapp.partyplanner.utils.AppDataKt.getPartyTypeFromStringArray;
import static com.arkapp.partyplanner.utils.AppDataKt.getPartyTypes;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.getDrawableRes;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.initGridAdapter;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.toastShort;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private PartyTypeAdapter partyTypeAdapter;
    private PrefRepository prefRepository;

    @Nullable
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        prefRepository = new PrefRepository(requireContext());

        return binding.getRoot();
    }

    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        super.onViewCreated(view, savedInstanceState);

        resetPartyData();
        initPartyTypeUI();
        initBudgetBtnListener();
        initDestinationBtnListener();
        initCalendar();

        //Used to store the guest count
        binding.guestEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    binding.guest.setError(null);
                    PartyDetails details = prefRepository.getCurrentPartyDetails();
                    details.setPartyGuest(Integer.parseInt(s.toString()));
                    prefRepository.setCurrentPartyDetails(details);
                }
            }
        });

        //Validating the screen input on button click
        binding.proceedBtn.setOnClickListener(v -> {
            if (binding.guestEt.getText().toString().isEmpty() ||
                    Integer.parseInt(binding.guestEt.getText().toString()) <= 0) {
                binding.guest.setError("Please enter guest count!");
                return;
            }

            if (prefRepository.getCurrentPartyDetails().getPartyType().isEmpty()) {
                toastShort(requireContext(), "Please select party type");
                return;
            }

            binding.guest.setError(null);

            new AddUnfinishedAsyncTask(requireActivity(), prefRepository).execute();
            findNavController(this).navigate(R.id.action_homeFragment_to_caterersListFragment);
        });

        if (AppDataKt.getCURRENT_SELECTED_OPTION() == OPTION_UNFINISHED)
            setUnfinishedPartyData();
        else
            setLastEnteredData();

        //Handling back click
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
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
                requireActivity().onBackPressed();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    //Setting the Unfinished party data in SQL
    private void setUnfinishedPartyData() {
        GetUnfinishedSummaryListener taskListener = (unfinishedDetail -> {
            Date date = unfinishedDetail.getPartyDate();
            if (date != null) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.setTime(date);
                binding.calendarView.setDate(selectedDate.getTimeInMillis());
            }

            String budget = unfinishedDetail.getPartyBudget();
            if (budget != null) {
                if (getString(R.string.low).equals(budget)) {
                    binding.lowBudget.performClick();
                } else if (getString(R.string.medium).equals(budget)) {
                    binding.mediumBudget.performClick();
                } else if (getString(R.string.high).equals(budget)) {
                    binding.highBudget.performClick();
                } else if (getString(R.string.very_high).equals(budget)) {
                    binding.veryHighBudget.performClick();
                } else {
                    binding.lowBudget.performClick();
                }
            }

            String destination = unfinishedDetail.getPartyDestination();
            if (destination != null) {
                if (destination.equals(getString(R.string.home))) {
                    binding.homeParty.performClick();
                } else
                    binding.venueParty.performClick();
            }

            Integer guest = unfinishedDetail.getPartyGuest();
            if (guest != null) {
                binding.guestEt.setText(guest.toString());
            }

            ArrayList<String> type = unfinishedDetail.getPartyType();
            partyTypeAdapter.selectedPartyType = getPartyTypeFromStringArray(type);
            partyTypeAdapter.notifyDataSetChanged();
            PartyDetails details = prefRepository.getCurrentPartyDetails();
            details.setPartyType(type);
            prefRepository.setCurrentPartyDetails(details);

        });
        new GetUnfinishedSummaryAsyncTask(requireActivity(), prefRepository, taskListener).execute();
    }

    //Initializing the calendar UI
    private void initCalendar() {
        binding.calendarView.setMinDate(Calendar.getInstance().getTimeInMillis());

        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {

            Calendar selectedPartyDate = Calendar.getInstance();
            selectedPartyDate.set(Calendar.YEAR, year);
            selectedPartyDate.set(Calendar.MONTH, month);
            selectedPartyDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            PartyDetails details = prefRepository.getCurrentPartyDetails();
            details.setPartyDate(selectedPartyDate.getTime());
            prefRepository.setCurrentPartyDetails(details);
        });
    }

    private void initBudgetBtnListener() {
        binding.lowBudget.setOnClickListener(v -> {
            binding.budget.setText(getString(R.string.low));

            binding.lowBudget.setBackground(getDrawableRes(requireContext(), R.drawable.bg_selected_start));
            binding.mediumBudget.setBackground(getDrawableRes(requireContext(), R.drawable.bg_unselected));
            binding.highBudget.setBackground(getDrawableRes(requireContext(), R.drawable.bg_unselected));
            binding.veryHighBudget.setBackground(getDrawableRes(requireContext(), R.drawable.bg_unselected_end));

            PartyDetails details = prefRepository.getCurrentPartyDetails();
            details.setPartyBudget(getString(R.string.low));
            prefRepository.setCurrentPartyDetails(details);
        });

        binding.mediumBudget.setOnClickListener(v -> {
            binding.budget.setText(getString(R.string.medium));

            binding.lowBudget.setBackground(getDrawableRes(requireContext(), R.drawable.bg_unselected_start));
            binding.mediumBudget.setBackground(getDrawableRes(requireContext(), R.drawable.bg_selected));
            binding.highBudget.setBackground(getDrawableRes(requireContext(), R.drawable.bg_unselected));
            binding.veryHighBudget.setBackground(getDrawableRes(requireContext(), R.drawable.bg_unselected_end));

            PartyDetails details = prefRepository.getCurrentPartyDetails();
            details.setPartyBudget(getString(R.string.medium));
            prefRepository.setCurrentPartyDetails(details);
        });

        binding.highBudget.setOnClickListener(v -> {
            binding.budget.setText(getString(R.string.high));

            binding.lowBudget.setBackground(getDrawableRes(requireContext(), R.drawable.bg_unselected_start));
            binding.mediumBudget.setBackground(getDrawableRes(requireContext(), R.drawable.bg_unselected));
            binding.highBudget.setBackground(getDrawableRes(requireContext(), R.drawable.bg_selected));
            binding.veryHighBudget.setBackground(getDrawableRes(requireContext(), R.drawable.bg_unselected_end));

            PartyDetails details = prefRepository.getCurrentPartyDetails();
            details.setPartyBudget(getString(R.string.high));
            prefRepository.setCurrentPartyDetails(details);
        });

        binding.veryHighBudget.setOnClickListener(v -> {
            binding.budget.setText(getString(R.string.very_high));

            binding.lowBudget.setBackground(getDrawableRes(requireContext(), R.drawable.bg_unselected_start));
            binding.mediumBudget.setBackground(getDrawableRes(requireContext(), R.drawable.bg_unselected));
            binding.highBudget.setBackground(getDrawableRes(requireContext(), R.drawable.bg_unselected));
            binding.veryHighBudget.setBackground(getDrawableRes(requireContext(), R.drawable.bg_selected_end));

            PartyDetails details = prefRepository.getCurrentPartyDetails();
            details.setPartyBudget(getString(R.string.very_high));
            prefRepository.setCurrentPartyDetails(details);
        });
    }

    //Initializing the Destination selection UI
    private void initDestinationBtnListener() {
        binding.homeParty.setOnClickListener(v -> {
            binding.homeParty.setBackground(getDrawableRes(requireContext(), R.drawable.bg_selected_start));
            binding.venueParty.setBackground(getDrawableRes(requireContext(), R.drawable.bg_unselected_end));

            PartyDetails details = prefRepository.getCurrentPartyDetails();
            details.setPartyDestination(getString(R.string.home));
            prefRepository.setCurrentPartyDetails(details);
        });

        binding.venueParty.setOnClickListener(v -> {
            binding.homeParty.setBackground(getDrawableRes(requireContext(), R.drawable.bg_unselected_start));
            binding.venueParty.setBackground(getDrawableRes(requireContext(), R.drawable.bg_selected_end));

            PartyDetails details = prefRepository.getCurrentPartyDetails();
            details.setPartyDestination(getString(R.string.other_venue));
            prefRepository.setCurrentPartyDetails(details);
        });
    }

    //Set the last entered data
    private void setLastEnteredData() {
        PartyDetails detail = prefRepository.getCurrentPartyDetails();

        Date date = detail.getPartyDate();

        if (date != null) {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.setTime(date);
            binding.calendarView.setDate(selectedDate.getTimeInMillis());

        }

        String budget = detail.getPartyBudget();
        if (budget != null) {
            if (getString(R.string.low).equals(budget)) {
                binding.lowBudget.performClick();
            } else if (getString(R.string.medium).equals(budget)) {
                binding.mediumBudget.performClick();
            } else if (getString(R.string.high).equals(budget)) {
                binding.highBudget.performClick();
            } else if (getString(R.string.very_high).equals(budget)) {
                binding.veryHighBudget.performClick();
            } else {
                binding.lowBudget.performClick();
            }
        }

        String destination = detail.getPartyDestination();
        if (destination != null) {
            if (destination.equals(getString(R.string.home))) {
                binding.homeParty.performClick();
            } else
                binding.venueParty.performClick();
        }

        Integer guest = detail.getPartyGuest();
        if (guest != null) {
            binding.guestEt.setText(guest.toString());
        }

        ArrayList<String> type = detail.getPartyType();
        if (type != null) {
            partyTypeAdapter.selectedPartyType = getPartyTypeFromStringArray(type);
            partyTypeAdapter.notifyDataSetChanged();
        }
    }

    private void initPartyTypeUI() {
        partyTypeAdapter = new PartyTypeAdapter(getPartyTypes(), prefRepository);
        initGridAdapter(binding.partyTypeRv, partyTypeAdapter, true, 3);
    }

    private void resetPartyData() {
        PartyDetails details = new PartyDetails(
                null,
                Calendar.getInstance().getTime(),
                getString(R.string.low),
                getString(R.string.home),
                null,
                new ArrayList<String>(),
                null,
                null,
                null,
                null,
                null,
                new ArrayList<String>()
        );
        prefRepository.setCurrentPartyDetails(details);
    }


    private class GetUnfinishedSummaryAsyncTask extends AsyncTask<Void, Void, PartyDetails> {
        private final Activity context;
        private final PrefRepository prefRepository;
        private final GetUnfinishedSummaryListener taskListener;

        public GetUnfinishedSummaryAsyncTask(@NotNull Activity context, @NotNull PrefRepository prefRepository, @NotNull GetUnfinishedSummaryListener taskListener) {
            super();
            this.context = context;
            this.prefRepository = prefRepository;
            this.taskListener = taskListener;
        }

        @Nullable
        @Override
        protected PartyDetails doInBackground(@NotNull Void... params) {
            UnfinishedDetailsDao unfinishedDao = (new Companion()).getDatabase(context).unfinishedDao();
            return AppDataKt.convertPartyFromUnfinished(unfinishedDao.getUserUnfinished(prefRepository.getCurrentUser().getUid()).get(0));
        }

        @SuppressLint({"SetTextI18n"})
        @Override
        protected void onPostExecute(@Nullable PartyDetails unfinishedSummary) {
            this.taskListener.onTaskEnded(unfinishedSummary);
        }
    }
}

