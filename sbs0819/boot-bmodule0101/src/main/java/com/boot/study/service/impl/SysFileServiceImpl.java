package com.boot.study.service.impl;

import com.boot.study.common.PageParam;
import com.boot.study.common.PageResult;
import com.boot.study.mapper.SysFileMapper;
import com.boot.study.model.SysFile;
import com.boot.study.model.SysUser;
import com.boot.study.service.SysFileService;
import com.boot.study.util.FileUploadUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class SysFileServiceImpl implements SysFileService {


    @Value(value = "${fdfs.tracker.conf.path}")
    private String trackerConfPath;

    @Value(value = "${fdfs.server.ip}")
    private String serverIp;


    @Autowired
    private SysFileMapper sysFileMapper;


    @Override
    public void addUser(SysFile sysFile) {


    }

    @Override
    public SysFile detail(Long id) {
        return null;
    }

    @Override
    public void update(SysFile sysFile) {
        SysFile entity = sysFileMapper.selectByPrimaryKey(sysFile.getId());
        if (entity != null) {
            entity.setFileName(sysFile.getFileName());
            sysFileMapper.updateByPrimaryKey(entity);
        }
    }

    @Override
    public void remove(Long id) {
        sysFileMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void batchRemove(Long[] ids) {

    }

    @Override
    public PageResult<SysFile> pageList(PageParam<SysFile> pageParam) {
        int limit = pageParam.getLimit();
        int page = pageParam.getPage();
        SysFile condition = pageParam.getCondition();
        PageResult<SysFile> pageResult = new PageResult<>();
        Example example = new Example(SysFile.class);
        Example.Criteria criteria = example.createCriteria();

        Optional.ofNullable(condition).ifPresent(param -> {
            criteria.andLike("fileName", "%" + param.getFileName() + "%");
        });
        int count = sysFileMapper.selectCountByExample(example);
        if (count > 0) {
            List<SysFile> sysFiles = sysFileMapper.selectByExampleAndRowBounds(example, pageParam.getRowBounds());
            sysFiles.forEach(sysFile -> sysFile.setPath(serverIp + "/" + sysFile.getPath()));
            pageResult.setDataList(sysFiles);
            pageResult.setCount(count);
        }
        return pageResult;

    }

    @Override
    public void upload(List<MultipartFile> fileList) {
        fileList.forEach(multipartFile -> {
            try {
                String originalFilename = multipartFile.getOriginalFilename();
                long size = multipartFile.getSize();
                String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                // 上传到服务器
                String uploadPath = FileUploadUtil.updoad(trackerConfPath, multipartFile.getBytes(), extension);
                SysFile file = SysFile.builder().fileName(originalFilename).extension(extension).fileSize(size).path(uploadPath).build();
                sysFileMapper.insert(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
