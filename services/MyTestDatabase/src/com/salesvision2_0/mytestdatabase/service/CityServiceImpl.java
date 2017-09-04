/*Copyright (c) 2015-2016 wavemaker-com All Rights Reserved.This software is the confidential and proprietary information of wavemaker-com You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the source code license agreement you entered into with wavemaker-com*/
package com.salesvision2_0.mytestdatabase.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.wavemaker.runtime.data.dao.WMGenericDao;
import com.wavemaker.runtime.data.exception.EntityNotFoundException;
import com.wavemaker.runtime.data.export.ExportType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import com.wavemaker.runtime.file.model.Downloadable;

import com.salesvision2_0.mytestdatabase.City;
import com.salesvision2_0.mytestdatabase.Personnel;


/**
 * ServiceImpl object for domain model class City.
 *
 * @see City
 */
@Service("MyTestDatabase.CityService")
@Validated
public class CityServiceImpl implements CityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityServiceImpl.class);

    @Lazy
    @Autowired
	@Qualifier("MyTestDatabase.PersonnelService")
	private PersonnelService personnelService;

    @Autowired
    @Qualifier("MyTestDatabase.CityDao")
    private WMGenericDao<City, Integer> wmGenericDao;

    public void setWMGenericDao(WMGenericDao<City, Integer> wmGenericDao) {
        this.wmGenericDao = wmGenericDao;
    }

    @Transactional(value = "MyTestDatabaseTransactionManager")
    @Override
	public City create(City cityInstance) {
        LOGGER.debug("Creating a new City with information: {}", cityInstance);
        City cityInstanceCreated = this.wmGenericDao.create(cityInstance);
        if(cityInstanceCreated.getPersonnelsForCityCode() != null) {
            for(Personnel personnelsForCityCode : cityInstanceCreated.getPersonnelsForCityCode()) {
                personnelsForCityCode.setCityByCityCode(cityInstanceCreated);
                LOGGER.debug("Creating a new child Personnel with information: {}", personnelsForCityCode);
                personnelService.create(personnelsForCityCode);
            }
        }

        if(cityInstanceCreated.getPersonnelsForCityCodeRelation() != null) {
            for(Personnel personnelsForCityCodeRelation : cityInstanceCreated.getPersonnelsForCityCodeRelation()) {
                personnelsForCityCodeRelation.setCityByCityCodeRelation(cityInstanceCreated);
                LOGGER.debug("Creating a new child Personnel with information: {}", personnelsForCityCodeRelation);
                personnelService.create(personnelsForCityCodeRelation);
            }
        }
        return cityInstanceCreated;
    }

	@Transactional(readOnly = true, value = "MyTestDatabaseTransactionManager")
	@Override
	public City getById(Integer cityId) throws EntityNotFoundException {
        LOGGER.debug("Finding City by id: {}", cityId);
        City cityInstance = this.wmGenericDao.findById(cityId);
        if (cityInstance == null){
            LOGGER.debug("No City found with id: {}", cityId);
            throw new EntityNotFoundException(String.valueOf(cityId));
        }
        return cityInstance;
    }

    @Transactional(readOnly = true, value = "MyTestDatabaseTransactionManager")
	@Override
	public City findById(Integer cityId) {
        LOGGER.debug("Finding City by id: {}", cityId);
        return this.wmGenericDao.findById(cityId);
    }


	@Transactional(rollbackFor = EntityNotFoundException.class, value = "MyTestDatabaseTransactionManager")
	@Override
	public City update(City cityInstance) throws EntityNotFoundException {
        LOGGER.debug("Updating City with information: {}", cityInstance);
        this.wmGenericDao.update(cityInstance);

        Integer cityId = cityInstance.getId();

        return this.wmGenericDao.findById(cityId);
    }

    @Transactional(value = "MyTestDatabaseTransactionManager")
	@Override
	public City delete(Integer cityId) throws EntityNotFoundException {
        LOGGER.debug("Deleting City with id: {}", cityId);
        City deleted = this.wmGenericDao.findById(cityId);
        if (deleted == null) {
            LOGGER.debug("No City found with id: {}", cityId);
            throw new EntityNotFoundException(String.valueOf(cityId));
        }
        this.wmGenericDao.delete(deleted);
        return deleted;
    }

	@Transactional(readOnly = true, value = "MyTestDatabaseTransactionManager")
	@Override
	public Page<City> findAll(QueryFilter[] queryFilters, Pageable pageable) {
        LOGGER.debug("Finding all Cities");
        return this.wmGenericDao.search(queryFilters, pageable);
    }

    @Transactional(readOnly = true, value = "MyTestDatabaseTransactionManager")
    @Override
    public Page<City> findAll(String query, Pageable pageable) {
        LOGGER.debug("Finding all Cities");
        return this.wmGenericDao.searchByQuery(query, pageable);
    }

    @Transactional(readOnly = true, value = "MyTestDatabaseTransactionManager")
    @Override
    public Downloadable export(ExportType exportType, String query, Pageable pageable) {
        LOGGER.debug("exporting data in the service MyTestDatabase for table City to {} format", exportType);
        return this.wmGenericDao.export(exportType, query, pageable);
    }

	@Transactional(readOnly = true, value = "MyTestDatabaseTransactionManager")
	@Override
	public long count(String query) {
        return this.wmGenericDao.count(query);
    }

    @Transactional(readOnly = true, value = "MyTestDatabaseTransactionManager")
	@Override
    public Page<Map<String, Object>> getAggregatedValues(AggregationInfo aggregationInfo, Pageable pageable) {
        return this.wmGenericDao.getAggregatedValues(aggregationInfo, pageable);
    }

    @Transactional(readOnly = true, value = "MyTestDatabaseTransactionManager")
    @Override
    public Page<Personnel> findAssociatedPersonnelsForCityCode(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated personnelsForCityCode");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("cityByCityCode.id = '" + id + "'");

        return personnelService.findAll(queryBuilder.toString(), pageable);
    }

    @Transactional(readOnly = true, value = "MyTestDatabaseTransactionManager")
    @Override
    public Page<Personnel> findAssociatedPersonnelsForCityCodeRelation(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated personnelsForCityCodeRelation");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("cityByCityCodeRelation.id = '" + id + "'");

        return personnelService.findAll(queryBuilder.toString(), pageable);
    }

    /**
	 * This setter method should only be used by unit tests
	 *
	 * @param service PersonnelService instance
	 */
	protected void setPersonnelService(PersonnelService service) {
        this.personnelService = service;
    }

}

