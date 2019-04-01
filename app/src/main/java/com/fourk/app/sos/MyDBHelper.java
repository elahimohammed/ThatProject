/*
 * Copyright (c) 2015 github.com/4-k
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.fourk.app.sos;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    // Database Name
    private static final String DATABASE_NAME = "crud.db";

    public MyDBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CONTACT = "CREATE TABLE IF NOT EXISTS " + Contact.TABLE  + "("
                + Contact.KEY_name  + " TEXT PRIMARY KEY ,"
                + Contact.KEY_phone + " TEXT )";
        db.execSQL(CREATE_TABLE_CONTACT);

        String CREATE_TABLE_LOCATION = "CREATE TABLE IF NOT EXISTS " + LocationData.TABLE  + "("
                + LocationData.KEY_lat  + " TEXT ,"
                + LocationData.KEY_lon + " TEXT )";
        db.execSQL(CREATE_TABLE_LOCATION);

        String CREATE_TABLE_EMAIL = "CREATE TABLE IF NOT EXISTS " + Email.TABLE  + "("
                + Email.KEY_name  + " TEXT ,"
                + Email.KEY_id + " TEXT )";
        db.execSQL(CREATE_TABLE_EMAIL);

        String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + User.TABLE  + "("
                + User.KEY_id  + " TEXT ,"
                + User.KEY_pass + " TEXT )";
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
