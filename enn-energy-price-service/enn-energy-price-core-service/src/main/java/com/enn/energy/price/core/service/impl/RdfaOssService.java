package com.enn.energy.price.core.service.impl;


import com.enn.energy.price.core.service.IRdfaOssDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RdfaOssService {

    @Autowired
    private IRdfaOssDemoService rdfaOssDemoService;

    public String uploadFileWithName()  {
        return rdfaOssDemoService.uploadFileWithName();
    }

    public byte[] getFile(String filePath)  {
        return rdfaOssDemoService.getFile(filePath);
    }
}