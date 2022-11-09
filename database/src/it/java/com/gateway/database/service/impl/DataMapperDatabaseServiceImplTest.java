package com.gateway.database.service.impl;

import com.gateway.commonapi.exception.InternalException;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
public class DataMapperDatabaseServiceImplTest {
    @TestConfiguration
    public static class DataMapperDatabaseServiceImplTestContextConfiguration {
        @Bean
        public DataMapperDatabaseService partnerMetaDatabaseService() {
            return new DataMapperDatabaseServiceImpl();
        }
    }

    @MockBean
    private DataMapperRepository dataMapperRepository;

    @MockBean
    private ErrorMessages errorMessage;

    @Autowired
    private DataMapperDatabaseService partnerMetaDatabaseService;

    @Test
    public void testAddDataMapper() {
        DataMapper datamapper = new DataMapper();
        Mockito.when(dataMapperRepository.save(datamapper)).thenReturn(datamapper);
        DataMapper dataMapperToAdd = partnerMetaDatabaseService.addDataMapper(datamapper);
        assertEquals(datamapper, dataMapperToAdd);
    }

    @Test
    public void testAddDataMapperCheckingFormat() {
        DataMapper datamapper = new DataMapper();
        datamapper.setFormat("NUMERIC_OPERATOR(*,100)");
        Mockito.when(dataMapperRepository.save(datamapper)).thenReturn(datamapper);
        DataMapper dataMapperToAdd = partnerMetaDatabaseService.addDataMapper(datamapper);
        assertEquals(datamapper, dataMapperToAdd);

        DataMapper datamapper2 = new DataMapper();
        datamapper2.setFormat("NUMERIC_OPERATOR(*,10\"0)");
        Mockito.when(dataMapperRepository.save(datamapper2)).thenReturn(datamapper2);
        assertThrows(InternalException.class, () -> {partnerMetaDatabaseService.addDataMapper(datamapper2);});
    }

    @Test
    public void testGetAllDataMappers() {
        List<DataMapper> dataMapperList = new ArrayList<>();
        Mockito.when(dataMapperRepository.findAll()).thenReturn(dataMapperList);
        List<DataMapper> dataMapperListToGet = partnerMetaDatabaseService.getAllDataMappers();
        assertEquals(dataMapperListToGet, dataMapperList);
    }

    @Test
    public void testUpdateDataMapper() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        DataMapper datamapper = new DataMapper();
        Mockito.when(dataMapperRepository.save(datamapper)).thenReturn(datamapper);
        DataMapper dataMapperToUpdate = partnerMetaDatabaseService.updateDataMapper(id, datamapper);
        assertEquals(dataMapperToUpdate, datamapper);
    }

    @Test
    public void testUpdateDataMapperCheckingFormat() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        DataMapper datamapper = new DataMapper();
        datamapper.setFormat("NUMERIC_OPERATOR(*,100)");
        Mockito.when(dataMapperRepository.save(datamapper)).thenReturn(datamapper);
        DataMapper dataMapperToUpdate = partnerMetaDatabaseService.updateDataMapper(id, datamapper);
        assertEquals(datamapper, dataMapperToUpdate);

        DataMapper datamapper2 = new DataMapper();
        datamapper2.setFormat("NUMERIC_OPERATOR(*,10\"0)");
        Mockito.when(dataMapperRepository.save(datamapper2)).thenReturn(datamapper2);
        assertThrows(InternalException.class, () -> {partnerMetaDatabaseService.updateDataMapper(id, datamapper2);});
    }

    @Test
    public void testDeleteDataMapper() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        partnerMetaDatabaseService.deleteDataMapper(id);
        verify(dataMapperRepository, times(1)).deleteById(id);
    }

    @Test
    public void testFindDataMapperById() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        DataMapper dataMapper = new DataMapper();
        dataMapper.setDataMapperId(id);
        Mockito.when(dataMapperRepository.findById(id)).thenReturn(Optional.of(dataMapper));
        DataMapper dataMapperToFind = partnerMetaDatabaseService.findDataMapperById(id);
        assertEquals(dataMapperToFind, dataMapper);
    }

    @Test
    public void testFindByActionPartnerActionId() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        DataMapper dataMapper = new DataMapper();
        dataMapper.setDataMapperId(id);
        List<DataMapper> dataMapperList = new ArrayList<>();
        dataMapperList.add(dataMapper);
        Mockito.when(dataMapperRepository.findByActionPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"))).thenReturn(dataMapperList);
        List<DataMapper> dataMapperListToGet = partnerMetaDatabaseService.findByActionPartnerActionId(id);
        assertEquals(dataMapperListToGet, dataMapperList);
    }

    @Test
    @Ignore
    public void testFindByActionPartnerActionWhenIdDataMapperIsNull() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Mockito.when(MessageFormat.format(errorMessage.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(DATA_MAPPER_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString()))).thenReturn(null);
                Mockito.when(dataMapperRepository.findByActionPartnerActionId(id)).thenReturn(null);
                partnerMetaDatabaseService.findByActionPartnerActionId(id);
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