package telegram;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import telegram.api.BotApi;
import telegram.api.FileApi;
import telegram.impl.FileApiImpl;
import telegram.impl.MultipartEncoder;

public class TelegramBot {

    public static final String API_URL = "https://api.telegram.org/";
    public static final String BOT_API_URL = API_URL + "bot";
    public static final String FILE_API_URL = API_URL + "file/bot";

    private final Feign.Builder feignBuilder;
    private final String token;

    public TelegramBot(String token) {
        this.token = token;

        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        this.feignBuilder = Feign.builder()
                .decoder(new GsonDecoder(gson))
                .encoder(new MultipartEncoder(new GsonEncoder(gson)));
    }

    public TelegramBot withLogger(Logger logger) {
        feignBuilder.logger(logger);
        return this;
    }

    public TelegramBot withLogLevel(Logger.Level logLevel) {
        feignBuilder.logLevel(logLevel);
        return this;
    }

    public BotApi create() {
        return feignBuilder.target(BotApi.class, BOT_API_URL + token + "/");
    }

    public static FileApi createFileApi(String token) {
        return new FileApiImpl(token);
    }
}
