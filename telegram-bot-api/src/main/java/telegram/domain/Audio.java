package telegram.domain;

public class Audio {

    private String fileId;
    private Long duration;
    private String performer;
    private String title;
    private String mimeType;
    private Long fileSize;

    public String getFileId() {
        return fileId;
    }

    public Long getDuration() {
        return duration;
    }

    public String getPerformer() {
        return performer;
    }

    public String getTitle() {
        return title;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    @Override
    public String toString() {
        return "Audio{" +
                "fileId='" + fileId + '\'' +
                ", duration=" + duration +
                ", performer='" + performer + '\'' +
                ", title='" + title + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }
}
