package model;

import java.io.InputStream;
import java.util.Date;

public class FileItem {

    private int fileId;
    private String fileName = "unknown";
    private String owner;
    private String location = null;
    private boolean folder = true;
    private String path = "-1";
    private Date updated = new Date();
    private boolean trashed = false;
    private int size = 0;
    private int oldParent = -1;
    private InputStream fileInputStream;

    public FileItem() {}

    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    public void setFileInputStream(InputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


    public int getFileId() {
        return fileId;
    }

    public int getOldParent() {
        return oldParent;
    }

    public void setOldParent(int old) {
        this.oldParent = old;
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
