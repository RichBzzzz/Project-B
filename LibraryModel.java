import java.util.*;

enum Genre { 
    HORROR, FANTASY, ROMANCE, SELF_HELP, BIOGRAPHY 
}

enum BookStatus { 
    AVAILABLE, BORROWED 
}

interface Searchable {
    List<Book> searchBooks(String query, String type);
    Book findBookById(String id);
}

class Book {
    private String bookID, title, author;
    private Genre genre;
    private BookStatus bookStatus;
    private int quantity;

    public Book(String bookID, String title, String author, Genre genre, BookStatus bookStatus, int quantity) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.bookStatus = bookStatus;
        this.quantity = quantity;
    }

    public void updateStatus() {
        this.bookStatus = (this.quantity > 0) ? BookStatus.AVAILABLE : BookStatus.BORROWED;
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
        updateStatus();
    }

    public void removeQuantity(int amount) {
        this.quantity = Math.max(0, this.quantity - amount);
        updateStatus();
    }

    public String getBookID() { 
        return bookID; 
    }

    public String getTitle() { 
        return title; 
    }

    public String getAuthor() { 
        return author; 
    }

    public String getGenreStr() { 
        return genre.toString(); 
    } 

    public int getQuantity() { 
        return quantity; 
    }

    public String getStatusStr() { 
        return bookStatus.toString(); 
    } 

    public Genre getGenre() { 
        return genre; 
    }
}

class History {
    String recordID, memberID, bookID, rentDate, dueDate;
    public History(String recordID, String memberID, String bookID, String rentDate, String dueDate) {
        this.recordID = recordID;
        this.memberID = memberID;
        this.bookID = bookID;
        this.rentDate = rentDate;
        this.dueDate = dueDate;
    }
    @Override
    public String toString() { return "Record: " + recordID + " | Book: " + bookID + " | Due: " + dueDate; }
}

abstract class User {
    protected String name, userName, password;
    public User(String name, String userName, String password) {
        this.name = name;
        this.userName = userName;
        this.password = password;
    }
    public boolean checkPassword(String attempt) { 
        return this.password.equals(attempt); 
    }

    public String getUserName() { 
        return userName; 
    }

    public String getName() { 
        return name; 
    }
}

class Member extends User {
    private String memberID;
    private List<String> readingList = new ArrayList<>(); 

    public Member(String name, String userName, String password, String memberID) {
        super(name, userName, password);
        this.memberID = memberID;
    }
    public String getMemberID() { return memberID; }
    public List<String> getReadingList() { return readingList; }
    public void addToReadingList(String entry) { readingList.add(entry); }
}

class Librarian extends User {
    private String librarianID;
    public Librarian(String name, String userName, String password, String librarianID) {
        super(name, userName, password);
        this.librarianID = librarianID;
    }
}

public class LibraryModel implements Searchable {
    private List<Book> allBooks = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private List<Librarian> librarians = new ArrayList<>();
    private List<History> historyRecords = new ArrayList<>();
    private User currentUser;

    public LibraryModel() {
        initializeData();
    }

    private void initializeData() {
        // Horror Books
        addBook(new Book("B003", "Pet Sematary", "Stephen King", Genre.HORROR, BookStatus.AVAILABLE, 3));
        addBook(new Book("B004", "Bird Box", "Josh Malerman", Genre.HORROR, BookStatus.AVAILABLE, 2));
        addBook(new Book("B002", "Dracula", "Bram Stoker", Genre.HORROR, BookStatus.AVAILABLE, 1));
        addBook(new Book("B012", "Caroline", "Neil Geiman", Genre.HORROR, BookStatus.AVAILABLE, 4));
        
        // Fantasy Books
        addBook(new Book("B001", "The Hobbit", "J.R.R. Tolkien", Genre.FANTASY, BookStatus.AVAILABLE, 5));
        addBook(new Book("B005", "A Game of Thrones", "George R. R. Martin", Genre.FANTASY, BookStatus.AVAILABLE, 4));
        addBook(new Book("B013", "Harry Potter", "J.K.Rowling", Genre.FANTASY, BookStatus.AVAILABLE, 2));
        addBook(new Book("B014", "Alice's Adventure in Wonderland", "Lewis Caroll", Genre.FANTASY, BookStatus.AVAILABLE, 3));

        // Romance Books
        addBook(new Book("B006", "The Great Gatsby", "F. Scott Fitzgerald", Genre.ROMANCE, BookStatus.AVAILABLE, 3));
        addBook(new Book("B007", "Pride and Prejudice", "Jane Austen", Genre.ROMANCE, BookStatus.AVAILABLE, 4));
        addBook(new Book("B015", "Romeo and Juliet", "William Shakespear", Genre.ROMANCE, BookStatus.AVAILABLE, 1));
        addBook(new Book("B016", "It Ends with Us", "Colleen Hoover", Genre.ROMANCE, BookStatus.AVAILABLE, 2));

        // Self Help Books
        addBook(new Book("B008", "10% Happier", "Dan Harris", Genre.SELF_HELP, BookStatus.AVAILABLE, 2));
        addBook(new Book("B009", "The End of Mental Illness", "Daniel G. Amen", Genre.SELF_HELP, BookStatus.AVAILABLE, 3));
        addBook(new Book("B017", "Atomic Habits", "James Clear", Genre.SELF_HELP, BookStatus.AVAILABLE, 3));
        addBook(new Book("B018", "The 5AM Club", "Robin Sharma", Genre.SELF_HELP, BookStatus.AVAILABLE, 4));

        // Biography Books
        addBook(new Book("B010", "The Diary of a Young Girl", "Anne Frank", Genre.BIOGRAPHY, BookStatus.AVAILABLE, 4));
        addBook(new Book("B011", "A Promised Land", "Barack Obama", Genre.BIOGRAPHY, BookStatus.AVAILABLE, 2));
        addBook(new Book("B019", "The Diary of a Young Girl", "Anne Frank", Genre.BIOGRAPHY, BookStatus.AVAILABLE, 1));
        addBook(new Book("B020", "Long Walk to Freedom", "Nelson Mandela", Genre.BIOGRAPHY, BookStatus.AVAILABLE, 2));

        librarians.add(new Librarian("Admin", "Admin", "123", "L001"));
        members.add(new Member("John UTS", "John", "123", "M001"));
    }

    public void addBook(Book book) { 
        allBooks.add(book); 
    }

    public List<Book> getAllBooks() { 
        return allBooks; 
    }
    
    public User authenticateUser(String username, String password) {
        for (Librarian l : librarians) {
         for (Librarian l : librarians) {
            if (l.getUserName().equals(username) && l.checkPassword(password)) {
                return l;
            }
        }
        for (Member m : members) {
            if (m.getUserName().equals(username) && m.checkPassword(password)) {
                return m;
            }
        }
        return null;
    }

    public String registerMember(String name, String user, String pass) {
        for(Member m : members) {
            if(m.getUserName().equals(user)) {
                return "Username taken";
            }
        }
        String id = "M" + (members.size() + 1);
        members.add(new Member(name, user, pass, id));
        return "Success! ID: " + id;
    }

    public String registerLibrarian(String name, String user, String pass){
        for (Librarian l : librarians) {
            if (l.getUserName().equals(user)) {
                return "Username taken";
            }
        }
        String id = "L" + (librarians.size() + 1);
        librarians.add(new Librarian(name, user, pass, id));
        return "Success! ID: " + id;

    }

    public void setCurrentUser(User user) { 
        this.currentUser = user; 
    }

    public User getCurrentUser() { 
        return currentUser; 
    }

    @Override
    public Book findBookById(String id) {
        for(Book b : allBooks) {
            if(b.getBookID().equals(id)) {
                return b;
            }
        }
        return null;
    }

    @Override
    public List<Book> searchBooks(String query, String type) {
        List<Book> results = new ArrayList<>();
        String q = query.toLowerCase();

        for (Book b : allBooks) {
            if (type.equals("TITLE") && b.getTitle().toLowerCase().contains(q)) {
                results.add(b);
            } else if (type.equals("AUTHOR") && b.getAuthor().toLowerCase().contains(q)) {
                results.add(b);
            } else if (type.equals("GENRE") && b.getGenre().toString().toLowerCase().equals(q)) {
                results.add(b);
            }
        }
        return results;
    }

    public String borrowBook(String bookID, String rentDate, String dueDate) {
        if (!(currentUser instanceof Member)) {
            return "Only members can borrow.";
        }

        Book b = findBookById(bookID);

        if (b == null) {
            return "Book not found.";
        }

        if (b.getQuantity() <= 0) {
            return "Out of stock.";
        }

        b.removeQuantity(1);
        String recID = "R" + (historyRecords.size() + 1);
        History h = new History(recID, ((Member)currentUser).getMemberID(), bookID, rentDate, dueDate);
        historyRecords.add(h);
        
        return "Book Borrowed Successfully!";
    }

    public String returnBook(String bookID) {
        Book b = findBookById(bookID);
        if (b != null) {
            b.addQuantity(1);
            return "Book Returned Successfully!";
        }
        return "Book ID not found.";
    }

    public List<String> getMemberHistory() {
        List<String> logs = new ArrayList<>();
        if (currentUser instanceof Member) {
            String memID = ((Member)currentUser).getMemberID();
            for(History h : historyRecords) {
                if(h.memberID.equals(memID)) logs.add(h.toString());
            }
        }
        return logs;
    }

    public List<Book> getWishListBook(){
        if (!(currentUser instanceof Member)) return new ArrayList<>();
        Member m = (Member) currentUser;
        List<Book> books = new ArrayList<>();

        for (String id : m.getReadingList()){
            Book b = findBookById(id);
            if (b != null){
                books.add(b);
            }
        }
        return books;
    }

}

