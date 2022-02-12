//package backend.whereIsMyTeam.oauth.OAuthRequest;
//
//import lombok.*;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.util.LinkedMultiValueMap;
//
//@Component
//@RequiredArgsConstructor
//public class OAuthRequestFactory {
//
//
//    private final GoogleInfo googleInfo;
//
//    public OAuthRequest getRequest(String code, String provider) {
//        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        //if(provider.equals("google")) {
//            map.add("grant_type", "authorization_code");
//            map.add("client_id", googleInfo.getGoogleClientId());
//            map.add("client_secret", googleInfo.getGoogleClientSecret());
//            map.add("redirect_uri", googleInfo.getGoogleRedirect());
//            map.add("code", code);
//
//            return new OAuthRequest(googleInfo.getGoogleTokenUrl(), map);
//        //}
//    }
//
//    public String getProfileUrl() {
//        //if (provider.equals("google"))
//        return googleInfo.getGoogleProfileUrl();
//
//    }
//
//    @Getter
//    @Component
//    static class GoogleInfo {
//        @Value("${spring.social.google.client_id}")
//        String googleClientId;
//        @Value("${spring.social.google.redirect}")
//        String googleRedirect;
//        @Value("${spring.social.google.client_secret}")
//        String googleClientSecret;
//        @Value("${spring.social.google.url.token}")
//        private String googleTokenUrl;
//        @Value("${spring.social.google.url.profile}")
//        private String googleProfileUrl;
//    }
//
//}