package com.bookmypacket.bmpclub.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.bookmypacket.bmpclub.db.BMPDatabaseHelper;

import java.util.HashMap;

import static android.content.ContentResolver.CURSOR_DIR_BASE_TYPE;
import static android.content.ContentResolver.CURSOR_ITEM_BASE_TYPE;
import static android.text.TextUtils.isEmpty;
import static com.bookmypacket.bmpclub.db.BMPDatabaseHelper.PACKET_ID;
import static com.bookmypacket.bmpclub.db.BMPDatabaseHelper.PACKET_TABLE;

/**
 * Created by Manish on 02-01-2016.
 */
public class BMPPacketDataProvider extends ContentProvider
{
    public static final  String                  PACKET_AUTHORITY      =
            "com.bookmypacket.bmpclub.provider.BMPPacket";
    public static final  String                  URL                   =
            "content://" + PACKET_AUTHORITY + "/packet";
    public static final  Uri                     BASE_CONTENT_URI      =
            Uri.parse("content://" + PACKET_AUTHORITY);
    private static final UriMatcher              uriMatcher            =
            new UriMatcher(UriMatcher.NO_MATCH);
    private static final int                     PACKET_LIST           = 1;
    private static final int                     PACKET                = 2;
    private static final String                  PACKET_PATH           = "packet";
    public static final  Uri                     CONTENT_URI           =
            BASE_CONTENT_URI.buildUpon().appendPath(PACKET_PATH).build();
    private static       HashMap<String, String> PACKET_PROJECTION_MAP = new HashMap<>();

    static
    {
        uriMatcher.addURI(PACKET_AUTHORITY, PACKET_PATH, PACKET_LIST);
        uriMatcher.addURI(PACKET_AUTHORITY, PACKET_PATH + "/#", PACKET);
    }

    private BMPDatabaseHelper databaseHelper;

    @Override
    public boolean onCreate()
    {
        databaseHelper = new BMPDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder)
    {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(PACKET_TABLE);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        switch (uriMatcher.match(uri))
        {
            case PACKET_LIST:
                qb.setProjectionMap(PACKET_PROJECTION_MAP);
                break;
            case PACKET:
                qb.appendWhere(PACKET_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri)
    {
        switch (uriMatcher.match(uri))
        {
            /**
             * Get all packet records
             */
            case PACKET_LIST:
                return CURSOR_DIR_BASE_TYPE + "/vnd.bookmypacket.bmpclub.packet";

            /**
             * Get a particular packet
             */
            case PACKET:
                return CURSOR_ITEM_BASE_TYPE + "/vnd.bookmypacket.bmpclub.packet";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues)
    {
        SQLiteDatabase db  = databaseHelper.getWritableDatabase();
        long           row = db.insert(PACKET_TABLE, "", contentValues);
        if (row > 0)
        {
            Uri pUri = ContentUris.withAppendedId(uri, row);
            return pUri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        SQLiteDatabase db    = databaseHelper.getWritableDatabase();
        int            count = 0;
        switch (uriMatcher.match(uri))
        {
            case PACKET_LIST:
                count = db.delete(PACKET_TABLE, selection, selectionArgs);
                break;
            case PACKET:
                String packetId = uri.getPathSegments().get(1);
                count = db.delete(PACKET_TABLE, PACKET_ID + " = " + packetId +
                        (!isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs)
    {
        SQLiteDatabase db    = databaseHelper.getWritableDatabase();
        int            count = 0;
        switch (uriMatcher.match(uri))
        {
            case PACKET_LIST:
                count = db.update(PACKET_TABLE, contentValues, selection, selectionArgs);
                break;
            case PACKET:
                count = db.update(PACKET_TABLE, contentValues,
                                  PACKET_ID + " = " + uri.getPathSegments().get(1) +
                                          (!isEmpty(selection) ? " AND (" + selection + ')' : ""),
                                  selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
