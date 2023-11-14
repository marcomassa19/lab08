package it.unibo.bank.impl;

import it.unibo.bank.api.AccountHolder;
import it.unibo.bank.api.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Assertions;

public class TestStrictBankAccount {

    private final static int INITIAL_AMOUNT = 100;

    // Create a new AccountHolder and a StrictBankAccount for it each time tests are executed.
    private AccountHolder mRossi;
    private BankAccount bankAccount;

    /**
     * Prepare the tests.
     */
    @BeforeEach
    public void setUp() {
        this.mRossi = new AccountHolder("Mario","Rossi",1);
        this.bankAccount = new StrictBankAccount(mRossi, INITIAL_AMOUNT);
    }

    /**
     * Test the initial state of the StrictBankAccount.
     */
    @Test
    public void testInitialization() {
        assertEquals(100.0, bankAccount.getBalance());
        assertEquals(0, bankAccount.getTransactionsCount());
        assertEquals(mRossi, bankAccount.getAccountHolder());
    }

    /**
     * Perform a deposit of 100€, compute the management fees, and check that the balance is correctly reduced.
     */
    @Test
    public void testManagementFees() {
        final double depositValue = 100.0;
        this.bankAccount.deposit(this.mRossi.getUserID(), depositValue);
        this.bankAccount.chargeManagementFees(this.mRossi.getUserID());
        assertEquals( 194.9, this.bankAccount.getBalance());
        assertEquals(0, this.bankAccount.getTransactionsCount());
    }

    /**
     * Test that withdrawing a negative amount causes a failure.
     */
    @Test
    public void testNegativeWithdraw() {
        final double amount = -25;
        
        try{
            this.bankAccount.withdraw(this.mRossi.getUserID(), amount);
            Assertions.fail();
        }catch(IllegalArgumentException exception){
            assertEquals("Cannot withdraw a negative amount", exception.getMessage());
        }
    }

    /**
     * Test that withdrawing more money than it is in the account is not allowed.
     */
    @Test
    public void testWithdrawingTooMuch() {
        final double amount = this.bankAccount.getBalance()+ 100;

        try{
            this.bankAccount.withdraw(this.mRossi.getUserID(), amount);
            Assertions.fail();
        }catch(IllegalArgumentException exception){
            assertEquals("Insufficient balance", exception.getMessage());
        }
    }
}
