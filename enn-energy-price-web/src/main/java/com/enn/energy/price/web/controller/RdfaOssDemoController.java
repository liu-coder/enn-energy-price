package com.enn.energy.price.web.controller;

import com.enn.energy.price.core.service.impl.RdfaOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/oss")
public class RdfaOssDemoController {

    @Autowired
    RdfaOssService rdfaOssService;

    @GetMapping(value = "/upload")
    public String upload() {
        return rdfaOssService.uploadFileWithName();
    }

    @GetMapping(value = "/download")
    public String download(@RequestParam("filePath")String filePath, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("image/png");
        OutputStream os = httpServletResponse.getOutputStream();
        os.write(rdfaOssService.getFile(filePath));
        os.flush();
        os.close();
        return "success";
    }
}
