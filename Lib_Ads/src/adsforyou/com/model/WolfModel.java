package adsforyou.com.model;

public class WolfModel  {
	public String Name, Package,Icon, Description;
	public WolfModel(){
	}
	
	public WolfModel(String mName,String mPackage,String mIcon, String mDescription){
		Name = mName;
		Package = mPackage;
		Icon = mIcon;
		Description = mDescription;
	}
	public void setName(String mName){
		Name = mName;
	}
	public String getName(){
		return Name;
	}
	public void setPackage(String mPackage){
		Package = mPackage;
	}
	public String getPackage(){
		return Package;
	}
	public void setIcon(String mIcon){
		Icon = mIcon;
	}
	public String getIcon(){
		return Icon;
	}
	public void setDescription(String mDescription){
		Description = mDescription;
	}
	public String getDescription(){
		return Description;
	}

}
