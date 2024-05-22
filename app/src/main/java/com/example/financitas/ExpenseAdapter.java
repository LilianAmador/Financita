package com.example.financitas;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.financitas.model.Expense;
import com.example.financitas.model.ExpenseType;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<Expense> {
    public ExpenseAdapter(Context context, List<Expense> expenses) {
        super(context, 0, expenses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        Expense expense = getItem(position);

        TextView motiveTextView = convertView.findViewById(android.R.id.text1);
        TextView amountTextView = convertView.findViewById(android.R.id.text2);

        motiveTextView.setText(expense.getMotive());

        String amountText;
        if (expense.getType() == ExpenseType.INCOME) {
            amountText = "+ " + String.valueOf(expense.getAmount());
            amountTextView.setTextColor(Color.GREEN);
        } else {
            amountText = "- " + String.valueOf(expense.getAmount());
            amountTextView.setTextColor(Color.RED);
        }
        amountTextView.setText(amountText);

        return convertView;
    }
}