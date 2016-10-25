package com.favorites.comm.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
*@ClassName: SFTPFileUtil
*@Description: SFTP文件处理
*@author YY 
*@date 2016年10月25日  下午3:31:12
*@version 1.0
*/
public class SFTPFileUtil {

	 Logger log = Logger.getLogger(this.getClass());
    
    private ChannelSftp sftp;  
      
    private Session session;  
    /** FTP 登录用户名*/  
    private String username;   
    /** FTP 登录密码*/  
    private String password;  
    /** 私钥文件的路径*/  
    private String keyFilePath;  
    /** FTP 服务器地址IP地址*/  
    private String host;  
    /** FTP 端口*/  
    private int port;  
      
  
    /** 
     * 构造基于密码认证的sftp对象 
     * @param userName 
     * @param password 
     * @param host 
     * @param port 
     */  
    public SFTPFileUtil(String username, String password, String host, int port) {  
        this.username = username;  
        this.password = password;  
        this.host = host;  
        this.port = port;  
    }  
  
    /** 
     * 构造基于秘钥认证的sftp对象 
     * @param userName 
     * @param host 
     * @param port 
     * @param keyFilePath 
     */  
    public SFTPFileUtil(String username, String host, int port, String keyFilePath) {  
        this.username = username;  
        this.host = host;  
        this.port = port;  
        this.keyFilePath = keyFilePath;  
    }  
  
    public SFTPFileUtil(){}  
  
    /** 
     * 连接sftp服务器 
     *  
     * @throws Exception 
     */  
    public void login(){  
        try {  
            JSch jsch = new JSch();  
            if (keyFilePath != null) {  
                jsch.addIdentity(keyFilePath);// 设置私钥  
                log.info("sftp connect,path of private key file：" + keyFilePath);  
            }  
            log.info("sftp connect by host:"+host+" username:"+username);  
  
            session = jsch.getSession(username, host, port);  
            log.info("Session is build");  
            if (password != null) {  
                session.setPassword(password);  
            }  
            Properties config = new Properties();  
            config.put("StrictHostKeyChecking", "no");  
              
            session.setConfig(config);  
            session.connect();  
            log.info("Session is connected");  
              
            Channel channel = session.openChannel("sftp");  
            channel.connect();  
            log.info("channel is connected");  
  
            sftp = (ChannelSftp) channel;  
            log.info(String.format("sftp server host:[%s] port:[%s] is connect successfull", host, port));  
        } catch (JSchException e) {  
            log.error("Cannot connect to specified sftp server：",e);
        }  
    }  
  
    /** 
     * 关闭连接 server 
     */  
    public void logout(){  
        if (sftp != null) {  
            if (sftp.isConnected()) {  
                sftp.disconnect();  
                log.info("sftp is closed already");  
            }  
        }  
        if (session != null) {  
            if (session.isConnected()) {  
                session.disconnect();  
                log.info("sshSession is closed already");  
            }  
        }  
    }  
  
    /** 
     * 将输入流的数据上传到sftp作为文件 
     *  
     * @param directory 上传到该目录 
     * @param sftpFileName sftp端文件名 
     * @param in 输入流 
     * @throws SftpException  
     * @throws Exception 
     */  
    public void upload(String directory, String sftpFileName, InputStream input) throws SftpException{  
        try {  
            sftp.cd(directory);  
        } catch (SftpException e) {  
            log.warn("directory is not exist");  
            sftp.mkdir(directory);  
            sftp.cd(directory);  
        }  
        sftp.put(input, sftpFileName);  
        log.info("file:"+sftpFileName+" is upload successful");  
    }  
  
    /** 
     * 上传单个文件 
     *  
     * @param directory 上传到sftp目录 
     * @param uploadFile 要上传的文件,包括路径 
     * @throws FileNotFoundException  
     * @throws SftpException  
     * @throws Exception 
     */  
    public void upload(String directory, String uploadFile) throws FileNotFoundException, SftpException{  
        File file = new File(uploadFile);  
        upload(directory, file.getName(), new FileInputStream(file));  
    }  
  
    /** 
     * 将byte[]上传到sftp，作为文件。注意:从String生成byte[]是，要指定字符集。 
     *  
     * @param directory 上传到sftp目录 
     * @param sftpFileName 文件在sftp端的命名 
     * @param byteArr 要上传的字节数组 
     * @throws SftpException  
     * @throws Exception 
     */  
    public void upload(String directory, String sftpFileName, byte[] byteArr) throws SftpException{  
        upload(directory, sftpFileName, new ByteArrayInputStream(byteArr));  
    }  
  
    /** 
     * 将字符串按照指定的字符编码上传到sftp 
     *  
     * @param directory 上传到sftp目录 
     * @param sftpFileName 文件在sftp端的命名 
     * @param dataStr 待上传的数据 
     * @param charsetName sftp上的文件，按该字符编码保存 
     * @throws UnsupportedEncodingException  
     * @throws SftpException  
     * @throws Exception 
     */  
    public void upload(String directory, String sftpFileName, String dataStr, String charsetName) throws UnsupportedEncodingException, SftpException{  
        upload(directory, sftpFileName, new ByteArrayInputStream(dataStr.getBytes(charsetName)));  
  
    }  
  
    /** 
     * 下载文件 
     *  
     * @param directory 下载目录 
     * @param downloadFile 下载的文件 
     * @param saveFile 存在本地的路径 
     * @throws SftpException  
     * @throws FileNotFoundException  
     * @throws Exception 
     */  
    public void download(String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException{  
        if (directory != null && !"".equals(directory)) {  
            sftp.cd(directory);  
        }  
        File file = new File(saveFile);  
        sftp.get(downloadFile, new FileOutputStream(file));  
        log.info("file:"+downloadFile+" is download successful");  
    }  
    
}