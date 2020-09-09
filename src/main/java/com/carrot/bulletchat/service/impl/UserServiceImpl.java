package com.carrot.bulletchat.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.carrot.bulletchat.domain.User;
import com.carrot.bulletchat.mapper.UserMapper;
import com.carrot.bulletchat.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author carrot
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public void resetPassWd() {

    }

    @Override
    public User getUser(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public List<User> getUserListByPage(Pagination page) {
        return baseMapper.selectPage(page,null);
    }
}
