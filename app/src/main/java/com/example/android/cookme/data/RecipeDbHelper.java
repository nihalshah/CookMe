package com.example.android.cookme.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.cookme.data.RecipeContract.RecipeEntry;
import com.example.android.cookme.data.RecipeContract.IngredientEntry;
import com.example.android.cookme.data.RecipeContract.RecipeIngredientRelationship;

/**
 * Created by eduardovaca on 17/07/15.
 */
public class RecipeDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    static final String DATABASE_NAME = "recipe.db";

    public RecipeDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //String for creating the Recipe Table
        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " + RecipeEntry.TABLE_NAME + " (" +
                RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeEntry.COL_NAME + " TEXT NOT NULL, " +
                RecipeEntry.COL_INSTRUCTIONS + " TEXT NOT NULL, " + //When we put the Photo we must add a coma
                RecipeEntry.COL_PHOTO + " BLOB " +
                ");";

        //String for creating the Ingredient Table
        final String SQL_CREATE_INGREDIENT_TABLE = "CREATE TABLE " + IngredientEntry.TABLE_NAME + " (" +
                IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IngredientEntry.COL_NAME + " TEXT NOT NULL " +
                ");";

        final String SQL_CREATE_RELATIONSHIP_TABLE = "CREATE TABLE " + RecipeIngredientRelationship.TABLE_NAME + " (" +
                RecipeIngredientRelationship.COL_RECIPE_KEY + " INTEGER NOT NULL, " +
                RecipeIngredientRelationship.COL_INGREDIENT_KEY + " INTEGER NOT NULL, " +
                RecipeIngredientRelationship.COL_UNITS + " TEXT NOT NULL, " +
                RecipeIngredientRelationship.COL_QUANTITY + " REAL NOT NULL, " +

                //Setup Recipe_Key column as foreign key
                "FOREIGN KEY (" + RecipeIngredientRelationship.COL_RECIPE_KEY + ") REFERENCES " +
                RecipeEntry.TABLE_NAME + " (" + RecipeEntry._ID + "), " +

                //Setup Ingredient_Key as foreign key
                "FOREIGN KEY (" + RecipeIngredientRelationship.COL_INGREDIENT_KEY + ") REFERENCES " +
                IngredientEntry.TABLE_NAME + " (" + IngredientEntry._ID + ") " +

                //Setup of composed Primary Key
                "PRIMARY KEY (" + RecipeIngredientRelationship.COL_RECIPE_KEY + ", " +
                                RecipeIngredientRelationship.COL_INGREDIENT_KEY + ") " +

                // To assure the application have just one unique combination Rep - Ing
                // it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + RecipeIngredientRelationship.COL_RECIPE_KEY + ", " +
                RecipeIngredientRelationship.COL_INGREDIENT_KEY + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME + ";");
        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME + ";");
        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENT_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeIngredientRelationship.TABLE_NAME + ";");
        sqLiteDatabase.execSQL(SQL_CREATE_RELATIONSHIP_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        /*We must use ALTER TABLE if we update the Db, so we dont lose user info*/
        onCreate(sqLiteDatabase);

    }
}
