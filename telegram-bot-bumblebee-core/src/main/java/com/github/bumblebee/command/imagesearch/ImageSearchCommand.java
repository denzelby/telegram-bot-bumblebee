package com.github.bumblebee.command.imagesearch;

import com.github.bumblebee.command.SingleArgumentCommand;
import com.github.bumblebee.command.imagesearch.domain.Image;
import com.github.bumblebee.command.imagesearch.domain.ImageProvider;
import com.github.bumblebee.command.imagesearch.domain.ImagesPreprocessor;
import com.github.bumblebee.command.imagesearch.exception.ImageSendException;
import com.github.bumblebee.service.RandomPhraseService;
import com.github.telegram.api.BotApi;
import com.github.telegram.domain.ChatAction;
import com.github.telegram.domain.Update;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.net.URL;
import java.util.List;

public class ImageSearchCommand extends SingleArgumentCommand {

    private static final Logger log = LoggerFactory.getLogger(ImageSearchCommand.class);

    private final BotApi botApi;
    private final RandomPhraseService randomPhrase;
    private final List<ImageProvider> providers;
    private final ImagesPreprocessor preprocessor;

    public ImageSearchCommand(BotApi botApi, RandomPhraseService randomPhrase, List<ImageProvider> providers,
                              ImagesPreprocessor preprocessor) {
        this.botApi = botApi;
        this.randomPhrase = randomPhrase;
        this.providers = providers;
        this.preprocessor = preprocessor;
    }

    @Override
    public void handleCommand(Update update, long chatId, String argument) {

        if (argument == null) {
            botApi.sendMessage(chatId, randomPhrase.surprise());
            return;
        }

        Long messageId = update.getMessage().getMessageId();

        botApi.sendChatAction(chatId, ChatAction.UPLOAD_PHOTO);

        for (ImageProvider provider : providers) {
            List<Image> images = search(provider, argument);
            if (!CollectionUtils.isEmpty(images)) {
                boolean isSent = sendRandomPicture(images, chatId, argument);
                if (isSent) {
                    return;
                }
            }
            log.warn("Provider {} failed to find images", provider.name());
        }
        botApi.sendMessage(chatId, randomPhrase.no(), messageId);
    }

    private List<Image> search(ImageProvider provider, String query) {
        try {
            return provider.search(query);
        } catch (Exception e) {
            log.error("Search failed", e);
            return null;
        }
    }

    private boolean sendRandomPicture(List<Image> pictures, Long chatId, String caption) {

        preprocessor.process(pictures);

        for (Image picture : pictures) {
            try {
                String url = picture.getUrl();
                log.info("Sending ({}): {}", picture.getContentType(), url);
                sendImage(url, chatId, caption);
                return true;
            } catch (ImageSendException e) {
                log.error("Image send failed, retrying...", e);
            }
        }
        return false;
    }

    private void sendImage(String url, Long chatId, String caption) throws ImageSendException {

        try {
//            String fileName = LinkUtils.getFileName(url);
            byte[] imageBytes = IOUtils.toByteArray(new URL(url));

            botApi.sendPhoto(chatId, imageBytes, "image/jpeg", caption, null, null, null);
        } catch (Exception e) {
            log.error("Failed to send: {}", url);
            throw new ImageSendException(e);
        }
    }
}
