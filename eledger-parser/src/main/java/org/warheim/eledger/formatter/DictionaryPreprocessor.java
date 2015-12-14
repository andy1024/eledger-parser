package org.warheim.eledger.formatter;

import org.warheim.formatter.Preprocessor;
import org.warheim.formatter.PreprocessorException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.warheim.di.Cacheable;

/**
 *
 * @author andy
 */
public class DictionaryPreprocessor implements Preprocessor, Cacheable {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DictionaryPreprocessor.class);
    private Map<String, String> dict;
    
    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    public DictionaryPreprocessor() {}
    
    public void init() throws FileNotFoundException, IOException {
        dict = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine())!=null) {
                String[] elems = line.split(";");
                dict.put(elems[0], elems[1]);
            }
        }
    }
    
    @Override
    public String process(String input) throws PreprocessorException {
        if (dict==null) {
            try {
                init();
            } catch (IOException ex) {
                logger.error("Reading dictionary failed", ex);
                throw new PreprocessorException(ex);
            }
        }
        String x = input;
        for (String key: dict.keySet()) {
            logger.debug("input " + x + " contains " + key + " = " + x.toLowerCase().contains(key.toLowerCase()));
            x = x.replaceAll("(?i)"+key, dict.get(key));
        }
        return x;
    }
    
}
