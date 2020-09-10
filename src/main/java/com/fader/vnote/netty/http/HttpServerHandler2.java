package com.fader.vnote.netty.http;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.Map;
import java.util.Set;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;


/**
 * @author FaderW
 * 2019/9/18
 */
@ChannelHandler.Sharable
public class HttpServerHandler2 extends SimpleChannelInboundHandler<FullHttpRequest> {

    private FullHttpRequest fullHttpRequest;

    private Map<String, String> queryString = Maps.newHashMap();
    private Map<String, String> headers = Maps.newHashMap();
    private Set<Cookie> cookies;
    private ByteBuf body = Unpooled.EMPTY_BUFFER;


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        this.fullHttpRequest = msg;
        HttpMethod httpMethod = msg.method();
        String uri = msg.uri();

        // parse url param
        parseQueryStringParam(uri);
        // write cookie and headers
        writeCookieAndHeader();

        // parse body
        body = msg.content();

        writeResponse(ctx);
    }


    private void writeResponse(ChannelHandlerContext ctx) {
        StringBuilder content = new StringBuilder();
        content.append("Welcome to web server\r\n");

        ByteBuf byteBuf = Unpooled.copiedBuffer(content.toString(), CharsetUtil.UTF_8);

        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, byteBuf);

        fullHttpResponse.headers().set(CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);

        ChannelFuture future = ctx.writeAndFlush(fullHttpResponse);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    private void parseQueryStringParam(String uri) {
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
        queryStringDecoder.parameters().forEach((k, v) -> {
            queryString.put(k, v.get(0));
        });
    }

    private void writeCookieAndHeader() {
        fullHttpRequest.headers().forEach(entry ->
            headers.put(entry.getKey().toString(), entry.getValue().toString()));

        String cookieValue = headers.get(HttpHeaderNames.COOKIE);
        if (null != cookieValue) {
            cookies = ServerCookieDecoder.decode(cookieValue);
        }
    }
}
