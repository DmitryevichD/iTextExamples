package by.dm13y.study.iText.impl;

import by.dm13y.study.iText.CheckBoxInjector;
import by.dm13y.study.iText.PdfFormFieldFactory;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import org.olap4j.impl.ArrayMap;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CheckBoxInjectorImpl implements CheckBoxInjector {
    private Pattern blockTextPattern = Pattern.compile("BT|ET");

    /**
     * {@link CheckBoxInjector#injectCheckBox(InputStream, by.dm13y.study.iText.PdfFormFieldFactory, String, int)}
     */
    @Override
    public ByteArrayOutputStream injectCheckBox(InputStream in, PdfFormFieldFactory pdfFormFieldFactory, String marker, int checkBoxWidth) throws IOException {
        PdfReader reader = new PdfReader(in);
        Map<Integer, List<MarkerPosition>> foundPositions = new ArrayMap<>();
        for(int i = 1; i <= reader.getNumberOfPages(); i++) {
            PdfDictionary dict = reader.getPageN(i);
            PdfObject pdfObject = dict.getDirectObject(PdfName.CONTENTS);
            if (pdfObject instanceof PRStream) {
                foundPositions.put(i, parsePageContent(((PRStream) pdfObject), marker));
            }
        }
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()){
            PdfStamper stamper = new PdfStamper(reader, out);
            PdfWriter writer = stamper.getWriter();
            for (Map.Entry<Integer, List<MarkerPosition>> pagePosInject : foundPositions.entrySet()) {
                pagePosInject.getValue().forEach(pos -> {
                    PdfFormField checkBox = pdfFormFieldFactory.buildCheckBox(writer, pos.getCheckBoxRectangle(checkBoxWidth));
                    stamper.addAnnotation(checkBox, pagePosInject.getKey());
                });
            }
            stamper.close();
            return out;
        }catch (DocumentException ex){
            ex.printStackTrace();
        }
        throw new IOException("");
    }


    /**
     * Method doing stream scanning, find string marker, and getting it position
     * @param stream pdf stream
     * @param stringMarker marker
     * @return list of found positions
     */
    private List<MarkerPosition> parsePageContent(PRStream stream, String stringMarker) throws IOException{
        String pdfBlockTextMarker = "BT|ET";
        String sourceString = new String(PdfReader.getStreamBytes(stream));
        return Arrays.stream(sourceString.split(pdfBlockTextMarker))
                .filter(str -> str.contains(stringMarker))
                .map(str -> str.split(MarkerPosition.MATRIX_OPERATOR)[0].replaceAll("\n", ""))
                .map(MarkerPosition::new)
                .collect(Collectors.toList());
    }

    /**
     * Helper class to store the coordinates of the marker
     */
    private static class MarkerPosition {
        private final static String MATRIX_OPERATOR = "Tm";
        private final double rotation;
        private final double scalling;
        private final double skewing;
        private final double occurs;
        private final double posX;
        private final double posY;
        /**
         * @param pdfTextPositionOperator - Text space is the coordinate system in which text is shown.
         * @see <a href="https://wwwimages2.adobe.com/content/dam/acom/en/devnet/pdf/pdf_reference_archive/pdf_reference_1-7.pdf">pdf ref - 5.3.1</a>
         *
         */
        public MarkerPosition(String pdfTextPositionOperator){
            double[] matrix = Arrays.stream(pdfTextPositionOperator.split(" "))
                    .mapToDouble(Double::parseDouble)
                    .toArray();

            rotation = matrix[0];
            scalling = matrix[1];
            skewing = matrix[2];
            occurs = matrix[3];
            posX = matrix[4];
            posY = matrix[5];
        }

        public Rectangle getCheckBoxRectangle(int checkBoxWidth){
            int checkBoxHeight = checkBoxWidth;
            return new Rectangle((int)posX, (int)posY, (int)posX + checkBoxWidth, (int)posY + checkBoxHeight);
        }
    }
}
