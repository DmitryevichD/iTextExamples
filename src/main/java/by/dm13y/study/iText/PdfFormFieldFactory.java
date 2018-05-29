package by.dm13y.study.iText;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RadioCheckField;

public interface PdfFormFieldFactory {
    PdfFormField buildCheckBox(PdfWriter writer, Rectangle rectangle);
}
