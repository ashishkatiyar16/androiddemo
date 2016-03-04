package com.bookmypacket.bmpclub.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Manish on 15-01-2016.
 */
public class BMPDatabaseHelper extends SQLiteOpenHelper
{
    public static final  String PACKET_TABLE        = "PACKET";
    public static final  String PACKET_ID           = "PACKET_ID";
    private static final String CREATE_PACKET_TABLE =
            "CREATE TABLE PACKET ( _ID INTEGER PRIMARY KEY AUTOINCREMENT, PACKET_ID TEXT, " +
                    "PACKET_JSON TEXT, ID_PROOF TEXT, STATUS TEXT, PACKET_DELIVERY TEXT, " +
                    "PACKET_ACCEPT TEXT, SERVER_STATUS TEXT)";
    private static final String DB_NAME             = "BookMyPacket";

    public BMPDatabaseHelper(Context ctx)
    {
        super(ctx, DB_NAME, null, 1);
    }

    public BMPDatabaseHelper(Context context, String name,
                             SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    public BMPDatabaseHelper(Context context, String name,
                             SQLiteDatabase.CursorFactory factory, int version,
                             DatabaseErrorHandler errorHandler)
    {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(CREATE_PACKET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PACKET_TABLE);
        onCreate(sqLiteDatabase);
    }
}
