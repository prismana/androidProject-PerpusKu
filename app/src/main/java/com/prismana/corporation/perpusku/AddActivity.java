package com.prismana.corporation.perpusku;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.prismana.corporation.perpusku.adapter.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddActivity extends AppCompatActivity
{

    DBHelper dbHelper;
    TextView TvStatus;

    MaterialButton BtnProses, hapus, simpan;
    EditText TxID, TxtglPinjam;

    TextInputEditText TxNama, TxJudul, TxtglKembali, TxStatus;
    TextInputLayout textInputLayout;
    long id;

    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dbHelper = new DBHelper(this);

        //mengambil id, ketika list di klik
        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        //statusbar
        statusBarColor();

        //inisialisasi komponen view
        TxID = findViewById(R.id.txID);
        TxNama = findViewById(R.id.txNamaAnggota);
        TxJudul = findViewById(R.id.txJudul);
        TxtglPinjam = findViewById(R.id.txPinjam);
        TxtglKembali = findViewById(R.id.txKembali);
        TxStatus = findViewById(R.id.txStatus);
        TvStatus = findViewById(R.id.tvStatus);
        BtnProses = findViewById(R.id.btnProses);
        textInputLayout = findViewById(R.id.materialInputLayoutStatus);

        //test 2 tombol material untuk SQLite
        simpan = findViewById(R.id.tombolSimpan);
        hapus = findViewById(R.id.tombolHapus);

        //set disable untuk tidak aktif saat pertama di activity
        hapus.setEnabled(false);
        BtnProses.setEnabled(false);

        simpan.setOnClickListener(v -> insertAndUpdate());
        hapus.setOnClickListener(v -> deleteAction());

        //formater untuk datepicker agar tanggal lebih dibaca
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        //proses select dari db helper untuk menampilkan data
        getData();

        //listener untuk memunculkan fragment material datepicker
        TxtglKembali.setOnClickListener(v -> showDateDialog());

        //listener untuk pengembalian buku
        BtnProses.setOnClickListener(v -> prosesKembali());

    }

    //untuk menyamakan warna status bar, agar terlihat material
    private void statusBarColor()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary, this.getTheme()));
        }
    }

    //method untuk mengembalikan buku
    private void prosesKembali()
    {
        final MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this)
                .setTitle("Kembalikan Buku")
                .setMessage("Proses buku untuk pengembalian?")
                .setPositiveButton("OK", (dialog, which) ->
                {
                    String idpinjam = TxID.getText().toString().trim();
                    String kembali = "Dikembalikan";

                    ContentValues values = new ContentValues();

                    values.put(DBHelper.row_status, kembali);
                    dbHelper.updateData(values, id);
                    Toast.makeText(AddActivity.this, "Proses Pengembalian Berhasil", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Batal", (dialog, which) -> dialog.cancel());

        materialAlertDialogBuilder.create().show();
    }

    //method untuk menyimpan input user kemudian insert ke database SQLite
    //dan untuk update data dari SQLite
    public void insertAndUpdate()
    {
        String idpinjam = TxID.getText().toString().trim();
        String nama = Objects.requireNonNull(TxNama.getText()).toString().trim();
        String judul = Objects.requireNonNull(TxJudul.getText()).toString().trim();
        String tglPinjam = TxtglPinjam.getText().toString().trim();
        String tglKembali = Objects.requireNonNull(TxtglKembali.getText()).toString().trim();
        String status = "Dipinjam";

        ContentValues values = new ContentValues();

        values.put(DBHelper.row_nama, nama);
        values.put(DBHelper.row_judul, judul);
        values.put(DBHelper.row_kembali, tglKembali);
        values.put(DBHelper.row_status, status);

        if (nama.equals("") || judul.equals("") || tglKembali.equals(""))
        {
            Toast.makeText(AddActivity.this, "Isi Data Dengan Lengkap", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (idpinjam.equals(""))
            {
                values.put(DBHelper.row_pinjam, tglPinjam);
                dbHelper.insertData(values);
            }
            else
            {
                dbHelper.updateData(values, id);
            }

            Toast.makeText(AddActivity.this, "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //date picker pakai material dan format dd-mm-yyyy===============================================================
    private void showDateDialog()
    {
        //inisiasi datepicker
        MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker().build();

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            //....
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selection);
            TxtglKembali.setText(dateFormatter.format(calendar.getTime()));
        });
        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
    }

    //select SQLite untuk liat data saat inten dikirim==================================================================
    private void getData()
    {
        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String tglPinjam = sdf1.format(c1.getTime());
        TxtglPinjam.setText(tglPinjam);

        Cursor cur = dbHelper.oneData(id);
        if (cur.moveToFirst())
        {
            @SuppressLint("Range") String idpinjam = cur.getString(cur.getColumnIndex(DBHelper.row_id));
            @SuppressLint("Range") String nama = cur.getString(cur.getColumnIndex(DBHelper.row_nama));
            @SuppressLint("Range") String judul = cur.getString(cur.getColumnIndex(DBHelper.row_judul));
            @SuppressLint("Range") String pinjam = cur.getString(cur.getColumnIndex(DBHelper.row_pinjam));
            @SuppressLint("Range") String kembali = cur.getString(cur.getColumnIndex(DBHelper.row_kembali));
            @SuppressLint("Range") String status = cur.getString(cur.getColumnIndex(DBHelper.row_status));

            TxID.setText(idpinjam);
            TxNama.setText(nama);
            TxJudul.setText(judul);
            TxtglPinjam.setText(pinjam);
            TxtglKembali.setText(kembali);
            TxStatus.setText(status);

            //mengatur bagaimana tombol ditampilkan berdasarkan isi teks
            if (TxID.equals("")) {
                textInputLayout.setVisibility(View.VISIBLE);
                TxStatus.setVisibility(View.GONE);
                BtnProses.setVisibility(View.GONE);
            } else {
                TxStatus.setVisibility(View.VISIBLE);
                BtnProses.setVisibility(View.VISIBLE);
            }

            if (status.equals("Dipinjam"))
            {
                BtnProses.setEnabled(true);
                textInputLayout.setVisibility(View.VISIBLE);
                textInputLayout.setEnabled(false);
                hapus.setEnabled(true); //hapus mode on saat status dipinjam
            } else
            {
                BtnProses.setEnabled(false);
                TxNama.setEnabled(false);
                TxJudul.setEnabled(false);
                TxtglKembali.setEnabled(false);
                TxStatus.setEnabled(false);
                TxtglPinjam.setEnabled(false);

                textInputLayout.setVisibility(View.VISIBLE);
                textInputLayout.setEnabled(false);
                simpan.setEnabled(false); //tombol simpan dimatikan
                hapus.setEnabled(true); //hapus mode on
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    //method untuk delete database SQLite
    private void deleteAction()
    {
        final MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this)
                .setTitle("Hapus Peminjaman")
                .setMessage("Perintah ini akan menghapus catatan peminjaman dari database. Apakah anda yakin?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteData(id);
                        Toast.makeText(AddActivity.this, "Terhapus", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        materialAlertDialogBuilder.create().show();
    }
}