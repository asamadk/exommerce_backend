package in.alifclothing.Jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

//The token we recieve if it is not correct we do not authorize user
public class JwtTokenVerifier extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

            String authorizationHeader = request.getHeader("Authorization");
            if(Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")){
                filterChain.doFilter(request,response);
                return;
            }

            String token = authorizationHeader.replace("Bearer ","");
            if(token.contains("Google_")){
                verifyGoogleToken(token.replace("Google_",""));
            }else{
                verifyAlifToken(token);
            }

         filterChain.doFilter(request,response);
    }

    private void verifyAlifToken(String token){
        System.out.println("INSIDE :: verifyAlifToken :: token = "+token);
        try {
            String secretKey = "samsung05192samadzaidapplebuilldingabrandcalledalif";
            Jws<Claims>  claimsJws = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();

            String username = body.getSubject();

            List<Map<String,String>> authorities =  (List<Map<String,String>>)body.get("authorities");

            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());


            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    simpleGrantedAuthorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (JwtException e){
            throw new IllegalStateException(String.format("Token %s cannot be trusted",token));
        }
    }

    private void verifyGoogleToken(String token){
        try{
            System.out.println("INSIDE :: verifyGoogleToken :: token = "+token);

            String secretKey = "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";
            Jws<Claims>  claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();

            String username = body.getSubject();

        }catch (Exception e){
            e.printStackTrace();
//            throw new IllegalStateException(String.format("Token %s cannot be trusted",token));
        }
    }
}
