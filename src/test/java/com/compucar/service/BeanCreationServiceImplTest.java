package com.compucar.service;

import com.compucar.dao.BeanCreationDao;
import com.compucar.model.BeanCreation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class BeanCreationServiceImplTest {

    private BeanCreationDao beanCreationDao;

    @Before
    public void initTest() {
        this.beanCreationDao = mock(BeanCreationDao.class);
    }

    @Test
    public void getAllBeanCreations_ok() {
        List<BeanCreation> mockList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BeanCreation bean = new BeanCreation();
            bean.setName("bean"+i);
            bean.setClassName("beanclass"+i);
            bean.setDate(LocalDateTime.now());
            mockList.add(bean);
        }
        when(beanCreationDao.findAll()).thenReturn(mockList);

        BeanCreationService service = new BeanCreationServiceImpl(beanCreationDao);
        List<BeanCreation> result = service.listBeans();

        Assert.assertEquals(10, result.size());
        Assert.assertEquals(mockList, result);

        verify(beanCreationDao, times(1)).findAll();
    }

    @After
    public void afterTest() {
        verifyNoMoreInteractions(beanCreationDao);
    }
}
