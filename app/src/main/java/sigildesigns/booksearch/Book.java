package sigildesigns.booksearch;

/**
 * {@link Book} represents a single book for the books data.
 * It contains a thumbnail of the book, the title, and the author(s).
 */

public class Book {

    // URL for the thumbnail
    private String mThumbnail;

    // Title of the book
    private String mTitle;

    // Author(s) of the book
    private String mAuthors;

    // URL for more information about the book
    private String mUrl;

    public Book(String thumbnail, String title, String authors, String url) {
        mThumbnail = thumbnail;
        mTitle = title;
        mAuthors = authors;
        mUrl = url;
    }

    // Get the Uri for the thumbnail
    public String getmThumbnail() {
        return mThumbnail;
    }

    // Get the title of the book
    public String getmTitle() {
        return mTitle;
    }

    // Get the author(s) of the book
    public String getmAuthors() {
        return mAuthors;
    }

    // Get the URL for more information
    public String getmUrl() {
        return mUrl;
    }

}
