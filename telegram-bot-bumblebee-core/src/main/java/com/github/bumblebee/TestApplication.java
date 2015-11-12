package com.github.bumblebee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import telegram.api.BotApi;
import telegram.api.FileApi;
import telegram.polling.TelegramUpdateService;

// just ugly temporary test app
public class TestApplication {

    private static final Logger log = LoggerFactory.getLogger(TestApplication.class);

    public static void main(String[] args) {

        BumblebeeBot bee = new BumblebeeBot();
        BotApi bot = bee.create();
        FileApi fileApi = bee.createFileApi();
        TelegramUpdateService updateService = new TelegramUpdateService(bot);

//        updateService.bind(new CommandHandler(update -> {
//
//            String chatId = update.getMessage().getChat().getId().toString();
//            bot.forwardMessage(chatId,
//                    chatId,
//                    update.getMessage().getMessageId());
//
//            bot.sendMessage(update.getMessage().getChat().getId(), "I'm fine!");
//            bot.sendChatAction(chatId, ChatAction.TYPING);
////            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(new String[]{"foo", "bar"}, new String[]{"wee", "Go!"});
//            ForceReply keyboard = new ForceReply();
//            bot.sendLocation(chatId, 53.931336f, 27.576950f, update.getMessage().getMessageId(),
//                    keyboard);
//            return false;
//        }, "/statusCommand"));
//
//        updateService.bind(new CommandHandler(update -> {
//
//            String chatId = update.getMessage().getChat().getId().toString();
//
//            try {
//                String path = TestApplication.class.getClassLoader().getResource("pic.jpg").getFile();
//                bot.sendPhoto(chatId, InputFile.photo(new File(path)));
//            } catch (Exception e) {
//                log.error("fail", e);
//            }
//            return false;
//        }, "/pic"));
//
//        updateService.bind(new CommandHandler(update -> {
//
//            String chatId = update.getMessage().getChat().getId().toString();
//
//            try {
//                String path = TestApplication.class.getClassLoader().getResource("meow.mp3").getFile();
//                bot.sendAudio(chatId, InputFile.audio(new File(path)));
//            } catch (Exception e) {
//                log.error("fail", e);
//            }
//            return false;
//        }, "/audio"));
//
//        updateService.bind(new CommandHandler(update -> {
//
//            String chatId = update.getMessage().getChat().getId().toString();
//
//            try {
//                String path = TestApplication.class.getClassLoader().getResource("simple.txt").getFile();
//                bot.sendDocument(chatId, InputFile.document(new File(path)));
//            } catch (Exception e) {
//                log.error("fail", e);
//            }
//            return false;
//        }, "/doc"));
//
//        updateService.bind(new CommandHandler(update -> {
//
//            String chatId = update.getMessage().getChat().getId().toString();
//
//            try {
//                String path = TestApplication.class.getClassLoader().getResource("gman.ogg").getFile();
//                bot.sendDocument(chatId, InputFile.voice(new File(path)));
//            } catch (Exception e) {
//                log.error("fail", e);
//            }
//            return false;
//        }, "/voice"));
//
//        updateService.bind(new CommandHandler(update -> {
//            BasicResponse<UserProfilePhotos> photos = bot.getUserProfilePhotos(update.getMessage().getFrom().getId());
//
//            System.out.println("photos = " + photos);
//
//            bot.sendPhoto(update.getMessage().getChat().getId().toString(),
//                    photos.getResult().getPhotos()[0][0].getFileId());
//            return false;
//        }, "/usr"));
//
//        updateService.bind(new CommandHandler(update -> {
//
//            BasicResponse<Boolean> hook = bot.setWebhook("");
//
//            if (!hook.getResult()) {
//                throw new IllegalStateException("Failed to set hook");
//            }
//            log.info(hook.toString());
//            return false;
//        }, "/hook"));
//
//        updateService.bind(new CommandHandler(update -> {
//
//            String fileId = bot.getUserProfilePhotos(update.getMessage().getFrom().getId())
//                    .getResult().getPhotos()[0][0].getFileId();
//
//            BasicResponse<telegram.domain.File> file = bot.getFile(fileId);
//
//            telegram.domain.File f = file.getResult();
//            bot.sendMessage(update.getMessage().getChat().getId(),
//                    MessageFormat.format("id={0}\n" +
//                            "size={1}\n" +
//                            "url: {2}", f.getFileId(), f.getFileSize(), fileApi.getDownloadUrl(f)));
//            return false;
//        }, "/getfile"));

        updateService.startPolling();
    }
}
