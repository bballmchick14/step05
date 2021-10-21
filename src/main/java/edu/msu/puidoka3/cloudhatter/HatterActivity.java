package edu.msu.puidoka3.cloudhatter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import edu.msu.puidoka3.cloudhatter.Cloud.LoadDlg;

public class HatterActivity extends AppCompatActivity {
    /**
     * Request code when selecting a picture
     */
    public static final int SELECT_PICTURE = 1;
    /**
     * Request code when selecting a color
     */
    private static final int SELECT_COLOR = 2;

    private static final String PARAMETERS = "parameters";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hatter);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        /*
         * Set up the spinner
         */

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.hats_spinner, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        getSpinner().setAdapter(adapter);
        getSpinner().setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int pos, long id) {
                getHatterView().setHat(pos);
                getColorButton().setEnabled(pos == HatterView.HAT_CUSTOM);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });
        /*
         * Restore any state
         */
        if(savedInstanceState != null) {
            getHatterView().getFromBundle(PARAMETERS, savedInstanceState);

        }
        /**
         * Ensure the user interface is up to date
         */
        updateUI();
    }
    /**
     * The hatter view object
     */
    private HatterView getHatterView() {
        return (HatterView) findViewById(R.id.hatterView);
    }

    /**
     * The color select button
     */
    private Button getColorButton() {
        return (Button)findViewById(R.id.buttonColor);
    }

    /**
     * The feather checkbox
     */
    private CheckBox getFeatherCheck() {
        return (CheckBox)findViewById(R.id.checkFeather);
    }

    /**
     * The hat choice spinner
     */
    private Spinner getSpinner() {
        return (Spinner) findViewById(R.id.spinnerHat);
    }

    /**
     * Called when it is time to create the options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hatter, menu);
        return true;
    }
    /**
     * Handle options menu selections
     * @param item Menu item selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_about:
                AboutDlg dlg = new AboutDlg();
                dlg.show(getSupportFragmentManager(), "About");
                return true;

            case R.id.menu_reset:
                getHatterView().reset();
                return true;

            case R.id.menu_load:
                LoadDlg dlg2 = new LoadDlg();
                dlg2.show(getSupportFragmentManager(), "load");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Ensure the user interface components match the current state
     */
    public void updateUI() {
        getSpinner().setSelection(getHatterView().getHat());
        getFeatherCheck().setChecked(getHatterView().getFeather());
        getColorButton().setEnabled(getHatterView().getHat() == HatterView.HAT_CUSTOM);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            // Response from the picture selection activity
            Uri imageUri = data.getData();
            setUri(imageUri);
        } else if(requestCode == SELECT_COLOR && resultCode == RESULT_OK) {
            // Response from the color selection activity
            assert data != null;
            int color = data.getIntExtra(ColorSelectActivity.COLOR, Color.BLACK);
            getHatterView().setColor(color);
        }

    }

    public void setUri(Uri uri) {
        getHatterView().setImageUri(uri);
    }
    /**
     * Handle the color select button
     * @param view Button view
     */
    public void onColor(View view) {
        // Get a picture from the gallery
        Intent intent = new Intent(this, ColorSelectActivity.class);
        startActivityForResult(intent, SELECT_COLOR);
    }


    /**
     * Handle a Picture button press
     */
    public void onPicture(View view) {
        // Bring up the picture selection dialog box
        PictureDlg dialog = new PictureDlg();
        dialog.show(getSupportFragmentManager(), null);
    }

    public void onFeather(View view) {
        getHatterView().setFeather(getFeatherCheck().isChecked());
        updateUI();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getHatterView().putToBundle(PARAMETERS, outState);
    }


}