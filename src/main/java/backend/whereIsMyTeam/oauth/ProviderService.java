//package backend.whereIsMyTeam.oauth;
//
//import backend.whereIsMyTeam.exception.Jwt.CommunicationException;
//import backend.whereIsMyTeam.oauth.OAuthRequest.OAuthRequest;
//import backend.whereIsMyTeam.oauth.OAuthRequest.OAuthRequestFactory;
//import com.nimbusds.oauth2.sdk.token.AccessToken;
//import lombok.RequiredArgsConstructor;
//import com.google.gson.Gson;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//
//
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
////소셜서비스 Access 토큰을 발급받는 곳
//public class ProviderService {
//
//    private final RestTemplate restTemplate;
//
//    //Json타입으로 보내기 위해
//    private final Gson gson;
//
//    private final OAuthRequestFactory oAuthRequestFactory;
//
//    //소셜서비스에 Profile 요청하기
//    public ProfileDto getProfile(String accessToken  ,String provider) throws CommunicationException {
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        httpHeaders.set("Authorization", "Bearer " + accessToken);
//
//        String profileUrl = oAuthRequestFactory.getProfileUrl();
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, httpHeaders);
//        ResponseEntity<String> response = restTemplate.postForEntity(profileUrl, request, String.class);
//
//        try {
//            if (response.getStatusCode() == HttpStatus.OK) {
//                return extractProfile(response, provider);
//            }
//        } catch (Exception e) {
//            throw new CommunicationException();
//        }
//        throw new CommunicationException();
//    }
//
//    private ProfileDto extractProfile(ResponseEntity<String> response, String provider) {
//        //if(provider.equals("google"))
//        ProfileDto googleProfile = gson.fromJson(response.getBody(), ProfileDto.class);
//        return new ProfileDto(googleProfile.getEmail());
//
//
//    }
//
//    public AccessToken getAccessToken(String code, String provider) {
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        OAuthRequest oAuthRequest = oAuthRequestFactory.getRequest(code, provider);
//        HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<>(oAuthRequest.getMap(), httpHeaders);
//
//        ResponseEntity<String> response = restTemplate.postForEntity(oAuthRequest.getUrl(), request, String.class);
//        try {
//            if (response.getStatusCode() == HttpStatus.OK) {
//                return gson.fromJson(response.getBody(), AccessToken.class);
//            }
//        } catch (Exception e) {
//            throw new CommunicationException();
//        }
//        throw new CommunicationException();
//    }
//}