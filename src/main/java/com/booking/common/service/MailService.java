package com.booking.common.service;

import java.io.File;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;

import com.booking.common.domain.MailInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


// 邮件业务类 MailService

@Service
public class MailService implements IMailService{
    @Autowired
    private JavaMailSenderImpl mailSender;//注入邮件工具类
    @Autowired
    private TemplateEngine templateEngine;// 用于将thymeleaf模板转换为String

    // 发送邮件
    public MailInfo sendMail(MailInfo mailInfo) {
        try {
            checkMail(mailInfo);		//1.检测邮件
            sendMimeMail(mailInfo);		//2.发送邮件
            return saveMail(mailInfo);	//3.保存邮件
        } catch (Exception e) {
            mailInfo.setStatus("fail");
            mailInfo.setError(e.getMessage());
            return mailInfo;
        }
    }

    //检测邮件信息类
    private void checkMail(MailInfo mailInfo) {
        if (StringUtils.isEmpty(mailInfo.getTo())) {
            throw new RuntimeException("邮件收信人不能为空");
        }
        if (StringUtils.isEmpty(mailInfo.getSubject())) {
            throw new RuntimeException("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(mailInfo.getText())) {
            throw new RuntimeException("邮件内容不能为空");
        }
    }

    //保存邮件
    private MailInfo saveMail(MailInfo mailInfo) {
        //将邮件保存到数据库..
        return mailInfo;
    }

    //获取邮件发信人
    public String getMailSendFrom() {
        return mailSender.getJavaMailProperties().getProperty("from");
    }
    
    //构建复杂邮件信息类
    private void sendMimeMail(MailInfo mailInfo) throws MessagingException {
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);	// true表示支持复杂类型
		if (StringUtils.isEmpty(mailInfo.getFrom())) {		// 邮件发信人从配置项读取
			mailInfo.setFrom(getMailSendFrom()); 	
		}
		messageHelper.setFrom(mailInfo.getFrom()); 			// 邮件发信人
		
		messageHelper.setTo(mailInfo.getTo().split(",")); 	// 邮件收信人
		messageHelper.setSubject(mailInfo.getSubject()); 	// 邮件主题
		messageHelper.setText(mailInfo.getText(), mailInfo.getIshtml()); // 邮件内容
		
		messageHelper.setCc(mailInfo.getFrom());
		if (!StringUtils.isEmpty(mailInfo.getCc())) { 		// 抄送
			messageHelper.setCc(mailInfo.getCc().split(","));
		}
		
		if (!StringUtils.isEmpty(mailInfo.getBcc())) { 		// 密送
			messageHelper.setBcc(mailInfo.getBcc().split(","));
		}
		
		if (null!=mailInfo.getMultipartFiles()) { 		// 添加邮件MultipartFile附件
			for (MultipartFile multipartFile : mailInfo.getMultipartFiles()) {
				messageHelper.addAttachment(multipartFile.getOriginalFilename(), multipartFile);
			}
		}
		
		if (null!=mailInfo.getFiles()) { 				// 添加邮件File附件
			File[] files=mailInfo.getFiles();
			for (File file : files) {
				messageHelper.addAttachment(file.getName(), file);
			}
		}
		if (StringUtils.isEmpty(mailInfo.getSentDate())) { 	// 发送时间
			mailInfo.setSentDate(new Date());
		}
		messageHelper.setSentDate(mailInfo.getSentDate());
		System.out.println("发送邮件到: "+mailInfo.getTo());
		mailSender.send(messageHelper.getMimeMessage()); 	// 正式发送邮件
		
		mailInfo.setStatus("ok");
    }

    public void sendTemplateMail(MailInfo mailInfo, String filename, Map<String, Object> variables) throws MessagingException {
        Context context = new Context();
    	context.setVariables(variables);// 设置模板的变量
    	sendTemplateMail(mailInfo,filename,context);
    }

    public void sendTemplateMail(MailInfo mailInfo, String filename, String variableKey, Object variableValue) throws MessagingException {
    	Context context = new Context();
    	context.setVariable(variableKey,variableValue);// 设置模板的变量
    	sendTemplateMail(mailInfo,filename,context);
    }
    
    private void sendTemplateMail(MailInfo mailInfo, String filename, Context context) {
    	String htmlText= templateEngine.process(filename, context); // 获取thymeleaf的html模板
	    mailInfo.setIshtml(true);
	    mailInfo.setText(htmlText);
    	sendMail(mailInfo);
    }
}