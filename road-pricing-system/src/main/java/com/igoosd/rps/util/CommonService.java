package com.igoosd.rps.util;

import com.baomidou.mybatisplus.plugins.Page;

import java.io.Serializable;
import java.util.List;

/**
 * 2018/4/8.
 */
public interface CommonService<T,ID extends Serializable> {

     void insert(T t);

     void update(T t);

     void deleteByKey(ID id);

     void delete(T t);

     T getEntityByKey(ID id);

     List<T> findList(T t);


     Page<T> findPage(T t, Page page);

     int getCount(T t);

     T getOne(T t);
}
