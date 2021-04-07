package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws>{

	public NewtonUniversalGravitationBuilder() {
		typeTag = "nlug";
		desc = "Newton Universal Gravitation";
	}
	
	@Override
	public NewtonUniversalGravitation createTheInstance(JSONObject info) {
		double G;
		if(info.has("G"))
			G = info.getDouble("G");
		else
			G = 6.67*Math.pow(10, -11);
		return new NewtonUniversalGravitation(G);
	}
	
	public JSONObject createData() {
		JSONObject data = new JSONObject();
		data.put("G", "(opcional) valor de la constante gravitacional");
		return data;
	}

}