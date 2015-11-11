package net.byteabyte.beak.network.common;

import android.util.Log;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import okio.Buffer;

class OkHttpLogger {
  private boolean printHeaders = false;

  public void setPrintHeaders(boolean printHeaders) {
    this.printHeaders = printHeaders;
  }

  void logRequest(String tag, Request request){
    try {
      Buffer buffer = new Buffer();
      if(request.body() != null) request.body().writeTo(buffer);
      String body = buffer.readUtf8();
      Log.d(tag, "Request  --------------");
      Log.d(tag, request.urlString());

      if(printHeaders) {
        Map<String, List<String>> headers = request.headers().toMultimap();
        printHeaders(tag, headers);
      }

      Log.d(tag, body);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  void logResponse(String tag, Response response){
    Log.d(tag, "Response --------------");
    Log.d(tag, String.valueOf(response.code()));

    if(printHeaders) {
      Map<String, List<String>> headers = response.headers().toMultimap();
      printHeaders(tag, headers);
    }
  }

  private void printHeaders(String tag, Map<String, List<String>> headers){
    Log.d(tag, "Headers");
    for(Map.Entry<String, List<String>> header: headers.entrySet()){

      String headerValue = "";
      for(String value: header.getValue()){
        headerValue += value;
        headerValue += ": ";
      }

      Log.d(tag, header.getKey() + ": " + headerValue);
    }
  }
}
