package data;

public enum TransportType {
    BUS("Bus"),
    TRAM("Tramwaj"),
    BOTH("Oba");

    String typeName;

    TransportType(String typeName){
        this.typeName = typeName;
    }

    @Override
    public String toString(){
        return typeName;
    }
}
