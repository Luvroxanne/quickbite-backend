package com.itmk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itmk.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: MK
 * @Description: 用户Mapper接口
 * @Date: 2024/3/20
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    @Update("UPDATE users SET password = #{newPassword}, updated_at = NOW() WHERE id = #{userId}")
    int updatePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);
} 