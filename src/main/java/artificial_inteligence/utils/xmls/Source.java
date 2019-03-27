package artificial_inteligence.utils.xmls;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"database"})
public class Source {

    public Source(){
        database = "Unknown";
    }

    public Source(String database) {
        this.database = database;
    }

    public String getDatabase() {
        return database;
    }

    @XmlElement
    public void setDatabase(String database) {
        this.database = database;
    }

    private String database;
}
