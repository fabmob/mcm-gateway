package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.database.model.DataMapper;
import com.gateway.database.repository.DataMapperRepository;
import com.gateway.database.service.DataMapperDatabaseService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.gateway.database.util.constant.DataMessageDict.DATA_MAPPER_WITH_ID_IS_NOT_FOUND;
import static com.gateway.database.util.constant.DataMessageDict.FIRST_PLACEHOLDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
public class DataMapperDatabaseServiceImplTest {
    @TestConfiguration
    public static class DataMapperDatabaseServiceImplTestContextConfiguration {
        @Bean
        public DataMapperDatabaseService mspMetaDatabaseService() {
            return new DataMapperDatabaseServiceImpl();
        }
    }

    @MockBean
    private DataMapperRepository dataMapperRepository;

    @MockBean
    private ErrorMessages errorMessage;

    @Autowired
    private DataMapperDatabaseService mspMetaDatabaseService;

    @Test
    public void testAddDataMapper() {
        DataMapper datamapper = new DataMapper();
        Mockito.when(dataMapperRepository.save(datamapper)).thenReturn(datamapper);
        DataMapper dataMapperToAdd = mspMetaDatabaseService.addDataMapper(datamapper);
        assertEquals(datamapper, dataMapperToAdd);
    }

    @Test
    public void testGetAllDataMappers() {
        List<DataMapper> dataMapperList = new ArrayList<>();
        Mockito.when(dataMapperRepository.findAll()).thenReturn(dataMapperList);
        List<DataMapper> dataMapperListToGet = mspMetaDatabaseService.getAllDataMappers();
        assertEquals(dataMapperListToGet, dataMapperList);
    }

    @Test
    public void testUpdateDataMapper() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        DataMapper datamapper = new DataMapper();
        Mockito.when(dataMapperRepository.save(datamapper)).thenReturn(datamapper);
        DataMapper dataMapperToUpdate = mspMetaDatabaseService.updateDataMapper(id, datamapper);
        assertEquals(dataMapperToUpdate, datamapper);
    }

    @Test
    public void testDeleteDataMapper() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        mspMetaDatabaseService.deleteDataMapper(id);
        verify(dataMapperRepository, times(1)).deleteById(id);
    }

    @Test
    public void testFindDataMapperById() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        DataMapper dataMapper = new DataMapper();
        dataMapper.setDataMapperId(id);
        Mockito.when(dataMapperRepository.findById(id)).thenReturn(Optional.of(dataMapper));
        DataMapper dataMapperToFind = mspMetaDatabaseService.findDataMapperById(id);
        assertEquals(dataMapperToFind, dataMapper);
    }

    @Test
    public void testFindByActionMspActionId() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        DataMapper dataMapper = new DataMapper();
        dataMapper.setDataMapperId(id);
        List<DataMapper> dataMapperList = new ArrayList<>();
        dataMapperList.add(dataMapper);
        Mockito.when(dataMapperRepository.findByActionMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"))).thenReturn(dataMapperList);
        List<DataMapper> dataMapperListToGet = mspMetaDatabaseService.findByActionMspActionId(id);
        assertEquals(dataMapperListToGet, dataMapperList);
    }

    @Test
    @Ignore
    public void testFindByActionMspActionWhenIdDataMapperIsNull() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Mockito.when(MessageFormat.format(errorMessage.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(DATA_MAPPER_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString()))).thenReturn(null);
                Mockito.when(dataMapperRepository.findByActionMspActionId(id)).thenReturn(null);
                mspMetaDatabaseService.findByActionMspActionId(id);
            }
        });
    }


    @Test
    public void testConstructorDataMapperDatabaseServiceImpl() {
        DataMapperDatabaseServiceImpl dataMapperDatabaseServiceImpl =
                new DataMapperDatabaseServiceImpl(dataMapperRepository);
        assertEquals(dataMapperRepository, dataMapperDatabaseServiceImpl.getDataMapperRepository());

    }


}