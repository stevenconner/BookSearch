package sigildesigns.booksearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getName();
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the EditText and button
        mEditText = (EditText) findViewById(R.id.edit_text);
        Button mButton = (Button) findViewById(R.id.button);

        // Set a click listener for the button
        mButton.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the button is clicked
            @Override
            public void onClick(View view) {
                String keyWord = mEditText.getText().toString();
                // Create a new intent to open the BookListActivity
                Intent bookListActivityIntent = new Intent(MainActivity.this, BookListActivity
                        .class);
                bookListActivityIntent.putExtra(BookListActivity.KEYWORD_EXTRA, keyWord);
                startActivity(bookListActivityIntent);
            }
        });
    }
}
