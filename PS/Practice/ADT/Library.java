public interface Library {
    public void addBook(String bookTitle);
    public boolean borrowBook(String bookTitle);
    public void returnBook(String bookTitle);
    public int totalBooks();
}
