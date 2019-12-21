package com.booking.common.service;

import com.booking.utils.MailInfo;

import java.util.Map;

import javax.mail.MessagingException;

public interface IMailService {
    //获取邮件发信人
    public String getMailSendFrom();
    //发送邮件
    public MailInfo sendMail(MailInfo mailInfo);
    public void sendTemplateMail(MailInfo mailInfo, String filename, Map<String, Object> variables) throws MessagingException;
    public void sendTemplateMail(MailInfo mailInfo, String filename, String variableKey, Object variableValue) throws MessagingException ;
}
