package com.crm.util;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.UUID;

public class UploadUtils {
    //抽取共同的springMVC文件上传方法
    public static String mulipartFileUpload(MultipartFile file, HttpServletRequest request) {
        if (file.getSize()==0)
            return "";
        String realPath1 = request.getSession().getServletContext().getRealPath("/upload");
        String realPath2 = realPath1.replace("\\", "/") + "/";
        String[] split = file.getOriginalFilename().split("\\.");
        String realName1 = split[0] + UUID.randomUUID() +"."+ split[1];
        String realName2 = "/upload/" + realName1;
        //springMVC上传
        if (file != null) {
            InputStream inputStream = null;
            FileOutputStream outputStream = null;
            try {
                inputStream = file.getInputStream();
                outputStream = new FileOutputStream(new File(realPath2 + realName1));
                IOUtils.copy(inputStream, outputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null)
                        inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputStream != null)
                            outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return realName2;
    }

    public static ResponseEntity<byte[]> download(String path) throws Exception {
        if (path == null || path.length() == 0)
            return null;
        String downloadFile = UserContext.get().getSession().getServletContext().getRealPath(path);//从我们的上传文件夹中去取
        //获取输入流
        FileInputStream fis = new FileInputStream(downloadFile);//新建一个文件

        byte[] tmp = new byte[fis.available()];
        fis.read(tmp);
        fis.close();

        //2. 将下载的文件流返回
        //文件下载响应头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "attachment;filename=" + downloadFile);

        return new ResponseEntity<byte[]>(tmp, headers, HttpStatus.OK);
    }
}
