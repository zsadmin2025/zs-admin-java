package com.zs.common.core.crypto;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.InputStream;

/**
 *
 */
public class CustomHttpInputMessage implements HttpInputMessage {

    private final HttpHeaders headers;
    private final InputStream body;

    public CustomHttpInputMessage(HttpHeaders headers, InputStream body) {
        this.headers = headers;
        this.body = body;
    }



    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }

    @Override
    public InputStream getBody() {
        return body;
    }
}
