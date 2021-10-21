package edu.msu.puidoka3.cloudhatter.Cloud;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import edu.msu.puidoka3.cloudhatter.Cloud.Models.Catalog;
import edu.msu.puidoka3.cloudhatter.Cloud.Models.Item;
import edu.msu.puidoka3.cloudhatter.R;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

@SuppressWarnings("deprecation")
public class Cloud {
    private static final String MAGIC = "NechAtHa6RuzeR8x";
    private static final String USER = "puidoka3";
    private static final String PASSWORD = "password";
    private static final String BASE_URL = "https://facweb.cse.msu.edu/dennisp/cse476x/";
    public static final String CATALOG_PATH = "hatter-cat.php";
    public static final String SAVE_PATH = "hatter-save.php";
    public static final String DELETE_PATH = "hatter-delete.php";
    public static final String LOAD_PATH = "hatter-load.php";
    private static final String UTF8 = "UTF-8";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build();


    /**
     * An adapter so that list boxes can display a list of filenames from
     * the cloud server.
     */
    public static class CatalogAdapter extends BaseAdapter {

        /**
         * The items we display in the list box. Initially this is
         * null until we get items from the server.
         */
        private Catalog catalog = new Catalog("", new ArrayList(), "");

        @Override
        public int getCount() {
            return catalog.getItems().size();
        }

        @Override
        public Item getItem(int position) {
            return catalog.getItems().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_item, parent, false);
            }

            TextView tv = (TextView)view.findViewById(R.id.textItem);
            tv.setText(catalog.getItems().get(position).getName());

            return view;
        }

        /**
         * Constructor
         */
        public CatalogAdapter(final View view) {
            // Create a thread to load the catalog
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        catalog = getCatalog();

                        if (catalog.getStatus().equals("no")) {
                            String msg = "Loading catalog returned status 'no'! Message is = '" + catalog.getMessage() + "'";
                            throw new Exception(msg);
                        }

                        view.post(new Runnable() {

                            @Override
                            public void run() {
                                // Tell the adapter the data set has been changed
                                notifyDataSetChanged();
                            }

                        });
                    } catch (Exception e) {
                        // Error condition! Somethign went wrong
                        Log.e("CatalogAdapter", "Something went wrong when loading the catalog", e);
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(view.getContext(), R.string.catalog_fail, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        }

        // Create a GET query
        public Catalog getCatalog() throws IOException {
            HatterService service = retrofit.create(HatterService.class);
            return service.getCatalog(USER, MAGIC, PASSWORD).execute().body();
        }

        public int getId(int position) {
            return (int)getItemId(position);
        }
    }

}
