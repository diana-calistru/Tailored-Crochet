package com.example.TailoredCrochet.services;

import com.example.TailoredCrochet.dto.*;
import com.example.TailoredCrochet.models.*;
import com.example.TailoredCrochet.repositories.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PatternService {

    private final PatternRepository patternRepo;
    private final PartRepository partRepo;
    private final MeasurementProfileRepository measurementProfileRepo;
    private final GarmentTypeRepository garmentTypeRepository;
    private final PdfService pdfService;
    private final UserRepository userRepository;
    private final SwatchRepository swatchRepository;

    public PatternService(PatternRepository patternRepo, PartRepository patternPartRepo,
                          MeasurementProfileRepository profileRepo, GarmentTypeRepository garmentTypeRepository,
                          PdfService pdfService, UserRepository userRepository,
                          SwatchRepository swatchRepository) {
        this.patternRepo = patternRepo;
        this.partRepo = patternPartRepo;
        this.measurementProfileRepo = profileRepo;
        this.garmentTypeRepository = garmentTypeRepository;
        this.pdfService = pdfService;
        this.userRepository = userRepository;
        this.swatchRepository = swatchRepository;
    }

    public void createPattern(CreatePatternRequest request) {
        User user = userRepository.findByEmail(request.getUserEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Measurement Profile
        MeasurementProfile profile = measurementProfileRepo.findByUserIdAndProfileName(
                        user.getId(), request.getMeasurementProfileName())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // Swatch
        Swatch swatch = new Swatch();
        swatch.setWidth(request.getSwatch().getWidth());
        swatch.setHeight(request.getSwatch().getHeight());
        swatch.setRows(request.getSwatch().getRows());
        swatch.setStitches(request.getSwatch().getStitches());
        swatch.setUser(user);
        Swatch savedSwatch = swatchRepository.save(swatch);

        Pattern pattern = new Pattern();
        pattern.setPatternName(request.getPatternName());
        pattern.setUser(user);
        pattern.setSwatch(savedSwatch);
        pattern.setMeasurementProfile(profile);

        // Get EXISTING parts from repository
        Set<Part> parts = request.getSelectedParts().stream()
                .map(partReq -> partRepo.findByPartTypeAndPartStyle(
                                partReq.getPartType(),
                                partReq.getPartStyle())
                        .orElseThrow(() -> new RuntimeException(
                                "Part not found: " + partReq.getPartType() + " - " + partReq.getPartStyle()))
                )
                .collect(Collectors.toSet());

        pattern.setParts(parts); // Set the parts collection

        patternRepo.save(pattern);
    }


    public List<PatternDTO> getPatterns(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Pattern> patterns = patternRepo.findByUserIdWithParts(user.getId()); // New custom query

        return patterns.stream().map(pattern -> {
            PatternDTO dto = new PatternDTO();
            dto.setId(pattern.getId());
            dto.setPatternName(pattern.getPatternName());
            dto.setMeasurementProfileName(pattern.getMeasurementProfile().getProfileName());// Add if needed

            List<PartDTO> partDTOs = pattern.getParts().stream().map(part -> {
                PartDTO partDTO = new PartDTO();
                partDTO.setId(part.getId()); // Add ID if needed
                partDTO.setPartType(part.getPartType());
                partDTO.setPartStyle(part.getPartStyle());
                partDTO.setImageUrl(part.getImageUrl()); // Add image URL
                return partDTO;
            }).toList();

            dto.setParts(partDTOs);
            return dto;
        }).toList();
    }

    public List<GarmentTypeDTO> getGarmentTypes() {
        List<GarmentType> garmentTypes = garmentTypeRepository.findAll();
        return garmentTypes.stream()
                .map(GarmentTypeDTO::new)
                .collect(Collectors.toList());
    }

    public List<PartDTO> getPartsForGarment(String garmentName) {
        GarmentType garmentType = garmentTypeRepository.findByGarmentNameWithParts(garmentName)
                .orElseThrow(() -> new RuntimeException("Garment type not found: " + garmentName));

        return garmentType.getParts().stream()
                .map(part -> {
                    PartDTO dto = new PartDTO();
                    dto.setId(part.getId());
                    dto.setPartType(part.getPartType());
                    dto.setPartStyle(part.getPartStyle());
                    dto.setImageUrl(part.getImageUrl());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    public void deletePattern(Long id) {
        if (!patternRepo.existsById(id)) {
            throw new RuntimeException("Pattern not found");
        }
        patternRepo.deleteById(id);
    }

    private Map<String, Double> buildVars(MeasurementProfile mp, Swatch swatch) {
        Map<String, Double> vars = new HashMap<>();
        vars.put("chest_circumference", mp.getChestCirc());
        vars.put("torso_length", mp.getTorsoLength());
        vars.put("neck_circumference", mp.getNeckCirc());
        vars.put("armhole_opening", computeArmholeOpening(mp));
        vars.put("sleeve_length", mp.getArmLength());
        vars.put("swatch_stitch_width", swatch.getWidth() / swatch.getStitches());
        vars.put("swatch_row_height", swatch.getHeight() / swatch.getRows());
        return vars;
    }

    private double computeArmholeOpening(MeasurementProfile mp) {
        return (mp.getChestCirc() / 2) * 0.35; // example formula
    }

    public String generatePartInstructions(String templateJson, Map<String, Double> vars) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode template = mapper.readTree(templateJson);
        ArrayNode output = mapper.createArrayNode();

        for (JsonNode row : template.get("rows")) {
            ObjectNode rowOut = mapper.createObjectNode();
            rowOut.put("stitch", row.get("stitch").asText());

            if (row.has("stitch_count_formula")) {
                String formula = row.get("stitch_count_formula").asText();
                double count = evalFormula(formula, vars);
                rowOut.put("stitch_count", Math.round(count));
            }
            if (row.has("rows_formula")) {
                String formula = row.get("rows_formula").asText();
                double rowsN = evalFormula(formula, vars);
                rowOut.put("rows", Math.round(rowsN));
            }
            output.add(rowOut);
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(output);
    }

    private double evalFormula(String formula, Map<String, Double> vars) {
        for (Map.Entry<String, Double> e : vars.entrySet()) {
            formula = formula.replace(e.getKey(), e.getValue().toString());
        }
        return new net.objecthunter.exp4j.ExpressionBuilder(formula).build().evaluate();
    }


    public byte[] generatePatternPdf(Long patternId) {
        // Get pattern
        Pattern pattern = patternRepo.findById(patternId)
                .orElseThrow(() -> new RuntimeException("Pattern not found"));

        // Get user
        User user = pattern.getUser();

        // Get measurement profile
        MeasurementProfile mp = pattern.getMeasurementProfile();

        // Build variables map
        Map<String, Double> vars = buildVars(mp, pattern.getSwatch());

        List<PartWithInstructions> partsWithInstructions = new ArrayList<>();

        for (Part pr : pattern.getParts()) {
            Part part = partRepo.findByPartTypeAndPartStyle(pr.getPartType(), pr.getPartStyle())
                    .orElseThrow(() -> new RuntimeException("Part not found: " + pr.getPartType() + " " + pr.getPartStyle()));

            String instructions = part.getInstructions();
            if (instructions == null) {
                throw new RuntimeException("Template missing for part: " + pr.getPartType() + " " + pr.getPartStyle());
            }

            String generatedInstructions = null;
            try {
                generatedInstructions = generatePartInstructions(instructions, vars);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            partsWithInstructions.add(new PartWithInstructions(pr.getPartType(), pr.getPartStyle(), generatedInstructions));
        }

        return pdfService.generatePatternPdf(pattern.getPatternName(), partsWithInstructions);
    }


}
