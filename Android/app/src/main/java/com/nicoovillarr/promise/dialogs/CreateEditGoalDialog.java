package com.nicoovillarr.promise.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nicoovillarr.promise.R;
import com.nicoovillarr.promise.models.Goal;

import java.util.function.Consumer;

public class CreateEditGoalDialog extends Dialog {

    private EditText etName;
    private EditText etDesc;

    private Goal goal;
    private Consumer<Goal> onSave = null;
    private Runnable onDelete = null;

    public CreateEditGoalDialog(@NonNull Context context, Goal goal) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_create_edit_goal);

        this.goal = goal;

        this.setUpView();
    }

    @Override
    public void show() {
        super.show();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setGravity(Gravity.BOTTOM);
    }

    public CreateEditGoalDialog setOnSaveListener(Consumer<Goal> onSave) {
        this.onSave = onSave;
        return this;
    }

    public CreateEditGoalDialog setOnDeleteListener(Runnable onDelete) {
        this.onDelete = onDelete;
        return this;
    }

    private void setUpView() {
        this.etName = findViewById(R.id.goalName);
        this.etName.setText(this.goal != null ? this.goal.getName() : "");

        this.etDesc = findViewById(R.id.goalDesc);
        this.etDesc.setText(this.goal != null ? this.goal.getDescription() : "");

        TextView tvTitle = findViewById(R.id.dialogTitle);
        tvTitle.setText(this.goal == null ? "Make a promise" : "Edit");

        Button onSaveBtn = findViewById(R.id.saveBtn);
        onSaveBtn.setOnClickListener(v -> this.onSaveClick());

        Button onDeleteBtn = findViewById(R.id.deleteBtn);
        if (this.goal == null) {
            onDeleteBtn.setVisibility(View.GONE);
        } else {
            onDeleteBtn.setOnClickListener(v -> this.onDeleteClick());
            onDeleteBtn.setVisibility(View.VISIBLE);
        }
    }

    private void onSaveClick() {
        if (this.onSave == null) {
            return;
        }

        String name = this.etName.getText().toString();
        String desc = this.etDesc.getText().toString();

        Goal g = this.goal == null ? new Goal() : this.goal;
        g.setName(name);
        g.setDescription(desc);

        this.onSave.accept(g);

        this.dismiss();
    }

    private void onDeleteClick() {
        if (this.onDelete == null || this.goal == null) {
            return;
        }

        this.onDelete.run();
        this.dismiss();
    }

}
