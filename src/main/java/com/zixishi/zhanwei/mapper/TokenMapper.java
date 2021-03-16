package com.zixishi.zhanwei.mapper;

import com.zixishi.zhanwei.model.Token;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TokenMapper {

    public Long save(Token token);

    /**
     * 根据账号id查询token表是否存在该token
     * @param id
     * @return
     */
    public Token findByAccount(Long id);


    public Integer countByAccount(Long id);

    Long findIdByAccount(long id);

    void update(Token token);

    Long findAccountByToken(String token);

    void deleteToken(long accountId);

}
