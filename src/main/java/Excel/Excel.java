package Excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Иллюстрация работы с Excel
 */
public class Excel {

    /**
     * Создает файл Excel и заполняет его разными данными с разными форматами
     */
    public static void test1() {
        System.out.println("Excel.test - Start");
        String fileName = "D:/TEMP/test1.xlsx";
        delete(fileName);
        Workbook workbook = new XSSFWorkbook(); // файл Excel
        Sheet sheet = workbook.createSheet("Прода пера"); // Excel лист
        DataFormat dataFormat = workbook.createDataFormat(); // Формат
        CellStyle cellStyle = workbook.createCellStyle(); // Стиль

        int rowNumber = 0; // Нумерация строк с нуля
        int colNumber = 0; // Нумерация колонок тоже с нуля
        Row row = sheet.createRow(rowNumber); // Строка
        Cell cell = row.createCell(colNumber); // Ячейка
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // Стиль для выравнивания
        cell.setCellStyle(cellStyle); // Применим
        cell.setCellValue("Этот крутой текст"); // Значение в ячейку
        // Обязательно после заполнения всех данных
        sheet.autoSizeColumn(colNumber);
        // Дата рождения
        // Установим формат ячейки в датовый
        cellStyle.setDataFormat(dataFormat.getFormat("dd.mm.yyyy"));
        rowNumber = 5; // Пятый ряд
        row = sheet.createRow(rowNumber);
        colNumber = 1; // Вторая колонка
        cell = row.createCell(colNumber);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(new Date());
        // Меняем размер  столбца
        sheet.autoSizeColumn(colNumber);
        // Деньги
        colNumber = 2; // Греться колонка
        cell = row.createCell(colNumber);
        CellStyle moneyStyle = workbook.createCellStyle();
        moneyStyle.setDataFormat(dataFormat.getFormat("###,##0.00"));
        cell.setCellStyle(moneyStyle);
        double d = Double.parseDouble("1234567890012.3456789");
        cell.setCellValue(d);
        // Обязательно после заполнения всех данных
        sheet.autoSizeColumn(colNumber);
        // double
        colNumber = 3; // Четвертая колонка
        cell = row.createCell(colNumber);
        cell.setCellValue(d);
        sheet.autoSizeColumn(colNumber);
        try {
            workbook.write(new FileOutputStream(fileName));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void delete(String fileName){
        File file = new File(fileName);
        System.out.println(file.delete());
    }

}
