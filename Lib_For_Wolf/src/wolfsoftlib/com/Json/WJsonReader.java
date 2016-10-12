package wolfsoftlib.com.Json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wolfsoftlib.com.model.WolfModel;


public class WJsonReader {

	public static ArrayList<WolfModel> getHome(JSONObject jsonObject, String mPackage)
			throws JSONException {
		ArrayList<WolfModel> products = new ArrayList<WolfModel>();

		JSONArray jsonArray = jsonObject.getJSONArray(WContentValue.TAG_PRODUCTS);
		WolfModel product;
		for (int i = 0; i < jsonArray.length(); i++) {
			product = new WolfModel();			
			JSONObject productObj = jsonArray.getJSONObject(i);
			product.setName(productObj.getString(WContentValue.NAME));
			product.setPackage(productObj.getString(WContentValue.PACKAGE));
			product.setIcon(productObj.getString(WContentValue.ICON));
			product.setDescription(productObj.getString(WContentValue.DESCRIPTION));	
			if(!product.Package.contains(mPackage))
			products.add(product);
		}
		return products;
	}
	
}
