package com.AlBeley.Program;

import javax.swing.*;
import java.awt.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javazoom.jl.player.Player;
import java.io.InputStream;
import java.net.URL;

 


    class App {

// Создаем массив для хранения данных ВНЕ 
static String[][] data = new String[10000][7];
static String[][] dataHtrix = new String[10000][1];
private static Player currentPlayer;

    
        App() {
        
        JFrame frame = new JFrame("Приемка товара");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

try {
            URL iconURL = getClass().getResource("/icon.png");
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                frame.setIconImage(icon.getImage());
            } else {
                System.err.println("Icon not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        
	// Получаем размер экрана
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 1); // 80% от ширины экрана
        int height = (int) (screenSize.height * 1); // 80% от высоты экрана
        frame.setSize(width, height);
        
        
        
            JTabbedPane jtp = new JTabbedPane();
            jtp.addTab("Внесение данных", new DataPanel());
            jtp.addTab("Внесение штрихкодов", new htrixPanel());
            jtp.addTab("Результат", new clousePanel());
            
           
        
            
            
JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Справка");

        JMenuItem aboutCompany = new JMenuItem("О компании");
        JMenuItem anthem = new JMenuItem("Гимн");

        // Добавляем обработчики событий
        aboutCompany.addActionListener(e -> showAboutCompanyDialog(frame));
        anthem.addActionListener(e -> {
            stopCurrentAnthem(); // Останавливаем текущий плеер, если он существует
            playAnthem(); // Запускаем новый
            JOptionPane.showMessageDialog(frame, "Гимн компании");
        });

        menu.add(aboutCompany);
        menu.add(anthem);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
        
        frame.add(jtp);
        frame.setVisible(true);
    }
    
 
 private static void showAboutCompanyDialog(Component parent) {
        String message = "Год разработки: 2024\nКомпания: ООО Старт 21\nИНН:6025047072\nРазработчик: Белей Александр Игоревич";
        JOptionPane.showMessageDialog(parent, message, "О компании", JOptionPane.INFORMATION_MESSAGE);

    }


private static void playAnthem() {
        try {
            InputStream mp3Stream = App.class.getClassLoader().getResourceAsStream("Gimn.mp3");
            if (mp3Stream == null) {
                JOptionPane.showMessageDialog(null, "Файл гимна не найден!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            currentPlayer = new Player(mp3Stream);
            new Thread(() -> {
                try {
                    currentPlayer.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


private static void stopCurrentAnthem() {
        if (currentPlayer != null) {
            currentPlayer.close(); // Останавливаем текущий плеер
            currentPlayer = null; // Сбрасываем ссылку на плеер
        }
    }





        public class DataPanel extends JPanel{

private static File selectedFile = null;
private static File selectedFile1 = null;


        public DataPanel(){
        	String[] colHeads1 = {"Наименование товара","Штрихкод из накладной","Кол-во по накладной"};
	      
            
	   
	
        JButton buttonCreateH = new JButton("Создать шаблон");
	JButton buttonloadH = new JButton("Загрузить из шаблона");

 	//Вложенный контейнер с кнопками
        JPanel container = new JPanel(new FlowLayout());
	container.add(buttonCreateH);
	container.add(buttonloadH);
	
	buttonCreateH.addActionListener(new ListenerAction());
        buttonloadH.addActionListener(new ListenerAction2());
       

           
            JTable table = new JTable(data, colHeads1);

		java.awt.Font font = new java.awt.Font("Times New Roman", java.awt.Font.PLAIN, 14);
        	table.setFont(font);
        	table.setRowHeight(25);
		
            // Получаем размер экрана
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.9); // 80% от ширины экрана
        int height = (int) (screenSize.height * 0.9); // 80% от высоты экрана
        table.setPreferredScrollableViewportSize(new Dimension(width, height));
	    

            JScrollPane dataPanelScroll = new JScrollPane(table);
         
            add(container, BorderLayout.NORTH);
	    add(dataPanelScroll, BorderLayout.CENTER);
	
         	    
        }


public static void fileRead() {

try {
            FileInputStream inputStream = new FileInputStream(selectedFile1);
            Workbook wb = new XSSFWorkbook(inputStream);
	    DataFormatter dataFormatter = new DataFormatter();
	  

            // Получаем первый лист
            var sheet = wb.getSheetAt(0);
            int rowCount = 10001; // Получаем количество строк

            for (int i = 1, g = 0; i < rowCount; i++, g++) {
                Row row = sheet.getRow(i); // Получаем строку
                  if (row != null) {
                    for (int j = 0; j < 3; j++) { // Перебираем первые три столбца
                        Cell cell = row.getCell(j); // Получаем ячейку
                        if (cell != null) {
                            data[g][j] = dataFormatter.formatCellValue(cell); // Записываем значение в массив
                        
                             }
			}
                }
            }


// Закрываем поток
inputStream.close();

} catch (IOException e) {
            e.printStackTrace();
        }



}


public static void choisLoadData(){

JFileChooser fileChooser1 = new JFileChooser();

fileChooser1.setCurrentDirectory(new File(System.getProperty("user.dir")));

fileChooser1.setDialogTitle("Выбор директории");
        
        fileChooser1.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser1.showOpenDialog(null);
      
        if (result == JFileChooser.APPROVE_OPTION ){
	selectedFile1 = fileChooser1.getSelectedFile();
        fileRead();
}
                 
}





public static void chois() {

	JFileChooser fileChooser = new JFileChooser();


fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

File defaultFile = new File("Hablon.xlsx");
    fileChooser.setSelectedFile(defaultFile);
	
	fileChooser.setDialogTitle("Сохранение файла");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
	    hablonCreate();
        } else { 
		return;
}

}	


public static void hablonCreate(){
	
if (selectedFile == null) {
            System.out.println("Файл не выбран.");
            return; // Обработка случая, когда файл не выбран
        }


try{

Workbook wb = new XSSFWorkbook();
Sheet sheet = wb.createSheet();

Row row1 = sheet.createRow(0);
Row row2 = sheet.createRow(1);
Row row3 = sheet.createRow(2);

Cell cell1 = row1.createCell(0);
Cell cell2 = row1.createCell(1);
Cell cell3 = row1.createCell(2);

CellStyle style = wb.createCellStyle();
style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());

org.apache.poi.ss.usermodel.Font font = wb.createFont(); //шрифт
font.setFontName("Times New Roman");
font.setFontHeightInPoints((short) 14); // сделали перобразование к шорту
font.setBold(true);

style.setFont(font);

cell1.setCellStyle(style);
cell2.setCellStyle(style);
cell3.setCellStyle(style);

sheet.setColumnWidth(0, 12000);
sheet.setColumnWidth(1, 8000);
sheet.setColumnWidth(2, 8000);

cell1.setCellValue("Наименование товара");
cell2.setCellValue("Штрихкод");
cell3.setCellValue("Кол-во по накладной");

FileOutputStream fos = new FileOutputStream(selectedFile);

wb.write(fos);
fos.close();

} catch (IOException e) {
            e.printStackTrace();
      		  }




	}


}

class ListenerAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
         
	DataPanel.chois();
        }}


class ListenerAction2 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
         
	DataPanel.choisLoadData();

        }}  

                 


       
    class htrixPanel extends JPanel{
        public htrixPanel(){

String[] colHeads2 = {"Внесенные Штрихкода"};
	      
            
	   
	
        JButton buttonWrite = new JButton("Записать внесенные данные и посчитать результат");
	

 	//Вложенный контейнер с кнопками
        JPanel container1 = new JPanel(new FlowLayout());
	container1.add(buttonWrite);
	
	buttonWrite.addActionListener(new ListenerAction3());
        
       

           
            JTable table1 = new JTable(dataHtrix, colHeads2);

		java.awt.Font font = new java.awt.Font("Times New Roman", java.awt.Font.PLAIN, 14);
        	table1.setFont(font);
        	table1.setRowHeight(25);
            
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.9); // 80% от ширины экрана
        int height = (int) (screenSize.height * 0.9); // 80% от высоты экрана
        table1.setPreferredScrollableViewportSize(new Dimension(width, height));

            JScrollPane dataPanelScroll1 = new JScrollPane(table1);
         
            add(container1, BorderLayout.NORTH);
	    add(dataPanelScroll1, BorderLayout.CENTER);

           
        }


public static void result() {
   

    // Заполняем шестой столбец данными из dataHtrix
    for (int i = 0; i < 10000; i++) {
        if (dataHtrix != null && dataHtrix[i] != null && dataHtrix[i].length > 0) {
            data[i][6] = dataHtrix[i][0];
        }
    }

    // Очищаем третий столбец
    for (int i = 0; i < data.length; i++) {
        data[i][3] = "";
    }

    // Подсчитываем совпадения во втором и шестом столбцах
    for (int i = 0; i < data.length; i++) {
        if (data[i][1] != null && !data[i][1].isEmpty()) {
            for (int j = 0; j < data.length; j++) {
                if (data[j][6] != null && !data[j][6].isEmpty()) {
                    if (data[i][1].equals(data[j][6])) {
                        try {
                            int currentValue = Integer.parseInt(data[i][3]);
                            data[i][3] = String.valueOf(currentValue + 1);
                        } catch (NumberFormatException e) {
                            data[i][3] = "1";
                        }
                    }
                }
            }
        }
    }

    // Очищаем четвертый столбец
    for (int i = 0; i < data.length; i++) {
        data[i][4] = "";
    }

// Очищаем пятый столбец
    for (int i = 0; i < data.length; i++) {
        data[i][5] = "";
    }


   


    // Находим уникальные значения в первом столбце
    HashSet<String> uniqueValuesInColumn1 = new HashSet<>();
    for (int i = 0; i < data.length; i++) {
        if (data[i][1] != null && !data[i][1].isEmpty()) {
            uniqueValuesInColumn1.add(data[i][1]);
        }
    }

    // Заполняем пятый столбец уникальными значениями из шестого столбца
    for (int i = 0, j = 0; i < data.length; i++) {
        if (data[i][6] != null && !data[i][6].isEmpty() && !uniqueValuesInColumn1.contains(data[i][6])) {
            data[j][5] = data[i][6];
            j++;
        }
    }



 // Проверяем совпадения во втором и третьем столбцах
    for (int i = 0; i < 10000; i++) {
        if (data[i][2].equals(data[i][3])) {
        } else {
            data[i][4] = "Несовпадение";
        }
    }


}




    }




    
    class clousePanel extends JPanel{



private static File selectedFile2 = null;

        public clousePanel(){

String[] colHeads3 = {"Наименование товара","Штрихкод","Кол-во по накладной","Фактическое кол-во","Результат","Неопознаные штрихкода"};

JButton buttonSave = new JButton("Сохранить результат в эксель");
	

 	//Вложенный контейнер с кнопками
        JPanel container2 = new JPanel(new FlowLayout());
	container2.add(buttonSave);
	
	buttonSave.addActionListener(new ListenerAction4());
        
       

           
            JTable table2 = new JTable(data, colHeads3);

        // Установка шрифта для всей таблицы
        java.awt.Font font = new java.awt.Font("Times New Roman", java.awt.Font.PLAIN, 14);
        table2.setFont(font);
        table2.setRowHeight(25); // Увеличение высоты строки для лучшего отображения шрифта

            
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.9); // 80% от ширины экрана
        int height = (int) (screenSize.height * 0.9); // 80% от высоты экрана
        table2.setPreferredScrollableViewportSize(new Dimension(width, height));

            JScrollPane dataPanelScroll2 = new JScrollPane(table2);
         
            add(container2, BorderLayout.NORTH);
	    add(dataPanelScroll2, BorderLayout.CENTER);


            
        }




public static void chois2() {

	JFileChooser fileChooser2 = new JFileChooser();


fileChooser2.setCurrentDirectory(new File(System.getProperty("user.dir")));

File defaultFile = new File("Result.xlsx");
    fileChooser2.setSelectedFile(defaultFile);
	
	fileChooser2.setDialogTitle("Сохранение файла");
        fileChooser2.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser2.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile2 = fileChooser2.getSelectedFile();
	    saveResult();
        } else { 
		return;
}

}	






public static void saveResult() {


if (selectedFile2 == null) {
            System.out.println("Файл не выбран.");
            return; // Обработка случая, когда файл не выбран
        }


try {
    Workbook wb = new XSSFWorkbook();
    Sheet sheet = wb.createSheet();

    // Создание заголовков
    Row headerRow = sheet.createRow(0);
    CellStyle style = wb.createCellStyle();
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());

    org.apache.poi.ss.usermodel.Font font = wb.createFont();
    font.setFontName("Times New Roman");
    font.setFontHeightInPoints((short) 14);
    font.setBold(true);
    style.setFont(font);

    String[] headers = {"Наименование товара", "Штрихкод", "Кол-во по накладной", "Фактическое кол-во", "Результат", "Неопознаные штрихкода"};
    
    for (int i = 0; i < headers.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(headers[i]);
        cell.setCellStyle(style);
    }

    // Установка ширины столбцов
    sheet.setColumnWidth(0, 12000);
    for (int i = 1; i < 6; i++) {
        sheet.setColumnWidth(i, 8000);
    }

    // Заполнение данными из массива data[10000][6]
    for (int i = 0; i < data.length; i++) {
        Row row = sheet.createRow(i + 1); // Начинаем с 1, так как 0 - это заголовки
        for (int j = 0; j < data[i].length; j++) {
            Cell cell = row.createCell(j);
            if (data[i][j] != null) {
                cell.setCellValue(data[i][j].toString()); // Преобразуем в строку, если это необходимо
            }
        }
    }

    // Запись в файл
    FileOutputStream fos = new FileOutputStream(selectedFile2);
    wb.write(fos);
    fos.close();
    wb.close(); // Закрываем Workbook

} catch (IOException e) {
    e.printStackTrace();
}





}

    }
    



class ListenerAction3 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
         
	htrixPanel.result();
	

        }} 


class ListenerAction4 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
         
	clousePanel.chois2();
	

        }} 






    
    public static void main(String args[]) {

try {
            // Установить внешний вид в соответствии с нативным внешним видом системы
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App();
            }
        });
    }
}

