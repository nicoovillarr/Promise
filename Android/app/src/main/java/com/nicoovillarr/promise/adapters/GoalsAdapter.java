package com.nicoovillarr.promise.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nicoovillarr.promise.R;
import com.nicoovillarr.promise.models.Goal;

import java.util.List;
import java.util.function.BiConsumer;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {

    private final Context ctx;
    private final List<Goal> goals;
    private BiConsumer<Integer, Goal> onRowClick = null;

    public GoalsAdapter(Context ctx, List<Goal> goals) {
        this.ctx = ctx;
        this.goals = goals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.ctx).inflate(R.layout.layout_goal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Goal goal = this.goals.get(position);
        holder.setIsDone(goal.getCompletedOn() != null);
        holder.setGoalValue(goal.getName());

        if (this.onRowClick != null) {
            holder.setOnNameClickListener(() -> this.onRowClick.accept(position, goal));
        }
    }

    @Override
    public int getItemCount() {
        return this.goals.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addGoal(Goal g) {
        this.goals.add(g);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateGoal(int position, Goal g) {
        this.goals.set(position, g);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void deleteGoal(int position) {
        this.goals.remove(position);
        notifyDataSetChanged();
    }

    public void setOnRowClick(BiConsumer<Integer, Goal> onRowClick) {
        this.onRowClick = onRowClick;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox isDone;
        private final TextView goalValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.isDone = itemView.findViewById(R.id.isDone);
            this.goalValue = itemView.findViewById(R.id.goalValue);
        }

        public void setIsDone(boolean done) {
            this.isDone.setChecked(done);
        }

        public void setGoalValue(String value) {
            this.goalValue.setText(value);
        }

        public void setOnNameClickListener(Runnable onNameClickListener) {
            this.goalValue.setOnClickListener(v -> onNameClickListener.run());
        }

    }

}
