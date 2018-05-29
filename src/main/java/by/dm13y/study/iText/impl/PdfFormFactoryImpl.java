package by.dm13y.study.iText.impl;

import by.dm13y.study.iText.PdfFormFieldFactory;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseField;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RadioCheckField;

import java.awt.*;

public class PdfFormFactoryImpl implements PdfFormFieldFactory {
    @Override
    public PdfFormField buildCheckBox(PdfWriter writer, Rectangle rectangle) {
        RadioCheckField checkbox = new RadioCheckField(writer, rectangle, "checkBox", "Yes");
        checkbox.setCheckType(RadioCheckField.TYPE_CHECK);
        checkbox.setChecked(false);
        checkbox.setBorderWidth(BaseField.BORDER_WIDTH_THIN);
        checkbox.setBorderColor(Color.BLACK);
        checkbox.setBackgroundColor(Color.WHITE);
        PdfFormField checkboxField = null;
        try {
            checkboxField = checkbox.getCheckField();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return checkboxField;
    }
}
