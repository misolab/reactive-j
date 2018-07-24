package misolab;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;


public class HiJwt {

    Logger logger = Logger.getLogger(HiJwt.class.getSimpleName());
    private static final String SALT = "misolab";

    public static void main(String[] args) {
        HiJwt hiJwt = new HiJwt();
        hiJwt.test();
    }

    private void test() {
        String result = sample("name", "ohdo");
        logger.info( result );


        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(generateKey())
                .parseClaimsJws(result);

        logger.info(claims.toString());

        logger.info((String) claims.getBody().get("name"));
    }


    public <T> String sample(String key, T data) {
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setHeaderParam("regDate", System.currentTimeMillis())
                .setSubject("sampleJWT")
                .claim(key, data)
                .signWith(SignatureAlgorithm.HS256, this.generateKey())
                .compact();
    }

    private byte[] generateKey() {
        byte[] key = null;
        try {
            key = SALT.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return key;
    }
}
