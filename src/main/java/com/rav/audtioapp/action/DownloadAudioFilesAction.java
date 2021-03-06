package com.rav.audtioapp.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.rav.audtioapp.dto.DownloadAudioFilesDTO;
import com.rav.audtioapp.service.DownloadAudioFilesService;
import com.rav.audtioapp.util.DownloadInputStream;

public class DownloadAudioFilesAction {

	private InputStream inputStream;
	private String fileName;
	private long contentLength;
	private String wordsSelected;
	private String gendersSelected;
	private String minimumAge;
	private String maximumAge;
	private String nativeLanguage;

	public String execute() throws FileNotFoundException {
		
		System.out.println(nativeLanguage);
		DownloadAudioFilesDTO dto = new DownloadAudioFilesDTO();

		dto.setGendersSelected(gendersSelected);
		dto.setMaximumAge(maximumAge);
		dto.setMinimumAge(minimumAge);
		dto.setWordsSelected(wordsSelected);
		dto.setNativeLanguageSelected(nativeLanguage);

		String fName = new DownloadAudioFilesService().process(dto);
		if (fName != null) {
			File fileToDownload = new File(fName);
			inputStream = new DownloadInputStream(fileToDownload);
			fileName = fileToDownload.getName();
			contentLength = fileToDownload.length();
			return "success";
		}
		return "invalidSelection";

	}

	public String getWordsSelected() {
		return wordsSelected;
	}

	public void setWordsSelected(String wordsSelected) {
		this.wordsSelected = wordsSelected;
	}

	public String getGendersSelected() {
		return gendersSelected;
	}

	public void setGendersSelected(String gendersSelected) {
		this.gendersSelected = gendersSelected;
	}

	public String getMinimumAge() {
		return minimumAge;
	}

	public void setMinimumAge(String minimumAge) {
		this.minimumAge = minimumAge;
	}

	public String getMaximumAge() {
		return maximumAge;
	}

	public void setMaximumAge(String maximumAge) {
		this.maximumAge = maximumAge;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public long getContentLength() {
		return contentLength;
	}

	public String getFileName() {
		return fileName;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public String getNativeLanguage() {
		return nativeLanguage;
	}

	public void setNativeLanguage(String nativeLanguage) {
		this.nativeLanguage = nativeLanguage;
	}
}
