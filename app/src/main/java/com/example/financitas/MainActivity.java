package com.example.financitas;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financitas.db.DbHelper;
import com.example.financitas.model.Expense;
import com.example.financitas.model.ExpenseType;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView calculatedIncomeTextView;
    private TextView calculatedExpensesTextView;
    private EditText motiveEditText;
    private EditText amountEditText;
    private Spinner expenseTypeSpinner;
    private ListView expenseListView;

    private List<Expense> expenseList = new ArrayList<>();
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expenseListView.setAdapter(new ExpenseAdapter(this, expenseList));

        calculatedIncomeTextView = findViewById(R.id.calculatedIncomeTextView);
        calculatedExpensesTextView = findViewById(R.id.calculatedExpensesTextView);
        motiveEditText = findViewById(R.id.motiveEditText);
        amountEditText = findViewById(R.id.amountEditText);
        expenseTypeSpinner = findViewById(R.id.expenseTypeSpinner);
        expenseListView = findViewById(R.id.expenseListView);

        // Set up the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.expense_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseTypeSpinner.setAdapter(adapter);

        // Set up the list view
        expenseListView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, expenseList));

        dbHelper = new DbHelper(this);
        loadExpensesFromDatabase();

        // Set up the add expense button
        Button addExpenseButton = findViewById(R.id.addExpenseButton);
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String motive = motiveEditText.getText().toString();
                double amount = Double.parseDouble(amountEditText.getText().toString());
                ExpenseType type = ExpenseType.values()[expenseTypeSpinner.getSelectedItemPosition()];

                addExpenseToDatabase(motive, amount, type);
                loadExpensesFromDatabase();
                updateCalculatedValues();
                clearInputFields();
            }
        });
    }

    private void addExpenseToDatabase(String motive, double amount, ExpenseType type) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("motive", motive);
        values.put("amount", amount);
        values.put("type", type.toString());
        db.insert(DbHelper.TABLE_EXPENSES, null, values);
        db.close();
    }

    private void loadExpensesFromDatabase() {
        expenseList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DbHelper.TABLE_EXPENSES, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String motive = cursor.getString(cursor.getColumnIndex("motive"));
            double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
            ExpenseType type = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex("type")));
            Expense expense = new Expense(id, motive, amount, type);
            expenseList.add(expense);
        }
        cursor.close();
        db.close();
        ((ExpenseAdapter) expenseListView.getAdapter()).notifyDataSetChanged();
    }

    private void updateCalculatedValues() {
        double totalIncome = 0;
        double totalExpenses = 0;

        for (Expense expense : expenseList) {
            if (expense.getType() == ExpenseType.INCOME) {
                totalIncome += expense.getAmount();
            } else {
                totalExpenses += expense.getAmount();
            }
        }

        calculatedIncomeTextView.setText(String.valueOf(totalIncome));
        calculatedExpensesTextView.setText(String.valueOf(totalExpenses));
    }

    private void clearInputFields() {
        motiveEditText.setText("");
        amountEditText.setText("");
        expenseTypeSpinner.setSelection(0);
    }
}