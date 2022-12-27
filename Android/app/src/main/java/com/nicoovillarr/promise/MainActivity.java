package com.nicoovillarr.promise;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.nicoovillarr.promise.adapters.GoalsAdapter;
import com.nicoovillarr.promise.databinding.ActivityMainBinding;
import com.nicoovillarr.promise.dialogs.CreateEditGoalDialog;
import com.nicoovillarr.promise.models.Goal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {

    private ViewModel viewModel;

    private PieChart pieChart;
    private RecyclerView goalsRecyclerView;
    private TextView noGoalsMsg;

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    final protected void customActivityInit() {
        this.viewModel = new ViewModel();
        this.getBinding().setViewModel(this.viewModel);
        this.getBinding().setLifecycleOwner(this);

        this.setToolbarTitle("Hi, nicoovillarr");

        float yearProgress = (float) LocalDate.now().getDayOfYear() / 365;
        this.viewModel.setYearProgress(Math.round(yearProgress * 100));

        this.pieChart = findViewById(R.id.pie_chart);
        this.loadPieChartData();

        this.goalsRecyclerView = findViewById(R.id.goalsRecyclerView);
        this.loadGoals();

        this.noGoalsMsg = findViewById(R.id.noGoalsMsg);
        this.updateNoGoalMsgVisibility();

        Button prevYearBtn = findViewById(R.id.prevYearBtn);
        prevYearBtn.setOnClickListener(v -> {
            this.viewModel.decreaseYear();
        });

        Button nextYearBtn = findViewById(R.id.nextYearBtn);
        nextYearBtn.setOnClickListener(v -> {
            this.viewModel.increaseYear();
        });
    }
    
    @Override
    final protected void onFabClick() {
        CreateEditGoalDialog dialog = new CreateEditGoalDialog(this, null);
        dialog.setOnSaveListener(f -> {
            GoalsAdapter adapter = (GoalsAdapter) this.goalsRecyclerView.getAdapter();
            assert adapter != null;
            adapter.addGoal(f);
            this.updateNoGoalMsgVisibility();
        }).show();
    }

    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(0.6f, "Completed"));
        entries.add(new PieEntry(0.1f, "In Progress"));
        entries.add(new PieEntry(0.3f, "Incomplete"));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Goals");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(false);

        pieChart.setData(data);
        pieChart.invalidate();

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);

        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setTouchEnabled(false);
    }

    private void loadGoals() {
        List<Goal> goals = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        this.goalsRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.goalsRecyclerView.getContext(), layoutManager.getOrientation());
        this.goalsRecyclerView.addItemDecoration(dividerItemDecoration);

        GoalsAdapter adapter = new GoalsAdapter(this, goals);
        this.goalsRecyclerView.setAdapter(adapter);
        adapter.setOnRowClick((i, old) -> {
            CreateEditGoalDialog dialog = new CreateEditGoalDialog(this, old);
            dialog.setOnSaveListener(newGoal -> adapter.updateGoal(i, newGoal))
                    .setOnDeleteListener(() -> {
                        adapter.deleteGoal(i);
                        this.updateNoGoalMsgVisibility();
                    })
                    .show();
        });
    }

    private void updateNoGoalMsgVisibility() {
        GoalsAdapter adapter = (GoalsAdapter) this.goalsRecyclerView.getAdapter();
        assert adapter != null;
        this.noGoalsMsg.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public static class ViewModel extends BaseObservable {

        private String yearTitle;
        private String goalsTitle;
        private Integer currentYear;
        private Integer yearProgress;
        private final HashMap<Integer, List<Goal>> years;

        public ViewModel() {
            this.currentYear = LocalDateTime.now().getYear();
            this.years = new HashMap<>();
            this.setGoalsTitle();
            this.setYearTitle();
        }

        public HashMap<Integer, List<Goal>> getYears() {
            return years;
        }

        @Bindable()
        public Integer getCurrentYear() {
            return currentYear;
        }

        public void increaseYear() {
            this.currentYear++;
            this.setGoalsTitle();
            this.notifyPropertyChanged(BR.currentYear);
        }

        public void decreaseYear() {
            this.currentYear--;
            this.setGoalsTitle();
            this.notifyPropertyChanged(BR.currentYear);
        }

        public void setYearTitle() {
            this.yearTitle = utils.getDateString(LocalDate.now());
            this.notifyPropertyChanged(BR.yearTitle);
        }

        @Bindable()
        public String getYearTitle() {
            return yearTitle;
        }

        @SuppressLint("DefaultLocale")
        private void setGoalsTitle() {
            this.goalsTitle = String.format("Your promises for %d...", this.currentYear);
            this.notifyPropertyChanged(BR.goalsTitle);
        }

        @Bindable()
        public String getGoalsTitle() {
            return this.goalsTitle;
        }

        @Bindable()
        public Integer getYearProgress() {
            return yearProgress;
        }

        public void setYearProgress(Integer yearProgress) {
            this.yearProgress = yearProgress;
        }

        public boolean addYearGoal(Integer year, Goal g) {
            if (!this.years.containsKey(year)) {
                this.years.put(year, new ArrayList<>());
            }

            return Objects.requireNonNull(this.years.get(year)).add(g);
        }
    }

}