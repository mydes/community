package com.example.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class FileController {

    private static String UPLOADED_FOLDER = "D://temp//";
    //处理文件上传
    @RequestMapping(value="/file/upload")
    public @ResponseBody Map<String,Object> demo(@RequestParam(value = "editormd-image-file", required = false) MultipartFile file, HttpServletRequest request) {
        System.out.println(file);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        System.out.println(request.getContextPath());
        String realPath = UPLOADED_FOLDER;
        String fileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        fileName = uuid+"_"+fileName;
//        File targetFile = new File(realPath, fileName);
//        if(!targetFile.exists()){
//            targetFile.mkdirs();
//        }
//        //保存
        try {
//            file.transferTo(targetFile);//保存到一个目标文件中。
           byte[] bytes = file.getBytes();

            Path path = Paths.get(UPLOADED_FOLDER + fileName);

            Files.write(path, bytes);
            resultMap.put("success", 1);
            resultMap.put("message", "上传成功！");
            resultMap.put("url",UPLOADED_FOLDER+fileName);
        } catch (Exception e) {
            resultMap.put("success", 0);
            resultMap.put("message", "上传失败！");
            e.printStackTrace();
        }
        System.out.println(resultMap.get("success"));
        return resultMap;


    }
}