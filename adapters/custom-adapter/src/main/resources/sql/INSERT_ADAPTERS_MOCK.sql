/* msp */
INSERT INTO msp.msp_meta (msp_id,has_hold,has_no_parking_zone,has_operating_zone,has_parking,has_pref_parking_zone,has_speed_limit_zone,has_station,has_station_status,has_vehicule,is_enabled,
logo_format,logo_url,name,operator,primary_color,secondary_color,type,url,url_webview,price_list_price_list_id) VALUES
('aae06a95-c946-4be7-bd60-d1c6919addb1', false, false, false, false, false, false, false, true, NULL, NULL,
 NULL, NULL, 'mockA', 'mockable.io', '#0054BB', '#0054BB', 'VELO',
 NULL, true, NULL);



/* msp_actions */
INSERT INTO msp.msp_actions (msp_action_id,name,action,is_authentication,is_refresh_authentication,is_pagination,selector_id) VALUES
('6a3b4f69-51f9-449d-9f30-48fe1e811b1a','mockA','AROUND_ME_SEARCH',0,NULL,0,NULL),
  ('815a8e1b-4c0c-4e80-aa55-d7588b7ea85d','mockB','AROUND_ME_SEARCH',0,NULL,0,NULL),
  ('ff7fdb2e-2641-4a7a-815c-bd505c84e9a1','mock available','AVAILABLE_ASSET_SEARCH',0,NULL,0,NULL),
  ('64a99b09-6401-4740-ac42-c4deb6c250a2','mock station','STATION_SEARCH',0,NULL,0,NULL),
  ('d98e66c3-825d-4d64-85c8-36a2f950ca02','station status','STATION_STATUS_SEARCH',0,NULL,0,NULL),
  ('88fa9f1d-d1d3-462f-a866-543e82b675ff','mock asset','ASSET_SEARCH',0,NULL,0,NULL),
  ('2ffbe35f-930d-4b75-9d53-3e24e721d7c4','mock global','GLOBAL_VIEW_SEARCH',0,NULL,0,NULL),
  ('b805bfc3-fe5e-4811-b3a2-907520db3e7a','mock zone','MSP_ZONE_SEARCH',0,NULL,0,NULL);

/* msp calls */
INSERT INTO msp.msp_calls (msp_call_id,execution_order,is_mocked,"method",nb_calls,url,action_id,body_id) VALUES
     ('cfc126d5-d17c-4957-b1b0-d89bc58b1aaa', 1, 0, 'GET', 1, 'http://demo7362099.mockable.io/mockA/aroundMe', 'a615f848-0f53-46fd-97fa-6b1f9f3db299', NULL),
	 ('cfc136d5-d17c-4957-b1b0-d89bc58b1aab', 1, 0, 'GET', 1, 'http://demo7362099.mockable.io/mockB/aroundMe', 'a616f848-0f53-46fd-97fa-6b1f9f3db299', NULL),
	 ('cfc136d5-d17c-4957-b1b0-d89bc58b1aac', 1, 0, 'GET', 1, 'http://demo7362099.mockable.io/available-asset-search', 'ff7fdb2e-2641-4a7a-815c-bd505c84e9a1', NULL),
	 ('cfc136d5-d17c-4957-b1b0-d89bc58b1aad', 1, 0, 'GET', 1, 'http://demo7362099.mockable.io/stations', '64a99b09-6401-4740-ac42-c4deb6c250a2', NULL),
	 ('cfc136d5-d17c-4957-b1b0-d89bc59b1aae', 1, 0, 'GET', 1, 'http://demo7362099.mockable.io/stationStatus', 'd98e66c3-825d-4d64-85c8-36a2f950ca02', NULL),
	 ('c34e250a-b0d4-46b6-9142-dad4d7676ef3', 1, 0, 'GET', 1, 'http://demo7362099.mockable.io/assets', '88fa9f1d-d1d3-462f-a866-543e82b675ff', NULL),
	 ('4496609a-1e72-4168-b05e-5db017ef2c8b', 1, 0, 'GET', 1, 'http://demo7362099.mockable.io/mockA/aroundMe', '2ffbe35f-930d-4b75-9d53-3e24e721d7c4', NULL),
	 ('4496609a-1e72-4168-b05e-5db017ef2c9b', 1, 0, 'GET', 1, 'http://demo7362099.mockable.io/mockB/aroundMe', '2ffbe35f-930d-4b75-9d53-3e24e721d7c4', NULL),
	 ('4486609a-1e72-4168-b05e-5db017ef2c8b', 1, 0, 'GET', 1, 'http://demo9071502.mockable.io/mspZoneMock', 'b805bfc3-fe5e-4811-b3a2-907520db3e7a', NULL);


 /* adapters */
INSERT INTO msp.adapters (adapter_id,adapter_name) VALUES
	('40142f60-9694-479f-a6cd-28b199b5e241', 'custom-adapter'),
	('453a930f-1787-4ef1-a457-082bcf0cfcba','default-adapter');

 /* msp-standard */
INSERT INTO msp.msp_standard (msp_standard_id,standard_name,is_active,version_standard,version_datamapping,msp_id,msp_action_id,adapter_id) VALUES
    ('0a45eac8-97df-4d28-8272-5f91716f41c6','FNMS',true,'V1','V2.x','aae06a95-c946-4be7-bd60-d1c6919addb1','6a3b4f69-51f9-449d-9f30-48fe1e811b1a','453a930f-1787-4ef1-a457-082bcf0cfcba'),
    ('d1d5a49c-5ffa-4cf8-87d3-ead0384359ef','FNMS',true,'V1','V2.x','aae06a95-c946-4be7-bd60-d1c6919addb1','ff7fdb2e-2641-4a7a-815c-bd505c84e9a1','453a930f-1787-4ef1-a457-082bcf0cfcba'),
    ('fe1f1afe-5e17-4485-ab20-ce6156bdd3ad','FNMS',true,'V1','V2.x','aae06a95-c946-4be7-bd60-d1c6919addb1','64a99b09-6401-4740-ac42-c4deb6c250a2','453a930f-1787-4ef1-a457-082bcf0cfcba'),
    ('1556232f-717b-4f4f-aff3-75ef310cc2d5','FNMS',true,'V1','V2.x','aae06a95-c946-4be7-bd60-d1c6919addb1','d98e66c3-825d-4d64-85c8-36a2f950ca02','453a930f-1787-4ef1-a457-082bcf0cfcba'),
    ('990105b4-c774-4321-8e74-0b856a7c0c01','FNMS',true,'V1','V2.x','aae06a95-c946-4be7-bd60-d1c6919addb1','88fa9f1d-d1d3-462f-a866-543e82b675ff','453a930f-1787-4ef1-a457-082bcf0cfcba'),
    ('6f991a3e-a928-4666-96c4-df0674feca98','FNMS',true,'V1','V2.x','aae06a95-c946-4be7-bd60-d1c6919addb1','2ffbe35f-930d-4b75-9d53-3e24e721d7c4','453a930f-1787-4ef1-a457-082bcf0cfcba'),
    ('7f0df434-3ca6-48d4-9cd8-df96ea9e3ee6','FNMS',true,'V1','V2.x','aae06a95-c946-4be7-bd60-d1c6919addb1','b805bfc3-fe5e-4811-b3a2-907520db3e7a','453a930f-1787-4ef1-a457-082bcf0cfcba');
