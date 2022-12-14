
package es.pruebas.reto1_example_2022;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import es.pruebas.reto1_example_2022.beans.Usuario;

public class DataManager extends SQLiteOpenHelper {

    private static final String DB_NAME = "reto1_grupo4.db";

    private static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "usuarios";


    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            EMAIL + " TEXT NOT NULL ," +
            PASSWORD + " TEXT NOT NULL " +
            ");";

    private final Context context;

    public DataManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /* Select All */

    public List<Usuario> selectAllUsers() {
        List<Usuario> ret = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery(query, null);
        Usuario user;
        while (cursor.moveToNext()) {
            user = new Usuario();
            user.setEmail(cursor.getString(0));
            user.setPassword(cursor.getString(1));
            ret.add(user);
        }
        cursor.close();
        sQLiteDatabase.close();
        return ret;
    }


    /* Insertar */

    public void insert(Usuario user) {

        ContentValues values = new ContentValues();
        values.put(EMAIL, user.getEmail());
        values.put(PASSWORD, user.getPassword());

        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        sQLiteDatabase.insert(TABLE_NAME, null, values);
        sQLiteDatabase.close();

    }

    /* Actualizar */

    public boolean update(Usuario user) {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(EMAIL, user.getEmail());
        args.put(PASSWORD, user.getPassword());
        String whereClause = EMAIL + "=" + user.getId();
        return sQLiteDatabase.update(TABLE_NAME, args, whereClause, null) > 0;
    }

    /* Borrar */

    public void deleteByEmail(String email) {

        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();

        sQLiteDatabase.delete(TABLE_NAME, EMAIL + "=?",
                new String[]{email});
        sQLiteDatabase.close();
    }

    /* ifExist / ifEmpty */

    public boolean ifTableExists() {
        boolean ret = false;
        Cursor cursor = null;
        try {
            SQLiteDatabase sQLiteDatabase = this.getReadableDatabase();
            String query = "select DISTINCT tbl_name from sqlite_master where tbl_name = '" +
                    TABLE_NAME + "'";
            cursor = sQLiteDatabase.rawQuery(query, null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    ret = true;
                }
            }
        } catch (Exception e) {
            // Nothing to do here...
        } finally {
            try {
                assert cursor != null;
                cursor.close();
            } catch (NullPointerException e) {
                // Nothing to do here...
            }
        }
        return ret;
    }
}

