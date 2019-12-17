package com.booking.utils;

import lombok.Data;

@Data
public class UploadResponse {
	private String imgUrl;
	private Integer retCode = 0;
}
