package by.dm13y.study.iText;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Service makes injections of pdf form field into a pdf-stream
 */
public interface CheckBoxInjector {
    /**
     *
     * @param in PDF file stream
     * @param formFieldFactory field which will be injecting to pdf
     * @param marker marker which defined position the checkbox in document
     * @param checkBoxWidth checkbox width
     * @return PDF file stream with injections checkboxes
     */
    ByteArrayOutputStream injectCheckBox(InputStream in, PdfFormFieldFactory formFieldFactory, String marker, int checkBoxWidth) throws IOException;
}
