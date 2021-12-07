package com.mytest.ssm.service.impl;

import java.io.File;

import com.mytest.ssm.service.IBaseService;

public class BaseServiceImpl implements IBaseService {

	@Override
	public void deletePhoto(String path) throws Exception {
		File file1 = new File("D:\\wEclipseProject\\shopssm"+path);
		
		File file2 = new File("E:/workspace/shopssm/WebContent"+path);
		 
		if(file1.exists()){
			file1.delete();
		}
		if(file2.exists()){
			file2.delete();
		}
	}

}
