package com.olky.exchangerateservice.apiCryptoResponse;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptoPriceResponse extends Mono<Map<String, Map<String, String>>> {
    private Map<String, Map<String, String>> crypto;
    @JsonAnySetter
    public void setCrypto(String symbol, Map<String, String> crypto) {
        this.crypto = new HashMap<>();
        this.crypto.put(symbol, crypto);
    }
    @Override
    public void subscribe(CoreSubscriber<? super Map<String, Map<String, String>>> coreSubscriber) {
        coreSubscriber.onNext(crypto);
        coreSubscriber.onComplete();
    }
}