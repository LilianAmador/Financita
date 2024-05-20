package com.example.financitas.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "finanzas.db"; // Cambié "DATABASE_NOMBRE" a "DATABASE_NAME" para que coincida con el estándar de nomenclatura.
    public static final String TABLE_GASTOS = "t_gastos";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Aquí debes escribir el código para crear tu tabla de gastos.
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_GASTOS +"(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tipo TEXT NOT NULL," +
                "ingreso FLOAT NOT NULL," +
                "gasto FLOAT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // el código para actualizar tu esquema de base de datos cuando cambie la versión.
        sqLiteDatabase.execSQL("DROP TABLE" + TABLE_GASTOS);
        onCreate(sqLiteDatabase);
    }
}
