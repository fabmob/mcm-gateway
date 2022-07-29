

	/* price list */
    INSERT INTO msp.price_list (price_list_id, comment, out_of_bound_fee, parking_forbidden_fee, fk_msp_meta) VALUES
       ('22808a15-1b7b-4605-9764-3c71a3f05ae4', '', 5, 3, null) ;

	/* msp */
	INSERT INTO msp.msp_meta (msp_id,has_hold,has_no_parking_zone,has_operating_zone,has_parking,has_pref_parking_zone,has_speed_limit_zone,has_station,has_station_status,has_vehicule,is_enabled,
	logo_format,logo_url,name,operator,primary_color,secondary_color,type,url,url_webview,price_list_price_list_id) VALUES
    ('b814c97e-df56-4651-ac50-11525537964a', false, false, false, false, false, false, false, true, NULL, NULL,
     'PNG', '@MAAS_API_DIRECTORY_ICON@/icon_jcDecaux@3x.png', 'JC_DECAUX', 'JC_DECAUX', '#0054BB', '#0054BB', 'TROTTINETTE',
      'https://api.jcdecaux.com', false, '22808a15-1b7b-4605-9764-3c71a3f05ae4');

	/* ---------- ACTION VEHICULE_SEARCH ---------- */

	/* selector */
	INSERT INTO msp.selector (selector_id,"key",value) VALUES
    	 ('ec0e9a76-f1fe-422c-803b-8332ad957941','assetType',null);

	/* msp_actions */
	INSERT INTO msp.msp_actions (msp_action_id,"action",is_authentication,is_pagination,is_refresh_authentication,name,selector_id) VALUES
     ('e222f848-0f53-46fd-97fa-6b1f9f3db255', 'AVAILABLE_ASSET_SEARCH', false, 0, NULL, 'MOCKABLE_IO_ASSET', 'ec0e9a76-f1fe-422c-803b-8332ad957941'),
      ('e222f848-0f53-46fd-97fa-6b1f9f3db299', 'STATION_SEARCH', false, 0, NULL, 'MOCKABLE_IO_STATION', NULL);

    /* data_mapper  */
    INSERT INTO msp.data_mapper  (data_mapper_id,champ_externe, champ_interne,contained_value,default_value,format,is_array,timezone,action_id) VALUES
           ('231866de-4e4f-4ab7-948b-372ce1acaef6', 'properties', 'sharedProperties.travelAbroad', 'travel', NULL, NULL, 1, 'Europe/Paris', 'e222f848-0f53-46fd-97fa-6b1f9f3db255'),
           ('231866de-4e4f-4ab7-948b-372ce1acaef9', 'properties', 'sharedProperties.airConditioning', 'air', NULL, NULL, 1, 'Europe/Paris', 'e222f848-0f53-46fd-97fa-6b1f9f3db255'),
           ('231866de-4e4f-4ab7-948b-372ce1acaef1', 'ID_Veihcule', 'assetTypeId', NULL, NULL, NULL, NULL, NULL, 'e222f848-0f53-46fd-97fa-6b1f9f3db255'),
           ('231866de-4e4f-4ab7-948b-372ce1acaef7', 'ID_Station', 'stationId', NULL, NULL, NULL, NULL, NULL, 'e222f848-0f53-46fd-97fa-6b1f9f3db255'),
           ('231866de-4e4f-4ab7-948b-372ce4acaef7', 'Num_Available', 'nrAvailable', NULL, NULL, NULL, NULL, NULL, 'e222f848-0f53-46fd-97fa-6b1f9f3db255'),
           ('331866d3-4e4f-4ab7-948b-372ce1acaef1', 'ID_STATION', 'stationId', NULL, NULL, NULL, NULL, NULL, 'e222f848-0f53-46fd-97fa-6b1f9f3db299'),
           ('331866d3-4e4f-4ab7-948b-372ce1acaef2', 'NAME', 'stationName', NULL, NULL, NULL, NULL, NULL, 'e222f848-0f53-46fd-97fa-6b1f9f3db299'),
           ('331866d3-4e4f-4ab7-948b-372ce1acaef3', 'Cross_Street', 'crossStreet', NULL, NULL, NULL, NULL, NULL, 'e222f848-0f53-46fd-97fa-6b1f9f3db299'),
           ('331866d3-4e4f-4ab7-948b-372ce1acaef8', 'ID_Region', 'regionId', NULL, NULL, NULL, NULL, NULL, 'e222f848-0f53-46fd-97fa-6b1f9f3db299'),
           ('331866d3-4e4f-4ab7-948b-372ce1acaef4', 'position.latitude', 'coordinates.lat', NULL, NULL, NULL, NULL, NULL, 'e222f848-0f53-46fd-97fa-6b1f9f3db299'),
           ('131866d1-4e4f-4ab7-948b-372ce1acaef5', 'posit', 'coordinates.lng', NULL, NULL, NULL, NULL, NULL, 'e222f848-0f53-46fd-97fa-6b1f9f3db299'),
           ('551866d1-4e4f-4ab7-948b-372ce1acaef5', 'position.longitude', 'coordinates.lng', NULL, NULL, NULL, NULL, NULL, 'e222f848-0f53-46fd-97fa-6b1f9f3db299'),
           ('221866d1-4e4f-4ab7-948b-372ce1acaef5', NULL, 'name', NULL, 'Island Central', NULL, NULL, NULL, 'e222f848-0f53-46fd-97fa-6b1f9f3db299'),
           ('441866d1-4e4f-4ab7-948b-372ce1acaef5', NULL, 'physicalAddress.city', NULL, 'paris', NULL, NULL, NULL, 'e222f848-0f53-46fd-97fa-6b1f9f3db299');



    /* body */
    INSERT INTO msp.body (body_id,is_static, template) VALUES
         	 ('9404fa40-1549-4fb9-bb89-d89bc58b1eed',1 ,NULL),
			 ('9404fa40-1549-4fb9-bb89-d89bc58b1eea',1 ,NULL);

	/* msp calls */
	INSERT INTO msp.msp_calls (msp_call_id,execution_order,is_mocked,"method",nb_calls,url,action_id,body_id) VALUES
         ('bfc126d5-d17c-4957-b1b0-e037e2dad9da', 1, 0, 'GET', 1, 'http://demo9071502.mockable.io/availableAssets/ArrayToBoolean', 'e222f848-0f53-46fd-97fa-6b1f9f3db255', '9404fa40-1549-4fb9-bb89-d89bc58b1eed'),
         ('bfc126d5-d17c-4957-b1b0-d89bc58b1eea', 1, 0, 'GET', 1, 'http://demo9071502.mockable.io/stationTest', 'e222f848-0f53-46fd-97fa-6b1f9f3db299', '9404fa40-1549-4fb9-bb89-d89bc58b1eea');

	/* headers */
	INSERT INTO msp.headers (headers_id,"key",process_function,security_flag,"sensitive",value,value_prefix,value_template,call_id) VALUES
     ('6de8ce89-90d2-475b-8e81-c087de351d88', 'Content-Type', NULL, NULL, NULL, 'application/json', NULL, NULL, 'bfc126d5-d17c-4957-b1b0-e037e2dad9da'),
     ('6de8ce89-90d2-475b-8e81-c087de351d11', 'Content-Type', NULL, NULL, NULL, 'application/json', NULL, NULL, 'bfc126d5-d17c-4957-b1b0-d89bc58b1eea');


    /* headers_values_template */
    INSERT INTO msp.headers_values_template (headers_values_template_id,"key",value,headers_id) VALUES
     ('a876195c-61f3-4bf3-94b8-3625c71c35b3', 'user', '{MAAS_CITYSCOOT_CLIENT_ID:kFEkfWQt8UATcsccM7Ey}', '6de8ce89-90d2-475b-8e81-c087de351d88'),
     ('a876195c-61f3-4bf3-94b8-3625c71c35b1', 'user', '{MAAS_CITYSCOOT_CLIENT_ID:kFEkfWQt8UATcsccM7Ey}', '6de8ce89-90d2-475b-8e81-c087de351d11');


     INSERT INTO msp.params (params_id,key,key_mapper,precision,sensitive,timezone,value,call_id) VALUES
        ('4e8a4733-a504-4358-bcdc-f6fb5141ed24','station','stationId',NULL,0,NULL,'null','bfc126d5-d17c-4957-b1b0-e037e2dad9da'),
        ('4e8a4733-a504-4358-bcdc-f6fb5141ed21','longitude','lon',NULL,0,NULL,NULL,'bfc126d5-d17c-4957-b1b0-d89bc58b1eea'),
        ('4e8a4733-a504-4358-bcdc-f6fb5141ed23','latitude','lat',NULL,0,NULL,NULL,'bfc126d5-d17c-4957-b1b0-d89bc58b1eea'),
        ('4e8a4733-a504-4358-bcdc-f6fb5141ed25','radius','rad',NULL,0,NULL,NULL,'bfc126d5-d17c-4957-b1b0-d89bc58b1eea');


 /* adapters */
        INSERT INTO msp.adapters (adapter_id,adapter_name) VALUES
           ('40142f60-9694-479f-a6cd-28b199b5e240', 'default-adapter');

    /* msp-standard */
    INSERT INTO msp.msp_standard (msp_standard_id,version_datamapping,version_standard,is_active,standard_name ,msp_action_id,msp_id,adapter_id) VALUES
       ('f7082f2f-317b-4e4c-ba77-54ce2f0dbddd', 'V1.0',  'V1',  true,  'FNMS',  'e222f848-0f53-46fd-97fa-6b1f9f3db255', 'b814c97e-df56-4651-ac50-11525537964a','40142f60-9694-479f-a6cd-28b199b5e240'),
       ('f7082f2f-317b-4e4c-ba77-54ce2f0dbdda', 'V1.0',  'V1',  true,  'FNMS',  'e222f848-0f53-46fd-97fa-6b1f9f3db299', 'b814c97e-df56-4651-ac50-11525537964a','40142f60-9694-479f-a6cd-28b199b5e240');

