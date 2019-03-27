package com.mxn672.foodrating.data.api;

public enum BusinessType {
    ALL(-1, "All"), DISTRIBUTORS(7, "Distributors/Transporters"), FARMERS(7838, "Farmers/growers"),
    HOSPITAS(5, "Hospitals/Childcare/Caring Premises"), HOTELS(7842,"Hotel/bed & breakfast/guest house"),
    IMPORTERS(14, "Importers/Exporters"), MANUFACTURES(7839, "Manufacturers/packers"), MOBILE(7864, "Mobile caterer"),
    OTHER(7841, "Other catering premises"), BAR(7843, "Pub/bar/nightclub"), RESTAURANT(1, "Restaurant/Cafe/Canteen"),
    RETAILERS(4613, "Retailers - other"), SUPERMARKETS(7840, "Retailers - supermarkets/hypermarkets"),
    SCHOOL(7845, "School/college/university"), TAKEAWAYS(7844, "Takeaway/sandwich shop");

    public int id;
    public String name;

    BusinessType(int id, String name){
        this.id = id;
        this.name = name;
    }
}
