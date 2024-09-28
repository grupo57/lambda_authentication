package br.com.techchallenge.lambda.creator.util;

import br.com.techchallenge.lambda.creator.ClaimsSupplier;

import java.security.NoSuchAlgorithmException;

public interface TokenGenerator {
    String generate(ClaimsSupplier claims);
}
