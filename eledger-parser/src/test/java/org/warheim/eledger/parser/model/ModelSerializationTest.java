package org.warheim.eledger.parser.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author andy
 */
public class ModelSerializationTest {
    
    public ModelSerializationTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void checkJsonSerialization() throws Exception {
        NotificationsData not = MockData.createTestData(0);
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
        .setPrettyPrinting().create();
        String retval = gson.toJson(not);
        System.out.println(retval);

        gson = new Gson();
        Type type = new TypeToken<NotificationsData>(){}.getType();
        NotificationsData not2 = gson.fromJson(retval, type);
        System.out.println(not2.showAll());

    }

}
