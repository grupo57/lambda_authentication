package br.com.techchallenge.lambda;

public class Parameters {
    private static final String ISSUER_ALLOWED_VALUE_KEY = "issuer";
    private static final String ISSUER_ALLOWED_VALUE_DEFAULT = "Grupo57";
    private static final String AUDIENCE_VALUE_KEY = "audience";
    private static final String AUDIENCE_VALUE_DEFAULT = "fiap-tech-challenge";
    private static final int EXPIRES_IN_SECONDS = 1200;

    //Must be atleast 128 bits long
    private static final String SECRET_KEY = "secretKeysecretKeysecretKeysecretKey";
    private static final String DB_ADMIN_USER_KEY = "db-user";
    private static final String DB_ADMIN_PWD_KEY = "db-pwd";
    private static final String DB_URL_KEY = "db-url";


    public static String getIssuerAllowed() {
        String value = System.getenv(ISSUER_ALLOWED_VALUE_KEY);
        return value == null || value.isEmpty() ? ISSUER_ALLOWED_VALUE_DEFAULT : value;
    }

    public static String getAudience() {
        String value = System.getenv(AUDIENCE_VALUE_KEY);
        return value == null || value.isEmpty() ? AUDIENCE_VALUE_DEFAULT : value;
    }

    public static int getExpiresInSeconds() {
        return EXPIRES_IN_SECONDS;
    }

    public static String getSecretKey() {
        return SECRET_KEY;
    }

    public static String getDbAdminUser() {
        String value = System.getenv(DB_ADMIN_USER_KEY);
        if (value == null || value.isEmpty())
            throw new RuntimeException("parametro: DB_ADMIN_USER não localizado");
        return value;
    }

    public static String getDbAdminPwd() {
        String value = System.getenv(DB_ADMIN_PWD_KEY);
        if (value == null || value.isEmpty())
            throw new RuntimeException("parametro: DB_ADMIN_PWD não localizado");
        return value;
    }

    public static String getDbUrl() {
        String value = System.getenv(DB_URL_KEY);
        if (value == null || value.isEmpty())
            throw new RuntimeException("parametro: DB_URL não localizado");
        return value;
    }

}
