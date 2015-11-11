package telegram.domain.request;

import java.io.File;

public class InputFile {

    private String mimeType;
    private File file;

    public InputFile(String mimeType, File file) {
        this.mimeType = mimeType;
        this.file = file;
    }

    public String getMimeType() {
        return mimeType;
    }

    public File getFile() {
        return file;
    }

    public static InputFile photo(File file) {
        return new InputFile("image/jpeg", file);
    }

    public static InputFile audio(File file) {
        return new InputFile("audio/mpeg", file);
    }

    public static InputFile video(File file) {
        return new InputFile("video/mp4", file);
    }

    public static InputFile voice(File file) {
        return new InputFile("audio/ogg", file);
    }

    public static InputFile document(File file) {
        return new InputFile("", file);
    }
}
