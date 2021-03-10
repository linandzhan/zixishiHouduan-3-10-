package com.zixishi.zhanwei.util;


import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class GetNullPropertyNamesUtil {
    /**
     * 获取实体类中，值为空的字段的名称
     * @param source 要检查的对象
     * @return 值为空的字段名称数组
     */
    public static String[] getNoValuePropertyNames (Object source) {
        Assert.notNull(source, "传递的参数对象不能为空");
        final BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();

        Set<String> noValuePropertySet = new HashSet<>();
        for (PropertyDescriptor pd:pds
             ) {
            Object srcValue= beanWrapper.getPropertyValue(pd.getName());
            if(srcValue==null){
                noValuePropertySet.add(pd.getName());
            }
        }
        String[] result=new String[noValuePropertySet.size()];
        return noValuePropertySet.toArray(result);
    }


}
