package telegram.domain;


public class PhotoSize {

    private String fileId;
    private Integer width;
    private Integer height;
    private Long fileSize;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "PhotoSize{" +
                "fileId='" + fileId + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", fileSize=" + fileSize +
                '}';
    }
}
