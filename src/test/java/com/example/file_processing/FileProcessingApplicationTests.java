package com.example.file_processing;

import com.example.file_processing.controller.FileController;
import com.example.file_processing.service.FileProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FileProcessingApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private FileProcessingService fileProcessingService;

	@InjectMocks
	private FileController fileController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testUploadFile_Success() throws Exception {
		// Arrange
		MockMultipartFile mockFile = new MockMultipartFile(
				"file",
				"test.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"Test file content".getBytes()
		);

		Mockito.when(fileProcessingService.processFile(Mockito.any())).thenReturn("File processed successfully");

		// Act & Assert
		mockMvc.perform(multipart("/api/files/upload").file(mockFile))
				.andExpect(status().isOk())
				.andExpect(content().string("File processed successfully"));
	}

	@Test
	void testUploadFile_EmptyFile() throws Exception {
		// Arrange
		MockMultipartFile emptyFile = new MockMultipartFile(
				"file",
				"empty.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"".getBytes()
		);

		// Act & Assert
		mockMvc.perform(multipart("/api/files/upload").file(emptyFile))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("The uploaded file is empty."));
	}

	@Test
	void testUploadFile_Exception() throws Exception {
		// Arrange
		MockMultipartFile mockFile = new MockMultipartFile(
				"file",
				"error.txt",
				MediaType.TEXT_PLAIN_VALUE,
				"This will cause an error".getBytes()
		);

		Mockito.when(fileProcessingService.processFile(Mockito.any())).thenThrow(new RuntimeException("Processing failed"));

		// Act & Assert
		mockMvc.perform(multipart("/api/files/upload").file(mockFile))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Error processing file: Processing failed"));
	}
}