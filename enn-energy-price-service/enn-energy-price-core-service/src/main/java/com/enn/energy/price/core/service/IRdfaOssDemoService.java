package com.enn.energy.price.core.service;

public interface IRdfaOssDemoService {
    //上传文件并指定名称，返回文件对应的web地址
    String uploadFileWithName() ;

    //下载文件数据并转换成数据流
    byte[] getFile(String filePath);
}