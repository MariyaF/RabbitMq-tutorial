package fronina.example;

public class ReportQuery {
    private Long regionId;
    private String email;

    public ReportQuery(Long regionId, String email) {
        this.regionId = regionId;
        this.email = email;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
