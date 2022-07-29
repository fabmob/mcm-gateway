
	/* msp */
	INSERT INTO msp.msp_meta (msp_id,has_hold,has_no_parking_zone,has_operating_zone,has_parking,has_pref_parking_zone,has_speed_limit_zone,has_station,has_station_status,has_vehicule,is_enabled,
	logo_format,logo_url,name,operator,primary_color,secondary_color,type,url,url_webview,price_list_price_list_id) VALUES
    ('a716c97e-df56-4651-ac50-11525537964d', false, false, false, false, false, false, false, true, NULL, NULL,
     NULL, NULL, 'MockedMSP', 'mockable.io', NULL, NULL, 'TROTTINETTE',
      'http://demo7362099.mockable.io/mockedmsp', false, NULL);


   /* ---------- ACTION ASSET_SEARCH AVEC MULTICALL---------- */


	/* msp_actions */
	INSERT INTO msp.msp_actions (msp_action_id,"action",is_authentication,is_pagination,is_refresh_authentication,name,selector_id) VALUES
    	 ('e333f848-0f53-46fd-97fa-6b1f9f3db211','ASSET_SEARCH',false,0,0,'MockedMSP',NULL);


	/* msp calls */
	INSERT INTO msp.msp_calls (msp_call_id,is_mocked,"method",nb_calls,execution_order,url,action_id,body_id) VALUES
    	  ('abc126d5-d17c-4957-b1b0-e037e2dad8fe',0,'GET',3,1, 'http://demo9071502.mockable.io/ReserverAsset','e333f848-0f53-46fd-97fa-6b1f9f3db211',null);

 	   /* msp calls */
    INSERT INTO msp.params_multi_calls (params_multi_calls_id, init_value, "key", timezone, value_offset, call_id) VALUES
           ('6de8ce89-90d2-475b-8e81-c087de551d70', 'NOW', 'TripStartDate', 'Europe/Paris', '30', 'abc126d5-d17c-4957-b1b0-e037e2dad8fe'),
            ('6de8ce89-90d2-475b-8e81-c087de851d61', 'OFFSET', 'TripEndDate', 'Europe/Paris', '30', 'abc126d5-d17c-4957-b1b0-e037e2dad8fe');

    /* adapters */
            INSERT INTO msp.adapters (adapter_id,adapter_name) VALUES
               ('40142f60-9694-479f-a6cd-28b199b5e240', 'default-adapter');

    /* msp-standard */
    INSERT INTO msp.msp_standard (msp_standard_id,is_active,standard_name ,version_datamapping ,version_standard ,msp_action_id,msp_id,adapter_id) VALUES
        ('a7082f2f-317b-4e4c-ba77-54ce2f0dbaab', TRUE, 'FNMS', 'V1.0', 'V1', 'e333f848-0f53-46fd-97fa-6b1f9f3db211', 'a716c97e-df56-4651-ac50-11525537964d','40142f60-9694-479f-a6cd-28b199b5e240');
