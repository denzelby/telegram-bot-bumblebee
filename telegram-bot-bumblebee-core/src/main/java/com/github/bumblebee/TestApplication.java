package com.github.bumblebee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import telegram.BumblebeeBot;
import telegram.api.BotApi;
import telegram.api.FileApi;
import telegram.domain.BasicResponse;
import telegram.domain.Message;
import telegram.domain.UserProfilePhotos;
import telegram.domain.request.ChatAction;
import telegram.domain.request.InputFile;
import telegram.domain.request.keyboard.ForceReply;
import telegram.polling.CommandHandler;
import telegram.polling.TelegramUpdateService;

import java.io.File;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

// just ugly temporary test app
public class TestApplication {

    private static final Logger log = LoggerFactory.getLogger(TestApplication.class);

    public static void main(String[] args) {

        BumblebeeBot bee = new BumblebeeBot();
        BotApi bot = bee.create();
        FileApi fileApi = bee.createFileApi();
        TelegramUpdateService updateService = new TelegramUpdateService(bot, 1, TimeUnit.SECONDS);

        updateService.onUpdate(update -> {
            log.info("Update: {}", update);

            Message message = update.getMessage();
            Integer chatId = message.getChat().getId();

            if (message.getText() != null) {
//                bot.sendMessage(chatId,
//                        MessageFormat.format("You said: {0}", message.getText()));
                log.info(">>> " + message.getText());
            } else if (message.getPhoto() != null) {
                bot.sendPhoto(chatId.toString(), message.getPhoto().get(0).getFileId(), "Pew-pew-pew, lazers");
            } else {
                bot.sendMessage(chatId, "Wut da fuk?!");
            }
        });

        updateService.onUpdate(new CommandHandler("/status", update -> {

            String chatId = update.getMessage().getChat().getId().toString();
            bot.forwardMessage(chatId,
                    chatId,
                    update.getMessage().getMessageId());

            bot.sendMessage(update.getMessage().getChat().getId(), "I'm fine!");
            bot.sendChatAction(chatId, ChatAction.TYPING);
//            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(new String[]{"foo", "bar"}, new String[]{"wee", "Go!"});
            ForceReply keyboard = new ForceReply();
            bot.sendLocation(chatId, 53.931336f, 27.576950f, update.getMessage().getMessageId(),
                    keyboard);
        }));

        updateService.onUpdate(new CommandHandler("/pic", update -> {

            String chatId = update.getMessage().getChat().getId().toString();

            try {
                String path = TestApplication.class.getClassLoader().getResource("pic.jpg").getFile();
                bot.sendPhoto(chatId, InputFile.photo(new File(path)));
            } catch (Exception e) {
                log.error("fail", e);
            }
        }));

        updateService.onUpdate(new CommandHandler("/audio", update -> {

            String chatId = update.getMessage().getChat().getId().toString();

            try {
                String path = TestApplication.class.getClassLoader().getResource("meow.mp3").getFile();
                bot.sendAudio(chatId, InputFile.audio(new File(path)));
            } catch (Exception e) {
                log.error("fail", e);
            }
        }));

        updateService.onUpdate(new CommandHandler("/doc", update -> {

            String chatId = update.getMessage().getChat().getId().toString();

            try {
                String path = TestApplication.class.getClassLoader().getResource("simple.txt").getFile();
                bot.sendDocument(chatId, InputFile.document(new File(path)));
            } catch (Exception e) {
                log.error("fail", e);
            }
        }));

        updateService.onUpdate(new CommandHandler("/voice", update -> {

            String chatId = update.getMessage().getChat().getId().toString();

            try {
                String path = TestApplication.class.getClassLoader().getResource("gman.ogg").getFile();
                bot.sendDocument(chatId, InputFile.voice(new File(path)));
            } catch (Exception e) {
                log.error("fail", e);
            }
        }));

        updateService.onUpdate(new CommandHandler("/usr", update -> {
            BasicResponse<UserProfilePhotos> photos = bot.getUserProfilePhotos(update.getMessage().getFrom().getId());

            System.out.println("photos = " + photos);

            bot.sendPhoto(update.getMessage().getChat().getId().toString(),
                    photos.getResult().getPhotos()[0][0].getFileId());
        }));

        updateService.onUpdate(new CommandHandler("/hook", update -> {

            BasicResponse<Boolean> hook = bot.setWebhook("");

            if (!hook.getResult()) {
                throw new IllegalStateException("Failed to set hook");
            }
            log.info(hook.toString());
        }));

        updateService.onUpdate(new CommandHandler("/getfile", update -> {

            String fileId = bot.getUserProfilePhotos(update.getMessage().getFrom().getId())
                    .getResult().getPhotos()[0][0].getFileId();

            BasicResponse<telegram.domain.File> file = bot.getFile(fileId);

            telegram.domain.File f = file.getResult();
            bot.sendMessage(update.getMessage().getChat().getId(),
                    MessageFormat.format("id={0}\n" +
                            "size={1}\n" +
                            "url: {2}", f.getFileId(), f.getFileSize(), fileApi.getDownloadUrl(f)));

        }));

        updateService.startPolling();
    }
}
