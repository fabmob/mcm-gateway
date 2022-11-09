package com.gateway.database.service.impl;

import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.exception.ConflictException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.database.model.Adapters;
import com.gateway.database.model.PartnerStandard;
import com.gateway.database.repository.AdaptersRepository;
import com.gateway.database.repository.PartnerStandardRepository;
import com.gateway.database.service.AdaptersDatabaseService;
import com.gateway.database.util.constant.DataMessageDict;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import static com.gateway.database.util.constant.DataMessageDict.*;

@Slf4j
@Service
@NoArgsConstructor
public class AdaptersDatabaseServiceImpl implements AdaptersDatabaseService {

    @Autowired
    private AdaptersRepository adaptersRepository;

    @Autowired
    private PartnerStandardRepository partnerStandardRepository;

    @Autowired
    private ErrorMessages errorMessage;

    private static final String CORRELATION_ID = String.valueOf(CommonUtils.setHeaders().getHeaders().get(GlobalConstants.CORRELATION_ID_HEADER));

    public AdaptersDatabaseServiceImpl(AdaptersRepository adaptersRepository) {
        this.adaptersRepository = adaptersRepository;

    }

    public AdaptersRepository getAdaptersRepository() {
        return adaptersRepository;
    }

    /**
     * Add a new Adapter
     *
     * @param adapter Adapter object
     * @return Adapter information for the Adapter added
     */
    @Override
    public Adapters addAdapter(Adapters adapter) {
        Adapters postedAdapter;
        try {
            postedAdapter = adaptersRepository.save(adapter);
        } catch (Exception e) {
            log.error(MessageFormat.format("CallId: {0}, {1}", CORRELATION_ID, e.getMessage()), e);
            throw new InternalException(CommonUtils.placeholderFormat(DataMessageDict.DUPLICATE_VALUE_OF_ADAPTER_NAME, FIRST_PLACEHOLDER, adapter.getAdapterName()));
        }
        return postedAdapter;
    }

    /**
     * Retrieve a list of Adapters transported into Adapters
     *
     * @return List of Adapters
     */
    @Override
    public List<Adapters> getAllAdapters() {
        return (List<Adapters>) adaptersRepository.findAll();
    }

    /**
     * Retrieve an Adapter information.
     *
     * @param id Identifier of the Adapter
     * @return Adapter information for the Adapter
     */
    @Override
    public Adapters findAdapterById(UUID id) {
        return adaptersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(errorMessage.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(ADAPTER_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString()))));
    }

    /**
     * Delete a Adapter
     *
     * @param id Identifier of the Adapter
     */
    @Override
    public void deleteAdapter(UUID id) {
        List<PartnerStandard> partnerStandardListUsingThisAdapter = partnerStandardRepository.findByAdapterId(id);
        if (partnerStandardListUsingThisAdapter.isEmpty()) {
            try {
                adaptersRepository.deleteById(id);
            } catch (Exception e) {
                throw new NotFoundException(MessageFormat.format(errorMessage.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(ADAPTER_WITH_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, id.toString())));
            }
        } else {
            StringBuilder listOfPartnerStandard = new StringBuilder();
            for (PartnerStandard partnerStandard : partnerStandardListUsingThisAdapter) {
                listOfPartnerStandard.append("[").append(partnerStandard.getPartnerStandardId()).append("]");
            }
            throw new ConflictException(MessageFormat.format(UNABLE_TO_DELETE_THIS_ADAPTER_ID_BECAUSE_IT_IS_USED_IN_ONE_OR_SEVERAL_STANDARDS, id, listOfPartnerStandard.toString()));
        }
    }
}
