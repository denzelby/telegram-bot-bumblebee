package telegram.impl;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import okio.Buffer;
import telegram.domain.request.InputFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

/**
 * Poor mans support of multipart form data for Feign.
 */
public class MultipartEncoder implements Encoder {

    private static final String CONTENT_TYPE = "Content-type";

    private final Encoder delegate;

    public MultipartEncoder(Encoder delegate) {
        this.delegate = delegate;
    }

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        if (isMultipart(template)) {
            encodeAsMultipart(object, bodyType, template);
        } else {
            delegate.encode(object, bodyType, template);
        }
    }

    private boolean isMultipart(RequestTemplate template) {
        Collection<String> contentType = template.headers().get(CONTENT_TYPE);
        return contentType != null && contentType.stream().anyMatch("multipart/form-data"::equals);
    }

    private void encodeAsMultipart(Object object, Type bodyType, RequestTemplate template) {

        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);

        for (Map.Entry<String, ?> param : ((Map<String, ?>) object).entrySet()) {

            final Object value = param.getValue();
            if (value instanceof InputFile) {
                InputFile inputFile = (InputFile) value;
                builder.addFormDataPart(
                        param.getKey(),
                        inputFile.getFile().getName(),
                        RequestBody.create(MediaType.parse(inputFile.getMimeType()), inputFile.getFile())
                );
            } else {
                builder.addFormDataPart(param.getKey(), value.toString());
            }
        }
        try {
            RequestBody requestBody = builder.build();
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            template.header(CONTENT_TYPE, requestBody.contentType().toString());
            // buffering? Never heard of it.
            template.body(buffer.readByteArray(), Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
