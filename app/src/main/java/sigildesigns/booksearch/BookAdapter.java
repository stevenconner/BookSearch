package sigildesigns.booksearch;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Modified version of ArrayAdapter to list multiple things in a list view
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent,
                    false);
        }

        // Get the {@link Book} object located at this position in the list
        Book currentBook = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID title_of_book
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_of_book);
        // Set the title to that TextView
        titleTextView.setText(currentBook.getmTitle());

        // Find the TextView in the list_item.xml layout with the ID author_of_book
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_of_book);
        // Set the author(s) to that TextView
        authorTextView.setText(currentBook.getmAuthors());

        return listItemView;
    }
}
