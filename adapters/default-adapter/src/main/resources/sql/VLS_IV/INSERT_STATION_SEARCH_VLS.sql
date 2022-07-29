
	/* msp */
	INSERT INTO msp.msp_meta (msp_id,has_hold,has_no_parking_zone,has_operating_zone,has_parking,has_pref_parking_zone,has_speed_limit_zone,has_station,has_station_status,has_vehicule,is_enabled,
	logo_format,logo_url,name,operator,primary_color,secondary_color,type,url,url_webview,price_list_price_list_id) VALUES
    ('b714c97e-df56-4651-ac50-11525537964e', false, false, false, false, false, false, false, true, NULL, NULL,
     NULL, NULL, 'gbfs', 'mockable.io', '#0054BB', '#0054BB', 'VELO',
      'https://api.gouv.fr/documentation/gbfs', true, NULL);


     /* selector */
         INSERT INTO msp.selector (selector_id,"key",value) VALUES
                    	 ('7b6ccf70-1b82-48a1-991b-8050827ad9b6','stations',null);

        /* ---------- ACTION STATION_SEARCH ---------- */

        /* msp_actions */
            INSERT INTO msp.msp_actions (msp_action_id,action,is_authentication,is_pagination,is_refresh_authentication,name,selector_id) VALUES
                ('a614f848-0f53-46fd-97fa-6b1f9f3db301', 'STATION_SEARCH', false, 0, NULL, 'GBFS', '7b6ccf70-1b82-48a1-991b-8050827ad9b6');

        /* data_mapper  */
            INSERT INTO msp.data_mapper  (data_mapper_id,champ_externe, champ_interne,contained_value,default_value,format,is_array,timezone,action_id) VALUES
                   ('121866de-4e4f-1ab7-948b-372ce1acaea6', 'station_id', 'stationId', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db301'),
                   ('131866de-4e4f-1ab7-948b-372ce1acaea6', 'name', 'name', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db301'),
                   ('142866de-4e4f-1ab7-948b-372ce1acaea9', 'lat', 'coordinates.lat', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db301'),
                   ('153866de-4e4f-1ab7-948b-372ce1acaea1', 'lon', 'coordinates.lng', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db301'),
                   ('191866d1-4e4f-4ab7-128b-372ce1acaea5', 'vehicle_docks_available', 'assetsDocksAvailable.vehicleTypeIds', 'vehicle_type_ids', NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db301'),
                   ('164866de-4e4f-1ab7-948b-372ce1acaea7', 'is_valet_station', 'isValet', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db301'),
                   ('178866d3-4e4f-1ab7-948b-372ce1acaea3', 'is_virtual_station', 'isVirtualStation', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db301'),
                   ('189866d3-4e4f-1ab7-948b-372ce1acaea8', 'capacity', 'capacity', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db301'),
                   ('191866d1-4e4f-1ab7-948b-372ce1acaea5', 'station_area.type', 'stationArea.type', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db301'),
                   ('191866d1-4e4f-1ab7-228b-372ce1acaea5', 'station_area.coordinates', 'stationArea.coordinates', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db301');

        /* msp calls */
            INSERT INTO msp.msp_calls (msp_call_id,execution_order,is_mocked,"method",nb_calls,url,action_id,body_id) VALUES
               ('bfc126d5-d19c-4957-b1b0-d89bc58b1aae', 1, 0, 'GET', 1, 'http://demo9071502.mockable.io/stationVLS', 'a614f848-0f53-46fd-97fa-6b1f9f3db301', NULL);

        /* params */
            INSERT INTO msp.params (params_id,key,key_mapper,precision,sensitive,timezone,value,call_id) VALUES
             	 ('4e8a4733-a504-4358-bcdc-f6fb5141ed01','lon','lon',NULL,0,NULL,'null','bfc126d5-d19c-4957-b1b0-d89bc58b1aae'),
             	 ('4e8a4733-a504-4358-bcdc-f6fb5141ed02','lat','lat',NULL,0,NULL,'null','bfc126d5-d19c-4957-b1b0-d89bc58b1aae');


    /* adapters */
        INSERT INTO msp.adapters (adapter_id,adapter_name) VALUES
           ('40142f60-9694-479f-a6cd-28b199b5e246', 'default-adapter');

        /* msp-standard */
          INSERT INTO msp.msp_standard (msp_standard_id,version_datamapping,version_standard,is_active,standard_name ,msp_action_id,msp_id,adapter_id) VALUES
            ('a6142f2f-317b-4e4c-ba99-55ce2f0dbdda', 'V1.0',  'V1',  true,  'GBFS',  'a614f848-0f53-46fd-97fa-6b1f9f3db301', 'b714c97e-df56-4651-ac50-11525537964e','40142f60-9694-479f-a6cd-28b199b5e246');

