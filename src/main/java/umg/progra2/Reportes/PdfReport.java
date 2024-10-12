package umg.progra2.Reportes;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import umg.progra2.DataBase.Model.Producto;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class PdfReport {
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

    private final String nombreUsuario = "Rene Alejandro Osorio Gonzalez";
    private final String carnetUsuario = "0905-23-10736";

    public void generateProductReport(List<Producto> productos, String outputPath, String tituloReporte) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        document.open();

        addTitle(document, tituloReporte);
        addUserInfo(document);  // Agregar información del usuario y fecha
        addProductTable(document, productos);
        addGithubLink(document);  // Agregar enlace de GitHub

        document.close();
    }

    private void addTitle(Document document, String tituloReporte) throws DocumentException {
        Paragraph title = new Paragraph(tituloReporte, TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);
    }

    private void addUserInfo(Document document) throws DocumentException {
        String fechaHoraActual = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        Paragraph userInfo = new Paragraph(
                "Reporte generado por: " + nombreUsuario + " | Carnet: " + carnetUsuario + "\nFecha y Hora: " + fechaHoraActual,
                NORMAL_FONT
        );
        userInfo.setAlignment(Element.ALIGN_LEFT);
        document.add(userInfo);
        document.add(Chunk.NEWLINE);
    }

    private void addProductTable(Document document, List<Producto> productos) throws DocumentException {
        PdfPTable table = new PdfPTable(6); // 6 columnas: ID, Descripción, Origen, Precio, Existencia, Total
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        table.setWidths(new float[]{1f, 2f, 2f, 1.5f, 1.5f, 1.5f});  // Ancho de columnas

        addTableHeader(table);
        addRowsWithGrouping(table, productos);
        document.add(table);
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("ID", "Descripción", "Origen", "Precio", "Existencia", "Total").forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setVerticalAlignment(Element.ALIGN_MIDDLE);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(columnTitle, HEADER_FONT));
            table.addCell(header);
        });
    }

    private void addRowsWithGrouping(PdfPTable table, List<Producto> productos) {
        String currentOrigen = null;
        double groupTotal = 0.0;
        double grandTotalPrecio = 0.0;
        int grandTotalExistencia = 0;
        double grandTotal = 0.0;
        int groupTotalExistencia = 0;

        for (Producto producto : productos) {
            double total = producto.getPrecio() * producto.getExistencia();

            if (currentOrigen == null || !producto.getOrigen().equals(currentOrigen)) {
                if (currentOrigen != null) {
                    // Mostrar total del grupo anterior
                    addGroupTotalRow(table, groupTotal, groupTotalExistencia);
                }
                currentOrigen = producto.getOrigen();
                groupTotal = 0.0;
                groupTotalExistencia = 0;

                // Fila de agrupación
                PdfPCell groupCell = new PdfPCell(new Phrase("Grupo: " + currentOrigen, NORMAL_FONT));
                groupCell.setColspan(6);
                table.addCell(groupCell);
            }

            // Fila del producto
            table.addCell(new Phrase(String.valueOf(producto.getIdProducto()), NORMAL_FONT));
            table.addCell(new Phrase(producto.getDescripcion(), NORMAL_FONT));
            table.addCell(new Phrase(producto.getOrigen(), NORMAL_FONT));
            table.addCell(new Phrase(String.format("Q%.2f", producto.getPrecio()), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(producto.getExistencia()), NORMAL_FONT));
            table.addCell(new Phrase(String.format("Q%.2f", total), NORMAL_FONT));

            groupTotal += total;
            groupTotalExistencia += producto.getExistencia();
            grandTotalPrecio += producto.getPrecio();
            grandTotalExistencia += producto.getExistencia();
            grandTotal += total;
        }

        // Imprimir total del último grupo
        addGroupTotalRow(table, groupTotal, groupTotalExistencia);

        // Agregar el gran total al final del reporte
        addGrandTotalRow(table, grandTotalPrecio, grandTotalExistencia, grandTotal);
    }

    private void addGroupTotalRow(PdfPTable table, double groupTotal, int groupTotalExistencia) {
        PdfPCell totalCellLabel = new PdfPCell(new Phrase("Total del grupo", NORMAL_FONT));
        totalCellLabel.setColspan(4);
        table.addCell(totalCellLabel);

        table.addCell(new Phrase(String.valueOf(groupTotalExistencia), NORMAL_FONT));
        table.addCell(new Phrase(String.format("Q%.2f", groupTotal), NORMAL_FONT));
    }

    private void addGrandTotalRow(PdfPTable table, double grandTotalPrecio, int grandTotalExistencia, double grandTotal) {
        PdfPCell grandTotalCellLabel = new PdfPCell(new Phrase("Gran Total", HEADER_FONT));
        grandTotalCellLabel.setColspan(4);
        grandTotalCellLabel.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(grandTotalCellLabel);

        table.addCell(new Phrase(String.valueOf(grandTotalExistencia), HEADER_FONT));
        table.addCell(new Phrase(String.format("Q%.2f", grandTotal), HEADER_FONT));
    }

    // Agregar el enlace a GitHub al final del reporte
    private void addGithubLink(Document document) throws DocumentException {
        Anchor anchor = new Anchor("Enlace del proyecto: Ver en GitHub", NORMAL_FONT);
        anchor.setReference("https://github.com/TuRepositorio/Proyecto");
        Paragraph paragraph = new Paragraph();
        paragraph.add(anchor);
        document.add(paragraph);
    }
}
