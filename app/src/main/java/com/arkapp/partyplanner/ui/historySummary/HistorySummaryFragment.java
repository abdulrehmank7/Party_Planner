package com.arkapp.partyplanner.ui.historySummary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.ActivityKt;
import androidx.recyclerview.widget.RecyclerView;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.R.id;
import com.arkapp.partyplanner.data.models.HistorySummary;
import com.arkapp.partyplanner.data.repository.PrefRepository;
import com.arkapp.partyplanner.data.room.AppDatabase.Companion;
import com.arkapp.partyplanner.data.room.HistorySummaryDao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.arkapp.partyplanner.utils.ViewUtilsKt.initVerticalAdapter;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.show;


public class HistorySummaryFragment extends Fragment {
    private PrefRepository prefRepository;

    public HistorySummaryFragment() {
        super(R.layout.fragment_history_summary);
    }

    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prefRepository = new PrefRepository(requireContext());

        new HistorySummaryFragment.HistorySummaryAsyncTask(requireActivity(), prefRepository).execute();
    }

    private class HistorySummaryAsyncTask extends AsyncTask<Void, Void, List<HistorySummary>> {
        private final Activity context;
        private final PrefRepository prefRepository;

        public HistorySummaryAsyncTask(@NotNull Activity context, @NotNull PrefRepository prefRepository) {
            super();
            this.context = context;
            this.prefRepository = prefRepository;
        }

        @Nullable
        @Override
        protected List<HistorySummary> doInBackground(@NotNull Void... params) {
            HistorySummaryDao historySummaryDao = (new Companion()).getDatabase(context).historySummaryDao();
            return historySummaryDao.getHistorySummary(prefRepository.getCurrentUser().getUid());
        }


        @SuppressLint({"SetTextI18n"})
        @Override
        protected void onPostExecute(@Nullable List<HistorySummary> summaryList) {

            if (!summaryList.isEmpty()) {
                HistorySummaryListAdapter adapter = new HistorySummaryListAdapter(summaryList, ActivityKt.findNavController(context, R.id.fragment), this.prefRepository);
                RecyclerView rv = context.findViewById(id.rvHistorySummary);
                initVerticalAdapter(rv, adapter, true);
            } else {
                TextView noHistory = context.findViewById(id.noHistoryTv);
                show(noHistory);
            }
        }
    }
}
