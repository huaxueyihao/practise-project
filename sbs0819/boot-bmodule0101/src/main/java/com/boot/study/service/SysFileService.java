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


    /**
     * 增加
     * @param sysFile
     */
    void addUser(SysFile sysFile);

    /**
     * 详情
     * @param id
     * @return
     */
    SysFile detail(Long id);

    /**
     * 修改
     * @param sysFile
     */
    void update(SysFile sysFile);

    /**
     * 删除
     * @param id
     */
    void remove(Long id);

    /**
     * 批量删除
     * @param ids
     */
    void batchRemove(Long[] ids);

    /**
     * 分页查询
     * @param pageParam
     * @return
     */
    PageResult<SysFile> pageList(PageParam<SysFile> pageParam);

    /**
     * 文件上传
     * @param fileList
     */
    void upload(List<MultipartFile> fileList);
}
