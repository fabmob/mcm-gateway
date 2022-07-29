package com.gateway.database.repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gateway.database.model.DataMapper;



@Repository
public interface DataMapperRepository extends CrudRepository<DataMapper, UUID>{

	List<DataMapper> findByActionMspActionId(UUID mspActionId);
	Optional<List<DataMapper>> findByActionMspActionIdAndChampInterne(UUID mspActionId, String champInterne);
}
