package telegram.domain.request;

import java.io.File;
import java.io.InputStream;

public class InputFile {

    private static final String MIME_PHOTO = "image/jpeg";
    private static final String MIME_AUDIO = "audio/mpeg";
    private static final String MIME_VIDEO = "video/mp4";
    private static final String MIME_VOICE = "audio/ogg";
    private static final String MIME_UNKNOWN = "";

    private String mimeType;

    // or stream+name or file should be defined, not both
    // todo: migrate to retrofit to not deal with such hacks
    private File file;
    private String fileName;
    private InputStream stream;

    public InputFile(String mimeType, File file) {
        this.mimeType = mimeType;
        this.file = file;
    }

    public InputFile(String mimeType, String fileName, InputStream stream) {
        this.mimeType = mimeType;
        this.fileName = fileName;
        this.stream = stream;
    }

    public String getMimeType() {
        return mimeType;
    }

    public File getFile() {
         return file;
    }

    public InputStream getStream() {
        return stream;
    }

    public String getFileName() {
        return (file != null) ? file.getName() : this.fileName;
    }

    public static InputFile photo(File file) {
        return new InputFile(MIME_PHOTO, file);
    }

    public static InputFile audio(File file) {
        return new InputFile(MIME_AUDIO, file);
    }

    public static InputFile video(File file) {
        return new InputFile(MIME_VIDEO, file);
    }

    public static InputFile voice(File file) {
        return new InputFile(MIME_VOICE, file);
    }

    public static InputFile document(File file) {
        return new InputFile(MIME_UNKNOWN, file);
    }

    public static InputFile photo(InputStream stream, String fileName) {
        return new InputFile(MIME_PHOTO, fileName, stream);
    }

    public static InputFile audio(InputStream stream, String fileName) {
        return new InputFile(MIME_AUDIO, fileName, stream);
    }

    public static InputFile video(InputStream stream, String fileName) {
        return new InputFile(MIME_VIDEO, fileName, stream);
    }

    public static InputFile voice(InputStream stream, String fileName) {
        return new InputFile(MIME_VOICE, fileName, stream);
    }

    public static InputFile document(InputStream stream, String fileName) {
        return new InputFile(MIME_UNKNOWN, fileName, stream);
    }

}
