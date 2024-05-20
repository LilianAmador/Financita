package com.example.financitas;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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

        calculatedIncomeTextView = findViewById(R.id.calculatedIncomeTextView);
        calculatedExpensesTextView = findViewById(R.id.calculatedExpensesTextView);
        motiveEditText = findViewById(R.id.motiveEditText);
        amountEditText = findViewById(R.id.amountEditText);
        expenseTypeSpinner = findViewById(R.id.expenseTypeSpinner);
        expenseListView = findViewById(R.id.expenseListView);

        dbHelper = new DbHelper(this);
        String databasePath = getDatabasePath(DbHelper.DATABASE_NAME).getAbsolutePath();
        Log.d("Database", "Database created at: " + databasePath);
        expenseListView.setAdapter(new ExpenseAdapter(this, expenseList));
        loadExpensesFromDatabase();


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
            int idColumnIndex = cursor.getColumnIndex("id");
            int motiveColumnIndex = cursor.getColumnIndex("motive");
            int amountColumnIndex = cursor.getColumnIndex("amount");
            int typeColumnIndex = cursor.getColumnIndex("type");

            if (idColumnIndex != -1 && motiveColumnIndex != -1 && amountColumnIndex != -1 && typeColumnIndex != -1) {
                int id = cursor.getInt(idColumnIndex);
                String motive = cursor.getString(motiveColumnIndex);
                double amount = cursor.getDouble(amountColumnIndex);
                ExpenseType type = ExpenseType.valueOf(cursor.getString(typeColumnIndex));
                Expense expense = new Expense(id, motive, amount, type);
                expenseList.add(expense);
            }
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