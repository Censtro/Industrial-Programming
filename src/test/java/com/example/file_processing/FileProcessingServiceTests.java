package com.example.file_processing;

import com.example.file_processing.service.FileProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileProcessingServiceTests {

    private FileProcessingService fileProcessingService;

    @BeforeEach
    void setUp() {
        fileProcessingService = new FileProcessingService();
    }

    @Test
    void testProcessTxtFile_ValidExpressions() throws Exception {
        // Arrange
        String content = "3 + 5\n10 - 2 * 3\n";
        MockMultipartFile txtFile = new MockMultipartFile(
                "file", "test.txt", "text/plain", content.getBytes()
        );

        String expected = "3 + 5 = 8.0\n10 - 2 * 3 = 4.0\n";

        // Act
        String result = fileProcessingService.processFile(txtFile);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void testProcessTxtFile_NoExpressions() throws Exception {
        // Arrange
        String content = "Hello World!\nNo math here.";
        MockMultipartFile txtFile = new MockMultipartFile(
                "file", "test.txt", "text/plain", content.getBytes()
        );

        // Act
        String result = fileProcessingService.processFile(txtFile);

        // Assert
        assertEquals("No mathematical expressions found in the file.", result.trim());
    }

    @Test
    void testProcessJsonFile_ValidExpressions() throws Exception {
        // Arrange
        String content = "{\"expression1\": \"3 * 4\", \"expression2\": \"7 - 2\"}";
        MockMultipartFile jsonFile = new MockMultipartFile(
                "file", "test.json", "application/json", content.getBytes()
        );

        // Act
        String result = fileProcessingService.processFile(jsonFile);

        // Assert
        assertEquals("3 * 4 = 12.0\n7 - 2 = 5.0", result.trim());
    }

    @Test
    void testProcessJsonFile_NoExpressions() throws Exception {
        // Arrange
        String content = "{\"message\": \"Hello World\", \"note\": \"No math here.\"}";
        MockMultipartFile jsonFile = new MockMultipartFile(
                "file", "test.json", "application/json", content.getBytes()
        );

        // Act
        String result = fileProcessingService.processFile(jsonFile);

        // Assert
        assertEquals("No mathematical expressions found in JSON.", result.trim());
    }

    @Test
    void testProcessXmlFile_ValidExpressions() throws Exception {
        // Arrange
        String content = "<root><expression>6 / 2</expression><expression>10 + 5</expression></root>";
        MockMultipartFile xmlFile = new MockMultipartFile(
                "file", "test.xml", "application/xml", content.getBytes()
        );

        // Act
        String result = fileProcessingService.processFile(xmlFile);

        // Assert
        assertEquals("6 / 2 = 3.0\n10 + 5 = 15.0", result.trim());
    }

    @Test
    void testProcessYamlFile_ValidExpressions() throws Exception {
        // Arrange
        String content = "expression1: 9 - 3\nexpression2: 4 * 5";
        MockMultipartFile yamlFile = new MockMultipartFile(
                "file", "test.yaml", "application/x-yaml", content.getBytes()
        );

        // Act
        String result = fileProcessingService.processFile(yamlFile);

        // Assert
        assertEquals("9 - 3 = 6.0\n4 * 5 = 20.0", result.trim());
    }

    @Test
    void testProcessUnknownFileType() throws Exception {
        // Arrange
        String content = "1 + 1";
        MockMultipartFile unknownFile = new MockMultipartFile(
                "file", "unknown.xyz", "application/xyz", content.getBytes()
        );

        // Act
        String result = fileProcessingService.processFile(unknownFile);

        // Assert
        assertEquals("1 + 1 = 2.0", result.trim());
    }

    @Test
    void testProcessFile_EmptyFile() throws Exception {
        // Arrange
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.txt", "text/plain", "".getBytes()
        );

        // Act
        String result = fileProcessingService.processFile(emptyFile);

        // Assert
        assertEquals("The file is empty or contains only whitespace.", result.trim());
    }

}
