package com.example.TailoredCrochet.controllers;

import com.example.TailoredCrochet.dto.*;
import com.example.TailoredCrochet.models.GarmentType;
import com.example.TailoredCrochet.services.PatternService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patterns")
public class PatternController {
    private final PatternService patternService;

    public PatternController(PatternService patternService) {
        this.patternService = patternService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPattern(@RequestBody CreatePatternRequest request) {
        try {
            patternService.createPattern(request);
            return ResponseEntity.ok("Pattern created successfully.");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<PatternDTO>> getPatterns(
            @RequestParam("email") String email )
    {
        try {
            List<PatternDTO> patternDTOS = patternService.getPatterns(email);
            return ResponseEntity.ok(patternDTOS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get-garment-types")
    public ResponseEntity<List<GarmentTypeDTO>> getGarmentTypes()
    {
        try {
            List<GarmentTypeDTO> garmentTypes = patternService.getGarmentTypes();
            return ResponseEntity.ok(garmentTypes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get-parts")
    public ResponseEntity<List<PartDTO>> getPartsForGarment(
            @RequestParam("garmentName") String garmentName
    ) {
        List<PartDTO> parts = patternService.getPartsForGarment(garmentName);
        return ResponseEntity.ok(parts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePattern(@PathVariable Long id) {
        patternService.deletePattern(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePatternPdf(@RequestParam("patternId") Long patternId,
                                                     @RequestParam("patternName") String patternName) {
        try {
            byte[] pdfBytes = patternService.generatePatternPdf(patternId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("pattern.pdf").build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().build();
        }
    }


}
