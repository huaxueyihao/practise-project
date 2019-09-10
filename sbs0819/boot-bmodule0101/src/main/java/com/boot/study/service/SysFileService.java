package com.boot.study.service;

import com.boot.study.bean.MenuTreeDto;
import com.boot.study.bean.MiniMenuTreeDto;
import com.boot.study.common.PageParam;
import com.boot.study.common.PageResult;
import com.boot.study.common.TreeDto;
import com.boot.study.common.ZTreeDto;
import com.boot.study.model.SysFile;
import com.boot.study.model.SysMenu;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SysFileService {


    void addUser(SysFile sysFile);

    SysFile detail(Long id);

    void update(SysFile sysFile);

    void remove(Long id);

    void batchRemove(Long[] ids);

    PageResult<SysFile> pageList(PageParam<SysFile> pageParam);

    /**
     * 文件上传
     * @param fileList
     */
    void upload(List<MultipartFile> fileList);
}
