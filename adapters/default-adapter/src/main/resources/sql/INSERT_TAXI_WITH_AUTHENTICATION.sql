
	/* msp */
	INSERT INTO msp.msp_meta (msp_id,has_hold,has_no_parking_zone,has_operating_zone,has_parking,has_pref_parking_zone,has_speed_limit_zone,has_station,has_station_status,has_vehicule,is_enabled,
	logo_format,logo_url,name,operator,primary_color,secondary_color,type,url,url_webview,price_list_price_list_id) VALUES
    ('a614c97e-df56-4651-ac50-11525537964a', false, false, false, false, false, false, false, true, NULL, NULL,
     NULL, NULL, 'le.taxi', 'mockable.io', '#0054BB', '#0054BB', 'TAXI',
      'https://api.gouv.fr/documentation/le-taxi', true, NULL);

	/* ---------- ACTION AROUND_ME_SEARCH ---------- */


	/* msp_actions */
	INSERT INTO msp.msp_actions (msp_action_id,"action",is_authentication,is_pagination,is_refresh_authentication,name,selector_id) VALUES
      ('a614f848-0f53-46fd-97fa-6b1f9f3db299', 'AROUND_ME_SEARCH', false, 0, NULL, 'le.taxi', NULL),
      ('e333f848-0f53-46fd-97fa-6b1f9f3db211',NULL,true,0,0,'MockedMSP',NULL);

    /* data_mapper  */
        INSERT INTO msp.data_mapper  (data_mapper_id,champ_externe, champ_interne,contained_value,default_value,format,is_array,timezone,action_id) VALUES
               ('211866de-4e4f-4ab7-948b-372ce1acaef6', 'data', 'assets.overriddenProperties.location.coordinates.lng', 'position.lon', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db299'),
               ('221866de-4e4f-4ab7-948b-372ce1acaef6', 'data', 'assets.overriddenProperties.location.coordinates.lat', 'position.lat', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db299'),
               ('222866de-4e4f-4ab7-948b-372ce1acaef9', 'data', 'assets.overriddenProperties.operator', 'operator', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db299'),
               ('333866de-4e4f-4ab7-948b-372ce1acaef1', 'data', 'assets.overriddenProperties.location.crowflyDistance', 'crowfly_distance', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db299'),
               ('444866de-4e4f-4ab7-948b-372ce1acaef7', 'data', 'assets.assetId', 'id', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db299'),
               ('888866d3-4e4f-4ab7-948b-372ce1acaef3', 'data', 'assets.overriddenProperties.brand',  'vehicle.constructor', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db299'),
               ('999866d3-4e4f-4ab7-948b-372ce1acaef8', 'data', 'assets.overriddenProperties.fuel', 'vehicle.engine', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db299'),
               ('521866d1-4e4f-4ab7-948b-372ce1acaef5', 'data', 'assets.assetType', NULL, 'TAXI', NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db299'),
               ('888866d3-4e4f-4ab7-948b-372ce1acaef4', 'data', 'assets.overriddenProperties.model', 'vehicle.model', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db299'),
               ('999866d3-4e4f-4ab7-948b-372ce1acaef9', 'data', 'assets.overriddenProperties.persons', 'vehicle.nb_seats', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db299'),
               ('998866d3-4e4f-4ab7-948b-372ce1acaef8', 'data', 'assets.overriddenProperties.travelAbroad', 'vehicle.characteristics.travelAbroad', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db299'),
               ('298866d3-4e4f-4ab7-948b-372ce1acaef2', 'data', 'assets.overriddenProperties.airConditioning', 'vehicle.characteristics.airConditioning', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db299'),
               ('398866d3-4e4f-4ab7-948b-372ce1acaef3', 'data', 'assets.overriddenProperties.infantSeat', 'vehicle.characteristics.infant', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db299'),
               ('331866de-4e4f-4ab7-948b-372ce1acaeb2','access_token','accessToken', NULL, NULL, NULL, 0, NULL,'e333f848-0f53-46fd-97fa-6b1f9f3db211');

	/* msp calls */
	INSERT INTO msp.msp_calls (msp_call_id,execution_order,is_mocked,"method",nb_calls,url,action_id,body_id) VALUES
         ('bfc126d5-d17c-4957-b1b0-d89bc58b1aae', 1, 0, 'GET', 1, 'http://demo7362099.mockable.io/le-taxi-mock/taxis', 'a614f848-0f53-46fd-97fa-6b1f9f3db299', NULL),
         ('abc126d5-d17c-4957-b1b0-e037e2dad5de',0,0,'GET',1, 'http://demo9071502.mockable.io/mockedtoken/oauth2/token','e333f848-0f53-46fd-97fa-6b1f9f3db211',NULL);



	/* headers */
	INSERT INTO msp.headers (headers_id,"key",process_function,security_flag,"sensitive",value,value_prefix,value_template,call_id) VALUES
             ('6de8ce89-90d2-475b-8e81-c087de351d86','Content-Type',NULL,NULL,NULL,'application/json',NULL,NULL,'bfc126d5-d17c-4957-b1b0-d89bc58b1aae'),
             ('6de8ce89-90d2-475b-8e81-c087de351d66','acces_token',NULL,1,NULL,'application/json',NULL,NULL,'bfc126d5-d17c-4957-b1b0-d89bc58b1aae');;


    INSERT INTO msp.params (params_id,key,key_mapper,precision,sensitive,timezone,value,call_id) VALUES
     	 ('4e8a4733-a504-4358-bcdc-f6fb5141ed10','lon','lon',NULL,0,NULL,NULL,'bfc126d5-d17c-4957-b1b0-d89bc58b1aae'),
     	 ('4e8a4733-a504-4358-bcdc-f6fb5141ed12','lat','lat',NULL,0,NULL,NULL,'bfc126d5-d17c-4957-b1b0-d89bc58b1aae');

 /* adapters */
        INSERT INTO msp.adapters (adapter_id,adapter_name) VALUES
           ('40142f60-9694-479f-a6cd-28b199b5e246', 'default-adapter');

    /* msp-standard */
    INSERT INTO msp.msp_standard (msp_standard_id,version_datamapping,version_standard,is_active,standard_name ,msp_action_id,msp_id,adapter_id) VALUES
       ('a6142f2f-317b-4e4c-ba77-54ce2f0dbdda', 'V1.0',  'V1',  true,  'le.taxi',  'a614f848-0f53-46fd-97fa-6b1f9f3db299', 'a614c97e-df56-4651-ac50-11525537964a','40142f60-9694-479f-a6cd-28b199b5e246'),
       ('a7082f2f-317b-4e4c-ba77-54ce2f0ddaaa', TRUE, 'FNMS', false, 'V1.1', 'e333f848-0f53-46fd-97fa-6b1f9f3db211', 'a614c97e-df56-4651-ac50-11525537964a','40142f60-9694-479f-a6cd-28b199b5e246');

