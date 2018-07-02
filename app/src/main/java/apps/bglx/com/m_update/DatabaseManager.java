package apps.bglx.com.m_update;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Artists.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String strSql = "create table T_ArtistID ("
                + "    idArtist integer primary key autoincrement,"
                + "    name text not null,"
                + "    idDeezer integer not null,"
                + "    idSpotify integer not null, "
                + "    idYoutube integer not null"
                + ")";
        db.execSQL( strSql );
        Log.i( "DATABASE", "onCreate invoked" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //String strSql = "alter table T_Scores add column ...";
        String strSql = "drop table T_ArtistID";
        db.execSQL( strSql );
        this.onCreate( db );
        Log.i( "DATABASE", "onUpgrade invoked" );

    }

    public void insertScore( String name, int idDeezer, int idSpotify, int idYoutube ) {
        name = name.replace( "'", "''" );
        String strSql = "insert into T_ArtistID (name, idDeezer, idSpotify, idYoutube) values ('"
                + name + "', " + idDeezer + ", " + idSpotify + ", " + idYoutube + ")";
        this.getWritableDatabase().execSQL( strSql );
        Log.i( "DATABASE", "insertScore invoked" );
    }

    public List<ArtistsData> readTop10() {
        List<ArtistsData> ids = new ArrayList<>();

        // 1ère technique : SQL
        String strSql = "select * from T_ArtistID limit 10";
        Cursor cursor = this.getReadableDatabase().rawQuery( strSql, null );

        // 2nd technique "plus objet"
        //Cursor cursor = this.getReadableDatabase().query( "T_ArtistID",
        //        new String[] { "idArtist", "name", "idDeezer", "idSpotify", "idYoutube" },
        //        null, null, null, null, null, "10" );
        cursor.moveToFirst();
        while( ! cursor.isAfterLast() ) {
            ArtistsData score = new ArtistsData( cursor.getInt( 0 ), cursor.getString( 1 ),
                    cursor.getInt( 2 ), cursor.getInt( 3 ), cursor.getInt( 4 ));
            ids.add( score );
            cursor.moveToNext();
        }
        cursor.close();

        return ids;
    }

}
