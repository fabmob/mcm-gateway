begin;


INSERT INTO msp.price_list (price_list_id, comment, out_of_bound_fee, parking_forbidden_fee, fk_msp_meta) VALUES
('22808a15-1b7b-4605-9764-3c71a3f05ae4', '', 5, 3, null),
('08b05a7e-e027-48ba-8916-83c7ddca8c9e', '', 5, 3, null),
('2d973d40-8e33-4b59-9334-a98bbf0e18c5', '', 5, 3, null),
('dad32a8c-0033-4e49-b6b9-cd051744f699', '', 5, 3, null),
('50d8c105-8742-4187-b4e8-c67c51a34229', '', 5, 3, null);


INSERT INTO msp.msp_meta (msp_id,has_hold,has_no_parking_zone,has_operating_zone,has_parking,has_pref_parking_zone,has_speed_limit_zone,has_station,has_station_status,has_vehicule,is_enabled,logo_format,logo_url,"name","operator",primary_color,secondary_color,"type",url,url_webview,price_list_price_list_id) VALUES
('b814c97e-df56-4651-ac50-11525537964f', false, false, false, false, false, false, false, true, NULL, NULL, 'PNG', '@MAAS_API_DIRECTORY_ICON@/icon_cityscoot@3x.png', 'Dott', 'Dott', '#0054BB', '#0054BB', 'TROTTINETTE', 'https://www.cityscoot.eu/paris/', false, '22808a15-1b7b-4605-9764-3c71a3f05ae4'),
('6c317901-749f-4d7d-82bb-afdc737b26b3', false, false, false, false, false, false, false, true, NULL, NULL, 'PNG', '@MAAS_API_DIRECTORY_ICON@/icon_cityscoot@3x.png', 'Dott', 'Dott', '#0054BB', '#0054BB', 'TROTTINETTE', 'https://www.cityscoot.eu/paris/', false, '08b05a7e-e027-48ba-8916-83c7ddca8c9e'),
('f6b6d937-dd91-4dd2-99d6-74958ee4e09a', false, false, false, false, false, false, false, true, NULL, NULL, 'PNG', '@MAAS_API_DIRECTORY_ICON@/icon_cityscoot@3x.png', 'Dott', 'Dott', '#0054BB', '#0054BB', 'TROTTINETTE', 'https://www.cityscoot.eu/paris/', false, '2d973d40-8e33-4b59-9334-a98bbf0e18c5'),
('28784eb7-de79-434d-a1b6-819ebfd50860', false, false, false, false, false, false, false, true, NULL, NULL, 'PNG', '@MAAS_API_DIRECTORY_ICON@/icon_cityscoot@3x.png', 'Dott', 'Dott', '#0054BB', '#0054BB', 'TROTTINETTE', 'https://www.cityscoot.eu/paris/', false, 'dad32a8c-0033-4e49-b6b9-cd051744f699'),
('3eeec675-2da2-421b-9b6c-ed05776b9f64', false, false, false, false, false, false, false, true, NULL, NULL, 'PNG', '@MAAS_API_DIRECTORY_ICON@/icon_cityscoot@3x.png', 'Dott', 'Dott', '#0054BB', '#0054BB', 'TROTTINETTE', 'https://www.cityscoot.eu/paris/', false, '50d8c105-8742-4187-b4e8-c67c51a34229');


INSERT INTO msp.price_list_item (dtype,price_list_item_id,fare_per_unit,fixed_fare,lower_price_limit,unit,upper_price_limit,price_list_distance,price_list_duration) VALUES
	 ('distance','86d370e4-06ce-4dd7-8c4c-2d7f80d46f3d',7,6,4,5,9,'22808a15-1b7b-4605-9764-3c71a3f05ae4',NULL),
	 ('distance','3070abb1-ef78-4388-992a-ed8e2632db11',7,6,4,5,9,'08b05a7e-e027-48ba-8916-83c7ddca8c9e',NULL),
	 ('distance','de0968a9-c598-40aa-aace-63aa863a95d5',7,6,4,5,9,'2d973d40-8e33-4b59-9334-a98bbf0e18c5',NULL),
	 ('distance','af3458b0-b5a6-4474-a99d-ec2ede3dee8d',7,6,4,5,9,'dad32a8c-0033-4e49-b6b9-cd051744f699',NULL),
	 ('distance','b29cb9e5-0ff5-45f5-a06f-412682f45bfb',7,6,4,5,9,'50d8c105-8742-4187-b4e8-c67c51a34229',NULL),
	 ('duration','30820b7c-5448-4285-a13a-0708fc873af6',7,6,4,5,9,NULL,'2d973d40-8e33-4b59-9334-a98bbf0e18c5'),
	 ('duration','0f82a2bd-4313-4498-b0f5-4dc9a6059770',7,6,4,5,9,NULL,'08b05a7e-e027-48ba-8916-83c7ddca8c9e'),
	 ('duration','87930fdf-34c1-41c9-885e-6ce66505b598',7,6,4,5,9,NULL,'22808a15-1b7b-4605-9764-3c71a3f05ae4'),
	 ('duration','15f24d7a-f36c-4d51-a979-202f411f1db6',7,6,4,5,9,NULL,'dad32a8c-0033-4e49-b6b9-cd051744f699'),
	 ('duration','623b0a97-b759-466f-86c5-935501255a66',7,6,4,5,9,NULL,'50d8c105-8742-4187-b4e8-c67c51a34229');

INSERT INTO msp.selector (selector_id,"key",value) VALUES
	 ('ec0e9a76-f1fe-422c-803b-8332ad957942','test_s','test_s'),
	 ('6d91188b-cee1-4351-b277-713ad0b22955','test_s','test_s'),
	 ('7d2449bc-dff9-4a58-8d06-8f5f27dbcf97','test_s','test_s'),
	 ('1b71f9ee-c8c6-428f-b60d-fffcea67c17b','test_s','test_s');

INSERT INTO msp.msp_actions (msp_action_id,"action",is_authentication,is_pagination,is_refresh_authentication,name,selector_id) VALUES
	 ('e222f848-0f53-46fd-97fa-6b1f9f3db265','search',false,0,0,'test_action','ec0e9a76-f1fe-422c-803b-8332ad957942'),
	 ('2c1a2210-4916-4aaa-8287-7a4d50e93518','search',false,0,0,'test_action','6d91188b-cee1-4351-b277-713ad0b22955'),
	 ('5e71dcb3-ca60-4a2a-9896-c509ad1ca756','search',false,0,0,'test_action','7d2449bc-dff9-4a58-8d06-8f5f27dbcf97'),
	 ('7c8f8780-a6fa-495d-8b36-24a20539adca','ZONE_OPERATING',false,1,0,'CityScoot','1b71f9ee-c8c6-428f-b60d-fffcea67c17b');

INSERT INTO msp.token (token_id,access_token,expire_at,msp_id) VALUES
	 ('ba48d9ae-722e-41a2-81b8-2f449ac71caf','accessToken test ','2022-03-17 11:51:52.202','b814c97e-df56-4651-ac50-11525537964f'),
     ('c13684f5-5438-4633-9af2-1e835bb7bc9e','accessToken test2','2022-03-22 11:51:52.202','6c317901-749f-4d7d-82bb-afdc737b26b3');


INSERT INTO msp.body (body_id,is_static,"template") VALUES
	 ('9404fa40-1549-4fb9-bb89-d89bc58b1eef',1,NULL),
	 ('531d12cc-9543-452b-aa34-105166971f1d',1,NULL);

INSERT INTO msp.body_params (body_params_id,is_refresh_token,"key",key_mapper,"precision","sensitive",timezone,value,body_id) VALUES
         ('c788a24a-d0e8-485f-b5b5-ad66340c30b7',NULL,NULL,'VhiculeID',NULL,30,NULL,NULL,'9404fa40-1549-4fb9-bb89-d89bc58b1eef'),
	 ('72f655c2-9b76-4a9e-956d-7ea63e3be0ae',NULL,'CITY_LOGIN','{MAAS_CITY_LOGIN:parisratpcitycontrol@goflash.com}',NULL,30,NULL,NULL,'531d12cc-9543-452b-aa34-105166971f1d');


INSERT INTO msp.msp_calls (msp_call_id,is_mocked,"method",nb_calls,execution_order,url,action_id,body_id) VALUES
	 ('cfceccab-e4d0-4622-bde1-cd2dc73088c4',0,'GET',1,1,'MAAS_COMMUNAUTO_GET_BOOKINGS_URL:https://pp.reservauto.net/Scripts/Client/Ajax/MobileApplication/ReservationList.asp','e222f848-0f53-46fd-97fa-6b1f9f3db265','9404fa40-1549-4fb9-bb89-d89bc58b1eef'),
     ('bfc126d5-d17c-4957-b1b0-e037e2dad9da',0,'GET',1,1,'@MAAS_CITYSCOOT_AUTHENTICATION_URL@','2c1a2210-4916-4aaa-8287-7a4d50e93518','531d12cc-9543-452b-aa34-105166971f1d');

INSERT INTO msp.headers (headers_id,"key",process_function,security_flag,"sensitive",value,value_prefix,value_template,call_id) VALUES
	 ('84f49cdd-d430-40d1-9ba2-4b52f89eed20','Content-Type',NULL,NULL,NULL,'application/json',NULL,NULL,'cfceccab-e4d0-4622-bde1-cd2dc73088c4'),
         ('6de8ce89-90d2-475b-8e81-c087de351d86','Content-Type',NULL,NULL,NULL,'application/json',NULL,NULL,'bfc126d5-d17c-4957-b1b0-e037e2dad9da');

INSERT INTO msp.headers_values_template (headers_values_template_id,"key",value,headers_id) VALUES
	 ('19e9e6b8-6ac0-405e-96b1-37ca955587e0','user','{MAAS_CITYSCOOT_CLIENT_ID:kFEkfWQt8UATcsccM7Ey}','84f49cdd-d430-40d1-9ba2-4b52f89eed20'),
         ('a876195c-61f3-4bf3-94b8-3625c71c35b3','user','{MAAS_CITYSCOOT_CLIENT_ID:kFEkfWQt8UATcsccM7Ey}','6de8ce89-90d2-475b-8e81-c087de351d86');

INSERT INTO msp.params_multi_calls (params_multi_calls_id,call_id,"key",value_offset,init_value,timezone) VALUES
	 ('28371a09-11fd-415c-b70f-8ae98806ee85','bfc126d5-d17c-4957-b1b0-e037e2dad9da','TripStartDate','30','NOW','Europe/Paris'),
	 ('f3bc84a0-e9a2-4bee-9f65-a46889236028','bfc126d5-d17c-4957-b1b0-e037e2dad9da','TripEndDate','30','OFFSET','Europe/Paris');


INSERT INTO msp.params (params_id,"key",key_mapper,"precision","sensitive",timezone,value,call_id) VALUES
	 ('5e8a4733-a504-4358-bcdc-f6fb5141ed88','StartDate',NULL,NULL,NULL,'Europe/Paris','-15','cfceccab-e4d0-4622-bde1-cd2dc73088c4');

INSERT INTO msp.data_mapper (data_mapper_id,champ_externe, champ_interne,contained_value,default_value,format,is_array,timezone,action_id) VALUES
	 ('231866de-4e4f-4ab7-948b-372ce1acaef2','StationID','ID_STATION',NULL,NULL,NULL,0,'','e222f848-0f53-46fd-97fa-6b1f9f3db265'),
	 ('2643cd99-7088-4f1b-b49b-116149886d1e','vehicleId','VEHICLE_ID',NULL,'VOITURE',NULL,0,'','2c1a2210-4916-4aaa-8287-7a4d50e93518'),
	 ('bf934856-0356-4152-8164-573bca1a3302','vehicleColor','COLOR',NULL,'VOITURE',NULL,0,'','2c1a2210-4916-4aaa-8287-7a4d50e93518'),
	 ('0e51c003-6222-449d-9f08-20a7e2b8d94d','stationNo:005->40 rue Dussoubs, 75002 Paris;','ADDRESS',NULL,NULL,NULL,0,'','5e71dcb3-ca60-4a2a-9896-c509ad1ca756');


 INSERT INTO msp.adapters (adapter_id,adapter_name) VALUES
     ('40142f60-9694-479f-a6cd-28b199b5e240', 'default-adapter'),
      ('40142f60-9694-479f-a6cd-28b199b5e241', 'custom-adapter');

 INSERT INTO msp.msp_standard (msp_standard_id,is_active,standard_name ,version_datamapping ,version_standard ,msp_action_id,msp_id,adapter_id) VALUES
     ('f7082f2f-317b-4e4c-ba77-54ce2f0dbfff', FALSE, 'FNMS', 'V1.0', 'V1.1', '7c8f8780-a6fa-495d-8b36-24a20539adca', 'b814c97e-df56-4651-ac50-11525537964f','40142f60-9694-479f-a6cd-28b199b5e240'),
     ('bffc3749-cbd3-47bd-a2d8-940693e74303', TRUE, 'FNMS', 'V2.0', 'V2.1', '2c1a2210-4916-4aaa-8287-7a4d50e93518', '6c317901-749f-4d7d-82bb-afdc737b26b3','40142f60-9694-479f-a6cd-28b199b5e240');

INSERT INTO msp.cache_param (msp_id, action_type, cache_param_id, soft_ttl, hard_ttl, refresh_cache_delay) VALUES
    ('28784eb7-de79-434d-a1b6-819ebfd50860', 'VEHICULE_SEARCH', '885a4c3d-b459-41d6-b1c1-e30a82ea777b', 20, 30, 15),
    ('28784eb7-de79-434d-a1b6-819ebfd50860', 'AVAILABLE_ASSET', '885a4c3d-b459-41d6-b1c1-e30a82ea888b', 45, 90, 20);
commit;