package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public boolean isFilenameAvailable(Integer userid, String filename) {
        return fileMapper.getFileCount(userid, filename) == 0;
    }

    public List<File> getAllFiles(Integer userid) {
        return fileMapper.getAllFiles(userid);
    }

    public File getFile(Integer fileid) {
        return fileMapper.getFile(fileid);
    }

    public int addFile(File file) {
        return fileMapper.addFile(file);
    }

    public void deleteFile(Integer fileid) {
        fileMapper.deleteFile(fileid);
    }
}
