package com.ruanmou.house.user.service.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

@Service
public class FileService {
	
	@Value("${image.file.path:}")
	private String filePath;

	@Autowired
	private QiNiuService qiNiuService;

	/**
	 * 把一个上传文件的列表中每一个文件上传指定路径下面最后这些文件相对路径组成的集合
	 * @param files
	 * @return
	 */
	public List<String> getImgPath(List<MultipartFile> files) {
	    if (Strings.isNullOrEmpty(filePath)) {
			filePath = getResourcePath();
		}
		List<String> paths = Lists.newArrayList();
		files.forEach(file -> {
			String path = null;
			if (!file.isEmpty()) {
				try {
					path = qiNiuService.uploadFile(file);
					paths.add(path);
				} catch (Exception e) {
					throw new IllegalArgumentException(e);
				}
			}
		});
		return paths;
	}
	
	public static String getResourcePath(){
	  File file = new File(".");
	  String absolutePath = file.getAbsolutePath();
	  return absolutePath;
	}

	/**
	 * 把文件保存到本地(图片服务: 提供上传接口)
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private File saveToLocal(MultipartFile file) throws IOException {
	 File newFile = new File(filePath + "/" + Instant.now().getEpochSecond() +"/"+file.getOriginalFilename());
	 if (!newFile.exists()) {
		 newFile.getParentFile().mkdirs();
		 newFile.createNewFile();
	 }
	 Files.write(file.getBytes(), newFile);
     return newFile;
	}

	public static void main(String[] args) {
		System.out.println(getResourcePath());
		String path = StringUtils.substringAfterLast("E:/a/b/c/d/dd.png", "E:/a/b/c/d/");

		HashMap<Object, Object> map = Maps.newHashMap();
		map.put("aname","dfsfdds");
		map.put("email",null);

		System.out.println(Joiner.on("&").useForNull("").withKeyValueSeparator("=").join(map));

		System.out.println(path);
	}

}
