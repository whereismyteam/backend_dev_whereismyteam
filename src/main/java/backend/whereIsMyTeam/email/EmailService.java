package backend.whereIsMyTeam.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender ;

    @Async //비동기 처리
    public void send(String email, String authToken) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(email); //보내는 주소
        smm.setSubject("구해줘 팀원 회원가입 이메일 인증"); //이메일 제목

        //차후 배포용으로 올릴 땐 구해줘 팀원 도메인 주소로 바꿔서 올려야함
        smm.setText("http://localhost:9000/emails/confirm-email?email="+email+"&authToken="+authToken); //이메일 내용

        javaMailSender.send(smm);
    }
}
