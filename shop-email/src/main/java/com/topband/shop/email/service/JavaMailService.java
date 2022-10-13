package com.topband.shop.email.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author huangyijun
 * @version v1.0
 * @className JavaMailUtils
 * @packageName com.roroldo.util
 * @date 2022/4/26 9:43
 */
@Component
@Slf4j
@RefreshScope
public class JavaMailService {
    @Resource
    private JavaMailSender mailSender;

    @Value("${shop.mail.from}")
    private String from;

    private static String emailTemplate;

    @Value("${shop.mail.subject}")
    private String subject;

    static {
        boolean isWindows = System.getProperty("os.name").toUpperCase().contains("WINDOWS");
        String filename = "email.html";
        if (isWindows) {
            FileReader fileReader = new FileReader(filename);
            emailTemplate = fileReader.readString();
        } else {
            InputStream is = null;
            BufferedReader bufferedReader = null;
            try {
                is = JavaMailService.class.getClassLoader().getResourceAsStream(filename);
                bufferedReader = IoUtil.getReader(is, StandardCharsets.UTF_8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                emailTemplate = sb.toString();
            } catch (IOException e) {
                log.error("读取邮件模板文件异常：{}", ExceptionUtils.getStackTrace(e));
            } finally {
                IoUtil.close(bufferedReader);
                IoUtil.close(is);
            }
        }
        log.debug("读取邮件内容：{}", emailTemplate);
    }

    public void sendMail(String to, String code) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            String content = String.format(emailTemplate, code);
            log.debug("发送邮件内容：{}", content);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("发送邮件异常:{}", ExceptionUtils.getStackTrace(e));
        }
    }
}
