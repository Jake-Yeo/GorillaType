package model;

import model.exceptions.InvalidApproxLengthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.sql.Time;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AccountTest {

    private Account account;

    @BeforeEach
    public void beforeEach() {
        account = new Account("Jake", "123456");
    }

    @Test
    public void testConstructor() {
        assertEquals("Jake", account.getUsername());
        assertEquals("123456", account.getPassword());
        Settings settings = new Settings();
        assertTrue(settings.contentEquals(account.getSettings()));
        assertEquals(new ArrayList<Log>(), account.getLogs());
    }

    @Test
    public void testChangeUsernameAndPassword() {
        account.changeUsername("Jack");
        account.changePassword("654321");

        assertEquals("Jack", account.getUsername());
        assertEquals("654321", account.getPassword());

        account.changeUsername("Jacd");
        account.changePassword("65432fwe1");
        account.changeUsername("John");
        account.changePassword("qazwsx");

        assertEquals("John", account.getUsername());
        assertEquals("qazwsx", account.getPassword());
    }

    @Test
    public void testContentEquals() throws InvalidApproxLengthException, FileNotFoundException {
        Account account2 = new Account("Jake", "123456");
        account2.getSettings().setIsAutomaticSaveEnabled(true);
        account2.getSettings().setApproxLength(12031204);
        account.getSettings().setIsAutomaticSaveEnabled(true);
        account.getSettings().setApproxLength(12031204);
        assertTrue(account.contentEquals(account2));
        assertTrue(account2.contentEquals(account));

        Prompt prompt2 = new Prompt(true, true, true, 51);
        prompt2.setPrompt("im get rich boller");
        UserResults userResults2 = new UserResults();
        userResults2.updateUserResults(prompt2, "im get rich boller");
        userResults2.setEndTime(new Time(23423L));
        userResults2.setStartTime(new Time(123L));
        Log log2 = new Log(prompt2, userResults2);

        account2.addLog(log2);
        assertFalse(account.contentEquals(account2));
        assertFalse(account2.contentEquals(account));
    }

    @Test
    public void testAddLogClearLog() throws FileNotFoundException {
        Prompt prompt1 = new Prompt(true, true, true, 50);
        prompt1.setPrompt("jeez louise");
        UserResults userResults = new UserResults();
        userResults.updateUserResults(prompt1, "hi there");
        Log log1 = new Log(prompt1, userResults);

        account.addLog(log1);
        ArrayList arrayList0 = new ArrayList<Log>();
        arrayList0.add(log1);
        assertEquals(arrayList0, account.getLogs());

        Prompt prompt2 = new Prompt(false, true, true, 503);
        prompt2.setPrompt("treez bergeez");
        UserResults userResults2 = new UserResults();
        userResults2.updateUserResults(prompt2, "hi tserghere");
        Log log2 = new Log(prompt2, userResults2);

        account.addLog(log2);
        ArrayList arrayList = new ArrayList<>();
        arrayList.add(log1);
        arrayList.add(log2);
        assertEquals(arrayList, account.getLogs());
        account.clearLogs();
        assertEquals(new ArrayList<Log>(), account.getLogs());
    }
}
