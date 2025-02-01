import java.util.ArrayList;
import java.util.Scanner;

// User class for login
class User {
    String username;
    String password;
    boolean isAdmin;

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}

// Movie class
class Movie {
    String title;
    ArrayList<Show> shows;

    public Movie(String title) {
        this.title = title;
        this.shows = new ArrayList<>();
    }

    public void addShow(String time, int seats) {
        shows.add(new Show(time, seats));
    }

    public void displayShows() {
        for (int i = 0; i < shows.size(); i++) {
            System.out.println((i + 1) + ". Time: " + shows.get(i).time + " | Available Seats: " + shows.get(i).availableSeats);
        }
    }
}

// Show class
class Show {
    String time;
    int availableSeats;

    public Show(String time, int availableSeats) {
        this.time = time;
        this.availableSeats = availableSeats;
    }

    public boolean bookSeats(int count) {
        if (count <= availableSeats) {
            availableSeats -= count;
            return true;
        }
        return false;
    }
}

// Booking class
class Booking {
    User user;
    Movie movie;
    Show show;
    int ticketCount;

    public Booking(User user, Movie movie, Show show, int ticketCount) {
        this.user = user;
        this.movie = movie;
        this.show = show;
        this.ticketCount = ticketCount;
    }

    public void displayBooking() {
        System.out.println("\n🎟️ Booking Confirmed 🎟️");
        System.out.println("User: " + user.username);
        System.out.println("Movie: " + movie.title);
        System.out.println("Show Time: " + show.time);
        System.out.println("Tickets: " + ticketCount);
    }
}

// Main class
public class MovieTicketBookingApp {
    static ArrayList<Movie> movies = new ArrayList<>();
    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static User currentUser = null;

    public static void main(String[] args) {
        // Sample users
        users.add(new User("admin", "admin123", true));
        users.add(new User("user1", "pass123", false));

        // Sample movies & shows
        Movie movie1 = new Movie("Avengers: Endgame");
        movie1.addShow("12:00 PM", 10);
        movie1.addShow("06:00 PM", 8);
        movies.add(movie1);

        Movie movie2 = new Movie("Interstellar");
        movie2.addShow("02:00 PM", 5);
        movie2.addShow("08:00 PM", 7);
        movies.add(movie2);

        loginMenu();
    }

    public static void loginMenu() {
        System.out.println("\n=== Movie Ticket Booking System ===");
        System.out.print("Enter Username: ");
        String username = scanner.next();
        System.out.print("Enter Password: ");
        String password = scanner.next();

        for (User user : users) {
            if (user.authenticate(username, password)) {
                currentUser = user;
                System.out.println("\n✅ Login Successful! Welcome, " + user.username);
                mainMenu();
                return;
            }
        }
        System.out.println("❌ Invalid credentials! Try again.");
        loginMenu();
    }

    public static void mainMenu() {
        while (true) {
            System.out.println("\n=== Main Menu ===");
            if (currentUser.isAdmin) {
                System.out.println("1. Add Movie");
                System.out.println("2. View Movies");
                System.out.println("3. Logout");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addMovie();
                        break;
                    case 2:
                        displayMovies();
                        break;
                    case 3:
                        System.out.println("Logging out...");
                        loginMenu();
                        break;
                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            } else {
                System.out.println("1. View Movies");
                System.out.println("2. Book Tickets");
                System.out.println("3. View My Bookings");
                System.out.println("4. Logout");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        displayMovies();
                        break;
                    case 2:
                        bookTicket();
                        break;
                    case 3:
                        viewBookings();
                        break;
                    case 4:
                        System.out.println("Logging out...");
                        loginMenu();
                        break;
                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            }
        }
    }

    public static void addMovie() {
        System.out.print("\nEnter Movie Title: ");
        scanner.nextLine(); // Consume newline
        String title = scanner.nextLine();
        Movie newMovie = new Movie(title);
        
        System.out.print("Enter number of shows: ");
        int numShows = scanner.nextInt();
        for (int i = 0; i < numShows; i++) {
            System.out.print("Enter Show Time (e.g., 05:00 PM): ");
            scanner.nextLine();
            String time = scanner.nextLine();
            System.out.print("Enter Available Seats: ");
            int seats = scanner.nextInt();
            newMovie.addShow(time, seats);
        }
        movies.add(newMovie);
        System.out.println("\n✅ Movie Added Successfully!");
    }

    public static void displayMovies() {
        if (movies.isEmpty()) {
            System.out.println("\nNo movies available!");
            return;
        }

        System.out.println("\nAvailable Movies:");
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i).title);
        }
    }

    public static void bookTicket() {
        displayMovies();
        if (movies.isEmpty()) return;

        System.out.print("\nEnter movie number: ");
        int movieChoice = scanner.nextInt();
        if (movieChoice < 1 || movieChoice > movies.size()) {
            System.out.println("Invalid selection!");
            return;
        }

        Movie selectedMovie = movies.get(movieChoice - 1);
        selectedMovie.displayShows();
        System.out.print("\nSelect a show: ");
        int showChoice = scanner.nextInt();
        if (showChoice < 1 || showChoice > selectedMovie.shows.size()) {
            System.out.println("Invalid show selection!");
            return;
        }

        Show selectedShow = selectedMovie.shows.get(showChoice - 1);
        System.out.print("Enter number of tickets: ");
        int tickets = scanner.nextInt();

        if (selectedShow.bookSeats(tickets)) {
            Booking newBooking = new Booking(currentUser, selectedMovie, selectedShow, tickets);
            bookings.add(newBooking);
            newBooking.displayBooking();
        } else {
            System.out.println("\n❌ Not enough seats available.");
        }
    }

    public static void viewBookings() {
        System.out.println("\n=== Your Bookings ===");
        for (Booking booking : bookings) {
            if (booking.user == currentUser) {
                booking.displayBooking();
            }
        }
    }
}
