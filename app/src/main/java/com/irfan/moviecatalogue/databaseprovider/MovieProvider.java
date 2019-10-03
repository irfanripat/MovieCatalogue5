package com.irfan.moviecatalogue.databaseprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.irfan.moviecatalogue.dao.MovieDAO;
import com.irfan.moviecatalogue.model.Movie;
import com.irfan.moviecatalogue.room.DBRoom;

public class MovieProvider extends ContentProvider {

    private static final String AUTHORITY = "com.irfan.moviecatalogue";
    private static final String SCHEME = "content";

    public static final Uri URI_MOVIE = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath("MovieDB")
            .build();
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int CODE_MOVIE_DIR = 1;
    private static final int CODE_MOVIE_ITEM = 2;

    static {
        sUriMatcher.addURI(AUTHORITY, "MovieDB", CODE_MOVIE_DIR);
        sUriMatcher.addURI(AUTHORITY, "MovieDB" + "/*", CODE_MOVIE_ITEM);
    }

    @Override
    public boolean onCreate() {
        return false;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + "MovieDB";
            case CODE_MOVIE_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + "MovieDB";
            default:
                throw new IllegalArgumentException("Invalid uri detected: " + uri);
        }

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @NonNull String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = sUriMatcher.match(uri);

        if (match == CODE_MOVIE_DIR || match == CODE_MOVIE_ITEM) {
            Context context = getContext();
            if (context == null) {
                return null;
            }
            MovieDAO movieDao = DBRoom.getInstance(context).movieDao();
            Cursor cursor = null;
            if (match == CODE_MOVIE_DIR && uri != null) {
                cursor = movieDao.getFavouriteMovieCursor();
                cursor.setNotificationUri(context.getContentResolver(), uri);
            }
            return cursor;
        } else {
            throw new IllegalArgumentException("Invalid uri detected: " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE_DIR:
                long id = 0;
                Context context = getContext();
                if (context == null) {
                    return null;
                }
                if (uri != null && contentValues != null) {
                    context.getContentResolver().notifyChange(uri, null);
                    id = DBRoom.getInstance(context).movieDao().addMovie(Movie.fromContentValues(contentValues));
                }
                return ContentUris.withAppendedId(uri, id);
            case CODE_MOVIE_ITEM:
                throw new IllegalArgumentException("Invalid uri, cannot insert with Id: " + uri);
            default:
                throw new IllegalArgumentException("Invalid uri detected: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE_DIR:
                throw new IllegalArgumentException("Invalid uri, cannot update without Id " + uri);
            case CODE_MOVIE_ITEM:
                int id = 0;

                if (uri != null) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    id = DBRoom.getInstance(getContext()).movieDao().deleteById(ContentUris.parseId(uri));
                }
                return id;
            default:
                throw new IllegalArgumentException("Invalid uri detected: " + uri);
        }

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @NonNull String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE_DIR:
                throw new UnsupportedOperationException("Invalid uri detected: " + uri);
            case CODE_MOVIE_ITEM:
                Context context = getContext();
                int id = 0;
                if (context == null) {
                    return 0;
                }
                if (contentValues != null && uri != null) {
                    Movie movie = Movie.fromContentValues(contentValues);
                    movie.setId((int) ContentUris.parseId(uri));

                    context.getContentResolver().notifyChange(uri, null);
                    id = DBRoom.getInstance(getContext()).movieDao().updateMovie(movie);
                }
                return id;
            default:
                throw new IllegalArgumentException("Invalid uri detected: " + uri);
        }
    }
}
