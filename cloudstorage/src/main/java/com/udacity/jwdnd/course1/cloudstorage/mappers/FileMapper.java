package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT COUNT(filename) FROM FILES WHERE userid = #{userid} AND filename = #{filename}")
    int getFileCount(Integer userid, String filename);

    @Select("SELECT fileid, filename FROM FILES WHERE userid = #{userid}")
    List<File> getAllFiles(Integer userid);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileid}")
    File getFile(Integer fileid);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES(#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileid")
    int addFile(File file);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileid}")
    void deleteFile(Integer fileid);
}
