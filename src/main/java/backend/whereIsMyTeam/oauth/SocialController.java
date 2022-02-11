package backend.whereIsMyTeam.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/social/login")
public class SocialController {

    private final Environment env;
    private final ProviderService providerService;


    @Value("${spring.social.google.client_id}")
    private String googleClientId;

    @Value("${spring.social.google.redirect}")
    private String googleRedirect;


    // 구글 로그인 페이지 테스트
    @GetMapping()
    public ModelAndView socialGoggleLogin(ModelAndView mav) {

        StringBuilder loginUrl2 = new StringBuilder()
                .append(env.getProperty("spring.social.google.url.login"))
                .append("?client_id=").append(googleClientId)
                .append("&response_type=code")
                .append("&scope=email%20profile")
                .append("&redirect_uri=").append(googleRedirect);



        mav.addObject("loginUrl2", loginUrl2);
        mav.setViewName("login");
        return mav;
    }

    // 인증 완료 후 리다이렉트 페이지
    @GetMapping(value = "/google")
    public ModelAndView redirectGoogle(ModelAndView mav, @RequestParam String code /*,@PathVariable String provider*/) {

        mav.addObject("code", code);
        mav.setViewName("redirect");
        return mav;
    }
}
