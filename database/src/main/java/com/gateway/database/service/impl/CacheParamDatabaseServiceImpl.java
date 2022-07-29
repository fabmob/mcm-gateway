package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.ConflictException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.database.model.CacheParam;
import com.gateway.database.model.CacheParamPK;
import com.gateway.database.repository.CacheParamRepository;
import com.gateway.database.service.CacheParamDatabaseService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.gateway.database.util.constant.DataMessageDict.*;

@Service
@Getter
@AllArgsConstructor
public class CacheParamDatabaseServiceImpl implements CacheParamDatabaseService {


    @Autowired
    private final CacheParamRepository cacheParamRepository;


    @Override
    public CacheParam addCacheParam(CacheParam cacheParam) {
        CacheParam mapper;
        if (cacheParam.getCacheParamPK() != null && cacheParam.getCacheParamPK().getMsp().getMspId() != null && cacheParam.getCacheParamPK().getActionType() != null) {
            if (cacheParamRepository.findById(cacheParam.getCacheParamPK()).isPresent()) {
                throw new ConflictException(CommonUtils.placeholderFormat(DATA_ALREADY_EXISTS_IN_DB_USE_PUT_INSTEAD, FIRST_PLACEHOLDER, cacheParam.getCacheParamPK().getMsp().getMspId().toString(), SECOND_PLACEHOLDER, cacheParam.getCacheParamPK().getActionType()));
            }
        } else {
            throw new InternalException(MSPID_MUST_AND_ACTION_TYPE_MUST_BE_NOT_NULL);

        }
        this.checkData(cacheParam);
        try {
            cacheParam.setCacheParamId(UUID.randomUUID());
            mapper = cacheParamRepository.save(cacheParam);
        } catch (Exception e) {
            throw new InternalException(CommonUtils.placeholderFormat(MSP_META_WITH_ID_IS_NOT_FOUND2, FIRST_PLACEHOLDER, cacheParam.getCacheParamPK().getMsp().getMspId().toString()));
        }
        return mapper;
    }

    /**
     * Retrieve a list of CacheParam transported into CacheParam
     *
     * @return List of CacheParam
     */
    @Override
    public List<CacheParam> getAllCacheParams() {
        return (List<CacheParam>) cacheParamRepository.findAll();
    }


    /**
     * Delete a CacheParam
     *
     * @param cacheParamPK Identifier of the CacheParam
     */
    public void deleteCacheParam(CacheParamPK cacheParamPK) {
        try {
            cacheParamRepository.deleteById(cacheParamPK);
        } catch (Exception e) {
            throw new NotFoundException(CommonUtils.placeholderFormat(NOT_FOUND_CACHE_PARAM_PK, FIRST_PLACEHOLDER, cacheParamPK.getMsp().getMspId().toString(), SECOND_PLACEHOLDER, cacheParamPK.getActionType()));
        }
    }

    /**
     * Delete CacheParam using cacheParamId and Delete using by PK
     *
     * @param cacheParamId cacheParam Id
     */
    public void deleteCacheParam(UUID cacheParamId) {
        CacheParam cacheParam = this.findCacheParamByID(cacheParamId);
        CacheParamPK cacheParamPK = new CacheParamPK();
        cacheParamPK.setActionType(cacheParam.getCacheParamPK().getActionType());
        cacheParamPK.setMsp(cacheParam.getCacheParamPK().getMsp());
        cacheParamRepository.deleteById(cacheParamPK);
    }

    /**
     * Retrieve a CacheParam information using PK.
     *
     * @param cacheParamPK Identifier of the CacheParam
     * @return CacheParam information for the CacheParam
     */

    public CacheParam findCacheParamByPK(CacheParamPK cacheParamPK) {
        return cacheParamRepository.findById(cacheParamPK)
                .orElseThrow(() -> new NotFoundException(CommonUtils.placeholderFormat(NOT_FOUND_CACHE_PARAM_PK, FIRST_PLACEHOLDER, cacheParamPK.getMsp().getMspId().toString(), SECOND_PLACEHOLDER, cacheParamPK.getActionType())));
    }

    /**
     * Retrieve a CacheParam information using ID
     *
     * @param cacheParamId Identifier of the CacheParam
     * @return cacheParam
     */
    @Override
    public CacheParam findCacheParamByID(UUID cacheParamId) {
        CacheParam cacheParam = cacheParamRepository.findByCacheParamId(cacheParamId);
        if (cacheParam == null) {
            throw new NotFoundException(CommonUtils.placeholderFormat(NOT_FOUND_CACHE_PARAM_ID, FIRST_PLACEHOLDER, cacheParamId.toString()));
        } else {
            return cacheParam;
        }
    }

    /**
     * Update a CacheParam information using ID
     *
     * @param cacheParamPK Identifier of the CacheParam
     * @return cacheParam
     */
    @Override
    public CacheParam updateCacheParam(CacheParamPK cacheParamPK, CacheParam cacheParam) {
        Optional<CacheParam> cacheParamBDD;
        this.checkData(cacheParam);
        cacheParamBDD = Optional.ofNullable(this.findCacheParamByPK(cacheParamPK));
        cacheParamBDD.ifPresent(param -> this.save(cacheParam, param));
        return cacheParam;
    }

    /**
     * Update a CacheParam information using PK
     *
     * @param cacheParamId Identifier of the CacheParam
     * @return cacheParam updated
     */
    @Override
    public CacheParam updateCacheParam(UUID cacheParamId, CacheParam cacheParam) {
        CacheParam cacheParamBDD;
        this.checkData(cacheParam);
        cacheParamBDD = this.findCacheParamByID(cacheParamId);
        this.save(cacheParam, cacheParamBDD);
        return cacheParam;
    }

    /**
     * filter by mspId or actionType or both
     *
     * @param mspId      msp Id
     * @param actionType type of action ("STATION_SEARCH")
     * @return List of Cache Param using filters
     */
    @Override
    public List<CacheParam> getAllCacheParamByCriteria(UUID mspId, String actionType) {
        List<CacheParam> cacheParamList = cacheParamRepository.findByCriteria(actionType, mspId);
        if (cacheParamList == null || cacheParamList.isEmpty()) {
            throw new NotFoundException(CommonUtils.placeholderFormat(NO_RESULTS, FIRST_PLACEHOLDER, String.valueOf(mspId), SECOND_PLACEHOLDER, String.valueOf(actionType)));
        }
        return cacheParamList;
    }

    /**
     * check if datas are ok
     *
     * @param cacheParam cacheParam
     */
    private void checkData(CacheParam cacheParam) {
        if (cacheParam.getSoftTTL() != null && cacheParam.getHardTTL() != null && cacheParam.getRefreshCacheDelay() != null) {
            if (cacheParam.getSoftTTL() > cacheParam.getHardTTL() || cacheParam.getRefreshCacheDelay() <= 5) {
                throw new InternalException(SOFT_TTL_VALUE_MUST_BE_LESS_THAN_HARD_TTL_AND_REFRESH_DELAY_MUST_BE_5);
            }
        } else {
            throw new InternalException(SOFT_TTL_VALUE_HARD_TTL_AND_REFRESH_DELAY_MUST_BE_NOT_NULL);
        }
    }

    /**
     * save the new cacheParam when updated
     *
     * @param cacheParam    cacheParam
     * @param cacheParamBDD cacheParam in the database
     */
    private void save(CacheParam cacheParam, CacheParam cacheParamBDD) {
        try {
            cacheParam.setCacheParamId(cacheParamBDD.getCacheParamId());
            cacheParam.setCacheParamPK(cacheParamBDD.getCacheParamPK());
            cacheParamRepository.save(cacheParam);
        } catch (Exception e) {
            throw new InternalException(SOMETHING_WENT_WRONG_DURING_SAVE_OPERATION);
        }
    }


}
