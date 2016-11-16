package sigildesigns.booksearch;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<List<Book>> {
    public final static String KEYWORD_EXTRA = "keyword";

    public String mQueryUrl = "https://www.googleapis.com/books/v1/volumes?q=";
    private BookAdapter mAdapter;

    // Constant value for the BookLoader ID.
    private static final int BOOK_LOADER_ID = 1;

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new BookLoader(this, mQueryUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        mAdapter.clear();

        /**
         * If there is a valid list of {@link Book}s, then add them to the adapter's data set.
         * This will trigger the ListView to update.
         */
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booklist);

        Intent intent = getIntent();
        String keyWord = intent.getStringExtra(KEYWORD_EXTRA);
        mQueryUrl = "https://www.googleapis.com/books/v1/volumes?q=" + keyWord;

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of books as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView} so the list can be populated
        bookListView.setAdapter(mAdapter);

        // Check to see if the network is active, otherwise don't attempt to load data
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with all loaders
            LoaderManager loaderManager = getLoaderManager();

            /**
             * Initialize the loader. Pass in the int ID constant defined above and pass in null
             * for the bundle. Pass in this activity for the LoaderCallbacks parameter (which is
             * valid because this activity implements the LoaderCallbacks interface).
             */
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        }

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the book.
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                // Find the current book that was clicked on
                Book currentBook = mAdapter.getItem(position);

                // Convert the String URL into a URI object to pass into intent
                Uri bookUri = Uri.parse(currentBook.getmUrl());

                // Create a new intent to view the book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                startActivity(websiteIntent);
            }
        });
    }
}
