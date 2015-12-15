package org.warheim.formatter;

import java.io.File;

/**
 *
 * @author andy
 */
public class FormattedDocument {
    private File file;
    private String contentType;

    public FormattedDocument(File file, String contentType) {
        this.file = file;
        this.contentType = contentType;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    
}
