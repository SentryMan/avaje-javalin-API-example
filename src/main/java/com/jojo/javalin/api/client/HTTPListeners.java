package com.jojo.javalin.api.client;

import io.avaje.http.client.RequestListener.Event;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;

public final class HTTPListeners {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(HTTPListeners.class);

  private static final String DELIMITER = "\n";

  private static final String AUTHORIZATION = "Authorization";

  private HTTPListeners() {}

  public static void logRequest(Event event) {

    final HttpResponse<?> response = event.response();

    final var request = response.request();

    final var micros = event.responseTimeMicros();

    final var sb = new StringBuilder();

    sb.append("statusCode:")
        .append(response.statusCode())
        .append(" method:")
        .append(request.method())
        .append(" uri:")
        .append(event.uri())
        .append(" timeMicros:")
        .append(micros);

    // log request headers

    headers(sb, "req-head: ", request.headers());

    // headers(sb, "res-head: ", response.headers());

    //    Log Request/Response body

    body(sb, "req-body: ", event.requestBody());

    body(sb, "res-body: ", event.responseBody());

    log.info(sb.toString());
  }

  private static void body(StringBuilder sb, String label, String body) {

    if (body != null) {

      sb.append(DELIMITER).append(label).append(body);
    }
  }

  private static void headers(StringBuilder sb, String label, HttpHeaders headers) {

    final var entries = headers.map().entrySet();

    if (!entries.isEmpty()) {

      sb.append(DELIMITER).append(label);

      for (final var entry : entries) {

        final var key = entry.getKey();

        if (obfuscate(key)) {

          sb.append(key).append("=<obfuscated>, ");

        } else {

          sb.append(key).append("=").append(entry.getValue()).append(", ");
        }
      }
    }
  }

  private static boolean obfuscate(String key) {

    return AUTHORIZATION.equals(key);
  }
}
