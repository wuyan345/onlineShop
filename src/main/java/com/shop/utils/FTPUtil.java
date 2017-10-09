package com.shop.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.shop.common.Message;

public class FTPUtil {

	private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);
	
	private static final String IP = "120.78.77.242";
	private static final String USERNAME = "ftpuser";
	private static final String PASSWORD = "123456";
	
	public static Message<List<String>> upload(MultipartFile[] multipartFiles){
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(IP);
			ftpClient.login(USERNAME, PASSWORD);
			ftpClient.changeWorkingDirectory("images");
			ftpClient.setBufferSize(1024);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);	// 防止出现乱码
			ftpClient.enterLocalPassiveMode();
			List<String> fileNameList = new ArrayList<>();
			for (MultipartFile multipartFile : multipartFiles) {
				String[] names = multipartFile.getOriginalFilename().split("\\.");	// "."是转义字符
				String fileName = UUID.randomUUID().toString() + "." + names[names.length-1];
				boolean isSuccess = ftpClient.storeFile(fileName, multipartFile.getInputStream());
				if(isSuccess == true)
					fileNameList.add(fileName);
			}
			return Message.successData(fileNameList);
		} catch (IOException e) {
			logger.error("上传失败", e);
			return Message.errorMsg("上传失败");
		}
	}
	
	public static Message upload(File file){
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(IP);
			ftpClient.login(USERNAME, PASSWORD);
			ftpClient.changeWorkingDirectory("images");
			ftpClient.setBufferSize(1024);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);	// 防止出现乱码
			ftpClient.enterLocalPassiveMode();
			String fileName = file.getName();
			boolean isSuccess = ftpClient.storeFile(fileName, new FileInputStream(file));
			if(isSuccess == false){
				logger.error("文件{}上传失败", fileName);
				return Message.errorMsg("上传失败");
			}
			return Message.successData(fileName);
		} catch (IOException e) {
			logger.error("上传失败", e);
			return Message.errorMsg("上传失败");
		}
	}
}
