package edu.msu.puidoka3.cloudhatter.Cloud;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import edu.msu.puidoka3.cloudhatter.HatterView;
import edu.msu.puidoka3.cloudhatter.R;

public class LoadDlg extends DialogFragment {



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the title
        builder.setTitle(R.string.load_fm_title);

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Pass null as the parent view because its going in the dialog layout
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.catalog_dlg, null);
        builder.setView(view);

        // Add a cancel button
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // Cancel just closes the dialog box
            }
        });

        final AlertDialog dlg = builder.create();

        // Find the list view
        ListView list = (ListView)view.findViewById(R.id.listHattings);

        // Create an adapter
        final Cloud.CatalogAdapter adapter = new Cloud.CatalogAdapter(list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Get the id of the one we want to load
                String catId = String.valueOf(adapter.getId(position));

                // Dismiss the dialog box
                dlg.dismiss();

                LoadingDlg loadDlg = new LoadingDlg();
                loadDlg.setCatId(catId);
                loadDlg.show(getActivity().getSupportFragmentManager(), "loading");

            }

        });

        return dlg;
    }

    public class LoadingDlg extends DialogFragment {

        /**
         * Create the dialog box
         */
        @Override
        public Dialog onCreateDialog(Bundle bundle) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // Set the title
            builder.setTitle(R.string.loading);

            builder.setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });


            // Create the dialog box
            final AlertDialog dlg = builder.create();

            // Get a reference to the view we are going to load into
            final HatterView view = (HatterView)getActivity().findViewById(R.id.hatterView);

            /*
             * Create a thread to load the hatting from the cloud
             */
            new Thread(new Runnable() {

                @Override
                public void run() {

                }
            }).start();
            return dlg;
        }


        /**
         * Id for the image we are loading
         */
        private String catId;

        public String getCatId() {
            return catId;
        }

        public void setCatId(String catId) {
            this.catId = catId;
        }
    }

}