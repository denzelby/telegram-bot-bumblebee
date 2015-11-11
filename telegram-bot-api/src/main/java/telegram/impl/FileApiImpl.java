package telegram.impl;

import telegram.TelegramBot;
import telegram.api.FileApi;
import telegram.domain.File;

public class FileApiImpl implements FileApi {

    private final String fileUrl;

    public FileApiImpl(String token) {
        this.fileUrl = TelegramBot.FILE_API_URL + token + "/";
    }

    @Override
    public String getDownloadUrl(File file) {
        return fileUrl + file.getFilePath();
    }

    @Override
    public String getDownloadUrl(String filePath) {
        return fileUrl + filePath;
    }

}
