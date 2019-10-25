package com.compucar.service;

import com.compucar.dao.BeanCreationDao;
import com.compucar.model.BeanCreation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BeanCreationServiceImpl implements BeanCreationService, BeanPostProcessor {

    @Autowired
    private BeanCreationDao beanCreationDao;

    public BeanCreationServiceImpl(BeanCreationDao beanCreationDao) {
        this.beanCreationDao = beanCreationDao;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        BeanCreation beanCreation = new BeanCreation();
        beanCreation.setName(beanName);
        beanCreation.setClassName(bean.getClass().getName());
        System.out.println(beanCreation);
        beanCreationDao.save(beanCreation);

        return bean;
    }

    @Override
    public List<BeanCreation> listBeans() {
        return beanCreationDao.findAll();
    }
}
