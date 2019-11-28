package com.compucar.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PdfCreator {

    public void createPdfFile() throws FileNotFoundException, DocumentException {
        // Se crea el documento
        Document document = new Document();

        // Se crea el OutputStream para el fichero donde queremos dejar el pdf.
        FileOutputStream pdfFile = new FileOutputStream("report.pdf");

        // Se asocia el documento al OutputStream y se indica que el espaciado entre
        // lineas sera de 20. Esta llamada debe hacerse antes de abrir el documento
        PdfWriter.getInstance(document, pdfFile).setInitialLeading(20);

        // Se abre el documento.
        document.open();

        PdfPTable table = new PdfPTable(10);
        for (int i = 0; i < 10; i++)
        {
            table.addCell("156");
            table.addCell("G532RF");
            table.addCell("2019-09-18T16:01:57");
            table.addCell("45");
            table.addCell("1500.0");
            table.addCell("6345");
            table.addCell("834");
            table.addCell("KJ54");
            table.addCell("MF72");
//            table.addCell(
//                    new Paragraph("Event: es un evento \nDiagnose: cambio el resultado del evento"),
//                    new Paragraph("Event: es otro evento \nDiagnose: otro es el resultado del evento"),
//                    new Paragraph("Event: \nDiagnose: otro es el resultado del evento")
//            );
        }
    }
}
