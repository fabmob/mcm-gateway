package com.gateway.dataapi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.data.PartnerMetaDTO;
import com.gateway.commonapi.dto.exceptions.NotFound;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.restConfig.RestConfig;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.ExceptionUtils;
import com.gateway.commonapi.utils.enums.PartnerTypeEnum;
import com.gateway.commonapi.utils.enums.TypeEnum;
import com.gateway.dataapi.model.mapper.PartnerMetaMapper;
import com.gateway.dataapi.service.PartnerMetaService;
import com.gateway.database.model.PartnerMeta;
import com.gateway.database.service.PartnerMetaDatabaseService;
import com.gateway.database.util.constant.DataMessageDict;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.gateway.commonapi.constants.CacheManagerDict.CACHE_MANAGER_REFRESH_PARTNERS_PATH;
import static com.gateway.commonapi.constants.GlobalConstants.BASE_ERROR_MESSAGE;
import static com.gateway.database.util.constant.DataMessageDict.*;

@Slf4j
@Service
public class PartnerMetaServiceImpl implements PartnerMetaService {

    @Autowired
    private PartnerMetaDatabaseService partnerMetaService;

    private final PartnerMetaMapper mapper = Mappers.getMapper(PartnerMetaMapper.class);

    RestConfig restConfig = new RestConfig();
    RestTemplate restTemplate = restConfig.restTemplate();
    
    @Value("${gateway.service.cachemanager.baseUrl}")
    private String cacheManagerUri;
    @Autowired
    private ErrorMessages errorMessages;

    @Override
    public List<PartnerMetaDTO> getPartnerMetasByExample(PartnerMeta partnerMetaExample) {
        List<PartnerMeta> entityPartnerMetas = partnerMetaService.findAllByExample(partnerMetaExample);
        return mapper.mapEntityToDto(entityPartnerMetas);
    }


    @Override
    public PartnerMetaDTO getPartnerMeta(UUID id) throws NotFoundException {
        PartnerMeta entityPartnerMeta = partnerMetaService.findPartnerMetaById(id);
        return mapper.mapEntityToDto(entityPartnerMeta);
    }

    /**
     * we get json as input in the format of a dto. we need to transform it into an entity to apply our database services.
     * once the treatment is finished, all the entities must be transformed back into dto for exposure
     **/
    @Override
    public PartnerMetaDTO postPartnerMeta(PartnerMetaDTO partnerMetaDTO) {
        if (partnerMetaDTO.getPartnerType() == null) {
            throw new BadRequestException(CommonUtils.placeholderFormat(DataMessageDict.PARTNER_TYPE_MUST_BE_NOT_NULL));
        }
        if (PartnerTypeEnum.MAAS == partnerMetaDTO.getPartnerType() && (!TypeEnum.isMaasType(partnerMetaDTO.getType().toString()))) {
            throw new BadRequestException(CommonUtils.placeholderFormat(PARTNER_TYPE_IS_NOT_COMPATIBLE_WITH_THIS_TYPE, FIRST_PLACEHOLDER, partnerMetaDTO.getPartnerType().toString(), SECOND_PLACEHOLDER, partnerMetaDTO.getType().toString()));
        }
        if (PartnerTypeEnum.MSP == partnerMetaDTO.getPartnerType() && (TypeEnum.isMaasType(partnerMetaDTO.getType().toString()))) {
            throw new BadRequestException(CommonUtils.placeholderFormat(PARTNER_TYPE_IS_NOT_COMPATIBLE_WITH_THIS_TYPE, FIRST_PLACEHOLDER, partnerMetaDTO.getPartnerType().toString(), SECOND_PLACEHOLDER, partnerMetaDTO.getType().toString()));
        }
        PartnerMeta partnerMeta = mapper.mapDtoToEntity(partnerMetaDTO);
        PartnerMeta entityPartnerMeta = partnerMetaService.createPartnerMeta(partnerMeta);
        UUID createdPartnerId = entityPartnerMeta.getPartnerId();
        return getPartnerMeta(createdPartnerId);
    }

    @Override
    public void putPartnerMeta(UUID id, PartnerMetaDTO partnerMetaDTO) {
        PartnerMeta partnerMeta = mapper.mapDtoToEntity(partnerMetaDTO);
        // trigger the update operation
        partnerMetaService.updatePartnerMeta(id, partnerMeta);
    }

    @SneakyThrows
    @Override
    public void deletePartnerMeta(UUID id) {
        try {
            this.partnerMetaService.removePartnerMeta(id);
        } catch (Exception e) {
            NotFound errorObject = new NotFound("myLabel", String.format("the id %s does not exist", id));
            ObjectMapper objectMapper = new ObjectMapper();
            String message = objectMapper.writeValueAsString(errorObject);
            throw new NotFoundException(message);
        }
    }

    @Override
    public PartnerMetaDTO patchPartnerMeta(Map<String, Object> updates, UUID id) {
        PartnerMeta partnerMetaPatched = partnerMetaService.updatePartnerMeta(updates, id);
        return mapper.mapEntityToDto(partnerMetaPatched);
    }

    @Override
    public void refreshCachePartnerMetas() {
        log.info("Refreshing partnerMetas in cache");
        String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().getFirst(GlobalConstants.CORRELATION_ID_HEADER));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(GlobalConstants.CORRELATION_ID_HEADER, correlationId);

        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        String urlRefreshCachePartners = cacheManagerUri + CACHE_MANAGER_REFRESH_PARTNERS_PATH;

        try {
            restTemplate.exchange(urlRefreshCachePartners, HttpMethod.POST, entity, Object.class);

        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlRefreshCachePartners));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlRefreshCachePartners));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlRefreshCachePartners));
        }

    }
}
