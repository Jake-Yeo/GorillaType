package model;

import model.exceptions.*;
import org.junit.jupiter.api.Test;
import model.persistence.JsonReader;
import model.persistence.JsonWriter;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {

    @Test
    public void testDirectoryDoesNotExist() {
        try {
            JsonWriter jsonWriter = new JsonWriter("./fewf/faewfg/refawefawegagreg.json");
            jsonWriter.open();
            jsonWriter.saveAccountUtilsObject(new AccountUtils());
            jsonWriter.close();
            fail("IOException expected");
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            fail("IOException expected");
        } catch (FileNotFoundException e) {
            // pass
        }
    }

    @Test
    public void testSaveEmptyAccountUtils() throws InvalidBooleanSelectionException {
        try {
            JsonReader jsonReader = new JsonReader("data/","testEmptyAccountUtils.json");
            JsonWriter jsonWriter = new JsonWriter(jsonReader.getsavedFileLocation());
            AccountUtils emptyAccountUtils = new AccountUtils();
            jsonWriter.open();
            jsonWriter.saveAccountUtilsObject(emptyAccountUtils);
            jsonWriter.close();

            assertTrue(emptyAccountUtils.contentEquals(jsonReader.parseAccountUtilsJson()));
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testSaveGeneralAccountUtils() {
        try {
            JsonReader jsonReader = new JsonReader("data/","testGeneralAccountUtils.json");
            JsonWriter jsonWriter = new JsonWriter(jsonReader.getsavedFileLocation());
            AccountUtils generalAccountUtils = createAccountUtils();

            jsonWriter.open();
            jsonWriter.saveAccountUtilsObject(generalAccountUtils);
            jsonWriter.close();

            assertTrue(generalAccountUtils.contentEquals(jsonReader.parseAccountUtilsJson()));
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }

    private AccountUtils createAccountUtils() throws FileNotFoundException, InterruptedException,
            UserOrPassWrongException, SettingsException {
        AccountUtils accountUtils = new AccountUtils();
        accountUtils.getListOfAccounts().add(createAccountOne());
        accountUtils.getListOfAccounts().add(createAccountTwo());
        accountUtils.login("jack", "Lonesome");
        return accountUtils;
    }

    private Account createAccountOne() throws FileNotFoundException, InterruptedException,
            InvalidApproxLengthException {
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
            SettingsException {
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
