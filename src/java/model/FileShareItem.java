package model;

public class FileShareItem {
    private int fileId;
    private String sharedTo; 
    private String sharedBy; 
    private boolean isPublic;

    public FileShareItem() {
    }

    public FileShareItem(int fileId, String sharedTo, String sharedBy, boolean isPublic) {
        this.fileId = fileId;
        this.sharedTo = sharedTo;
        this.sharedBy = sharedBy;
        this.isPublic = isPublic;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getSharedTo() {
        return sharedTo;
    }

    public void setSharedTo(String sharedTo) {
        this.sharedTo = sharedTo;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(String sharedBy) {
        this.sharedBy = sharedBy;
    }
}
