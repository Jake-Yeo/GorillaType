package model;

import model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;

public class AccountUtilsTest {

    private AccountUtils accountUtils;

    @BeforeEach
    public void beforeEach() throws PasswordTooShortException, UsernameTakenException, UsernameTooShortException {
        accountUtils = new AccountUtils();
        accountUtils.signUp("Jake", "123456");
        accountUtils.signUp("John", "324532");
        accountUtils.signUp("Joey", "534523");
    }

    @Test
    public void testConstructor() {
        accountUtils = new AccountUtils();
        assertTrue(accountUtils.getListOfAccounts().isEmpty());
        assertNull(accountUtils.getAccountLoggedIn());
    }

    @Test
    public void testSignUp() {
        accountUtils = new AccountUtils();
        try {
            accountUtils.signUp("Jake", "123456");
            assertEquals(accountUtils.getAccountLoggedIn(), accountUtils.getListOfAccounts().get(0));
            assertEquals("Jake", accountUtils.getAccountLoggedIn().getUsername());
            assertEquals("123456", accountUtils.getAccountLoggedIn().getPassword());
            accountUtils.signUp("John", "324532");
            accountUtils.signUp("Joey", "534523");
            assertEquals(accountUtils.getAccountLoggedIn(), accountUtils.getListOfAccounts().get(2));
            assertEquals("Joey", accountUtils.getAccountLoggedIn().getUsername());
            assertEquals("534523", accountUtils.getAccountLoggedIn().getPassword());
            assertEquals("Joey", accountUtils.getListOfAccounts().get(2).getUsername());
            assertEquals("534523", accountUtils.getListOfAccounts().get(2).getPassword());
        } catch (SignupLoginException signupLoginException) {
            fail("SignupLoginException not supposed to be thrown");
        }
    }

    @Test
    public void testUsernameTakenException() {
        try {
            accountUtils.signUp("Joey", "534523");
            fail("UsernameTakenException supposed to be thrown");
        } catch (UsernameTakenException usernameTakenException) {
            //pass
        } catch (SignupLoginException signupLoginException) {
            fail("SignupLoginException not supposed to be thrown");
        }
    }

    @Test
    public void testPasswordTooShortException() {
        try {
            accountUtils.signUp("Joeeee", "53");
            fail("PasswordTooShortException supposed to be thrown");
        } catch (PasswordTooShortException passwordTooShortException) {
            //pass
        } catch (SignupLoginException signupLoginException) {
            fail("SignupLoginException not supposed to be thrown");
        }
    }


    @Test
    public void testUserNameTooShortException() {
        try {
            accountUtils.signUp("", "534523");
            fail("UsernameTooShortException supposed to be thrown");
        } catch (UsernameTooShortException usernameTooShortException) {
            //pass
        } catch (SignupLoginException signupLoginException) {
            fail("SignupLoginException not supposed to be thrown");
        }
    }

    @Test
    public void testLogin() {
        try {
            accountUtils.login("Jake", "123456");
            assertEquals("Jake", accountUtils.getAccountLoggedIn().getUsername());
            assertEquals("123456", accountUtils.getAccountLoggedIn().getPassword());
            accountUtils.login("Joey", "534523");
            assertEquals("Joey", accountUtils.getAccountLoggedIn().getUsername());

            assertEquals("Joey", accountUtils.getListOfAccounts().get(2).getUsername());
            assertEquals("534523", accountUtils.getListOfAccounts().get(2).getPassword());
        } catch (SignupLoginException signupLoginException) {
            fail("SignupLoginException should not be thrown");
        }
    }

    @Test
    public void testPasswordUserOrPassWrongException() {
        try {
            accountUtils.login("Joey", "534523");
        } catch (SignupLoginException signupLoginException) {
            fail("SignupLoginException should not be thrown");
        }
        try {
            accountUtils.login("Jake", "123");
            fail("UserOrPassWrongException supposed to be thrown");
        } catch (UserOrPassWrongException userOrPassWrongException) {
            // pass
        }
        assertEquals("Joey", accountUtils.getAccountLoggedIn().getUsername());
        assertEquals("534523", accountUtils.getAccountLoggedIn().getPassword());
    }

    @Test
    public void testUsernameUserOrPassWrongException() {
        try {
            accountUtils.login("Joey", "534523");
        } catch (SignupLoginException signupLoginException) {
            fail("SignupLoginException should not be thrown");
        }
        try {
            accountUtils.login("DNE", "534523");
            fail("UserOrPassWrongException supposed to be thrown");
        } catch (UserOrPassWrongException userOrPassWrongException) {
            // pass
        }
        assertEquals("Joey", accountUtils.getAccountLoggedIn().getUsername());
        assertEquals("534523", accountUtils.getAccountLoggedIn().getPassword());
    }

    @Test
    public void testLogout() throws UserOrPassWrongException {
        accountUtils.logOut();
        assertEquals(null, accountUtils.getAccountLoggedIn());
        assertEquals("Joey", accountUtils.getListOfAccounts().get(2).getUsername());
        assertEquals("534523", accountUtils.getListOfAccounts().get(2).getPassword());

        try {
            accountUtils.login("John", "324532");
        } catch (SignupLoginException signupLoginException) {
            fail("SignupLoginException should not be thrown");
        }
        assertEquals("John", accountUtils.getAccountLoggedIn().getUsername());
        assertEquals("324532", accountUtils.getAccountLoggedIn().getPassword());
        accountUtils.logOut();
        assertEquals(null, accountUtils.getAccountLoggedIn());
    }

    @Test
    public void testDeleteLoggedInAccount() throws UserOrPassWrongException {
        Account loggedInAccount = accountUtils.getAccountLoggedIn();
        accountUtils.deleteLoggedInAccount();
        assertFalse(accountUtils.getListOfAccounts().contains(loggedInAccount));
        try {
            accountUtils.login("Jake", "123456");
        } catch (SignupLoginException signupLoginException) {
            fail("SignupLoginException should not be thrown");
        }
        assertEquals("Jake", accountUtils.getAccountLoggedIn().getUsername());
        assertEquals("123456", accountUtils.getAccountLoggedIn().getPassword());
        accountUtils.deleteLoggedInAccount();
        assertEquals(null, accountUtils.getAccountLoggedIn());
    }

    @Test
    public void testPrintOutLogs() throws FileNotFoundException {
        Prompt prompt1 = new Prompt(true, true, true, 50);
        prompt1.setPrompt("aejurgioaejrgio");
        UserResults userResults = new UserResults();
        userResults.updateUserResults(prompt1, "hi there");
        userResults.setStartTime(new Time(123124L));
        userResults.setEndTime(new Time(123124L));
        Log log1 = new Log(prompt1, userResults);

        Prompt prompt2 = new Prompt(false, true, true, 503);
        UserResults userResults2 = new UserResults();
        userResults2.setStartTime(new Time(123124L));
        userResults2.setEndTime(new Time(123124L));
        prompt2.setPrompt("afewrgsethbsethstrhb");
        userResults2.updateUserResults(prompt2, "hi tserghere");
        Log log2 = new Log(prompt2, userResults2);

        Account account = new Account("Jakeys", "12324");
        account.addLog(log1);
        account.addLog(log2);

        assertEquals("LOG 0:\n" + log1.toString()
                + "\n\nLOG 1:\n" + log2.toString() + "\n\n", accountUtils.printLogs(account));
        account = new Account("Jakeys", "12324");
        account.addLog(log1);
        assertEquals("",accountUtils.filterLogsDisplay(100, account));
        assertEquals("Prompt: aejurgioaejrgio\n" +
                "Are Sentences Enabled: true\n" +
                "Are Words Enabled: true\n" +
                "Are Numbers Enabled: true\n" +
                "Approx Length: 50\n" +
                "Actual Length: 15\n" +
                "\n" +
                "User Input: hi there\n" +
                "Accuracy: 0%\n" +
                "Words Per Minute: -1\n" +
                "Start Time: 1969-12-31 16:02:124 PST\n" +
                "End Time: 1969-12-31 16:02:124 PST\n" +
                "Duration (seconds): 0",accountUtils.filterLogsDisplay(0, account));
    }

    @Test
    public void testContentEquals() throws PasswordTooShortException, UsernameTakenException,
            UsernameTooShortException, InvalidApproxLengthException {
        AccountUtils accountUtils2 = new AccountUtils();
        accountUtils2.signUp("Jake", "123456");
        accountUtils2.signUp("John", "324532");
        accountUtils2.signUp("Joey", "534523");

        assertTrue(accountUtils.contentEquals(accountUtils2));
        assertTrue(accountUtils2.contentEquals(accountUtils));
        accountUtils2.getAccountLoggedIn().getSettings().setApproxLength(1240230);
        assertFalse(accountUtils.contentEquals(accountUtils2));
        assertFalse(accountUtils2.contentEquals(accountUtils));
    }
}
