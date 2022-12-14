-- clear previous data
DO 
'
DECLARE var_body VARCHAR(50) := to_regclass(''msp.body'');
DECLARE var_body_params VARCHAR(50) := to_regclass(''msp.body_params'');
DECLARE var_headers VARCHAR(50) := to_regclass(''msp.headers'');
DECLARE var_headers_values_temp VARCHAR(50) := to_regclass(''msp.headers_values_template'');
DECLARE var_partner_calls VARCHAR(50) := to_regclass(''msp.partner_calls'');
DECLARE var_partner_actions VARCHAR(50) := to_regclass(''msp.partner_actions'');
DECLARE var_params VARCHAR(50) := to_regclass(''msp.params'');
DECLARE var_params_multi_calls VARCHAR(50) := to_regclass(''msp.params_multi_calls'');
DECLARE var_selector VARCHAR(50) := to_regclass(''msp.selector'');
DECLARE var_token VARCHAR(50) := to_regclass(''msp.token'');
DECLARE var_partner_meta VARCHAR(50) := to_regclass(''msp.partner_meta'');
DECLARE var_price_list VARCHAR(50) := to_regclass(''msp.price_list'');
DECLARE var_price_list_item VARCHAR(50) := to_regclass(''msp.price_list_item'');
DECLARE var_data_mapper VARCHAR(50) := to_regclass(''msp.data_mapper'');
DECLARE var_cache_param VARCHAR(50) := to_regclass(''msp.cache_param'');
DECLARE var_partner_standard VARCHAR(50) := to_regclass(''msp.partner_standard'');
DECLARE var_adapters VARCHAR(50) := to_regclass(''msp.adapters'');
DECLARE var_gateway_params VARCHAR(50) := to_regclass(''configuration.gateway_params'');


BEGIN
  IF var_body IS not NULL THEN
     truncate msp.body cascade;
  END IF;
    IF var_body_params IS not NULL THEN
     truncate msp.body_params cascade;
   END IF;
     IF var_headers IS not NULL THEN
     truncate msp.headers cascade;
   END IF;
    IF var_headers_values_temp IS not NULL THEN
     truncate msp.headers_values_template cascade;
   END IF;
    IF var_partner_calls IS not NULL THEN
     truncate msp.partner_calls cascade;
   END IF;
    IF var_partner_actions IS not NULL THEN
     truncate msp.partner_actions cascade;
   END IF;
     IF var_token IS not NULL THEN
     truncate msp.token cascade;
   END IF;
    IF var_params IS not NULL THEN
     truncate msp.params cascade;
   END IF;
    IF var_params_multi_calls IS not NULL THEN
     truncate msp.params_multi_calls cascade;
   END IF;
    IF var_selector IS not NULL THEN
     truncate msp.selector cascade;
   END IF;
    IF var_data_mapper IS not NULL THEN
     truncate msp.data_mapper cascade;
   END IF;
      IF var_price_list IS not NULL THEN
     truncate msp.price_list cascade;
   END IF;
      IF var_price_list_item IS not NULL THEN
     truncate msp.price_list_item cascade;
   END IF;
      IF var_partner_meta IS not NULL THEN
     truncate msp.partner_meta cascade;
   END IF;
   IF var_cache_param IS not NULL THEN
     truncate msp.cache_param cascade;
   END IF;
      IF var_partner_standard IS not NULL THEN
     truncate msp.partner_standard cascade;
   END IF;
      IF var_adapters IS not NULL THEN
     truncate msp.adapters cascade;
   END IF;
      IF var_gateway_params IS not NULL THEN
     truncate configuration.gateway_params cascade;
   END IF;
END; 
'
LANGUAGE 'plpgsql';