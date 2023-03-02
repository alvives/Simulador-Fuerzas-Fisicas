package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;


public class BuilderBasedFactory<T> implements Factory {
	private List<Builder<T>> builders;
	
	
	//CONSTRUCTOR:
	public BuilderBasedFactory(List<Builder<T>> builders) {
		super();
		this.builders = builders;
	}

	//METODOS
	@Override
	public T createInstance(JSONObject info) throws IllegalArgumentException {
		boolean found = false;
		int cont = 0;
		T t = null;
		
		while (!found && cont < builders.size()) {
			try {
				t = builders.get(cont).createInstance(info);
				if(t != null)
					found = true;
			}
			catch(IllegalArgumentException e) {
				throw e;
			}
			cont++;
		}
		
		if(!found)
			throw new IllegalArgumentException("Falla el type");
		
		return t;
	}
	//El método createInstance de la factoría ejecuta los constructores uno a uno hasta que encuentre el constructor 
	//capaz de crear el objeto correspondiente — debe lanzar una excepcion IllegalArgumentException en caso de fallo.
	

	@Override
	public List<JSONObject> getInfo() {
		List<JSONObject> list = new ArrayList <JSONObject>();
		
		for(int i = 0; i < builders.size(); i++) {
			list.add(builders.get(i).getBuilderInfo());
		}
		
		return list;
	}

}
