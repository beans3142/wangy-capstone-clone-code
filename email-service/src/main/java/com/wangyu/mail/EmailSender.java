package com.wangyu.mail;

public interface EmailSender {

    void sendActivationEmail(String toEmail, String nickname, String activationCode);
}
