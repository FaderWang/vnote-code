package com.fader.vnote.netty.http;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpHeaderUtil.isTransferEncodingChunked;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;

/**
 * @author FaderW
 * 2019/9/17
 */

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private static final String FAVICON_URI = "/favicon.ico";

    private HttpPostRequestDecoder decoder;

    private HttpRequest httpRequest;

    private StringBuilder content = new StringBuilder();

    private Map<String, String> parameters = Maps.newHashMap();
    private Map<String, String> attributes = Maps.newHashMap();

    private String json;

    private static HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (decoder != null) {
            decoder.cleanFiles();
        }
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            this.httpRequest = (HttpRequest) msg;

            String url = httpRequest.uri();
            URI uri = new URI(url);

            System.err.println("url ---> " + url);
            System.err.println("uri path ---> " + uri.getPath());
            if (url.contains(FAVICON_URI)) {
                return;
            }
            content.setLength(0);
            content.append("Welcome to web server\r\n");
            content.append("Version: " + httpRequest.protocolVersion().toString()+ "\r\n");
            content.append("Uri: " + url + "\r\n");
            content.append("\r\n");

            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(url);
            //parse url param
            Map<String, List<String>> parameters = queryStringDecoder.parameters();
            for (Entry<String, List<String>> param : parameters.entrySet()) {
                content.append("parameter: " + param.getKey() + "=" + param.getValue() + "\r\n");
                this.parameters.put(param.getKey(), param.getValue().get(0));
            }

            //write headers
            for (Entry<CharSequence, CharSequence> entry : httpRequest.headers()) {
                content.append("Header: " + entry.getKey() + "=" + entry.getValue() + "\r\n");
            }
            content.append("\r\n");

            //write cookies
            Set<Cookie> cookies;
            String value = (String) httpRequest.headers().get(COOKIE);
            if (null != value) {
                cookies = ServerCookieDecoder.decode(value);
            } else {
                cookies = Collections.emptySet();
            }
            for (Cookie cookie : cookies) {
                content.append("Cookie: " + cookie.toString() + "\r\n");
            }
            content.append("\r\n");
        } else if (msg instanceof HttpContent) {
            HttpContent chunking = (HttpContent) msg;
            parseHttpMessage(chunking);

            if (msg instanceof LastHttpContent) {
                writeResponse(ctx);
                reset();
            }
        }

    }

    private void parseHttpMessage(HttpContent httpContent) {
        if (httpRequest.method().equals(HttpMethod.POST)) {
            if (httpRequest.headers().get(CONTENT_TYPE).toString().contains("json")) {
                ByteBuf jsonBuf = httpContent.content();
                this.json = jsonBuf.toString(CharsetUtil.UTF_8);
                content.append("Json: " + this.json);
                return;
            }
        }
        decoder = new HttpPostRequestDecoder(factory, httpRequest);
        decoder.offer(httpContent);
        try {
            while (decoder.hasNext()) {
                InterfaceHttpData data = decoder.next();
                if (data != null) {
                    try {
                        parseHttpData(data);
                    } finally {
                        data.release();
                    }
                }
            }
        } catch (HttpPostRequestDecoder.EndOfDataDecoderException e) {
            content.append("\r\n\r\nEND OF CONTENT CHUNK BY CHUNK\r\n\r\n");
        }

    }


    /**
     * HttpDataType有三种类型
     * Attribute, FileUpload, InternalAttribute
     * @param data
     */
    private void parseHttpData(InterfaceHttpData data) {
        if (data.getHttpDataType().equals(InterfaceHttpData.HttpDataType.Attribute)) {
            Attribute attribute = (Attribute) data;
            String value;
            try {
                value = attribute.getValue();
            } catch (IOException e) {
                content.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ":"
                        + attribute.getName() + " Error while reading value: " + e.getMessage() + "\r\n");
                return;
            }
            if (value.length() > 100) {
                content.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ":"
                        + attribute.getName() + " data too long\r\n");
            } else {
                content.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ":"
                        + attribute.toString() + "\r\n");
                attributes.put(attribute.getName(), value);
            }
        }
    }


    private void reset() {
        httpRequest = null;
        json = null;
        parameters = null;
        attributes = null;
        decoder.destroy();
        decoder = null;
    }

    private void writeResponse(ChannelHandlerContext ctx) {
        ByteBuf byteBuf = Unpooled.copiedBuffer(content.toString(), CharsetUtil.UTF_8);
        System.out.println(content.toString());

        // Decide whether to close the connection or not.
        boolean close = httpRequest.headers().contains(CONNECTION, CLOSE, true)
                || httpRequest.protocolVersion().equals(HttpVersion.HTTP_1_0)
                && !httpRequest.headers().contains(CONNECTION, KEEP_ALIVE, true);

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        if (!close) {
            response.headers().set(CONTENT_LENGTH, String.valueOf(byteBuf.readableBytes()));

        }

        Set<Cookie> cookies;
        String value = (String) httpRequest.headers().get(COOKIE);
        if (value == null) {
            cookies = Collections.emptySet();
        } else {
            cookies = ServerCookieDecoder.decode(value);
        }
        if (!cookies.isEmpty()) {
            // Reset the cookies if necessary.
            for (Cookie cookie : cookies) {
                response.headers().add(SET_COOKIE, ServerCookieEncoder.encode(cookie));
            }
        }
        // Write the response.
        ChannelFuture future = ctx.channel().writeAndFlush(response);
        // Close the connection after the write operation is done if necessary.
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
