package com.gateway.api.service.ivservice.impl;

import com.gateway.api.util.constant.GatewayApiPathDict;
import com.gateway.api.util.enums.AssetClass;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.api.model.*;
import com.gateway.api.service.ivservice.IVService;
import com.gateway.api.util.enums.MSPType;
import com.gateway.api.util.enums.ZoneStatus;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.gateway.api.util.constant.GatewayApiPathDict.*;

@Service
public class IVServiceImpl implements IVService {

    @Autowired
    private ErrorMessages errorMessages;

    //MOCK D IMPLEMENTATION

    MSPMeta msp1 = new MSPMeta((UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759")), "msp1", MSPType.TROTTINETTE, true, true, true);
    MSPMeta msp2 = new MSPMeta((UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b735")), "msp2", MSPType.AUTOPARTAGE, true, true, true );
    MSPMeta msp3 = new MSPMeta((UUID.fromString("14390fdf-34c1-41c9-885e-6ce669264483")), "msp3", MSPType.VELO, true, true, false);
    List<MSPMeta> liste = List.of(msp1,msp2, msp3);


    Station station1 = new Station(msp1.getMspId(),"sta1", "station1");
    Station station2 = new Station(msp1.getMspId(),"sta2", "station2");
    Station station3 = new Station(msp2.getMspId(),"sta3", "station3");
    Station station4 = new Station(msp2.getMspId(),"sta4", "station4");
    Station station5 = new Station(msp3.getMspId(),"sta5", "station5");
    List<Station> stations = List.of(station1, station2, station3, station4, station5);


    StationStatus stationStatus1 = new StationStatus(msp1.getMspId(),"sta1",1L,true, true, true);
    StationStatus stationStatus2 = new StationStatus(msp1.getMspId(),"sta2",0L,true,true,true);
    StationStatus stationStatus3 = new StationStatus(msp2.getMspId(),"sta3",1L,true,true,true);
    StationStatus stationStatus4 = new StationStatus(msp2.getMspId(),"sta4",0L,true,true,true);
    StationStatus stationStatus5 = new StationStatus(msp3.getMspId(),"sta5",0L,true,true,true);
    List<StationStatus> stationStatus = List.of(stationStatus1, stationStatus2, stationStatus3, stationStatus4, stationStatus5);

    Parking park1 = new Parking(msp1.getMspId(), "park1","parking1");
    Parking park2 = new Parking(msp2.getMspId(), "park2","parking2");
    Parking park3 = new Parking(msp3.getMspId(), "park3","parking3");
    List<Parking> parkings = List.of(park1,park2,park3);

    Asset asset1 = new Asset(msp1.getMspId(),"asset1","trotinette",false);
    Asset asset2 = new Asset(msp1.getMspId(),"asset2","trotinette",true);
    Asset asset4 = new Asset(msp2.getMspId(),"asset4","trotinette",true);
    Asset asset3 = new Asset(msp2.getMspId(),"asset3","voiture",false);
    List<Asset> assets= List.of(asset1, asset2, asset3,asset4);

    GlobalView globalView = new GlobalView(stations, stationStatus,assets , parkings);


    MSPZone mspZone1 = new MSPZone(msp1.getMspId(),"msp1", ZoneStatus.AVAILABLE);
    MSPZone mspZone2 = new MSPZone(msp2.getMspId(),"msp2",ZoneStatus.AVAILABLE);
    MSPZone mspZone3 = new MSPZone(msp3.getMspId(),"msp3",ZoneStatus.AVAILABLE);
    List<MSPZone> mspZones = List.of(mspZone1, mspZone2, mspZone3);

    AssetType assetType1 = new AssetType("assettype1", msp1.getMspId(), "sta1", 1 , List.of(asset1,asset2), AssetClass.BICYCLE );
    AssetType assetType2 = new AssetType("assettype2", msp2.getMspId(), "sta3", 1 , List.of(asset3), AssetClass.CAR );
    AssetType assetType3 = new AssetType("assettype3", msp2.getMspId(), "sta4", 0 , List.of(asset4), AssetClass.CAR );
    List<AssetType> availableAssets = List.of(assetType1, assetType2, assetType3);

    public static final String MSP_ID = "mspId";

    @Override
    public List<Station> getStations(UUID mspId) {
        MSPMeta mspMeta = null;

        for(MSPMeta msp : liste){
            if(msp.getMspId().equals(mspId)){
                mspMeta = msp;
            }
        }
        if (mspMeta != null) {

            List<Station> resp = new ArrayList<>();
            for(Station station : stations){
                if(station.getMspId().equals(mspId)){
                    resp.add(station);
                }
            }

            return resp;

        } else {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(),CommonUtils.placeholderFormat(GatewayApiPathDict.GET_MSP_STATIONS_PATH, MSP_ID, String.valueOf(mspId))));
        }
    }

    @Override
    public List<StationStatus> getStationStatus(UUID mspId)  {
        MSPMeta mspMeta = null;

        for(MSPMeta msp : liste){
            if(msp.getMspId().equals(mspId)){
                mspMeta = msp;
            }
        }
        if (mspMeta != null) {

            List<StationStatus> resp = new ArrayList<>();
            for(StationStatus station : stationStatus){
                if(station.getMspId().equals(mspId)){
                    resp.add(station);
                }
            }

            return resp;

        } else {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(),CommonUtils.placeholderFormat(GET_MSP_STATIONS_STATUS_PATH, MSP_ID, String.valueOf(mspId))));
        }

    }

    @Override
    public List<Asset> getAssets(UUID mspId)  {
        MSPMeta mspMeta = null;

        for(MSPMeta msp : liste){
            if(msp.getMspId().equals(mspId)){
                mspMeta = msp;
            }
        }
        if (mspMeta != null) {

            List<Asset> resp = new ArrayList<>();
            for(Asset asset : assets){
                if(asset.getMspId().equals(mspId)){
                    resp.add(asset);
                }
            }

            return resp;

        } else {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(),CommonUtils.placeholderFormat("/msps/{mspId}/assets", MSP_ID, String.valueOf(mspId))));
        }

    }

    @Override
    public List<AssetType> getAvailableAssets(UUID mspId, String stationId) {
        MSPMeta mspMeta = null;

        for(MSPMeta msp : liste){
            if(msp.getMspId().equals(mspId)){
                mspMeta = msp;
            }
        }
        if (mspMeta != null) {

            List<AssetType> resp = new ArrayList<>();
            for(AssetType assetType : availableAssets){
                if(assetType.getMspId().equals(mspId)){
                    if(assetType.getStationId().equals(stationId) || stationId == null){
                        resp.add(assetType);
                    }

                }
            }
            return resp;
        } else {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(),CommonUtils.placeholderFormat("/msps" + GET_MSP_AVAILABLE_ASSETS_PATH, MSP_ID, String.valueOf(mspId))));
        }

    }


    @Override
    public GlobalView getGlobalView()  {
        return globalView;
    }

    @Override
    public GlobalView getAroundMe(MSPAroundMeRequest mspAroundMeRequest) {

       List<Station> stationsAround = new ArrayList<>();
       List<StationStatus> stationStatusAround = new ArrayList<>();
       List<Parking> parkingsAround = new ArrayList<>();
       List<Asset> assetsAround = new ArrayList<>();

       //Verify mspIds:
        List<UUID> mspIdsToVerify = mspAroundMeRequest.getMspIds();
        List<UUID> ids = new ArrayList<>();
        liste.stream().map(MSPMeta::getMspId).forEach(ids::add);

        for(UUID id : mspIdsToVerify){
            if(!ids.contains(id)){
                throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(),CommonUtils.placeholderFormat("/msps"+ GET_MSPS_AROUND_ME_PATH + " for MSP with id : {mspId} ", MSP_ID, String.valueOf(id))));
            }
        }

        //Infos around me for selected msps
       List<UUID> mspIds = mspAroundMeRequest.getMspIds();

       for(Station station: stations){
           if(mspIds.contains(station.getMspId())){
               stationsAround.add(station);
           }
       }
       for(StationStatus status: stationStatus){
           if(mspIds.contains(status.getMspId())){
               stationStatusAround.add(status);
           }
       }
       for(Parking parking: parkings){
           if(mspIds.contains(parking.getMspId())){
               parkingsAround.add(parking);
           }
       }
       for(Asset asset: assets){
           if(mspIds.contains(asset.getMspId())){
               assetsAround.add(asset);
           }
       }

       return new GlobalView(stationsAround, stationStatusAround, assetsAround , parkingsAround);




    }
}
