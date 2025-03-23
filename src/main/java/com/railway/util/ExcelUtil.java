package com.railway.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class ExcelUtil {
    
    /**
     * Get cell value as string regardless of cell type
     */
    public static String getCellValue(Cell cell) {
        if (cell == null) return "";
        
        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    // Handle both integer and decimal values
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == Math.floor(numericValue)) {
                        return String.valueOf((int) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    try {
                        return String.valueOf(cell.getStringCellValue());
                    } catch (Exception e) {
                        try {
                            return String.valueOf(cell.getNumericCellValue());
                        } catch (Exception ex) {
                            return "";
                        }
                    }
                default:
                    return "";
            }
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Safely parse cell value to integer
     */
    public static int getCellValueAsInt(Cell cell, int defaultValue) {
        if (cell == null) return defaultValue;
        
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            } else {
                return Integer.parseInt(getCellValue(cell));
            }
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
