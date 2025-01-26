import java.util.ArrayList;

public class LibrarySystem implements Library{
    ArrayList<String> books;

    public LibrarySystem(){
        books = new ArrayList<>();
    }

    @Override
    public void addBook(String bookTitle) {
        if (!books.contains(bookTitle)){
            books.add(bookTitle);
        }

    }

    @Override
    public boolean borrowBook(String bookTitle) {
        if (books.contains(bookTitle)){
            books.remove(bookTitle);
            return true;
        }
        return false;
    }

    @Override
    public void returnBook(String bookTitle) {
        if (!books.contains(bookTitle)){
            books.add(bookTitle);
        }

    }

    @Override
    public int totalBooks() {
        return books.size();
    }

    public static void main(String[] args) {
        LibrarySystem library = new LibrarySystem();

        library.addBook("1984");
        library.addBook("To Kill a Mockingbird");
        library.addBook("The Great Gatsby");

        library.borrowBook("1984");
        library.returnBook("1984");
        System.out.println(library.totalBooks());

        library.borrowBook("1984");
        library.returnBook("1984");
        System.out.println(library.totalBooks());
    }
}
