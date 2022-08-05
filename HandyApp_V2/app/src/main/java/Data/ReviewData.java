package Data;

public class ReviewData {
    private int image1;
    private String reviewname;
    private String review_text;
    private String divider;
    public ReviewData(int image1, String reviewname, String review_text, String divider) {
        this.image1 = image1;
        this.reviewname = reviewname;
        this.review_text = review_text;
        this.divider = divider;
    }

    public int getImage1() {
        return image1;
    }

    public String getReviewname() {
        return reviewname;
    }

    public String getReview_text() {
        return review_text;
    }
    public String getDivider() {
        return divider;
    }

}
