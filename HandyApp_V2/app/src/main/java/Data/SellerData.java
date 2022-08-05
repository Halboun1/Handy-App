package Data;

public class SellerData {
    Integer imgBg;
    String tvRating,tvTitle,tvUser,tvTiming,tvPrice;

    public SellerData(Integer imgBg, String tvRating, String tvTitle, String tvUser, String tvTiming, String tvPrice) {
        this.imgBg = imgBg;
        this.tvRating = tvRating;
        this.tvTitle = tvTitle;
        this.tvUser = tvUser;
        this.tvPrice = tvPrice;
    }

    public Integer getImgBg() {
        return imgBg;
    }

    public void setImgBg(Integer imgBg) {
        this.imgBg = imgBg;
    }

    public String getTvRating() {
        return tvRating;
    }

    public void setTvRating(String tvRating) {
        this.tvRating = tvRating;
    }

    public String getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(String tvTitle) {
        this.tvTitle = tvTitle;
    }

    public String getTvUser() {
        return tvUser;
    }

    public void setTvUser(String tvUser) {
        this.tvUser = tvUser;
    }

    public String getTvTiming() {
        return tvTiming;
    }

    public void setTvTiming(String tvTiming) {
        this.tvTiming = tvTiming;
    }

    public String getTvPrice() {
        return tvPrice;
    }

    public void setTvPrice(String tvPrice) {
        this.tvPrice = tvPrice;
    }



    boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
