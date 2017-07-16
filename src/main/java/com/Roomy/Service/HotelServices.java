package com.Roomy.Service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Roomy.Repository.Hotel_MasterRepository;
import com.Roomy.Response.Domain.HotelsBasedOnCityResponse;
import com.Roomy.Response.Domain.MetaDataHoteResponse;
import com.Roomy.domain.Hotel_Master;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class HotelServices {

	private final static Logger LOGGER = Logger.getLogger(HotelServices.class.getName());

	@Autowired
	Hotel_MasterRepository hotel_MasterRepository;

	@RequestMapping(value = "/getListofHotelsByCity", method = RequestMethod.GET, produces = "application/json")
	public Object getHotelsBasedonCity(@RequestParam(value = "cityName", required = true) String cityName,
			@RequestParam(value = "customerToken") String customerToken) throws JsonProcessingException, JSONException {
		LOGGER.info("Hotel Serviec :: getListofHotels " + cityName);
		HotelsBasedOnCityResponse hotelsBasedOnCityResponse = null;
		MetaDataHoteResponse metaDataHoteResponse = null;
		List<HotelsBasedOnCityResponse> lisOfHotelDetails = new ArrayList<HotelsBasedOnCityResponse>();
		List<Hotel_Master> lisOFHotels = hotel_MasterRepository.getHotelMasterDetails(cityName);
		if (!lisOFHotels.isEmpty()) {
			for (Hotel_Master hotel : lisOFHotels) {
				hotelsBasedOnCityResponse = buildHotelDetailsReponse(hotel);
				lisOfHotelDetails.add(hotelsBasedOnCityResponse);
			}
			return lisOfHotelDetails;
		} else {
			metaDataHoteResponse = new MetaDataHoteResponse();
			metaDataHoteResponse.setStatusCode("400");
			metaDataHoteResponse.setFailureMessage("No Details Found for the Given City");
		}

		return metaDataHoteResponse;
	}

	private HotelsBasedOnCityResponse buildHotelDetailsReponse(Hotel_Master hotel) {
		HotelsBasedOnCityResponse hotelsBasedOnCityResponse;
		hotelsBasedOnCityResponse = new HotelsBasedOnCityResponse();
		hotelsBasedOnCityResponse.setHotelName(hotel.getHotel_Name());
		hotelsBasedOnCityResponse.setAddress1(hotel.getHotel_Address());
		hotelsBasedOnCityResponse.setMinCost(hotel.getPricinginfo().get(0).getPricePerMinute());
		hotelsBasedOnCityResponse.setHourCost(hotel.getPricinginfo().get(0).getPricePerMinute() * 60);
		hotelsBasedOnCityResponse.setCity(hotel.getHotel_City());
		hotelsBasedOnCityResponse.setState(hotel.getHotel_State());
		hotelsBasedOnCityResponse.setLattitue(hotel.getLatitude());
		hotelsBasedOnCityResponse.setLongitude(hotel.getLongitude());
		hotelsBasedOnCityResponse.setCurrencyType(hotel.getPricinginfo().get(0).getCurrencyType());
		return hotelsBasedOnCityResponse;
	}
}