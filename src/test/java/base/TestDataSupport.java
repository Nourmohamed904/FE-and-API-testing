package base;

import com.framework.utils.ConfigReader;
import com.framework.utils.ExcelReader;

public class TestDataSupport {
    private final ConfigReader configReader;

    public TestDataSupport(ConfigReader configReader) {
        this.configReader = configReader;
    }

    public Object[][] getSheetData(String sheetName) {
        return new ExcelReader(configReader.getRequiredProperty("testdata.filepath"), sheetName).getData();
    }

    public String[] getValidLoginCredentials() {
        for (Object[] row : getSheetData("Login")) {
            if (row.length >= 3 && "valid".equalsIgnoreCase(String.valueOf(row[2]))) {
                return new String[]{String.valueOf(row[0]), String.valueOf(row[1])};
            }
        }
        throw new IllegalStateException("No valid login row found in Login sheet.");
    }
}
