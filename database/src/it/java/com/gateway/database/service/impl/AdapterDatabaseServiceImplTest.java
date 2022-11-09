package com.gateway.database.service.impl;

import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.database.model.Adapters;
import com.gateway.database.repository.AdaptersRepository;
import com.gateway.database.repository.PartnerStandardRepository;
import com.gateway.database.service.AdaptersDatabaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
public class AdapterDatabaseServiceImplTest {
    @TestConfiguration
    public static class DefaultAdapterDatabaseServiceImplTestContextConfiguration {
        @Bean
        public AdaptersDatabaseService adapterDatabaseService() {
            return new AdaptersDatabaseServiceImpl();
        }
    }

    @MockBean
    private AdaptersRepository adaptersRepository;

    @MockBean
    private PartnerStandardRepository partnerStandardRepository;

    @MockBean
    private ErrorMessages errorMessage;

    @Autowired
    private AdaptersDatabaseService adaptersDatabaseService;

    @Test
    public void testAddAdapter() {
        Adapters adapters = new Adapters(UUID.randomUUID(), "defaultAdapter");
        when(adaptersRepository.save(adapters)).thenReturn(adapters);
        Adapters adapterToAdd = adaptersDatabaseService.addAdapter(adapters);
        assertEquals(adapters, adapterToAdd);
    }

    @Test
    public void testGetAllAdapters() {
        List<Adapters> AdaptersList = new ArrayList<>();
        when(adaptersRepository.findAll()).thenReturn(AdaptersList);
        List<Adapters> AdaptersListToGet = adaptersDatabaseService.getAllAdapters();
        assertEquals(AdaptersListToGet, AdaptersList);
    }

    
    @Test
    public void testDeleteAdapters() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        adaptersDatabaseService.deleteAdapter(id);
        verify(adaptersRepository, times(1)).deleteById(id);
    }


    @Test
    public void testFindAdaptersById() {
        UUID id = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        Adapters Adapters = new Adapters();
        Adapters.setAdapterId(id);
        when(adaptersRepository.findById(id)).thenReturn(Optional.of(Adapters));
        Adapters AdaptersToFind = adaptersDatabaseService.findAdapterById(id);
        assertEquals(AdaptersToFind, Adapters);
    }
    

    @Test
    public void testConstructorAdaptersDatabaseServiceImpl() {
        AdaptersDatabaseServiceImpl AdaptersDatabaseServiceImpl = new AdaptersDatabaseServiceImpl(adaptersRepository);
        assertEquals(adaptersRepository, AdaptersDatabaseServiceImpl.getAdaptersRepository());

    }

}