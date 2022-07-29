
	/* msp */
	INSERT INTO msp.msp_meta (msp_id,has_hold,has_no_parking_zone,has_operating_zone,has_parking,has_pref_parking_zone,has_speed_limit_zone,has_station,has_station_status,has_vehicule,is_enabled,
	logo_format,logo_url,name,operator,primary_color,secondary_color,type,url,url_webview,price_list_price_list_id) VALUES
    ('14390fdf-34c1-41c9-885e-6ce66505b759', false, false, false, false, false, false, false, true, NULL, NULL,
     NULL, NULL, 'mockA', 'mockable.io', '#0054BB', '#0054BB', 'VELO',
     NULL, true, NULL),
	 ('14390fdf-34c1-41c9-885e-6ce66505b735', false, false, false, false, false, false, false, true, NULL, NULL,
     NULL, NULL, 'mockB', 'mockable.io', '#0054BB', '#0054BB', 'VELO',
     NULL, true, NULL);

	/* ---------- AROUND_ME_SEARCH ---------- */


	/* msp_actions */
	INSERT INTO msp.msp_actions (msp_action_id,"action",is_authentication,is_pagination,is_refresh_authentication,name,selector_id) VALUES
      ('a615f848-0f53-46fd-97fa-6b1f9f3db299', 'AROUND_ME_SEARCH', false, 0, NULL, 'mockA', NULL),
	  ('a616f848-0f53-46fd-97fa-6b1f9f3db299', 'AROUND_ME_SEARCH', false, 0, NULL, 'mockB', NULL);


	/* msp calls */
	INSERT INTO msp.msp_calls (msp_call_id,execution_order,is_mocked,"method",nb_calls,url,action_id,body_id) VALUES
         ('cfc126d5-d17c-4957-b1b0-d89bc58b1aae', 1, 0, 'GET', 1, 'http://demo7362099.mockable.io/mockA/aroundMe', 'a615f848-0f53-46fd-97fa-6b1f9f3db299', NULL),
		 ('cfc136d5-d17c-4957-b1b0-d89bc58b1aae', 1, 0, 'GET', 1, 'http://demo7362099.mockable.io/mockB/aroundMe', 'a616f848-0f53-46fd-97fa-6b1f9f3db299', NULL);

    INSERT INTO msp.params (params_id,key,key_mapper,precision,sensitive,timezone,value,call_id) VALUES
     	 ('5e8a4733-a504-4358-bcdc-f6fb5141ed10','lon','lon',NULL,0,NULL,NULL,'cfc126d5-d17c-4957-b1b0-d89bc58b1aae'),
     	 ('5e8a4733-a504-4358-bcdc-f6fb5141ed12','lat','lat',NULL,0,NULL,NULL,'cfc126d5-d17c-4957-b1b0-d89bc58b1aae'),

		 ('7e8a4733-a504-4358-bcdc-f6fb5141ed10','lon','lon',NULL,0,NULL,NULL,'cfc136d5-d17c-4957-b1b0-d89bc58b1aae'),
     	 ('7e8a4733-a504-4358-bcdc-f6fb5141ed12','lat','lat',NULL,0,NULL,NULL,'cfc136d5-d17c-4957-b1b0-d89bc58b1aae');

 /* adapters */
        INSERT INTO msp.adapters (adapter_id,adapter_name) VALUES
           ('40142f60-9694-479f-a6cd-28b199b5e240', 'default-adapter');

    /* msp-standard */
    INSERT INTO msp.msp_standard (msp_standard_id,version_datamapping,version_standard,is_active,standard_name ,msp_action_id,msp_id,adapter_id) VALUES
       ('a6152f2f-317b-4e4c-ba77-54ce2f0dbdda', 'V1.0',  'V1',  true,  'le.taxi',  'a615f848-0f53-46fd-97fa-6b1f9f3db299', '14390fdf-34c1-41c9-885e-6ce66505b759','40142f60-9694-479f-a6cd-28b199b5e240'),
	   ('a6162f2f-317b-4e4c-ba77-54ce2f0dbdda', 'V1.0',  'V1',  true,  'le.taxi',  'a616f848-0f53-46fd-97fa-6b1f9f3db299', '14390fdf-34c1-41c9-885e-6ce66505b735','40142f60-9694-479f-a6cd-28b199b5e240');
