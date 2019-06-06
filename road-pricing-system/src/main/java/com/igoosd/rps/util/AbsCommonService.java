package com.igoosd.rps.util;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import java.io.Serializable;
import java.util.List;

/**
 * 2018/4/8.
 */
public abstract  class AbsCommonService<T,ID extends Serializable> implements  CommonService<T,ID>  {


    @Override
    public void insert(T t) {
        getMapper().insert(t);
    }

    @Override
    public void update(T t) {
        getMapper().updateById(t);
    }

    @Override
    public void deleteByKey(ID id) {
        getMapper().deleteById(id);
    }

    @Override
    public void delete(T t) {
        getMapper().delete(new EntityWrapper<>(t));
    }

    @Override
    public T getEntityByKey(ID id) {
        return getMapper().selectById(id);
    }

    @Override
    public List<T> findList(T t) {
        return getMapper().selectList(new EntityWrapper<T>(t));
    }

    @Override
    public Page<T> findPage(T t, Page page) {
        List<T>  list = getMapper().selectPage(page,new EntityWrapper<T>(t));
        page.setRecords(list);
        return page;
    }

    @Override
    public int getCount(T t) {
        return getMapper().selectCount(new EntityWrapper<T>(t));
    }

    @Override
    public T getOne(T t) {
        return getMapper().selectOne(t);
    }

    /**
     * 获取Dao
     * @return
     */
    protected abstract BaseMapper<T> getMapper();

}
