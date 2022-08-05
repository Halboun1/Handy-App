package Activities.models;

public class Review {
    String keyid;
    String rate;
    String review;
    String uid;

    public Review(String keyid, String rate, String review, String uid) {
        this.keyid = keyid;
        this.rate = rate;
        this.review = review;
        this.uid = uid;
    }

    public String getKeyid() {
        return keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Review() {
    }
}
