package com.carrot.bulletchat.service;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.baomidou.mybatisplus.service.IService;
import com.carrot.bulletchat.domain.User;

import java.util.List;

/**
 * @author carrot
 */
public interface IUserService extends IService<User> {
    /**
     * 用户重置密码
     */
    void resetPassWd();

    /**
     * 获取某个用户
     * @param id
     * @return
     */
    User getUser(Long id);

    /**
     * 分页查询展示用户
     * @param page
     * @return
     */
    List<User> getUserListByPage(Pagination page);
}
