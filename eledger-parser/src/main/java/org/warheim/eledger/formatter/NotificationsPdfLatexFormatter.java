package org.warheim.eledger.formatter;

import org.warheim.formatter.FormattingException;
import org.warheim.formatter.Formatter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import static org.warheim.eledger.formatter.LatexEscaper.escape;
import org.warheim.eledger.parser.Config;
import org.warheim.eledger.parser.model.InfoOnSubject;
import org.warheim.eledger.parser.model.Message;
import org.warheim.eledger.parser.model.Subject;
import org.warheim.eledger.parser.model.User;

/**
 * Notifications formatter that uses external PdfLaTeX installation
 * Use only if you meet its requirements (see README.MD)
 *
 * @author andy
 */
public class NotificationsPdfLatexFormatter extends NotificationsFreeRollFormatter {

    //values with defaults
    protected String fontSize="5pt";
    protected String width="54mm";
    protected String left="0.4cm";
    protected String right="0.3cm";
    protected String top="0.2cm";
    protected String bottom="0.8cm";
    protected String minimalHeight="2cm";
    protected String internalVerticalMargin="0.8cm";
    protected String strech="0.5";
    protected String languagePackage="polski";
    
    protected Integer maxContentLength = 256;

    public void setMaxContentLength(Integer maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    public void setMaxContentLength(String sMaxContentLength) {
        setMaxContentLength(Integer.parseInt(sMaxContentLength));
    }

    public NotificationsPdfLatexFormatter() {
    }

    @Override
    protected void addSeparator(StringBuilder str, SepType sepType) {
        switch (sepType) {
            case THIN : str.append("\\makebox[\\linewidth]{\\rule{\\paperwidth}{0.3pt}}\n");
                break;
            case NORMAL : str.append("\\makebox[\\linewidth]{\\rule{\\paperwidth}{0.5pt}}\n");
                break;
            case BOLD : str.append("\\makebox[\\linewidth]{\\rule{\\paperwidth}{0.6pt}}\n");
                break;
        }
        str.append("\\newline\n");
    }
    //TODO: implement some kind of translation dictionary to shorten common subject names and other repetitive elements
    
    @Override
    protected void makeHeader(StringBuilder str) throws FormattingException {
        str.append("\\documentclass{article}\n");
        str.append("\\usepackage{geometry}\n");
        str.append("\\geometry{paperheight=\\maxdimen,paperwidth=").append(width)
                .append(",left=").append(left)
                .append(",right=").append(right)
                .append(",top=").append(top)
                .append(",bottom=").append(bottom)
                .append("}\n");
        str.append("\\usepackage[utf8]{inputenc}\n");
        str.append("\\usepackage{").append(languagePackage).append("}\n");
        str.append("\\usepackage{setspace}\n");
        str.append("\\usepackage{marvosym}\n");
        str.append("\\setstretch{").append(strech).append("}\n");
        str.append("\\begin{document}\n");
        str.append("\\setbox0=\\vbox{\n");
        str.append("\\setlength\\parindent{0pt}\n");
    }

    @Override
    protected void makeFooter(StringBuilder str) throws FormattingException {
        str.append("}\n");
        str.append("\\dimen0=\\dp0\n");
        str.append("\\pdfpageheight=\\dimexpr\\ht0+").append(internalVerticalMargin).append("\\relax\n");
        str.append("\\ifdim\\pdfpageheight<").append(minimalHeight).append("\\pdfpageheight=").append(minimalHeight).append(" \\fi\n");
        str.append("\\unvbox0\\kern-\\dimen0\n");
        str.append("\\end{document}\n");
    }

    @Override
    protected File prepareSourceDocument(StringBuilder str) throws IOException, FormattingException, InterruptedException {
        File tempFile = File.createTempFile(this.getClass().getName(), ".tex"); 
        try (PrintWriter pw = new PrintWriter(tempFile, "UTF-8")) {
            pw.println(str.toString());
        }
        Process p = Runtime.getRuntime().exec("pdflatex -output-directory /tmp " + tempFile.getAbsolutePath());
        p.waitFor(60, TimeUnit.SECONDS);
        String outname = tempFile.getPath().replace(".tex", ".pdf");
        return new File(outname);
    }
    
    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }

    public String getMinimalHeight() {
        return minimalHeight;
    }

    public void setMinimalHeight(String minimalHeight) {
        this.minimalHeight = minimalHeight;
    }

    public String getInternalVerticalMargin() {
        return internalVerticalMargin;
    }

    public void setInternalVerticalMargin(String internalVerticalMargin) {
        this.internalVerticalMargin = internalVerticalMargin;
    }

    public String getStrech() {
        return strech;
    }

    public void setStrech(String strech) {
        this.strech = strech;
    }

    public String getLanguagePackage() {
        return languagePackage;
    }

    public void setLanguagePackage(String languagePackage) {
        this.languagePackage = languagePackage;
    }

    @Override
    protected void putUser(StringBuilder str, User user) {
        str.append("\\Info "); //man icon
        str.append("\\textbf{\\textsf{").append(user.getFullname()).append("}}\n");
        str.append("\\newline");
    }

    @Override
    protected void putSubject(StringBuilder str, Subject subject) {
        str.append("\\textbf{").append(escape(subject.getName())).append("}\n");
        str.append("\\newline\n");
    }

    @Override
    protected void putInfoOnSubject(StringBuilder str, InfoOnSubject info) {
        switch (info.getType()) {
            case TASK: str.append("\\Writinghand"); //writing hand icon
                break;
            case TEST: str.append("\\Clocklogo"); //clock icon
                break;
        }

        str.append("\\textsl{\\textsf{\\small{").append(info.getDate()).append("}}} ").append(escape(info.getContent()))
           .append("\n");
        str.append("\\newline\n");
    }

    @Override
    protected void putMessage(StringBuilder str, Message msg) {
        str.append("\\Letter"); //letter icon
        str.append("\\textsl{\\textsf{\\small{").append(msg.getDate()).append("}}} ");
        str.append("\\textsl{\\small{").append(msg.getSender()).append("}} ");
        str.append("\\textsf{").append(escape(msg.getTitle())).append("} ");
        if (Config.get(Config.KEY_MULTIPLE_RECIPIENTS).equals(msg.getRecipients())) {
            str.append("$\\infty$ ");
        } else {
            str.append("\\textsl{").append(escape(msg.getRecipients())).append("} ");
        }

        if (maxContentLength!=null && msg.getContent().length()>maxContentLength) {
        str.append(escape(msg.getContent().substring(0, maxContentLength)))
           .append("...")
           .append("\n");
        } else {
        str.append(escape(msg.getContent()))
           .append("\n");
        }
        str.append("\\newline\n");
    }

    
}
