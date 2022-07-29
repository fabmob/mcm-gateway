

     /* selector */
     INSERT INTO msp.selector (selector_id,"key",value) VALUES
                	 ('da464061-ebb1-4a57-b1d2-97e8312c4ebe','geofencing_zones',null);

    /* ---------- ACTION MSP_ZONE_SEARCH ---------- */

    /* msp_actions */
        INSERT INTO msp.msp_actions (msp_action_id,"action",is_authentication,is_pagination,is_refresh_authentication,name,selector_id) VALUES
            ('a614f848-0f53-46fd-97fa-6b1f9f3db303', 'MSP_ZONE_SEARCH', false, 0, NULL, 'GBFS', 'da464061-ebb1-4a57-b1d2-97e8312c4ebe');


    /* data_mapper  */
        INSERT INTO msp.data_mapper(data_mapper_id,champ_externe, champ_interne,contained_value,default_value,format,is_array,timezone,action_id) VALUES
               ('121866de-4e4f-2ab7-848b-372ce1acaea6', 'type', 'type', NULL, NULL, NULL, 0, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db303'),
               ('57eacf4e-1940-436e-b945-85f8d4833aa5', 'features', 'zones.type', 'type', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db303'),
               ('656f4cae-8527-43a0-a80f-00ac82818744', 'features', 'zones.geometry', 'geometry', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db303'),
               ('d627595d-4060-440e-8380-a1fe9f3f2a81', 'features', 'zones.name', 'properties.name', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db303'),
               ('b4b729f7-5699-411c-8f5a-424bbc7c89fc', 'features', 'zones.rules.vehicleTypeId', 'properties.rules.vehicle_type_id', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db303'),
               ('7b6ccf70-1b82-48a1-991b-8050827ad9b6', 'features', 'zones.rules.isRideAllowed', 'properties.rules.ride_allowed', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db303'),
               ('8b6ccf70-1b82-48a1-991b-8050827ad9b6', 'features', 'zones.rules.isRideThroughAllowed', 'properties.rules.ride_through_allowed', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db303'),
               ('9b6ccf70-1b82-48a1-991b-8050827ad9b6', 'features','zones.rules.maximumSpeedKph', 'properties.rules.maximum_speed_kph', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db303'),
               ('7b6ccf70-1b82-48a1-991b-8050827ad9b7', 'features', 'zones.rules.isStationParking', 'properties.rules.station_parking', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db303');

  /* msp calls */
        INSERT INTO msp.msp_calls (msp_call_id,execution_order,is_mocked,"method",nb_calls,url,action_id,body_id) VALUES
           ('8e664121-9269-4a18-8fef-160cfe650968', 1, 0, 'GET', 1, 'http://demo9071502.mockable.io/area', 'a614f848-0f53-46fd-97fa-6b1f9f3db303', NULL);


    /* msp-standard */
      INSERT INTO msp.msp_standard (msp_standard_id,version_datamapping,version_standard,is_active,standard_name ,msp_action_id,msp_id,adapter_id) VALUES
        ('1f70bb33-34ce-4254-bc2d-45331dfa914c', 'V1.0',  'V1',  true,  'GBFS',  'a614f848-0f53-46fd-97fa-6b1f9f3db303', 'b714c97e-df56-4651-ac50-11525537964e','40142f60-9694-479f-a6cd-28b199b5e246');

