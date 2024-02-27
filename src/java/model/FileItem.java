package model;

import java.util.Date;

public class FileItem {

    private int fileId;
    private String fileName = "unknown";
    private String owner;
    private String location = null;
    private boolean folder = true;
    private String path = "root";
    private Date updated = new Date();
    private boolean trashed = false;

    public FileItem() {}

    public int getFileId() {
        return fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getOwner() {
        return owner;
    }

    public String getLocation() {
        return location;
    }

    public boolean isFolder() {
        return folder;
    }

    public String getPath() {
        return path;
    }

    public String getDescendantPath() {
        return path + "." + fileId;
    }

    public Date getUpdated() {
        return updated;
    }

    public boolean isTrashed() {
        return trashed;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setIsFolder(boolean isFolder) {
        this.folder = isFolder;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public void setTrashed(boolean trashed) {
        this.trashed = trashed;
    }
}
