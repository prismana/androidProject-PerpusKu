package com.prismana.corporation.perpusku;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.prismana.corporation.perpusku.adapter.CustomCursorAdapter;
import com.prismana.corporation.perpusku.adapter.DBHelper;
import com.prismana.corporation.perpusku.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{

    ListView lv;
    DBHelper dbHelper;

    private AppBarConfiguration appBarConfiguration;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        com.prismana.corporation.perpusku.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        statusBarColor();

        setSupportActionBar(binding.toolbar);

        //log out test
        binding.efab.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AddActivity.class)));

        dbHelper =new DBHelper(this);
        lv = findViewById(R.id.list_pinjam);
        lv.setOnItemClickListener(this);

        //inisialiasasi firebase
        mAuth = FirebaseAuth.getInstance();

        setupListView();
    }

    private void setupListView()
    {
        Cursor cursor = dbHelper.allData();
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, cursor, 1);
        lv.setAdapter(customCursorAdapter);
    }

    private void statusBarColor()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary, this.getTheme()));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logoutItem) {
            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this)
                    .setTitle("Logout").setMessage("Apakah anda ingin keluar akun?").setPositiveButton("OK", (dialog, which) -> {
                        mAuth.signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    })
                    .setNegativeButton("Batal", (dialog, which) -> dialog.cancel());

            materialAlertDialogBuilder.create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long I)
    {
        TextView getID = view.findViewById(R.id.listID);
        final long id = Long.parseLong(getID.getText().toString());
        Cursor cur = dbHelper.oneData(id);
        cur.moveToFirst();

        Intent idpinjam = new Intent(MainActivity.this, AddActivity.class);
        idpinjam.putExtra(DBHelper.row_id, id);
        startActivity(idpinjam);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setupListView();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null)
        {
            startActivity(new Intent(MainActivity.this,  LoginActivity.class));
        }
    }
}