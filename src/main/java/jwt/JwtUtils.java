package jwt;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    private final static String key = "b77ee3f38a38f4aa35e0743ff86852d0b08d08e90c1a2c986bdb19b0f1201ac5";
//    private final static String key = "36f06c94164ab481f1ed586d596f369df006b283e13c4533581192231a63520c";



    public static void main(String[] args) {

        Map<String, Object> param = new HashMap<>();
        param.put("userId","1234");
        param.put("ctn","010");
        param.put("carrierType","L");

        String res = createJWT(key , param, 999999999);
        System.out.println(res);
    }

    public static String createJWT(String apiKey, Map<String, Object> param, long expireTime) {

        // The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(apiKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Map<String, Object> authMap = new HashMap<>();
        authMap.put("userId", param.get("userId"));
        authMap.put("ctn", param.get("ctn"));
        authMap.put("clientIp", param.get("clientIp"));
        authMap.put("devInfo", param.get("devInfo"));
        authMap.put("osInfo", param.get("osInfo"));
        authMap.put("nwInfo", param.get("nwInfo"));
        authMap.put("devModel", param.get("devModel"));
        if(param.get("carrierType").equals("L") || param.get("carrierType").equals("K")
                || param.get("carrierType").equals("S")) {
            authMap.put("carrierType", param.get("carrierType"));
        }else {
            authMap.put("carrierType", "E");
        }

        // Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setClaims(authMap).setIssuedAt(now).signWith(signatureAlgorithm, signingKey);

        // if it has been specified, let's add the expiration
        if (expireTime >= 0) {
            long expMillis = nowMillis + expireTime;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }

    public Claims parseJWT(String apiKey, String jwt) {

        Claims claims = null;

        if(jwt != null) {
            JwtParser parser = null;
            try {
                parser = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(apiKey));
                claims = parser.parseClaimsJws(jwt).getBody();
            } catch (Exception e) {
                System.out.println("parseJWT error apiKey : "+apiKey+", jwt : "+jwt+", class : "+e.getClass()+", Message : "+e.getMessage()+"");
            }
        }

        return claims;
    }


}
