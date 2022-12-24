package com.nicoovillarr.promise;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.nicoovillarr.promise.dialogs.CreateEditGoalDialog;
import com.nicoovillarr.promise.models.Goal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ProgressBar yearProgress;
    private PieChart pieChart;
    private RecyclerView goalsRecyclerView;
    private TextView noGoalsMsg;

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    final protected void customActivityInit() {
        this.setToolbarTitle("Hi, nicoovillarr");

        this.yearProgress = findViewById(R.id.year_progress);
        this.yearProgress.setProgress(80);

        this.pieChart = findViewById(R.id.pie_chart);
        this.loadPieChartData();

        this.goalsRecyclerView = findViewById(R.id.goalsRecyclerView);
        this.loadGoals();

        this.noGoalsMsg = findViewById(R.id.noGoalsMsg);
        this.updateNoGoalMsgVisibility();
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
            dialog
                    .setOnSaveListener(newGoal -> adapter.updateGoal(i, newGoal))
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

}