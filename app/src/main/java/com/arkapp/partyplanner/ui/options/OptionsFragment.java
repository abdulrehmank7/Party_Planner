package com.arkapp.partyplanner.ui.options;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.navigation.ActivityKt;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.R.id;
import com.arkapp.partyplanner.data.models.SummaryDetails;
import com.arkapp.partyplanner.data.models.UnfinishedDetails;
import com.arkapp.partyplanner.data.models.UserLogin;
import com.arkapp.partyplanner.data.repository.PrefRepository;
import com.arkapp.partyplanner.data.room.AppDatabase.Companion;
import com.arkapp.partyplanner.data.room.SummaryDetailsDao;
import com.arkapp.partyplanner.data.room.UnfinishedDetailsDao;
import com.arkapp.partyplanner.data.room.UserLoginDao;
import com.arkapp.partyplanner.utils.AppDataKt;
import com.arkapp.partyplanner.utils.ViewUtilsKt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static androidx.navigation.fragment.FragmentKt.findNavController;
import static com.arkapp.partyplanner.utils.AppDataKt.OPTION_CHECKLIST;
import static com.arkapp.partyplanner.utils.AppDataKt.OPTION_CREATE;
import static com.arkapp.partyplanner.utils.AppDataKt.OPTION_PAST;
import static com.arkapp.partyplanner.utils.AppDataKt.OPTION_UNFINISHED;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.showAlertDialog;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.toastShort;


public class OptionsFragment extends Fragment {
    private PrefRepository prefRepository;


    public OptionsFragment() {
        super(R.layout.fragment_options);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefRepository = new PrefRepository(requireContext());

        AppDataKt.setOPENED_GUEST_LIST(false);
        addUserName();

        getView().findViewById(id.createChecklistBtn).setOnClickListener(it -> {
            AppDataKt.setCURRENT_SELECTED_OPTION(OPTION_CREATE);
            findNavController(this).navigate(R.id.action_optionsFragment_to_homeFragment);
        });

        getView().findViewById(id.unfinishedChecklistBtn).setOnClickListener(it -> {
            toastShort(requireContext(), "Fetching data...");
            new GetUnfinishedSummaryAsyncTask(requireActivity(), prefRepository).execute();
        });

        getView().findViewById(id.checklistBtn).setOnClickListener(it -> {
            toastShort(requireContext(), "Fetching data...");
            new SummaryAsyncTask(requireActivity(), prefRepository).execute();
        });


        getView().findViewById(id.logoutBtn).setOnClickListener(it ->
                showAlertDialog(requireContext(), "Logout", "Do you want to logout?", "Logout", "Cancel", (DialogInterface.OnClickListener) (new DialogInterface.OnClickListener() {
                    public final void onClick(DialogInterface dialog, int $noName_1) {
                        prefRepository.clearData();
                        findNavController(OptionsFragment.this).navigate(id.action_optionsFragment_to_splashFragment);
                        toastShort(requireContext(), "Logged Out!");
                        dialog.dismiss();
                    }
                })));

        getView().findViewById(id.pastChecklistBtn).setOnClickListener(it -> {
            AppDataKt.setCURRENT_SELECTED_OPTION(OPTION_PAST);
            findNavController(OptionsFragment.this).navigate(R.id.action_optionsFragment_to_historySummaryFragment);
        });
    }

    private void addUserName() {
        if (AppDataKt.getENTERED_USER_NAME().length() > 0)
            new CheckLoggedInUserAsyncTask(requireActivity(), prefRepository).execute();

    }

    private class CheckLoggedInUserAsyncTask extends AsyncTask<Void, Void, List<UserLogin>> {
        private final Activity context;
        private final PrefRepository prefRepository;

        public CheckLoggedInUserAsyncTask(@NotNull Activity context, @NotNull PrefRepository prefRepository) {
            super();
            this.context = context;
            this.prefRepository = prefRepository;
        }

        @Nullable
        @Override
        protected List<UserLogin> doInBackground(@NotNull Void... params) {
            UserLoginDao userLoginDao = (new Companion()).getDatabase(context).userLoginDao();
            return userLoginDao.checkLoggedInUser(AppDataKt.getENTERED_USER_NAME());
        }

        @SuppressLint({"SetTextI18n"})
        @Override
        protected void onPostExecute(@Nullable List<UserLogin> data) {
            prefRepository.setCurrentUser(data.get(0));
        }

    }

    private class SummaryAsyncTask extends AsyncTask<Void, Void, List<SummaryDetails>> {
        private final Activity context;
        private final PrefRepository prefRepository;

        public SummaryAsyncTask(@NotNull Activity context, @NotNull PrefRepository prefRepository) {
            this.context = context;
            this.prefRepository = prefRepository;
        }

        @Nullable
        @Override
        protected List<SummaryDetails> doInBackground(@NotNull Void... params) {
            SummaryDetailsDao summaryDetailsDao = (new Companion()).getDatabase(context).summaryDao();
            return summaryDetailsDao.getUserSummary(prefRepository.getCurrentUser().getUid());
        }

        @SuppressLint({"SetTextI18n"})
        @Override
        protected void onPostExecute(@Nullable List<SummaryDetails> summaryData) {

            if (summaryData != null && !summaryData.isEmpty()) {
                AppDataKt.setCURRENT_SELECTED_OPTION(OPTION_CHECKLIST);
                ActivityKt.findNavController(context, R.id.fragment).navigate((R.id.action_optionsFragment_to_finalChecklistFragment));
            } else
                ViewUtilsKt.toast(context, "No checklist found!. Please create a new checklist.");

        }
    }

    private class GetUnfinishedSummaryAsyncTask extends AsyncTask<Void, Void, List<UnfinishedDetails>> {
        private final Activity context;
        private final PrefRepository prefRepository;

        public GetUnfinishedSummaryAsyncTask(@NotNull Activity context, @NotNull PrefRepository prefRepository) {
            super();
            this.context = context;
            this.prefRepository = prefRepository;
        }

        @Nullable
        @Override
        protected List<UnfinishedDetails> doInBackground(@NotNull Void... params) {
            UnfinishedDetailsDao unfinishedDetailsDao = (new Companion()).getDatabase(context).unfinishedDao();
            return unfinishedDetailsDao.getUserUnfinished(prefRepository.getCurrentUser().getUid());
        }

        @SuppressLint({"SetTextI18n"})
        @Override
        protected void onPostExecute(@Nullable List<UnfinishedDetails> unfinishedSummary) {
            if (unfinishedSummary != null && !unfinishedSummary.isEmpty()) {
                AppDataKt.setCURRENT_SELECTED_OPTION(OPTION_UNFINISHED);
                ActivityKt.findNavController(context, R.id.fragment).navigate(R.id.action_optionsFragment_to_homeFragment);
            } else
                ViewUtilsKt.toast(context, "No unfinished data found!. Please create a new checklist.");

        }
    }
}
