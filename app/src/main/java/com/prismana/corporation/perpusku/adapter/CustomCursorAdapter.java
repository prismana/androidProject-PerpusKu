package com.prismana.corporation.perpusku.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.prismana.corporation.perpusku.R;

public class CustomCursorAdapter extends CursorAdapter {
    private LayoutInflater ly;
    private SparseBooleanArray mSelectedItems;

    public CustomCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        ly = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSelectedItems = new SparseBooleanArray();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View v = ly.inflate(R.layout.row_data, viewGroup, false);
        MyHolder holder = new MyHolder();

        holder.ListID = v.findViewById(R.id.listID);
        holder.ListJudul = v.findViewById(R.id.listJudul);
        holder.ListNama = v.findViewById(R.id.listNama);
        holder.ListPinjam = v.findViewById(R.id.listTglPinjam);
        holder.ListStatus = v.findViewById(R.id.listStatus);

        v.setTag(holder);
        return v;
    }

    @SuppressLint({"Range", "SetTextI18n"})
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        MyHolder holder = (MyHolder)view.getTag();

        holder.ListID.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_id)));
        holder.ListJudul.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_judul)));
        holder.ListNama.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_nama)));
        holder.ListPinjam.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_pinjam)) +
                " - " + cursor.getString(cursor.getColumnIndex(DBHelper.row_kembali)));
        holder.ListStatus.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_status)));
    }

    class MyHolder{
        TextView ListID;
        TextView ListJudul;
        TextView ListNama;
        TextView ListPinjam;
        TextView ListStatus;
    }
}