package com.enn.energy.price.integration;

import com.enn.energy.price.core.service.IRdfaOssDemoService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;
import top.rdfa.framework.fs.api.bean.FileMetaData;
import top.rdfa.framework.fs.api.service.OssApi;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
/**
 * OSS 使用demo
 * https://confluence.enncloud.cn/pages/viewpage.action?pageId=521372341
 */
@Service
public class RdfaOssDemo implements IRdfaOssDemoService {

    @Resource
    OssApi ossApi;


    //上传文件并指定名称，返回文件对应的web地址
    public String uploadFileWithName()  {
        try {
            FileMetaData fmd = ossApi.upload("easterEgg.png",haveANiceDayLOL());
            return "uploadFileWithName end, fileUrl: " + fmd.getUrl() + " , filePath: " + fmd.getFullName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //下载文件数据并转换成数据流
    public byte[] getFile(String filePath)  {
        try {
            return ossApi.getObject(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private byte[] haveANiceDayLOL(){
        FileSystemResource  fileRs = new FileSystemResource("enn-energy-price-starter/src/main/resources/niceDaySample.txt");
        byte[] bytes = new byte[0];
        try (FileInputStream ins = new FileInputStream(fileRs.getFile())){
            FileChannel insChannel = ins.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) insChannel.size());
            while ((insChannel.read(byteBuffer)) > 0) {
                System.out.println("reading");
            }
            String fileTxt = new String(byteBuffer.array());
            BASE64Decoder base64Decoder = new BASE64Decoder();
            bytes = base64Decoder.decodeBuffer(fileTxt);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}