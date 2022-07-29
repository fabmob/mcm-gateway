
    /* selector */
     INSERT INTO msp.selector (selector_id,"key",value) VALUES
         ('8a10c3e5-1aa7-4071-a319-9267baa18af9','bikes',null);
     /* ---------- ACTION ASSET_SEARCH ---------- */
     /* msp_actions */
     INSERT INTO msp.msp_actions (msp_action_id,"action",is_authentication,is_pagination,is_refresh_authentication,name,selector_id) VALUES
          ('a614f848-0f53-46fd-97fa-6b1f9f3db302', 'ASSET_SEARCH', false, 0, NULL, 'GBFS', '8a10c3e5-1aa7-4071-a319-9267baa18af9');


        /* data_mapper  */
        INSERT INTO msp.data_mapper  (data_mapper_id,champ_externe, champ_interne,contained_value,default_value,format,is_array,timezone,action_id) VALUES
               ('121866de-4e4f-1ab7-848b-372ce1acaea6', 'bike_id', 'assetId', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db302'),
               ('131866de-4e4f-1ab7-848b-372ce1acaea6', 'last_reported', 'lastReported', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db302'),
               ('142866de-4e4f-1ab7-848b-372ce1acaea9', 'lat', 'overriddenProperties.location.coordinates.lat', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db302'),
               ('153866de-4e4f-1ab7-848b-372ce1acaea1', 'lon', 'overriddenProperties.location.coordinates.lng', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db302'),
               ('164866de-4e4f-1ab7-848b-372ce1acaea7', 'is_reserved', 'isReserved', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db302'),
               ('178866d3-4e4f-1ab7-848b-372ce1acaea3', 'is_disabled', 'isDisabled', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db302'),
               ('189866d3-4e4f-1ab7-848b-372ce1acaea8', 'vehicle_type_id', 'assetType', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db302'),
               ('191866d1-4e4f-1ab7-848b-372ce1acaea5', 'current_range_meters', 'overriddenProperties.currentRangeMeters', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db302'),
               ('191866d1-4e4f-1ab7-548b-372ce1acaea5', 'station_id', 'overriddenProperties.location.stationId', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db302'),
               ('191866d1-4e4f-1ab7-548b-372ce1acaea6', 'home_station', 'homeStationId', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db302'),
               ('201866d1-4e4f-1ab7-448b-372ce1acaea5', 'pricing_plan_id', 'planId', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db302'),
               ('201866d1-4e4f-1ab7-448b-382ce1acaea5', 'vehicle_equipment', 'overriddenProperties.infantSeat', 'child_seat_a', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db302'),
               ('201866d1-4e4f-1ab7-448b-382ce1acaea6', 'vehicle_equipment', 'overriddenProperties.winterTires', 'winter_tires', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db302'),
               ('201866d1-4e4f-1ab7-448b-382ce1acaea7', 'vehicle_equipment', 'overriddenProperties.snowChains', 'snow_chains', NULL, NULL, 1, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db302');


     /* msp calls */
     INSERT INTO msp.msp_calls (msp_call_id,execution_order,is_mocked,"method",nb_calls,url,action_id,body_id) VALUES
           ('bfc126d5-d29c-4957-b1b0-d89bc58b1aae', 1, 0, 'GET', 1, 'http://demo9071502.mockable.io/vehicleStatus', 'a614f848-0f53-46fd-97fa-6b1f9f3db302', NULL);

    /* msp-standard */
      INSERT INTO msp.msp_standard (msp_standard_id,version_datamapping,version_standard,is_active,standard_name ,msp_action_id,msp_id,adapter_id) VALUES
        ('a6142f2f-317b-4e4c-ba99-65ce2f0dbdda', 'V1.0',  'V1',  true,  'GBFS',  'a614f848-0f53-46fd-97fa-6b1f9f3db302', 'b714c97e-df56-4651-ac50-11525537964e','40142f60-9694-479f-a6cd-28b199b5e246');
