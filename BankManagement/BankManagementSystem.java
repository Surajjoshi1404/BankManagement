import java.io.*;
import java.util.*;

class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    String accountNumber;
    int age;
    double balance;

    public Account(String name, String accountNumber, int age, double balance) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.age = age;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{ " +
                "Name='" + name + '\'' +
                ", AccountNumber='" + accountNumber + '\'' +
                ", Age=" + age +
                ", Balance=" + balance +
                " }";
    }
}

public class BankManagementSystem {
    private static final String FILE_NAME = "accounts.dat";
    private static Map<String, Account> accounts = new HashMap<>();

    public static void main(String[] args) {
        loadAccounts();

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nBank Management System");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Balance Inquiry");
            System.out.println("5. Update Account Details");
            System.out.println("6. Delete Account");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createAccount(scanner);
                    break;
                case 2:
                    depositMoney(scanner);
                    break;
                case 3:
                    withdrawMoney(scanner);
                    break;
                case 4:
                    balanceInquiry(scanner);
                    break;
                case 5:
                    updateAccount(scanner);
                    break;
                case 6:
                    deleteAccount(scanner);
                    break;
                case 7:
                    saveAccounts();
                    System.out.println("Exiting system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 7);

        scanner.close();
    }

    private static void createAccount(Scanner scanner) {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        if (accounts.containsKey(accountNumber)) {
            System.out.println("Account already exists with this account number.");
            return;
        }
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        System.out.print("Enter Initial Balance: ");
        double balance = scanner.nextDouble();
        scanner.nextLine();

        if (balance < 1000) {
            System.out.println("Minimum balance must be ₹1000.");
            return;
        }

        Account account = new Account(name, accountNumber, age, balance);
        accounts.put(accountNumber, account);
        System.out.println("Account created successfully.");
    }

    private static void depositMoney(Scanner scanner) {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        Account account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        System.out.print("Enter Amount to Deposit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        account.balance += amount;
        System.out.println("Amount deposited successfully. New Balance: ₹" + account.balance);
    }

    private static void withdrawMoney(Scanner scanner) {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        Account account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        System.out.print("Enter Amount to Withdraw: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (account.balance - amount < 1000) {
            System.out.println("Insufficient balance. Minimum balance must be ₹1000.");
            return;
        }

        account.balance -= amount;
        System.out.println("Amount withdrawn successfully. New Balance: ₹" + account.balance);
    }

    private static void balanceInquiry(Scanner scanner) {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        Account account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        System.out.println("Current Balance: ₹" + account.balance);
    }

    private static void updateAccount(Scanner scanner) {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        Account account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        System.out.print("Enter New Name (or press Enter to keep current): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            account.name = name;
        }
        System.out.print("Enter New Age (or press 0 to keep current): ");
        int age = scanner.nextInt();
        scanner.nextLine();
        if (age > 0) {
            account.age = age;
        }
        System.out.println("Account details updated successfully.");
    }

    private static void deleteAccount(Scanner scanner) {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        if (accounts.remove(accountNumber) != null) {
            System.out.println("Account deleted successfully.");
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void loadAccounts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accounts = (Map<String, Account>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No previous accounts found.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
