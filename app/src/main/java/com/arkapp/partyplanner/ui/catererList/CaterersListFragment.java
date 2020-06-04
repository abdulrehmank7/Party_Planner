package com.arkapp.partyplanner.ui.catererList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.ActivityKt;
import androidx.recyclerview.widget.RecyclerView;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.data.models.Caterer;
import com.arkapp.partyplanner.data.repository.PrefRepository;
import com.arkapp.partyplanner.data.room.AppDatabase.Companion;
import com.arkapp.partyplanner.data.room.CatererDao;
import com.arkapp.partyplanner.utils.ViewUtilsKt;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.arkapp.partyplanner.utils.AppDataKt.HIGH_BUDGED_LIMIT;
import static com.arkapp.partyplanner.utils.AppDataKt.LOW_BUDGED_LIMIT;
import static com.arkapp.partyplanner.utils.AppDataKt.MEDIUM_BUDGED_LIMIT;
import static com.arkapp.partyplanner.utils.AppDataKt.VERY_HIGH_BUDGED_LIMIT;

public class CaterersListFragment extends Fragment {
    private PrefRepository prefRepository;

    public CaterersListFragment() {
        super(R.layout.fragment_venue_list);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewUtilsKt.hideKeyboard(requireActivity());
        prefRepository = new PrefRepository(requireContext());

        //Budget list is identified here
        double budgetLimit = 0.0;
        String partyBudget = prefRepository.getCurrentPartyDetails().getPartyBudget();
        if (getString(R.string.low).equals(partyBudget)) {
            budgetLimit = LOW_BUDGED_LIMIT;
        } else if (getString(R.string.medium).equals(partyBudget)) {
            budgetLimit = MEDIUM_BUDGED_LIMIT;
        } else if (getString(R.string.high).equals(partyBudget)) {
            budgetLimit = HIGH_BUDGED_LIMIT;
        } else if (getString(R.string.very_high).equals(partyBudget)) {
            budgetLimit = VERY_HIGH_BUDGED_LIMIT;
        }

        //Fetching the caterer data from SQL in background thread
        new CatererAsyncTask(requireActivity(), prefRepository, budgetLimit).execute();
    }


    private static final class CatererAsyncTask extends AsyncTask<Void, Void, List<Caterer>> {
        private final Activity context;
        private final PrefRepository prefRepository;
        private final double budgetLimit;

        public CatererAsyncTask(@NotNull Activity context, @NotNull PrefRepository prefRepository, double budgetLimit) {
            super();
            this.context = context;
            this.prefRepository = prefRepository;
            this.budgetLimit = budgetLimit;
        }

        @Nullable
        @Override
        protected List<Caterer> doInBackground(@NotNull Void... params) {
            CatererDao catererDao = (new Companion()).getDatabase((Context) this.context).catererDao();
            return catererDao.getAllCaterersInBudget(budgetLimit, prefRepository.getCurrentPartyDetails().getPartyGuest());
        }

        @SuppressLint({"SetTextI18n"})
        @Override
        protected void onPostExecute(@Nullable List<Caterer> catererList) {
            //Filtering the caterer after getting the caterer data

            ArrayList<String> selectedPartyType = prefRepository.getCurrentPartyDetails().getPartyType();
            ArrayList<Caterer> filteredCatererList = new ArrayList<>();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            Gson gson = new Gson();

            for (Caterer caterer : catererList) {
                ArrayList<String> catererPartyType = gson.fromJson(caterer.getPartyType(), type);
                for (String partyType : catererPartyType) {
                    if (selectedPartyType.contains(partyType)) {
                        filteredCatererList.add(caterer);
                        break;
                    }
                }
            }

            if (filteredCatererList.isEmpty()) {
                TextView noItemTv = context.findViewById(R.id.noItemFound);
                noItemTv.setText("No caterer found with the selection.\nChange selection and try again");
                ViewUtilsKt.show(noItemTv);
                return;
            }

            //Setting the caterer using Recycler view in the fragment.
            CaterersListAdapter adapter = new CaterersListAdapter(context, filteredCatererList, ActivityKt.findNavController(context, R.id.fragment), prefRepository);
            RecyclerView rv = (RecyclerView) context.findViewById(R.id.venueListRv);
            ViewUtilsKt.initVerticalAdapter(rv, adapter, true);
        }
    }
}
