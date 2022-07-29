
	/* msp */
	INSERT INTO msp.msp_meta (msp_id,has_hold,has_no_parking_zone,has_operating_zone,has_parking,has_pref_parking_zone,has_speed_limit_zone,has_station,has_station_status,has_vehicule,is_enabled,
	logo_format,logo_url,name,operator,primary_color,secondary_color,type,url,url_webview,price_list_price_list_id) VALUES
    ('a714c97e-df56-4651-ac50-11525537963b', false, false, false, false, false, false, false, true, NULL, NULL,
     NULL, NULL, 'MockedMSP', 'mockable.io', NULL, NULL, 'TROTTINETTE',
      'http://demo7362099.mockable.io/mockedmsp', false, NULL);


    /* token */
      	INSERT INTO msp.token (token_id, access_token, expire_at, msp_id) VALUES
      	('b714c97e-df56-4651-ac50-115255379999','MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3','1970-01-01 00:00:00.000', 'a714c97e-df56-4651-ac50-11525537963b');

	/* ---------- ACTION AUTHENTICATION + ACTION WITH BODY---------- */

	/* msp_actions */
	INSERT INTO msp.msp_actions (msp_action_id,"action",is_authentication,is_pagination,is_refresh_authentication,name,selector_id) VALUES
    	 ('e333f848-0f53-46fd-97fa-6b1f9f3db222',NULL,true,0,0,'MockedMSP',NULL);

    /* data_mapper  */
          INSERT INTO msp.data_mapper  (data_mapper_id,champ_externe, champ_interne,contained_value,default_value,format,is_array,timezone,action_id) VALUES
                   ('331866de-4e4f-4ab7-948b-372ce1acaed2','access_token','accessToken', NULL, NULL, NULL, 0, NULL,'e333f848-0f53-46fd-97fa-6b1f9f3db222');


    /* body */
    INSERT INTO msp.body (body_id,is_static,template) VALUES
         	 ('8304fa40-1549-4fb9-bb89-d89bc58b1eed',1 ,'{"client_id": "${CLIENT_ID}", "client_secret": "${CLIENT_SECRET}", "grant_type": "${CLIENT_CREDENTIALS}", "scope": "${SCOPE}"}'),
         	 ('7204fa40-1549-4fb9-bb89-d89bc58b1eed',0 ,'{"station_id": "${STATION_ID}", "vehicle_id": "${VEHICLE_ID}", "reservation_start": "${START_TIME}","reservation_end": "${END_TIME}", "additionalKey":"${ADDITIONAL}"}');

    /* body params */
        INSERT INTO msp.body_params (body_params_id,is_refresh_token,"key",key_mapper,"precision","sensitive",timezone,value,body_id) VALUES
                 ('c788a24a-d0e8-485f-b5b5-ad66340c30b7',NULL,NULL,'CLIENT_ID',NULL,0,NULL,'123client_id','8304fa40-1549-4fb9-bb89-d89bc58b1eed'),
                 ('c888a24a-d0e8-485f-b5b5-ad66340c30b8',NULL,NULL,'CLIENT_SECRET',NULL,0,NULL,'123client_secret','8304fa40-1549-4fb9-bb89-d89bc58b1eed'),
                 ('c999a24a-d0e8-485f-b5b5-ad66340c30b9',NULL,NULL,'CLIENT_CREDENTIALS',NULL,0,NULL,'123client_credentials','8304fa40-1549-4fb9-bb89-d89bc58b1eed'),
                 ('c666a24a-d0e8-485f-b5b5-ad66340c30b6',NULL,NULL,'SCOPE',NULL,0,NULL,'monscope','8304fa40-1549-4fb9-bb89-d89bc58b1eed'),

                 ('c111a24a-d0e8-485f-b5b5-ad66340c30b6',NULL,'stationId','STATION_ID',NULL,0,NULL,NULL,'7204fa40-1549-4fb9-bb89-d89bc58b1eed'),
                 ('c222a24a-d0e8-485f-b5b5-ad66340c30b6',NULL,'assetId','VEHICLE_ID',NULL,0,NULL,NULL,'7204fa40-1549-4fb9-bb89-d89bc58b1eed'),
                 ('c333a24a-d0e8-485f-b5b5-ad66340c30b6',NULL,NULL,'ADDITIONAL',NULL,1,NULL,'123abc','7204fa40-1549-4fb9-bb89-d89bc58b1eed'),
                 ('c444a24a-d0e8-485f-b5b5-ad66340c30b6',NULL,'reserveFrom','START_TIME','-15',0,'Europe/Paris',NULL,'7204fa40-1549-4fb9-bb89-d89bc58b1eed'),
                 ('c555a24a-d0e8-485f-b5b5-ad66340c30b6',NULL,'reserveTo','END_TIME','+15',0,'Europe/Paris',NULL,'7204fa40-1549-4fb9-bb89-d89bc58b1eed');



	/* msp calls */
	INSERT INTO msp.msp_calls (msp_call_id,is_mocked,"method",nb_calls,execution_order,url,action_id,body_id) VALUES
    	  ('abc126d5-d17c-4957-b1b0-e037e2dad9de',0,'GET',1,1, 'http://demo9071502.mockable.io/mockedtoken/oauth2/token','e333f848-0f53-46fd-97fa-6b1f9f3db222','8304fa40-1549-4fb9-bb89-d89bc58b1eed');

 /* adapters */
        INSERT INTO msp.adapters (adapter_id,adapter_name) VALUES
           ('40142f60-9694-479f-a6cd-28b199b5e241', 'adapter');


    /* msp-standard */
    INSERT INTO msp.msp_standard (msp_standard_id,is_active,standard_name ,version_datamapping ,version_standard ,msp_action_id,msp_id,adapter_id) VALUES
        ('a7082f2f-317b-4e4c-ba77-54ce2f0dbaaa', TRUE, 'FNMS', 'V1.0', 'V1.1', 'e333f848-0f53-46fd-97fa-6b1f9f3db222', 'a714c97e-df56-4651-ac50-11525537963b','40142f60-9694-479f-a6cd-28b199b5e241');
