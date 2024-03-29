package com.stee.inventory.service.impl;

import com.google.common.collect.Lists;
import com.stee.inventory.dao.LampPoleDao;
import com.stee.inventory.dao.LampPoleModelDao;
import com.stee.inventory.entity.PoleQueryBean;
import com.stee.inventory.service.ILampPoleModelService;
import com.stee.sel.common.ResultData;
import com.stee.sel.constant.ResponseCode;
import com.stee.sel.inventory.LampPoleModelEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class LampPoleModelServiceImpl implements ILampPoleModelService{

    @Resource
    private LampPoleModelDao lampPoleModelDao;
    @Resource
    private LampPoleDao lampPoleDao;

    @Override
    public ResultData<LampPoleModelEntity> getAll() {
        ResultData<LampPoleModelEntity> resultData = new ResultData<>();
        try {
            List<LampPoleModelEntity> findAll = lampPoleModelDao.findAll();
            resultData.setData(findAll);
            resultData.setStatus(ResponseCode.SUCCESS.getCode());
        } catch (Exception e) {
            resultData.setStatus(ResponseCode.FAILED.getCode());
            e.printStackTrace();
        }
        return resultData;
    }

    @Override
    public String save(LampPoleModelEntity config) {
        if (null != config) {
            try {
                lampPoleModelDao.save(config);
                return ResponseCode.SUCCESS.getCode();
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseCode.FAILED.getCode();
            }
        } else {
            return ResponseCode.ERROR_PARAM.getCode();
        }
    }

    @Override
    public boolean isNameExits(String name) {
        LampPoleModelEntity poleModelConfig = new LampPoleModelEntity();
        poleModelConfig.setLampPoleModelId(name);
        ExampleMatcher NAME_MATCHER = ExampleMatcher.matching().withMatcher("name",
                ExampleMatcher.GenericPropertyMatchers.ignoreCase());
        Example<LampPoleModelEntity> example = Example.<LampPoleModelEntity>of(poleModelConfig, NAME_MATCHER);

        return lampPoleModelDao.exists(example);
    }

    @Transactional
    @Override
    public String delete(String id) {
        try {
            lampPoleDao.deleteByLampPoleModelId(id);
            lampPoleModelDao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseCode.FAILED.getCode();
        }
        return ResponseCode.SUCCESS.getCode();
    }

    @Override
    public ResultData<LampPoleModelEntity> findByQueryBean(PoleQueryBean query) {
        ResultData<LampPoleModelEntity> resultData = new ResultData<>();
        List<LampPoleModelEntity> list = Lists.newArrayList();
        if (null == query) {
            resultData.setStatus(ResponseCode.ERROR_PARAM.getCode());
            try {
                list = lampPoleModelDao.findAll();
                resultData.setStatus(ResponseCode.SUCCESS.getCode());
            } catch (Exception e) {
                resultData.setStatus(ResponseCode.FAILED.getCode());
            }
        } else {
            try {
                list = lampPoleModelDao.findAll(where(query.getId(), query.getDescription(), query.getHeightStart(), query.getHeightEnd()));
                resultData.setStatus(ResponseCode.SUCCESS.getCode());
            } catch (Exception e) {
                resultData.setStatus(ResponseCode.FAILED.getCode());
            }
        }

        resultData.setData(list);

        return resultData;
    }

    //    @Override
//    public Result<Page<LampPoleModel>> getAll(Pageable pageable) {
//        try {
//            Page<LampPoleModel> pageResult=lampPoleModelDao.findAll(pageable);
//            return new Result<Page<LampPoleModel>>().success(pageResult);
//        }catch(Exception e){
//            throw new ServiceException("get all lamp model configuration from database failed, error message:"+e.getMessage());
//        }
//
//    }
//
//    @Override
//    public Result save(LampPoleModel lampPoleModel) {
//            try {
//                lampPoleModelDao.save(lampPoleModel);
//                return Result.success();
//            } catch (Exception e) {
//                throw new ServiceException("save lampPoleModel configuration to database failed, error message:"+e.getMessage());
//            }
//    }
//
//    @Override
//    public Result<Boolean> isNameExits(String name) {
//        LampPoleModel poleModelConfig = new LampPoleModel();
//        poleModelConfig.setLampPoleModelId(name);
//        ExampleMatcher NAME_MATCHER = ExampleMatcher.matching().withMatcher("lampPoleModelId",
//                ExampleMatcher.GenericPropertyMatchers.ignoreCase());
//        Example<LampPoleModel> example = Example.<LampPoleModel>of(poleModelConfig, NAME_MATCHER);
//        try{
//            Boolean isExists=lampPoleModelDao.exists(example);
//            return new Result<Boolean>().success(isExists);
//        } catch (Exception e){
//            throw new ServiceException("validate lamp pole id failed, error message:"+e.getMessage());
//        }
//    }
//
//    @Override
//    public Result delete(String id) {
//        try{
//            lampPoleModelDao.delete(id);
//            return Result.success();
//        } catch (Exception e){
//            throw new ServiceException("delete lamp pole configuration failed, error message:"+e.getMessage());
//        }
//    }
//
//    @Override
//    public Result<Page<LampPoleModel>> findByQueryBean(PoleQueryBean query,Pageable pageable) {
////        List<LampPoleModel> list = Lists.newArrayList();
//        try {
//            Page<LampPoleModel> pageResult = lampPoleModelDao.findAll(where(query.getId(),query.getDescription(),query.getHeightStart(),query.getHeightEnd()),pageable);
//            return new Result<Page<LampPoleModel>>().success(pageResult);
//            } catch (Exception e) {
//            throw new ServiceException("get lamp pole configuration failed, error message:"+e.getMessage());
//            }
//        }
//
    private Specification<LampPoleModelEntity> where(final String id, final String desc, final Double heightStart, final Double heightEnd) {
        return new Specification<LampPoleModelEntity>() {

            @Override
            public Predicate toPredicate(Root<LampPoleModelEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (null != id && !id.equals("")) {
                    predicates.add(cb.like(root.<String>get("lampPoleModelId"),"%" +  id + "%"));
                }
                if (null != desc && !desc.equals("")) {
                    predicates.add(cb.like(root.<String>get("description"), "%" + desc + "%"));
                }
                if (null != heightStart && null != heightEnd) {
                    predicates.add(cb.between(root.<Double>get("height"), heightStart, heightEnd));
                }
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            }
        };
    }
}
