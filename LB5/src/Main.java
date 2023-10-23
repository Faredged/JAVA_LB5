import java.util.ArrayList;
import java.util.List;

class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

class NegativeAmountException extends Exception {
    public NegativeAmountException(String message) {
        super(message);
    }
}

class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message) {
        super(message);
    }
}

class BankAccount {
    private int accountNumber;
    private String accountName;
    private double balance;

    public BankAccount(int accountNumber, String accountName, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.balance = initialDeposit;
    }

    public void deposit(double amount) throws NegativeAmountException {
        if (amount < 0) {
            throw new NegativeAmountException("Amount should be positive");
        }
        balance += amount;
    }

    public void withdraw(double amount) throws NegativeAmountException, InsufficientFundsException {
        if (amount < 0) {
            throw new NegativeAmountException("Amount should be positive");
        }
        if (balance < amount) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountSummary() {
        return "Account Number: " + accountNumber + "\nAccount Name: " + accountName + "\nBalance: " + balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }
}

class Bank {
    private List<BankAccount> accounts;

    public Bank() {
        accounts = new ArrayList<>();
    }

    public void createAccount(String accountName, double initialDeposit) {
        int accountNumber = accounts.size() + 1; // Просто збільшуємо кількість рахунків на 1
        BankAccount newAccount = new BankAccount(accountNumber, accountName, initialDeposit);
        accounts.add(newAccount);
    }

    public BankAccount findAccount(int accountNumber) throws AccountNotFoundException {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                return account;
            }
        }
        throw new AccountNotFoundException("Account not found");
    }

    public void transferMoney(int fromAccountNumber, int toAccountNumber, double amount)
            throws AccountNotFoundException, NegativeAmountException, InsufficientFundsException {
        if (fromAccountNumber == toAccountNumber) {
            throw new IllegalArgumentException("Source and destination accounts cannot be the same");
        }

        BankAccount fromAccount = findAccount(fromAccountNumber);
        BankAccount toAccount = findAccount(toAccountNumber);

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);
    }
}

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        bank.createAccount("John Doe", 1000);
        bank.createAccount("Jane Smith", 1000);

        try {
            bank.transferMoney(1, 2, 500);
            System.out.println(bank.findAccount(1).getAccountSummary());
            System.out.println();
            System.out.println(bank.findAccount(2).getAccountSummary());
            System.out.println();
            bank.transferMoney(1,2,-51);
        } catch (AccountNotFoundException | NegativeAmountException | InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            bank.transferMoney(1,2,501);
        } catch (AccountNotFoundException | NegativeAmountException | InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}