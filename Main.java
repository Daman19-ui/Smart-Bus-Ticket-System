import java.io.*;
import java.util.*;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static final String USERS_FILE = "users.txt";
    static final String BUSES_FILE = "buses.txt";
    static final String BOOKINGS_FILE = "bookings.txt";
    static String loggedInUserName = "";
    static boolean isAdmin = false;

    public static void main(String[] args) throws IOException {
        while (true) {
            System.out.println("\n SMART BUS TICKET SYSTEM ");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1 -> registerUser();
                case 2 -> loginUser();
                case 3 -> {
                    System.out.println("🚪 Thank you for using the system!");
                    return;
                }
                default -> System.out.println("❌ Invalid option. Try again.");
            }
        }
    }

    static void registerUser() throws IOException {
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Password: ");
        String pass = sc.nextLine();

        BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE, true));
        bw.write(name + "," + email + "," + pass);
        bw.newLine();
        bw.close();

        System.out.println(" Registered Successfully.");
    }

    static void loginUser() throws IOException {
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Password: ");
        String pass = sc.nextLine();

        if (email.equals("admin@bus.com") && pass.equals("admin123")) {
            loggedInUserName = "Admin";
            isAdmin = true;
            System.out.println(" Login Successful. Welcome Admin.");
            showDashboard();
            return;
        }

        BufferedReader br = new BufferedReader(new FileReader(USERS_FILE));
        String line;
        while ((line = br.readLine()) != null) {
            String[] arr = line.split(",");
            if (arr[1].equals(email) && arr[2].equals(pass)) {
                loggedInUserName = arr[0];
                isAdmin = false;
                System.out.println("Login Successful. Welcome " + arr[0]);
                br.close();
                showDashboard();
                return;
            }
        }
        br.close();
        System.out.println("Invalid credentials.");
    }

    static void showDashboard() throws IOException {
        while (true) {
            System.out.println("\n DASHBOARD - " + (isAdmin ? "ADMIN" : loggedInUserName));
            if (isAdmin) {
                System.out.println("1. Add Bus");
                System.out.println("2. View Buses");
                System.out.println("3. View All Bookings");
                System.out.println("4. Logout");
            } else {
                System.out.println("1. View Buses");
                System.out.println("2. Book Ticket");
                System.out.println("3. View My Bookings");
                System.out.println("4. Logout");
            }

            System.out.print("Choose: ");
            int ch = sc.nextInt();
            sc.nextLine();

            if (isAdmin) {
                switch (ch) {
                    case 1 -> addBus();
                    case 2 -> viewBuses();
                    case 3 -> viewBookings();
                    case 4 -> {
                        System.out.println("Logged out.");
                        return;
                    }
                    default -> System.out.println(" Invalid option.");
                }
            } else {
                switch (ch) {
                    case 1 -> viewBuses();
                    case 2 -> bookTicket();
                    case 3 -> viewBookings();
                    case 4 -> {
                        System.out.println("Logged out.");
                        return;
                    }
                    default -> System.out.println(" Invalid option.");
                }
            }
        }
    }

    static void addBus() throws IOException {
         System.out.print("Enter Bus Number: ");
        String busno = sc.nextLine();
          System.out.print("Enter Bus Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Source: ");
        String source = sc.nextLine();
        System.out.print("Enter Destination: ");
        String dest = sc.nextLine();
        System.out.print("Enter Total Seats: ");
        String seats = sc.nextLine();

        BufferedWriter bw = new BufferedWriter(new FileWriter(BUSES_FILE, true));
        bw.write(source + "," + dest + "," + seats);
        bw.newLine();
        bw.close();

        System.out.println("🚌 Bus Added Successfully.");
    }

    static void viewBuses() throws IOException {
        File file = new File(BUSES_FILE);
        if (!file.exists() || file.length() == 0) {
            System.out.println("❌ No buses found.");
            return;
        }
BufferedReader br = new BufferedReader(new FileReader(file));
String line;
int count = 1;
System.out.println("\n🚌 Available Buses:");
while ((line = br.readLine()) != null) {
    String[] arr = line.split(",");
    if (arr.length >= 5) {
        System.out.println(count++ + ". Bus No: " + arr[0] + " | Name: " + arr[1] + " | From: " + arr[2] + " | To: " + arr[3] + " | Seats: " + arr[4]);
    } else {
        System.out.println("⚠️ Corrupted or Incomplete Bus Data: " + line);
    }
}


// public static void addBus() throws IOException {
//     BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//     System.out.print("Enter Bus Number: ");
//     String busNumber = br.readLine();

//     System.out.print("Enter Bus Name: ");
//     String busName = br.readLine();

//     System.out.print("Enter Source: ");
//     String source = br.readLine();

//     System.out.print("Enter Destination: ");
//     String destination = br.readLine();

//     System.out.print("Enter Total Seats: ");
//     String seats = br.readLine();

//     String busData = busNumber + "," + busName + "," + source + "," + destination + "," + seats;

//     FileWriter fw = new FileWriter("buses.txt", true); // true = append mode
//     fw.write(busData + "\n");
//     fw.close();

//     System.out.println("✅ Bus Added Successfully!");
// }

br.close();

        br.close();
    }

  static void bookTicket() throws IOException {
    File file = new File(BUSES_FILE);
    if (!file.exists() || file.length() == 0) {
        System.out.println("❌ No buses available for booking.");
        return;
    }

    viewBuses();

    System.out.print("Enter Bus No. to book (e.g., PB11AB6496): ");
    String inputBusNo = sc.nextLine().trim();

    BufferedReader br = new BufferedReader(new FileReader(BUSES_FILE));
    List<String> buses = new ArrayList<>();
    String line;
    boolean found = false;
    String selectedBus = "";

    while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts[0].equalsIgnoreCase(inputBusNo)) {
            found = true;
            selectedBus = line;
            break;
        }
    }
    br.close();

    if (!found) {
        System.out.println("❌ Bus Number not found.");
        return;
    }

    BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKINGS_FILE, true));
    bw.write(loggedInUserName + "," + selectedBus);
    bw.newLine();
    bw.close();

    System.out.println("✅ Ticket Booked Successfully for Bus " + inputBusNo);
}


    static void viewBookings() throws IOException {
        File file = new File(BOOKINGS_FILE);
        if (!file.exists() || file.length() == 0) {
            System.out.println("❌ No bookings found.");
            return;
        }

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        int count = 1;
        System.out.println("\n📋 Bookings:");
        while ((line = br.readLine()) != null) {
            String[] arr = line.split(",");
            // arr[0] is username
            if (isAdmin || arr[0].equals(loggedInUserName)) {
                System.out.println(count++ + ". " + arr[0] + ": " + arr[1] + " -> " + arr[2] + " | From: " + arr[3]  + " | To: " + arr[4] + " | seats: " + arr[5] );
            }
        }
        br.close();
    }
}
