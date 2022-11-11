package es.pruebas.reto1_example_2022;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DataManager extends SQLiteOpenHelper {

    private static final String DB_NAME = "reto1_grupo4.db";

    private static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "usuarios";

    private static final String LOGIN = "login";
    private static final String ID = "id";
    private static final String NOMBRE = "nombre";
    private static final String APELLIDOS = "apellidos";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            LOGIN + " TEXT NOT NULL, " +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NOMBRE + " TEXT NOT NULL, " +
            APELLIDOS + " TEXT NOT NULL, " +
            EMAIL + " TEXT NOT NULL ," +
            PASSWORD + " TEXT NOT NULL " +
            ");";

    private final Context context;

    public List<Users> ret = new ArrayList<>();

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

    public List<Users> selectAllUsers () {
        List<Users> ret = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery(query, null);
        Users user;
        while (cursor.moveToNext()) {
            user = new Users();
            user.setLogin(cursor.getString(0));
            //user.setId(cursor.getInt(1));
            user.setNombre(cursor.getString(1));
            user.setApellidos(cursor.getString(2));
            user.setEmail(cursor.getString(3));
            user.setPassword(cursor.getString(4));


            ret.add(user);
        }
        cursor.close();
        sQLiteDatabase.close();
        return ret;
    }

    /* Select by Id */

    public Users selectById (int id) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + ID +
                " = " + "'" + id + "'";
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery(query, null);

        Users user = new Users ();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            user.setId(cursor.getInt(0));
            user.setNombre(cursor.getString(1));
            user.setApellidos(cursor.getString(2));
            user.setEmail(cursor.getString(3));
            user.setPassword(cursor.getString(4));
            cursor.close();
        } else {
            user = null;
        }
        sQLiteDatabase.close();
        return user;
    }

    /* Insertar */

    public boolean insert (Users user) {
        boolean existe = false;

       // Toast.makeText(context, "ANTES DEL IF", Toast.LENGTH_LONG).show();

        if(buscar(user.getLogin())==0){

//            Toast.makeText(context, "DESPUES DE EL IF", Toast.LENGTH_LONG).show();

            ContentValues values = new ContentValues();
            values.put(LOGIN, user.getLogin());
            values.put(NOMBRE, user.getNombre());
            values.put(APELLIDOS, user.getApellidos());
            values.put(EMAIL, user.getEmail());
            values.put(PASSWORD, user.getPassword());

            SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
            sQLiteDatabase.insert(TABLE_NAME, null, values);
            sQLiteDatabase.close();

        } else {
            return existe =true;
        }
        return existe;
    }

    /* Actualizar */

    public boolean update (Users user) {
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(ID, user.getId());
        args.put(NOMBRE, user.getNombre());
        args.put(APELLIDOS, user.getApellidos());
        args.put(EMAIL, user.getEmail());
        args.put(PASSWORD, user.getPassword());
        String whereClause = ID + "=" + user.getId();
        return sQLiteDatabase.update(TABLE_NAME, args, whereClause, null) > 0;
    }

    /* Borrar */

    public int deleteById (int id) {
        int ret;
        SQLiteDatabase sQLiteDatabase = this.getWritableDatabase();
        Users user = new Users ();
        user.setId(id);
        ret = sQLiteDatabase.delete(TABLE_NAME, ID + "=?",
                new String[] {
                        String.valueOf(user.getId())
                });
        sQLiteDatabase.close();
        return ret;
    }

    /* ifExist / ifEmpty */

    public boolean ifTableExists() {
        boolean ret = false;
        Cursor cursor = null;
        try {
            SQLiteDatabase sQLiteDatabase = this.getReadableDatabase();
            String query = "select DISTINCT tbl_name from sqlite_master where tbl_name = '" +
                    TABLE_NAME + "'";
            cursor = sQLiteDatabase.rawQuery( query, null );
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    ret = true;
                }
            }
        } catch (Exception e) {
            // Nothing to do here...
        } finally{
            try{
                assert cursor != null;
                cursor.close();
            } catch (NullPointerException e) {
                // Nothing to do here...
            }
        }
        return ret;
    }

    //Buscar usuario

    public int buscar(String u){
        int x=0;
        ret = selectAllUsers();
        for (Users us:ret) {
            if(us.getLogin().equalsIgnoreCase(u)){
                x++;
            }
        }
        return x;
    }

}
