package com.github.bumblebee.webhook.registration;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telegram.api.BotApi;
import telegram.domain.SetWebHookResponse;
import telegram.domain.request.InputFile;

@Service
public class TelegramWebHookRegistrator {

  private static final Logger log = LoggerFactory.getLogger(TelegramWebHookRegistrator.class);
  private static final String URL_POSTFIX = "/webhook";

  private final BotApi botApi;

  @Autowired
  public TelegramWebHookRegistrator(BotApi botApi) {
    this.botApi = botApi;
  }

  public boolean registerWebHook(String url, String filePath) {
    log.info("Registering webhook: {}", url + URL_POSTFIX);
    return setWebHook(url + URL_POSTFIX, filePath);
  }

  public boolean removeWebHook() {
    log.info("Removing webhook...");
    return setWebHook("", null);
  }

  private boolean setWebHook(String hookUrl, String filePath) {
    File file;

    String resource = TelegramWebHookRegistrator.class.getClassLoader().getResource(filePath).getFile();

    file = new File(resource);

    InputFile inputFile = InputFile.document(file);

    SetWebHookResponse response = botApi.setWebhook(hookUrl, inputFile);
    log.info("Webhook registration: success = {}, description = {}", response.getResult(),
        response.getDescription());

    return response.getResult() != null && response.getResult();
  }

}
