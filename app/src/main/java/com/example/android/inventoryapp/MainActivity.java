package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.Contract;
import com.example.android.inventoryapp.data.DBHelper;
import com.example.android.inventoryapp.data.Contract.ProductEntry;

public class MainActivity extends AppCompatActivity {

    private DBHelper productDBHelper;
    private EditText productNameEditText;
    private EditText priceEditText;
    private EditText quantityEditText;
    private EditText supplierNameEditText;
    private EditText supplierPhoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productDBHelper = new DBHelper(this);
        productNameEditText = findViewById(R.id.edit_product_name);
        priceEditText = findViewById(R.id.edit_product_price);
        quantityEditText = findViewById(R.id.edit_quantity);
        supplierNameEditText = findViewById(R.id.edit_supplier_name);
        supplierPhoneEditText = findViewById(R.id.edit_supplier_phone_number);
        displayDatabaseInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        SQLiteDatabase db = productDBHelper.getReadableDatabase();

        String[] projection = {Contract.ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRICE,
                ProductEntry.COLUMN_QUANTITY,
                ProductEntry.COLUMN_SUPPLIER_NAME,
                ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER,};

        Cursor cursor = db.query(ProductEntry.TABLE_NAME, projection, null, null, null, null, null);

        TextView displayView = (TextView) findViewById(R.id.text_view_product);
        try {

            displayView.setText("The products table contains " + cursor.getCount() + " products.\n\n");
            displayView.append(ProductEntry._ID + " - " +
                    ProductEntry.COLUMN_PRODUCT_NAME + " - " +
                    ProductEntry.COLUMN_PRICE + " - " +
                    ProductEntry.COLUMN_QUANTITY + " - " +
                    ProductEntry.COLUMN_SUPPLIER_NAME + " - " +
                    ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplierName + " - " +
                        currentSupplierPhone));
            }
        } finally {
            cursor.close();
        }
    }

    private void insertPet() {
        // get name
        String nameString = productNameEditText.getText().toString().trim();
        // get price
        String priceString = priceEditText.getText().toString().trim();
        // get quantity
        String quantityString = quantityEditText.getText().toString().trim();
        // get supplier name
        String supplierNameString = supplierNameEditText.getText().toString().trim();
        // get supplier phone number
        String supplierPhoneString = supplierPhoneEditText.getText().toString().trim();

        int priceInt = Integer.parseInt(priceString);
        int quantityInt = Integer.parseInt(quantityString);

        // TODO: read price in $ and cents combine strings

        // add to database

        SQLiteDatabase db = productDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_NAME, nameString);
        contentValues.put(ProductEntry.COLUMN_PRICE, priceInt);
        contentValues.put(ProductEntry.COLUMN_QUANTITY, quantityInt);
        contentValues.put(ProductEntry.COLUMN_SUPPLIER_NAME, supplierNameString);
        contentValues.put(ProductEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierPhoneString);

        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, contentValues);

        // check if pet data was inserted correctly
        if (newRowId != -1) {
            Toast toast = Toast.makeText(this, "Product saved with ID" + newRowId, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(this, "Error with saving the product", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to DB
                insertPet();
                displayDatabaseInfo();
                clearEditText();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearEditText(){
        productNameEditText.getText().clear();
        priceEditText.getText().clear();
        quantityEditText.getText().clear();
        supplierNameEditText.getText().clear();
        supplierPhoneEditText.getText().clear();
    }
}
