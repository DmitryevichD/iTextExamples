package by.dm13y.study.iText.impl;

import by.dm13y.study.iText.CheckBoxInjector;
import by.dm13y.study.iText.PdfFormFieldFactory;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;

import static org.junit.Assert.*;

public class CheckBoxInjectorImplTest {
    @Test
    public void injectCheckBox() throws Exception {
        String src_pdf = "pdf_form_source.pdf";
        String out_pdf = "pdf_form_output.pdf";
        FileInputStream fileInputStream = new FileInputStream(src_pdf);

        PdfFormFieldFactory formFieldFactory = new PdfFormFactoryImpl();

        CheckBoxInjector checkBoxInjector = new CheckBoxInjectorImpl();
        ByteArrayOutputStream byteArrayOutputStream = checkBoxInjector.injectCheckBox(fileInputStream, formFieldFactory, "(])", 20);
        byteArrayOutputStream.writeTo(new FileOutputStream(out_pdf));

    }

}