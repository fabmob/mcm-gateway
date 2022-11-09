CREATE SCHEMA IF NOT EXISTS msp;
CREATE SCHEMA IF NOT EXISTS configuration;

TRUNCATE  msp.price_list cascade;
TRUNCATE  msp.partner_meta cascade;
TRUNCATE  msp.price_list_item cascade;
begin;

INSERT INTO msp.price_list (price_list_id, comment, out_of_bound_fee, parking_forbidden_fee, fk_msp_meta) VALUES
('22808a15-1b7b-4605-9764-3c71a3f05ae4', '', 5, 3, null),
('08b05a7e-e027-48ba-8916-83c7ddca8c9e', '', 5, 3, null),
('2d973d40-8e33-4b59-9334-a98bbf0e18c5', '', 5, 3, null),
('dad32a8c-0033-4e49-b6b9-cd051744f699', '', 5, 3, null),
('50d8c105-8742-4187-b4e8-c67c51a34229', '', 5, 3, null);


INSERT INTO msp.partner_meta (partner_id,partner_type,has_hold,has_no_parking_zone,has_operating_zone,has_parking,has_pref_parking_zone,has_speed_limit_zone,has_station,has_station_status,has_vehicle,is_enabled,logo_format,logo_url,"name","operator",primary_color,secondary_color,"type",url,url_webview,price_list_price_list_id) VALUES
('b814c97e-df56-4651-ac50-11525537964f', 'MSP',false, false, false, false, false, false, false, true, NULL, NULL, 'PNG', '@MAAS_API_DIRECTORY_ICON@/icon_cityscoot@3x.png', 'Dott', 'Dott', '#0054BB', '#0054BB', 'CARPOOLING', 'https://www.cityscoot.eu/paris/', false, '22808a15-1b7b-4605-9764-3c71a3f05ae4'),
('6c317901-749f-4d7d-82bb-afdc737b26b3', 'MSP',false, false, false, false, false, false, false, true, NULL, NULL, 'PNG', '@MAAS_API_DIRECTORY_ICON@/icon_cityscoot@3x.png', 'Dott', 'Dott', '#0054BB', '#0054BB', 'CARPOOLING', 'https://www.cityscoot.eu/paris/', false, '08b05a7e-e027-48ba-8916-83c7ddca8c9e'),
('f6b6d937-dd91-4dd2-99d6-74958ee4e09a', 'MSP',false, false, false, false, false, false, false, true, NULL, NULL, 'PNG', '@MAAS_API_DIRECTORY_ICON@/icon_cityscoot@3x.png', 'Dott', 'Dott', '#0054BB', '#0054BB', 'CARPOOLING', 'https://www.cityscoot.eu/paris/', false, '2d973d40-8e33-4b59-9334-a98bbf0e18c5'),
('28784eb7-de79-434d-a1b6-819ebfd50860', 'MSP',false, false, false, false, false, false, false, true, NULL, NULL, 'PNG', '@MAAS_API_DIRECTORY_ICON@/icon_cityscoot@3x.png', 'Dott', 'Dott', '#0054BB', '#0054BB', 'CARPOOLING', 'https://www.cityscoot.eu/paris/', false, 'dad32a8c-0033-4e49-b6b9-cd051744f699'),
('3eeec675-2da2-421b-9b6c-ed05776b9f64', 'MSP',false, false, false, false, false, false, false, true, NULL, NULL, 'PNG', '@MAAS_API_DIRECTORY_ICON@/icon_cityscoot@3x.png', 'Dott', 'Dott', '#0054BB', '#0054BB', 'CARPOOLING', 'https://www.cityscoot.eu/paris/', false, '50d8c105-8742-4187-b4e8-c67c51a34229');


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

commit;