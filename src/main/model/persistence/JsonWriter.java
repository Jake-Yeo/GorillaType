package model.persistence;

import model.AccountUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;

public class JsonWriter {

    private PrintWriter printWriter;
    private String saveLocation;

    // Effects: Creates a json writer that will save json objects to the given save location
    public JsonWriter(String saveLocation) {
        this.saveLocation = saveLocation;
    }

    public void open() throws FileNotFoundException {
        printWriter = new PrintWriter(saveLocation);
    }


    // Effects: Opens the JsonWriter, saves the object. It will throw a
    // FileLocationNotFoundException if the saveLocation does not exist.
    public void saveAccountUtilsObject(AccountUtils accountUtils) {
        JSONObject accountUtilsJson = accountUtils.toJson();
        printWriter.print(accountUtilsJson.toString());
    }

    //Effects: closes the Json writer
    public void close() {
        printWriter.close();
    }
}
