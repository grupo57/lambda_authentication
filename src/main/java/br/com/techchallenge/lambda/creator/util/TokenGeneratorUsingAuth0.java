package br.com.techchallenge.lambda.creator.util;

import br.com.techchallenge.lambda.Parameters;
import br.com.techchallenge.lambda.creator.ClaimsCreatorAdmin;
import br.com.techchallenge.lambda.creator.ClaimsSupplier;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

public class TokenGeneratorUsingAuth0 implements TokenGenerator {

    public String generate(ClaimsSupplier claims) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(Parameters.getSecretKey());

            JWTCreator.Builder jwtBuilder = JWT.create()
                    .withAudience(Parameters.getAudience())
                    .withIssuer(Parameters.getIssuerAllowed())
                    .withExpiresAt(this.generateExpirationDate());
                    ;

            for (Map.Entry<String, String> entry : claims.get().entrySet())
                jwtBuilder.withClaim(entry.getKey(), entry.getValue());

            return jwtBuilder.sign(algorithm);
        } catch (Exception e) {
            throw new RuntimeException("Error while authenticating user");
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusSeconds(Parameters.getExpiresInSeconds()).toInstant(ZoneOffset.of("-03:00"));
    }

    public static void main(String[] args) {
        System.out.println(new TokenGeneratorUsingAuth0().generate(new ClaimsCreatorAdmin(12L, "Edu")));
    }

}
