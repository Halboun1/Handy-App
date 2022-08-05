package Data;

public class CategoryData {
    Integer imgCategory;
    String tvCategory;

    public CategoryData(Integer imgCategory, String tvCategory) {
        this.imgCategory = imgCategory;
        this.tvCategory = tvCategory;
    }

    public Integer getImgCategory() {
        return imgCategory;
    }

    public void setImgCategory(Integer imgCategory) {
        this.imgCategory = imgCategory;
    }

    public String getTvCategory() {
        return tvCategory;
    }

    public void setTvCategory(String tvCategory) {
        this.tvCategory = tvCategory;
    }

    boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
