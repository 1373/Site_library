package library;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class PurchaseBookFrame extends JFrame{
    
    private KeyValueComboBoxModel<String, String> readers = new KeyValueComboBoxModel<String, String>();
    private int readerId = -1;
    
    private int bookId;
    private String bookName;
    private String pubYear;
    private String publisherName;
    private String price;
    private String purDate;
    
    public static void main(String[] args) {
        PurchaseBookFrame pbf = new PurchaseBookFrame(1, "Книга1", "2011", "Издательство1", "111.00");
        pbf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pbf.setVisible(true);
    }
    
    public PurchaseBookFrame(int bookId, String bookName, String pubYear, String publisherName, String price)
    {
        this.bookId = bookId;
        this.bookName = bookName;
        this.pubYear = pubYear;
        this.publisherName = publisherName;
        this.price = price;
        this.purDate = new SimpleDateFormat("dd.MM.yyyy").format(new GregorianCalendar().getTime());//текущая дата
        
        initComponents();//инициализация компонентов
        initData();//инициализация данных
        initActions();//инициализация событий
    }
    
    private void initActions() {
        cbReaders.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    try {
                        readerId = Integer.parseInt(readers.getSelectedKey());
                    } catch (Exception ex) {
                        readerId = -1;
                        System.out.println("ОШИБКА ПРЕОБРАЗОВАНИЯ КОДА ЧИТАТЕЛЯ В ЧИСЛОВОЙ ТИП!");
                    }
                }
            }
        });
        
        btSale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (readerId == -1) {
                    JOptionPane.showMessageDialog(null, "Выберите покупателя!", "ОШИБКА!", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        Connection conn = ConnectionToDB.getConnection();
                        try {
                            Statement stat = conn.createStatement();
                            
                            stat.executeUpdate(""
                                    + "INSERT INTO Purchase ("
                                        + "bookId, "
                                        + "readerId, "
                                        + "purDate, "
                                        + "price) "
                                    + "VALUES ("
                                        + "" + bookId + ", "
                                        + "" + readerId + ", "
                                        + "now()::date, "
                                        + "" + price + ")");
                            
                            stat.executeUpdate(""
                                    + "UPDATE Books "
                                    + "SET countInStock = countInStock - 1"
                                    + "WHERE bookId = " + bookId + "");
                            
                            JOptionPane.showMessageDialog(null, "Книга успешно продана!", "УРААА!", JOptionPane.INFORMATION_MESSAGE);

                            dispose();
                        } finally {
                            conn.close();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        btCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void initData() {
        setReaders();
    }
    
    private void setReaders() {
        cbReaders.setModel(readers);
        cbReaders.setRenderer(new ComboBoxRenderer());

        try {
            Connection conn = ConnectionToDB.getConnection();
            try {
                Statement stat = conn.createStatement();
                ResultSet res = stat.executeQuery(""
                        + "SELECT readerId, readerName "
                        + "FROM Readers");

                readers.clear();
                readers.put("-1", "");

                while (res.next()) {
                    readers.put(Integer.toString(res.getInt("readerId")), res.getString("readerName"));
                }
            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void initComponents() {

        lbBookName = new javax.swing.JLabel();
        lbBookNameValue = new javax.swing.JLabel();
        lbPublisher = new javax.swing.JLabel();
        lbPublisherValue = new javax.swing.JLabel();
        lbPubYear = new javax.swing.JLabel();
        lbPubYearValue = new javax.swing.JLabel();
        lbPrice = new javax.swing.JLabel();
        lbPriceValue = new javax.swing.JLabel();
        lbPurDate = new javax.swing.JLabel();
        lbPurDateValue = new javax.swing.JLabel();
        lbReaders = new javax.swing.JLabel();
        cbReaders = new javax.swing.JComboBox();
        btSale = new javax.swing.JButton();
        btCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Продажа книги");

        lbBookName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbBookName.setText("Название книги:");

        lbBookNameValue.setText(bookName);

        lbPublisher.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbPublisher.setText("Издательство:");

        lbPublisherValue.setText(publisherName);

        lbPubYear.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbPubYear.setText("Год издания:");

        lbPubYearValue.setText(pubYear);

        lbPrice.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbPrice.setText("Цена:");

        lbPriceValue.setText(price);

        lbPurDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbPurDate.setText("Дата продажи:");

        lbPurDateValue.setText(purDate);

        lbReaders.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbReaders.setText("Покупатель:");

//        cbReaders.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btSale.setText("Продать");
        

        btCancel.setText("Отмена");
        

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbBookName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbBookNameValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lbPublisher)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbPublisherValue, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbPubYear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbPubYearValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbPrice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbPriceValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbPurDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbPurDateValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbReaders)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbReaders, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btSale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbBookName)
                    .addComponent(lbBookNameValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPublisher)
                    .addComponent(lbPublisherValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPubYear)
                    .addComponent(lbPubYearValue)
                    .addComponent(lbPrice)
                    .addComponent(lbPriceValue)
                    .addComponent(lbPurDate)
                    .addComponent(lbPurDateValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbReaders)
                    .addComponent(cbReaders, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btSale)
                    .addComponent(btCancel))
                .addContainerGap())
        );

        pack();
    }
    
    private javax.swing.JButton btSale;
    private javax.swing.JButton btCancel;
    private javax.swing.JComboBox cbReaders;
    private javax.swing.JLabel lbBookName;
    private javax.swing.JLabel lbPurDateValue;
    private javax.swing.JLabel lbReaders;
    private javax.swing.JLabel lbBookNameValue;
    private javax.swing.JLabel lbPublisher;
    private javax.swing.JLabel lbPublisherValue;
    private javax.swing.JLabel lbPubYear;
    private javax.swing.JLabel lbPubYearValue;
    private javax.swing.JLabel lbPrice;
    private javax.swing.JLabel lbPriceValue;
    private javax.swing.JLabel lbPurDate;
}
