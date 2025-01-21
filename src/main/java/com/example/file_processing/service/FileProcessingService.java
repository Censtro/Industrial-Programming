package com.example.file_processing.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileProcessingService {

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = new XmlMapper();
    private final YAMLMapper yamlMapper = new YAMLMapper();

    public String processFile(MultipartFile file) {
        try {
            String content = new String(file.getBytes());
            String contentType = file.getContentType();
            String originalFilename = file.getOriginalFilename();


            if (contentType == null) {

                contentType = guessContentTypeByExtension(originalFilename);
            }

            if (contentType != null) {
                if (contentType.contains("json")) {
                    return processJson(content);
                } else if (contentType.contains("xml")) {
                    return processXml(content);
                } else if (contentType.contains("yaml") || contentType.contains("yml")) {
                    return processYaml(content);
                }
            }


            return processTxt(content);

        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading file: " + e.getMessage();
        }
    }


    private String processTxt(String fileContent) {
        System.out.println("Received TXT file content: \n" + fileContent);

        if (fileContent == null || fileContent.isBlank()) {
            return "The file is empty or contains only whitespace.";
        }

        try {
            Pattern pattern = Pattern.compile("(\\d+\\s*[+\\-*/]\\s*\\d+(\\s*[+\\-*/]\\s*\\d+)*)");
            Matcher matcher = pattern.matcher(fileContent);

            StringBuilder result = new StringBuilder();
            while (matcher.find()) {
                String expression = matcher.group();
                System.out.println("Found expression: " + expression);

                double calculatedValue = evaluateExpression(expression);
                result.append(expression)
                        .append(" = ")
                        .append(calculatedValue)
                        .append("\n");
            }

            if (result.length() == 0) {
                return "No mathematical expressions found in the file.";
            }

            return result.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error while processing file: " + e.getMessage();
        }
    }


    private String processJson(String jsonContent) {
        try {
            // Парсим JSON в виде Map (или List, если корень — массив)
            Object obj = jsonMapper.readValue(jsonContent, Object.class);

            // Рекурсивно обходим структуру, ищем строки-выражения
            List<String> results = new ArrayList<>();
            traverseAndEvaluate(obj, results);


            if (results.isEmpty()) {
                return "No mathematical expressions found in JSON.";
            } else {
                return String.join("\n", results);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing JSON: " + e.getMessage();
        }
    }


    private String processXml(String xmlContent) {
        try {
            Object obj = xmlMapper.readValue(xmlContent, Object.class);

            List<String> results = new ArrayList<>();
            traverseAndEvaluate(obj, results);

            if (results.isEmpty()) {
                return "No mathematical expressions found in XML.";
            } else {
                return String.join("\n", results);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing XML: " + e.getMessage();
        }
    }


    private String processYaml(String yamlContent) {
        try {
            Object obj = yamlMapper.readValue(yamlContent, Object.class);

            List<String> results = new ArrayList<>();
            traverseAndEvaluate(obj, results);

            if (results.isEmpty()) {
                return "No mathematical expressions found in YAML.";
            } else {
                return String.join("\n", results);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing YAML: " + e.getMessage();
        }
    }

    @SuppressWarnings("unchecked")
    private void traverseAndEvaluate(Object node, List<String> results) {
        if (node == null) {
            return;
        }
        if (node instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) node;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                traverseAndEvaluate(entry.getValue(), results);
            }
        } else if (node instanceof List) {
            List<Object> list = (List<Object>) node;
            for (Object item : list) {
                traverseAndEvaluate(item, results);
            }
        } else if (node instanceof String) {
            String str = (String) node;
            if (containsOperators(str)) {
                try {
                    double value = evaluateExpression(str);
                    results.add(str + " = " + value);
                } catch (Exception e) {
                    System.err.println("Cannot evaluate expression: " + str);
                }
            }
        }
    }

    private boolean containsOperators(String str) {
        return str.matches(".*[+\\-*/].*\\d.*");
    }

    private double evaluateExpression(String expression) {
        Expression exp = new ExpressionBuilder(expression)
                .build();
        return exp.evaluate();
    }

    private String guessContentTypeByExtension(String filename) {
        if (filename == null) return null;
        String lower = filename.toLowerCase();
        if (lower.endsWith(".json")) {
            return "application/json";
        } else if (lower.endsWith(".xml")) {
            return "application/xml";
        } else if (lower.endsWith(".yaml") || lower.endsWith(".yml")) {
            return "application/x-yaml";
        } else if (lower.endsWith(".txt")) {
            return "text/plain";
        }
        return null;
    }
}
