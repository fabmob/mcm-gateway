/* msp */
	INSERT INTO msp.msp_meta (msp_id,has_hold,has_no_parking_zone,has_operating_zone,has_parking,has_pref_parking_zone,has_speed_limit_zone,has_station,has_station_status,has_vehicule,is_enabled,
	logo_format,logo_url,name,operator,primary_color,secondary_color,type,url,url_webview,price_list_price_list_id) VALUES
    ('c614c97e-df56-4651-ac50-115255379111', false, false, false, false, false, false, false, true, NULL, NULL,
     NULL, NULL, 'COVOITURAGE', 'mockable.io', '#0054BB', '#0054BB', 'COVOITURAGE',
      'https://api.gouv.fr/documentation/Carpooling', true, NULL);



  /* msp_actions */
        INSERT INTO msp.msp_actions (msp_action_id,"action",is_authentication,is_pagination,is_refresh_authentication,name,selector_id) VALUES
            ('a614f848-0f53-46fd-97fa-6b1f9f3db666', 'PASSENGER_REGULAR_TRIPS_SEARCH', false, 0, NULL, 'Covoiturage', NULL);


    /* msp calls */
           INSERT INTO msp.msp_calls (msp_call_id,execution_order,is_mocked,"method",nb_calls,url,action_id,body_id) VALUES
              ('8e664121-9269-4a18-8fef-160cfe650922', 1, 0, 'GET', 1, 'http://demo9471709.mockable.io/passenger_regular_trips', 'a614f848-0f53-46fd-97fa-6b1f9f3db666', NULL);


    /* msp params */
            INSERT INTO msp.params (params_id,key,key_mapper,precision,sensitive,timezone,value,call_id) VALUES
             	 ('4e8a4733-a504-4358-bcdc-f6fb5155ed10','departureLat','departureLat',NULL,0,NULL,NULL,'bfc126d5-d17c-4957-b1b0-d89bc58b1aae'),
             	  ('4e8a4733-a504-4358-bcdc-f6fb5351ed10','departureLng','departureLng',NULL,0,NULL,NULL,'bfc126d5-d17c-4957-b1b0-d89bc58b1aae'),
             	   ('4e8a4733-a504-4358-bcdc-f6fb3741ed10','arrivalLat','arrivalLat',NULL,0,NULL,NULL,'bfc126d5-d17c-4957-b1b0-d89bc58b1aae'),
             	    ('4e8a4733-a504-4358-bcdc-f6fb5741ed10','arrivalLng','arrivalLng',NULL,0,NULL,NULL,'bfc126d5-d17c-4957-b1b0-d89bc58b1aae'),
             	     ('4e8a4733-a504-4358-bcdc-f6fb9741ed10','departureTimeOfDay','departureTimeOfDay',NULL,0,NULL,NULL,'bfc126d5-d17c-4957-b1b0-d89bc58b1aae'),
             	      ('4e8a4733-a504-4358-bcdc-f6fb5361ed10','timeDelta','timeDelta',NULL,0,NULL,"NULL",'bfc126d5-d17c-4957-b1b0-d89bc58b1aae'),
             	       ('4e8a4733-a504-4358-bcdc-f6fb2241ed10','departureRadius','departureRadius',NULL,0,NULL,"NULL",'bfc126d5-d17c-4957-b1b0-d89bc58b1aae'),
             	        ('4e8a4733-a504-4358-bcdc-f6fb2541ed10','arrivalRadius','arrivalRadius',NULL,0,NULL,"NULL",'bfc126d5-d17c-4957-b1b0-d89bc58b1aae'),
             	         ('4e8a4733-a504-4358-bcdc-f6fb4741ed10','minDepartureDate','minDepartureDate',NULL,0,NULL,"NULL",'bfc126d5-d17c-4957-b1b0-d89bc58b1aae'),
             	          ('4e8a4733-a504-4358-bcdc-f6fb5741ed10','maxDepartureDate','maxDepartureDate',NULL,0,NULL,"NULL",'bfc126d5-d17c-4957-b1b0-d89bc58b1aae'),
             	           ('4e8a4733-a504-4358-bcdc-f6fb1141ed12','count','count',NULL,0,NULL,"NULL",'bfc126d5-d17c-4957-b1b0-d89bc58b1aae');

   /* adapters */
        INSERT INTO msp.adapters (adapter_id,adapter_name) VALUES
           ('40142f60-9694-479f-a6cd-28b199b5e222', 'default-adapter');

   /* msp-standard */
         INSERT INTO msp.msp_standard (msp_standard_id,version_datamapping,version_standard,is_active,standard_name ,msp_action_id,msp_id, adapter_id) VALUES
           ('2f60bb33-34ce-4254-bc2d-45331dfa914f', 'V1.0',  'V1',  true,  'GBFS',  'a614f848-0f53-46fd-97fa-6b1f9f3db666', 'c614c97e-df56-4651-ac50-115255379111','40142f60-9694-479f-a6cd-28b199b5e222');

