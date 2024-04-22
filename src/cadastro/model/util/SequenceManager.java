package cadastro.model.util;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SequenceManager {
    ConectorBD conectorBD;

    public SequenceManager(ConectorBD conectorBD) {
        this.conectorBD = conectorBD;
    }

    public int getValue(String nomeSequencia) throws SQLException {
        String sql = "SELECT NEXT VALUE FOR " + nomeSequencia;
        try(ResultSet result = conectorBD.getSelect(sql)) {
            if(result.next()) {
                return result.getInt(1);
            }
        }
        return -1;
    }
}
