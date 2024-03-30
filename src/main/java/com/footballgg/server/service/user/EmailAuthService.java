package com.footballgg.server.service.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailAuthService {
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final RedisUtil redisUtil;
    private String authCode; // 인증 번호

    // 인증번호 8자리 무작위 생성
    public void createCode(String email) {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0; i<8; i++) {
            int idx = random.nextInt(3);

            switch (idx) {
                case 0 :
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        authCode = key.toString();
    }

    // 타임리프를 이용한 context 설정
    public String setContext() {
        Context context = new Context();
        context.setVariable("code", authCode);
        return templateEngine.process("EmailAuth", context);
    }

    // 메일 양식 작성
    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {
        createCode(email); // 인증코드 생성 후 DB에 저장
        String setFrom = "qlrqod3356@gmail.com"; // 발신자 메일
        String toEmail = email; // 수신자 메일
        String title = "[해축지지] 이메일 인증코드가 발급되었습니다."; // 이메일 제목

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
        message.setSubject(title);

        // 메일 내용
        message.setFrom(setFrom);
        message.setText(setContext(), "utf-8", "html"); // EmailAuth.html

        return message;
    }

    //메일 전송
    public void sendEmail(String email) throws MessagingException, UnsupportedEncodingException {

        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(email);
        //실제 메일 전송
        emailSender.send(emailForm);

        // 유효 시간(30분)동안 {email, authKey} 저장
        redisUtil.setDataExpire(email, authCode, 60 * 30L);
    }

    // 메일 인증 검증
    public Boolean verifyAuthCode(String email, String code){
        String result = redisUtil.getData(email);
        if(result == null)
            return false;
        else
            return result.equals(code);
    }
}
