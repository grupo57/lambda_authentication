package br.com.techchallenge.lambda.creator;

import java.util.Map;
import java.util.function.Supplier;

public interface ClaimsSupplier extends Supplier<Map<String,String>> {
}
