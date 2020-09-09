package com.carrot.bulletchat.domain;




import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * @author carrot
 */
@TableName("carrot_user")
public class User implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @TableField(value = "name")
    private String name;
    /**
     * 当使用的字段与数据库不一致时需要指定一下，工具自动生成的代码可避免
     */
    @TableField(value = "telnum")
    private String telphoneNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelphoneNum() {
        return telphoneNum;
    }

    public void setTelphoneNum(String telphoneNum) {
        this.telphoneNum = telphoneNum;
    }
}
