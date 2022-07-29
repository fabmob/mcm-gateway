
      /* selector */
      	INSERT INTO msp.selector (selector_id,"key",value) VALUES
          	 ('ec0e9a76-f1fe-422c-803b-8332ad957950','stations',null);

	/* ---------- ACTION STATION_STATUS_SEARCH ---------- */
	/* msp_actions */
    	INSERT INTO msp.msp_actions (msp_action_id,"action",is_authentication,is_pagination,is_refresh_authentication,name,selector_id) VALUES
          ('a614f848-0f53-46fd-97fa-6b1f9f3db300', 'STATION_STATUS_SEARCH', false, 0, NULL, 'GBFS', 'ec0e9a76-f1fe-422c-803b-8332ad957950');


    /* data_mapper  */
         INSERT INTO msp.data_mapper (data_mapper_id,champ_externe, champ_interne,contained_value,default_value,format,is_array,timezone,action_id) VALUES
                      ('121866de-4e4f-4ab7-948b-372ce1acaea6', 'station_id', 'stationId', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db300'),
                      ('191866d1-4e4f-4ab7-948b-372ce1acaea5', 'vehicle_docks_available', 'assetsDocksAvailable.vehicleTypeIds', 'vehicle_type_ids', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db300'),
                      ('308866d3-4e4f-4ab7-948b-372ce1acaea4', 'vehicle_docks_available', 'assetsDocksAvailable.count', 'count', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db300'),
                      ('b0a5906b-0f58-4dc2-98a5-6e727d4af2fc', 'vehicle_types_available', 'numAssetsAvailableType.count', 'count', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db300'),
                      ('142866de-4e4f-4ab7-948b-372ce1acaea9', 'vehicle_types_available', 'numAssetsAvailableType.vehicleTypeId', 'vehicle_type_id', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db300'),
                      ('178866d3-4e4f-4ab7-948b-372ce1acaea3', 'is_installed', 'isInstalled',  NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db300'),
                      ('153866de-4e4f-4ab7-948b-372ce1acaea1', 'is_returning', 'isReturning', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db300'),
                      ('201866d1-4e4f-4ab7-948b-372ce1acaea5', 'is_renting', 'isRenting', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db300'),
                      ('408866d3-4e4f-4ab7-948b-372ce1acaea4', 'last_reported', 'lastReported',NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db300'),
                      ('00417544-8c45-4916-8113-901c1abec868', 'num_docks_available', 'numDocksAvailable',NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db300'),
                      ('00617544-8c45-4916-8113-901c1abec868', 'num_bikes_available', 'numAssetsAvailable',NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db300');


    /* msp calls */
	    INSERT INTO msp.msp_calls (msp_call_id,execution_order,is_mocked,"method",nb_calls,url,action_id,body_id) VALUES
            ('bfc126d5-d18c-4957-b1b0-d89bc58b1aae', 1, 0, 'GET', 1, 'http://demo9071502.mockable.io/stationStatusVLS', 'a614f848-0f53-46fd-97fa-6b1f9f3db300', NULL);

    /* params */
        INSERT INTO msp.params (params_id,key,key_mapper,precision,sensitive,timezone,value,call_id) VALUES
         	 ('4e8a4733-a504-4358-bcdc-f6fb5141ed26','station_id','stationId',NULL,0,NULL,'null','bfc126d5-d18c-4957-b1b0-d89bc58b1aae');


    /* msp-standard */
        INSERT INTO msp.msp_standard (msp_standard_id,version_datamapping,version_standard,is_active,standard_name ,msp_action_id,msp_id,adapter_id) VALUES
            ('a6142f2f-317b-4e4c-ba77-55ce2f0dbdda', 'V1.0',  'V1',  true,  'GBFS',  'a614f848-0f53-46fd-97fa-6b1f9f3db300', 'b714c97e-df56-4651-ac50-11525537964e','40142f60-9694-479f-a6cd-28b199b5e246');

