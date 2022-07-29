  /* selector */
     INSERT INTO msp.selector (selector_id,"key",value) VALUES
                	 ('da464071-ebb1-4a57-b1d2-97e8312c4ebe','vehicle_types',null);


    /* ---------- ACTION VEHICLE_TYPES_SEARCH ---------- */
  /* msp_actions */
        INSERT INTO msp.msp_actions (msp_action_id,"action",is_authentication,is_pagination,is_refresh_authentication,name,selector_id) VALUES
            ('a614f848-0f53-46fd-97fa-6b1f9f3db304', 'VEHICLE_TYPES_SEARCH', false, 0, NULL, 'GBFS', 'da464071-ebb1-4a57-b1d2-97e8312c4ebe');

   /* data_mapper */
   INSERT INTO msp.data_mapper(data_mapper_id,champ_externe, champ_interne,contained_value,default_value,format,is_array,timezone,action_id) VALUES
                     ('57eacf4e-1940-436e-b945-15f8d4833aa5', 'vehicle_type_id', 'vehicleTypeId', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db304'),
                     ('656f4cae-8527-43a0-a80f-10ac82818744', 'form_factor', 'formFactor', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db304'),
                     ('d627595d-4060-440e-8381-a1fe9f3f2a81', 'propulsion_type', 'propulsionType', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db304'),
                     ('b4b729f7-5699-411c-8f6a-424bbc7c89fc', 'name', 'name', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db304'),
                     ('7b6ccf70-1b82-48a1-993b-8050827ad9b6', 'max_range_meters', 'maxRangeMeters', NULL, NULL, NULL, NULL, NULL, 'a614f848-0f53-46fd-97fa-6b1f9f3db304');

    /* msp calls */
           INSERT INTO msp.msp_calls (msp_call_id,execution_order,is_mocked,"method",nb_calls,url,action_id,body_id) VALUES
              ('9e664121-9269-4a18-8fef-160cfe650968', 1, 0, 'GET', 1, 'http://demo9071502.mockable.io/vehicleTypes', 'a614f848-0f53-46fd-97fa-6b1f9f3db304', NULL);


   /* msp-standard */
         INSERT INTO msp.msp_standard (msp_standard_id,version_datamapping,version_standard,is_active,standard_name ,msp_action_id,msp_id,adapter_id) VALUES
           ('2f70bb33-34ce-4254-bc2d-45331dfa914c', 'V1.0',  'V1',  true,  'GBFS',  'a614f848-0f53-46fd-97fa-6b1f9f3db304', 'b714c97e-df56-4651-ac50-11525537964e','40142f60-9694-479f-a6cd-28b199b5e246');

