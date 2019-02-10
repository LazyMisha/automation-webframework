package com.project.ddt;

import com.project.logger.Log;
import org.testng.annotations.DataProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.project.utils.FileSystem.getDataProviderPath;

public class DataProviderManager {

    /**
     * for use data provider you must create file
     * in '/data' package and give name as test name that use this data file
     * */
    @DataProvider(name = "testData")
    public Object[][] provider(Method method) {
        String pathToFile = getDataProviderPath(method.getName());
        Log.info("DDT file = " + pathToFile);
        List<String> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                records.add(line);
            }
        } catch (IOException e) {
            Log.error("File not found - '" + pathToFile + "'");
        }
        Object[][] objects = new Object[records.size()][1];
        for (int i = 0; i < records.size(); i++) {
            objects[i][0] = records.get(i);
        }
        return objects;
    }
}
