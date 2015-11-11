package telegram.api;

import telegram.domain.File;

public interface FileApi {

    String getDownloadUrl(File file);

    String getDownloadUrl(String filePath);
}
