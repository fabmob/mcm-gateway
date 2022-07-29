package com.gateway.dataapi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.dto.data.MspMetaDTO;
import com.gateway.commonapi.dto.exceptions.NotFound;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.dataapi.model.mapper.MspMetaMapper;
import com.gateway.dataapi.service.MspMetaService;
import com.gateway.database.model.MspMeta;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class MspMetaServiceImpl implements MspMetaService {

    @Autowired
    private com.gateway.database.service.MspMetaDatabaseService mspMetaService;

    private final MspMetaMapper mapper = Mappers.getMapper(MspMetaMapper.class);

    @Override
    public List<MspMetaDTO> getMspMetas() {
        List<MspMeta> entityMspMetas = mspMetaService.findAllMspMeta();
        return mapper.mapEntityToDto(entityMspMetas);
    }

    @Override
    public MspMetaDTO getMspMeta(UUID id) throws NotFoundException {
        MspMeta entityMspMeta = mspMetaService.findMspMetaById(id);
        return mapper.mapEntityToDto(entityMspMeta);
    }

    /**
     * we get json as input in the format of a dto. we need to transform it into an entity to apply our database services.
     * once the treatment is finished, all the entities must be transformed back into dto for exposure
     **/
    @Override
    public MspMetaDTO postMspMeta(MspMetaDTO mspMetaDTO) {
        MspMeta mspMeta = mapper.mapDtoToEntity(mspMetaDTO);
        MspMeta entityMspMeta = mspMetaService.createMspMeta(mspMeta);
        UUID createdMspId = entityMspMeta.getMspId();
        return getMspMeta(createdMspId);
    }

    @Override
    public void putMspMeta(UUID id, MspMetaDTO mspMetaDTO) {
        MspMeta mspMeta = mapper.mapDtoToEntity(mspMetaDTO);
        // trigger the update operation
        mspMetaService.updateMspMeta(id, mspMeta);
    }

    @SneakyThrows
    @Override
    public void deleteMspMeta(UUID id) {
        try {
            this.mspMetaService.removeMspMeta(id);
        } catch (Exception e) {
            NotFound errorObject = new NotFound("myLabel", String.format("the id %s does not exist", id));
            ObjectMapper objectMapper = new ObjectMapper();
            String message = objectMapper.writeValueAsString(errorObject);
            throw new NotFoundException(message);
        }
    }

    @Override
    public MspMetaDTO patchMspMeta(Map<String, Object> updates, UUID id) {
        MspMeta mspMetaPatched = mspMetaService.updateMspMeta(updates, id);
        return mapper.mapEntityToDto(mspMetaPatched);
    }
}
