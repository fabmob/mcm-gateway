


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
    	 ('ec0e9a76-f1fe-422c-803b-8332ad957941','test_s','test_s');

	/* msp_actions */
	INSERT INTO msp.msp_actions (msp_action_id,"action",is_authentication,is_pagination,is_refresh_authentication,name,selector_id) VALUES
    	 ('e222f848-0f53-46fd-97fa-6b1f9f3db266','STATION_SEARCH',0,0,NULL,'JC_DECAUX','ec0e9a76-f1fe-422c-803b-8332ad957941');

    /* body */
    INSERT INTO msp.body (body_id,is_static, template) VALUES
         	 ('9404fa40-1549-4fb9-bb89-d89bc58b1eed',1 ,NULL);

	/* msp calls */
	INSERT INTO msp.msp_calls (msp_call_id,is_mocked,"method",nb_calls,execution_order,url,action_id,body_id) VALUES
    	  ('bfc126d5-d17c-4957-b1b0-e037e2dad9de',0,'GET',1,1, 'https://api.jcdecaux.com/vls/v1/stations','e222f848-0f53-46fd-97fa-6b1f9f3db266','9404fa40-1549-4fb9-bb89-d89bc58b1eed');

	/* headers */
	INSERT INTO msp.headers (headers_id,"key",process_function,security_flag,"sensitive",value,value_prefix,value_template,call_id) VALUES
             ('6de8ce89-90d2-475b-8e81-c087de351d86','Content-Type',NULL,NULL,NULL,'application/json',NULL,NULL,'bfc126d5-d17c-4957-b1b0-e037e2dad9de');

    /* headers_values_template */
    INSERT INTO msp.headers_values_template (headers_values_template_id,"key",value,headers_id) VALUES
             ('a876195c-61f3-4bf3-94b8-3625c71c35b4','user','{MAAS_CITYSCOOT_CLIENT_ID:kFEkfWQt8UATcsccM7Ey}','6de8ce89-90d2-475b-8e81-c087de351d86');

    INSERT INTO msp.params (params_id,key,value,call_id) VALUES
     	 ('5e8a4733-a504-4358-bcdc-f6fb5141ed10','apiKey','a8b04d043bc08144f91000fecbcfb15b174acdef','bfc126d5-d17c-4957-b1b0-e037e2dad9de'),
     	 ('5e8a4733-a504-4358-bcdc-f6fb5141ed12','contract','lyon','bfc126d5-d17c-4957-b1b0-e037e2dad9de');

    /* msp-standard */
    INSERT INTO msp.msp_standard (msp_standard_id,is_active,standard_name ,version_datamapping ,version_standard ,msp_action_id,msp_id) VALUES
        ('f7082f2f-317b-4e4c-ba77-54ce2f0dbfff', FALSE, 'FNMS', 'V1.0', 'V1.1', 'e222f848-0f53-46fd-97fa-6b1f9f3db266', 'b814c97e-df56-4651-ac50-11525537964a'),
        ('bffc3749-cbd3-47bd-a2d8-940693e74303', TRUE, 'FNMS', 'V2.0', 'V2.1', 'e222f848-0f53-46fd-97fa-6b1f9f3db266', 'b814c97e-df56-4651-ac50-11525537964a');
