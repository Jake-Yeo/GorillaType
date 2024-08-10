package model;

import model.exceptions.*;
import org.junit.jupiter.api.Test;
import model.persistence.JsonReader;
import model.persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    @Test
    public void testFileDoesNotExist() {
        JsonReader jsonReader = new JsonReader("./aefawef/agaerfse/rgsregv");
        try {
            jsonReader.parseAccountUtilsJson();
            fail("FileNotFoundException expected");
        } catch (IOException ioException) {
            // Success
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }

    @Test
    public void testReadEmptyAccountUtils() throws InvalidBooleanSelectionException {
        JsonReader jsonReader = new JsonReader("data/", "testEmptyAccountUtils.json");
        try {
            JsonWriter jsonWriter = new JsonWriter(jsonReader.getsavedFileLocation());
            jsonWriter.open();
            jsonWriter.saveAccountUtilsObject(new AccountUtils());
            jsonWriter.close();
            AccountUtils accountUtils = jsonReader.parseAccountUtilsJson();
            assertTrue(accountUtils.contentEquals(new AccountUtils()));
        } catch (Exception ex) {
            fail("Exception not expected.");
        }
    }

    @Test
    public void testSavedFileLocation() {
        JsonReader jsonReader = new JsonReader("data/", "testEmptyAccountUtils.json");
        jsonReader.setSavedFileLocation("ahhhh");
        assertEquals("ahhhhGorillaType.json", jsonReader.getsavedFileLocation());
    }

    @Test
    public void testReadGeneralAccountUtils() throws IOException,
            UserOrPassWrongException, InterruptedException, SettingsException {
        JsonReader jsonReader = new JsonReader("data/", "testGeneralAccountUtils.json");
        JsonWriter jsonWriter = new JsonWriter(jsonReader.getsavedFileLocation());
        AccountUtils accountUtils = createAccountUtils();
        jsonWriter.open();
        jsonWriter.saveAccountUtilsObject(accountUtils);
        jsonWriter.close();
        AccountUtils parsedAccountUtils = null;
        parsedAccountUtils = jsonReader.parseAccountUtilsJson();
            assertTrue(accountUtils.contentEquals(parsedAccountUtils));
    }

    private AccountUtils createAccountUtils() throws FileNotFoundException, InterruptedException,
            UserOrPassWrongException, SettingsException {
        AccountUtils accountUtils = new AccountUtils();
        accountUtils.getListOfAccounts().add(createAccountOne());
        accountUtils.getListOfAccounts().add(createAccountTwo());
        accountUtils.login("joey", "boeyd");
        return accountUtils;
    }

    private Account createAccountOne() throws FileNotFoundException, InterruptedException,
            SettingsException {
        Account account1 = new Account("joey", "boeyd");
        account1.getSettings().setApproxLength(500);
        account1.getSettings().setAreNumbersEnabled(true);
        account1.getSettings().setAreWordsEnabled(false);
        account1.getSettings().setAreSentencesEnabled(true);

        TypingTestUtils typingTestUtils = new TypingTestUtils(true);
        UserResults userResults = new UserResults();
        Prompt prompt = new Prompt(true, false, true, 500);
        String userInputString = "";
        for (int i = 0; i < prompt.getActualLength(); i++) {
            userInputString += "a";
        }
        Thread.sleep(250);
        userResults.updateUserResults(prompt, userInputString);

        account1.addLog(new Log(prompt, userResults));
        return account1;
    }

    private Account createAccountTwo() throws FileNotFoundException, InterruptedException,
            InvalidApproxLengthException {
        Account account2 = new Account("jack", "Lonesome");
        account2.getSettings().setApproxLength(1000);
        account2.getSettings().setAreNumbersEnabled(false);
        account2.getSettings().setAreWordsEnabled(true);
        account2.getSettings().setAreSentencesEnabled(false);

        TypingTestUtils typingTestUtils = new TypingTestUtils(true);
        UserResults userResults = new UserResults();
        Prompt prompt = new Prompt(false, true, false, 1000);
        String userInputString = "";
        for (int i = 0; i < prompt.getActualLength(); i++) {
            userInputString += "b";
        }
        Thread.sleep(300);
        userResults.updateUserResults(prompt, userInputString);

        UserResults userResults2 = new UserResults();
        Prompt prompt2 = new Prompt(false, true, false, 1000);
        String userInputString2 = "";
        for (int i = 0; i < prompt.getActualLength(); i++) {
            userInputString2 += "c";
        }
        Thread.sleep(200);
        userResults2.updateUserResults(prompt2, userInputString2);

        account2.addLog(new Log(prompt, userResults));
        account2.addLog(new Log(prompt2, userResults2));
        return account2;
    }
}
