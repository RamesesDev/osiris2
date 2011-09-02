/*
 * TestConvert.java
 * Created on June 28, 2011, 10:29 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package test;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;


/**
 *
 * @author jzamss
 */
public class TestConvert {
    
    /** Creates a new instance of TestConvert */
    public TestConvert() {
    }
    
    public static void main(String[] args) throws Exception{
         try {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("D://testpdf.pdf"));
            document.open();
            document.addAuthor("Author of the Doc");
            document.addCreator("Creator of the Doc");
            document.addSubject("Subject of the Doc");
            document.addCreationDate();
            document.addTitle("This is the title");
//SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
//SAXmyHtmlHandler shh = new SAXmyHtmlHandler(document);
            HTMLWorker htmlWorker = new HTMLWorker(document);
            String str = "<html><head><title>titlu</title></head><body><table border=\"1\"><tr><td><p style=’font-size: 10pt; font-family: Times’>" +
                    "Cher Monsieur,</p><br><p align=’justify’ style=’text-indent: 2em; font-size: 10pt; font-family: Times’>" +
                    "asdasdasdsadas<br></p><p align=’justify’ style=’text-indent: 2em; font-size: 10pt; font-family: Times’>" +
                    "En vous remerciant &agrave; nouveau de la confiance que vous nous t&eacute;moignez,</p>" +
                    "<br><p style=’font-size: 10pt; font-family: Times’>Bien Cordialement,<br>" +
                    "<br>ADMINISTRATEUR ADMINISTRATEUR<br>Ligne directe : 04 42 91 52 10<br>Acadomia&reg; – " +
                    "37 BD Aristide Briand  – 13100 Aix en Provence  </p></td></tr></table></body></html>";
            htmlWorker.parse(new StringReader(str));
            document.close();
        } catch(DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
            
}
