package com.grafcan.ide.search;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SearchSQLiteHelper  extends SQLiteOpenHelper {

	//
	//String sqlCreate = "CREATE TABLE tblbusqueda (codigo INTEGER, nombre TEXT, obs TEXT)";
	/*
	String sqlCreate = "CREATE TABLE tblbusqueda (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"codigo INTEGER, " +
			"nombre TEXT, " +
			"obs TEXT" +
			"x TEXT" +
			"y TEXT" +
			"date_add DATETIME DEFAULT current_timestamp)";
	*/
	
	String sqlCreate = "CREATE TABLE tblbusqueda (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"codigo INTEGER, " +
			"nombre TEXT, " +
			"localizacion TEXT, " +
			"clasificacion TEXT, " +
			"obs TEXT, " +
			"x TEXT, " +
			"y TEXT, " +
			"date_add DATETIME DEFAULT current_timestamp)";


	public SearchSQLiteHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }
	@Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.
 
        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS tblbusqueda");
 
        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }
}
