package com.example.TailoredCrochet.services;

import com.example.TailoredCrochet.dto.PartWithInstructions;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfService {

    public byte[] generatePatternPdf(String patternName, List<PartWithInstructions> partsWithInstructions) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font textFont = new Font(Font.HELVETICA, 12);

            document.add(new Paragraph("Pattern: " + patternName, titleFont));
            document.add(Chunk.NEWLINE);

            for (PartWithInstructions part : partsWithInstructions) {
                document.add(new Paragraph(part.getPartType() + " - " + part.getPartStyle(), headerFont));
                document.add(Chunk.NEWLINE);

                // Parse and format instructions
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                com.fasterxml.jackson.databind.JsonNode instructions = mapper.readTree(part.getInstructionsJson());

                for (com.fasterxml.jackson.databind.JsonNode row : instructions) {
                    String line = "Stitch: " + row.get("stitch").asText();
                    if (row.has("stitch_count")) {
                        line += ", Count: " + row.get("stitch_count").asInt();
                    }
                    if (row.has("rows")) {
                        line += ", Rows: " + row.get("rows").asInt();
                    }
                    document.add(new Paragraph(line, textFont));
                }
                document.add(Chunk.NEWLINE);
            }

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
