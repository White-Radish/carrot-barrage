package com.carrot.bulletchat.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.carrot.bulletchat.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author carrot
 * mybatis plus中的BaseMapper已经封装了一般使用的crud了
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
