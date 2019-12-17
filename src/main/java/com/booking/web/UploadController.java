package com.booking.web;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.booking.utils.ResponseEntity;
import com.booking.utils.UploadResponse;

@RestController
@RequestMapping(value="/upload")
public class UploadController {
	
	/**
	 * 文件上传
	 * @param directoryName
	 * @param file
	 * @param req
	 * @return
	 */
	@PostMapping
    public ResponseEntity uploadFile(String directoryName, MultipartFile file, HttpServletRequest req) {
		String realPath = req.getServletContext().getRealPath("/upload/"+directoryName);
	    File folder = new File(realPath);
	    if (!folder.exists()) {
	        folder.mkdirs();
	    }
	    System.out.println(realPath);
	    String oldName = file.getOriginalFilename();
	    String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
	    try {
			file.transferTo(new File(folder,newName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	    String url = "/upload/"+ directoryName + "/" + newName;
	    UploadResponse data = new UploadResponse();
	    data.setImgUrl(url);
	    System.out.println(url);
	    return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(data);
    }
	
	/**
	 * 根据路径删除文件
	 * @param filePath
	 * @param req
	 * @return
	 */
	@DeleteMapping
	public ResponseEntity deleteFile(String filePath, HttpServletRequest req) {
		String realPath = req.getServletContext().getRealPath(filePath);
		File file = new File(realPath);
		if(file.exists()) {
			file.delete();
			return ResponseEntity.ofSuccess().status(HttpStatus.OK);
		}else {
			return ResponseEntity.ofFailed();
		}
	}
}
