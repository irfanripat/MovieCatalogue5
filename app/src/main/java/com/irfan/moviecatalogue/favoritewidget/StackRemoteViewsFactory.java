package com.irfan.moviecatalogue.favoritewidget;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.irfan.moviecatalogue.R;
import com.irfan.moviecatalogue.model.Movie;

import static com.irfan.moviecatalogue.databaseprovider.MovieProvider.URI_MOVIE;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String API_POSTER_PATH = "https://image.tmdb.org/t/p/w500";
    private Cursor cursor;
    private Context context;

    StackRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }
        long identityToken = Binder.clearCallingIdentity();
        cursor = context.getContentResolver().query(URI_MOVIE, null, null, null, null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (cursor == null)
            return 0;
        else
            return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || cursor == null || !cursor.moveToPosition(position)) {
            return null;
        }
        Movie movie = getPosition(position);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_widget);

        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .asBitmap()
                    .load(API_POSTER_PATH + movie.getPosterPath())
                    .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        remoteViews.setImageViewBitmap(R.id.img_widget_id, bitmap);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return cursor.moveToPosition(position) ? cursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private Movie getPosition(int pos) {
        if (!cursor.moveToPosition(pos)) {
            throw new IllegalArgumentException("No data found");
        }
        return new Movie(cursor);
    }
}
