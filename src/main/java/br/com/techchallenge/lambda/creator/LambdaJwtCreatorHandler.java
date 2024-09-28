package br.com.techchallenge.lambda.creator;

import br.com.techchallenge.lambda.creator.database.Database;
import br.com.techchallenge.lambda.creator.model.Response;
import br.com.techchallenge.lambda.creator.util.TokenGenerator;
import br.com.techchallenge.lambda.creator.util.TokenGeneratorUsingAuth0;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class LambdaJwtCreatorHandler implements RequestHandler<Map<String,String>, Response> {

    @Override
    public Response handleRequest(Map<String, String> input, Context context) {
        LambdaLogger logger = context.getLogger();

        logger.log("Input: " + input);
        logger.log("Database: " + Database.getTime());

        String tipo = input.get("tipo");
        if (tipo == null || tipo.isEmpty())
            throw new IllegalArgumentException("Error, geração do jwt: tipo não definido");

        if ("admin".equalsIgnoreCase(tipo))
            return new CreateResponseAdmin(input.get("user"), input.get("passwd")).create();
        if ("cliente".equalsIgnoreCase(tipo))
            return new CreateResponseCliente(input.get("cpf")).create();
        if ("anonimo".equalsIgnoreCase(tipo))
            return new CreateResponseAnonimo().create();

        throw new RuntimeException("Error, geração do jwt: tipo não identificado");
    }

    private Response getSuccessfulResponse(ClaimsSupplier creator) {
        TokenGenerator generator = new TokenGeneratorUsingAuth0();

        String token = null;
        token = generator.generate(creator);
        return new Response(token);
    }


    public abstract static class AbstractCreateResponseFrom {

        protected Response createToken(ClaimsSupplier creator) {
            TokenGenerator generator = new TokenGeneratorUsingAuth0();

            String token = null;
            token = generator.generate(creator);
            return new Response(token);
        }

        public abstract Response create();

    }

    public static class CreateResponseCliente extends AbstractCreateResponseFrom {

        private final String cpf;

        public CreateResponseCliente(String cpf) {
            this.cpf = cpf;
        }

        @Override
        public Response create() {
            if (cpf == null || cpf.isEmpty())
                throw new IllegalArgumentException("Obrigatório informar o cpf");

            String sql = """
                    SELECT c.id, c.cpf, c.nome
                    FROM clientes c
                    WHERE c.cpf = :cpf
                    """;

            try {
                List<Map<String, Object>> result = Database.query(sql, s -> {
                    try {
                        // setString usa index começando com 1
                        s.setString(1, cpf);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                if (result.isEmpty())
                    throw new IllegalArgumentException("cpf não encontrado");
                if (result.size() > 1)
                    throw new IllegalArgumentException("mais de um cpf encontrado");

                return createToken(new ClaimsCreatorCliente(
                        result.get(0).get("cpf").toString(),
                        result.get(0).get("nome").toString()));
            } catch (SQLException e) {
                throw new IllegalArgumentException("erro", e);
            }
        }
    }

    public static class CreateResponseAdmin extends AbstractCreateResponseFrom {

        private final String user;
        private final String passwd;

        public CreateResponseAdmin(String user, String passwd) {
            this.user = user;
            this.passwd = passwd;
        }

        @Override
        public Response create() {
            if (user == null || user.isEmpty())
                throw new IllegalArgumentException("Obrigatório informar o user");
            if (passwd == null || passwd.isEmpty())
                throw new IllegalArgumentException("Obrigatório informar o passwd");

            String sql = """
                    SELECT u.id, u.nome
                    FROM usuario u
                    WHERE u.user = :user
                    """;

            try {
                List<Map<String, Object>> result = Database.query(sql, s -> {
                    try {
                        s.setString(1, user);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                if (result.isEmpty())
                    throw new IllegalArgumentException("usuário não encontrado");
                if (result.size() > 1)
                    throw new IllegalArgumentException("usuário não encontrado");

                if (!isValidateHash(passwd.getBytes(), ((String)result.get(0).get("passwd")).getBytes()))
                    throw new IllegalArgumentException("usuario e senha inválidos");

                return createToken(new ClaimsCreatorCliente(
                        result.get(0).get("id").toString(),
                        result.get(0).get("nome").toString()));
            } catch (SQLException e) {
                throw new IllegalArgumentException("erro", e);
            }
        }
    }

    public static class CreateResponseAnonimo extends AbstractCreateResponseFrom {

        @Override
        public Response create() {
            return createToken(new ClaimsCreatorAnonimo());
        }

    }

    public static String getHash(String passwordToHash) {
        return passwordToHash;
    }

    public static boolean isValidateHash(byte[] param, byte[] value) {
        return true;
    }

}