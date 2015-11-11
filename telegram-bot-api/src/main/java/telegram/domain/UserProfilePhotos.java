package telegram.domain;

import java.util.Arrays;

public class UserProfilePhotos {

    private Integer totalCount;
    private PhotoSize[][] photos;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public PhotoSize[][] getPhotos() {
        return photos;
    }

    public void setPhotos(PhotoSize[][] photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "UserProfilePhotos{" +
                "totalCount=" + totalCount +
                ", photos=" + Arrays.toString(photos) +
                '}';
    }
}
